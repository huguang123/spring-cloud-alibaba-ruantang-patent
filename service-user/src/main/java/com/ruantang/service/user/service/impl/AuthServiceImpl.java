package com.ruantang.service.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.Asserts;
import com.ruantang.commons.service.RedisService;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.security.util.JwtTokenUtil;
import com.ruantang.service.user.client.TenantFeignClient;
import com.ruantang.service.user.domain.SysUserDetails;
import com.ruantang.service.user.model.dto.PermDataPolicyDTO;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.model.dto.TenantDTO;
import com.ruantang.service.user.model.dto.TenantRolePermissionDTO;
import com.ruantang.service.user.model.request.TenantRoleVerifyRequest;
import com.ruantang.service.user.service.AuthService;
import com.ruantang.service.user.service.PermDataPolicyService;
import com.ruantang.service.user.service.PermService;
import com.ruantang.service.user.service.SysRolesService;
import com.ruantang.service.user.util.DefaultPermissionUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers> implements AuthService {

    @Value("${security.jwt.tokenHeader:Authorization}")
    private String tokenHeader;

    @Value("${security.jwt.tokenPrefix:Bearer }")
    private String tokenPrefix;

    @Resource
    private SysUsersMapper sysUsersMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisService redisService;

    @Autowired
    private HttpServletRequest request;

    @Resource
    private SysRolesService sysRolesService;
    
    @Resource
    private PermService permService;
    
    @Resource
    private PermDataPolicyService policyService;

    @Resource
    private TenantFeignClient tenantFeignClient;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> login(SysUserRegisterDTO sysUserRegisterDTO) {

        //检查是否存在账号对应用户
        SysUsers sysUsers = sysUsersMapper.selectOne(new LambdaQueryWrapper<SysUsers>().eq(SysUsers::getLoginName, sysUserRegisterDTO.getLoginName()));
        if (Objects.isNull(sysUsers)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        //进行用户认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(sysUserRegisterDTO.getLoginName(), sysUserRegisterDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //认证未通过
        if (Objects.isNull(authenticate)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        //通过验证，生成jwt
        UserDetails userDetails = new SysUserDetails(sysUsers, sysRolesService.getRolesList());
        String token = jwtTokenUtil.generateToken(userDetails);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        //将用户信息存入redis缓存，设置失效时间，退出登录时删除
        redisService.set("AUTH:TOKEN:" + userDetails.getUsername(), token, jwtTokenUtil.getExpiration());

        // 获取用户角色信息
        List<SysRoles> userRoles = sysRolesService.getUserRoles(sysUsers.getId());
        List<String> roleNames = userRoles.stream()
                .map(SysRoles::getRolesCode)
                .collect(Collectors.toList());
        
        // 获取用户角色ID列表
        List<Long> roleIds = userRoles.stream()
                .map(SysRoles::getId)
                .collect(Collectors.toList());
        
        // 构建权限信息
        Set<String> buttons = new HashSet<>();
        Set<String> apis = new HashSet<>();
        Map<String, PermDataPolicyDTO> dataPolicies = new HashMap<>();
        log.info("roleIds:{}",roleIds);
        
        // 获取用户所属租户ID
        Long tenantId = sysUsers.getTenantId();
        if (tenantId != null && !roleIds.isEmpty()) {
            // 调用租户服务验证角色权限状态
            TenantRoleVerifyRequest verifyRequest = new TenantRoleVerifyRequest();
            verifyRequest.setTenantId(tenantId);
            verifyRequest.setRoleIds(roleIds);

            ApiResult<List<TenantRolePermissionDTO>> verifyResult = tenantFeignClient.verifyTenantRolePermissions(verifyRequest);
            log.info("verifyResult:{}",verifyResult);

            if (verifyResult != null && verifyResult.getCode() == 200 && verifyResult.getData() != null) {
                List<TenantRolePermissionDTO> rolePermissions = verifyResult.getData();

                // 分类角色：继承权限变更的角色和不继承的角色
                List<Long> inheritRoleIds = new ArrayList<>();

                for (TenantRolePermissionDTO rolePermission : rolePermissions) {
                    // 是否继承权限变更(1:继承 0:不继承)
                    if (rolePermission.getIsInherit() != null && rolePermission.getIsInherit() == 1) {
                        // 继承权限变更，使用原有逻辑获取权限
                        inheritRoleIds.add(rolePermission.getRoleId());
                    } else {
                        // 不继承权限变更，从权限快照中获取权限
                        String permissionSnapshot = rolePermission.getPermissionSnapshot();
                        if (!StringUtils.isEmpty(permissionSnapshot)) {
                            try {
                                JSONObject snapshot = JSON.parseObject(permissionSnapshot);

                                // 获取权限ID列表
                                List<Long> permIds = snapshot.getJSONArray("permIds").toJavaList(Long.class);
                                if (!CollectionUtils.isEmpty(permIds)) {
                                    // 根据权限ID获取按钮和API权限
                                    List<String> roleButtons = permService.getButtonsByPermIds(permIds);
                                    List<String> roleApis = permService.getApisByPermIds(permIds);

                                    buttons.addAll(roleButtons);
                                    apis.addAll(roleApis);
                                }

                                // 获取数据策略ID列表
                                List<Long> dataPolicyIds = snapshot.getJSONArray("dataPolicyIds").toJavaList(Long.class);
                                if (!CollectionUtils.isEmpty(dataPolicyIds)) {
                                    // 根据数据策略ID获取数据策略
                                    List<PermDataPolicyDTO> policies = policyService.getPoliciesByIds(dataPolicyIds);
                                    if (!CollectionUtils.isEmpty(policies)) {
                                        for (PermDataPolicyDTO policy : policies) {
                                            dataPolicies.put(policy.getEffectTables(), policy);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // 解析权限快照失败，记录日志
                                log.error("Parse permission snapshot failed: " + e.getMessage(), e);
                            }
                        }
                    }
                }

                // 处理继承权限变更的角色
                if (!inheritRoleIds.isEmpty()) {
                    log.info("inheritRoleIds:{}",inheritRoleIds);
                    // 获取这些角色的操作权限
                    List<String> inheritButtons = permService.getButtonsByRoleIds(inheritRoleIds);
                    List<String> inheritApis = permService.getApisByRoleIds(inheritRoleIds);
                    log.info("inheritApis:{}",inheritApis);

                    buttons.addAll(inheritButtons);
                    apis.addAll(inheritApis);

                    // 获取这些角色的数据权限策略
                    List<PermDataPolicyDTO> inheritPolicies = policyService.getPoliciesByRoleIds(inheritRoleIds);
                    if (!CollectionUtils.isEmpty(inheritPolicies)) {
                        for (PermDataPolicyDTO policy : inheritPolicies) {
                            dataPolicies.put(policy.getEffectTables(), policy);
                        }
                    }
                }
            } else {
                // 验证失败或无结果，使用原有逻辑获取所有权限
                List<String> allButtons = permService.getUserPermButtons(sysUsers.getId());
                List<String> allApis = permService.getUserApiPerms(sysUsers.getId());
                Map<String, PermDataPolicyDTO> allPolicies = policyService.getUserDataPolicies(sysUsers.getId());

                buttons.addAll(allButtons);
                apis.addAll(allApis);
                dataPolicies.putAll(allPolicies);
            }
        } else {
            // 无租户ID或角色，使用原有逻辑获取所有权限
            List<String> allButtons = permService.getUserPermButtons(sysUsers.getId());
            List<String> allApis = permService.getUserApiPerms(sysUsers.getId());
            Map<String, PermDataPolicyDTO> allPolicies = policyService.getUserDataPolicies(sysUsers.getId());

            buttons.addAll(allButtons);
            apis.addAll(allApis);
            dataPolicies.putAll(allPolicies);
        }
        
        // 【新增】检查是否为新用户，如果是则分配基础权限
        if (DefaultPermissionUtil.isNewUser(roleIds, buttons, apis)) {
            log.info("检测到新用户 [{}]，分配默认基础权限", sysUsers.getLoginName());
//            DefaultPermissionUtil.assignDefaultPermissions(buttons, apis, dataPolicies);
            DefaultPermissionUtil.assignDefaultPermissions(buttons, apis);

            // 记录新用户权限分配日志
            log.info("新用户 [{}] 已分配基础权限: buttons={}, apis={}, dataPolicies={}", 
                    sysUsers.getLoginName(), buttons, apis, dataPolicies.keySet());
        }
        
        // 构建用户权限信息
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("roles", roleNames);
        authMap.put("role_ids", roleIds);
        authMap.put("apis", new ArrayList<>(apis));
        authMap.put("buttons", new ArrayList<>(buttons));
        authMap.put("data_perms", dataPolicies);

        // 添加用户平台类型信息，用于网关验证防止跨平台访问
        // 默认为未知类型
        Integer userPlatformType = 0;
        String userPlatformTypeName = "未知平台类型";

        // 获取租户类型信息
        if (tenantId != null) {
            try {
                ApiResult<TenantDTO> tenantResult = tenantFeignClient.getTenantById(tenantId);
                if (tenantResult != null && tenantResult.getCode() == 200 && tenantResult.getData() != null) {
                    TenantDTO tenant = tenantResult.getData();
                    // 提取租户类型作为用户平台类型
                    // 1：平台管理租户、2：企业商户租户、3：代理所租户
                    userPlatformType = tenant.getTenantType();
                    userPlatformTypeName = tenant.getTenantTypeName();
                }
            } catch (Exception e) {
                // 获取租户信息失败，记录日志但不影响登录流程
                log.error("Failed to get tenant info: " + e.getMessage(), e);
            }
        } else {
            // 无租户ID的情况，设置为游客类型
            userPlatformType = 0;
            userPlatformTypeName = "未关联租户";
        }

        // 只存储用户平台类型信息，简化网关判断逻辑
        authMap.put("user_platform_type", userPlatformType);
        authMap.put("user_platform_type_name", userPlatformTypeName);

        // 将用户权限信息存储到redis缓存，设置失效时间
        redisService.set("AUTH:PERMISSION:" + userDetails.getUsername(), JSON.toJSONString(authMap), jwtTokenUtil.getExpiration());

        return tokenMap;
    }

    @Override
    public Boolean logout() {
        //请求头获取token，通过token获取用户名，删除redis缓存
        String token = resolveToken(request);
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (redisService.hasKey("AUTH:TOKEN:" + username)) {
            redisService.del("AUTH:TOKEN:" + username);
            redisService.del("AUTH:PERMISSION:" + username);
            return true;
        }
        return false;
    }

    @Override
    public SysUserDTO register(SysUserRegisterDTO dto) {
        // 优化：使用一个查询检查所有重复项
        QueryWrapper<SysUsers> duplicateCheckWrapper = new QueryWrapper<>();
        
        // 构建OR条件：检查用户名、邮箱、手机号是否已存在
        duplicateCheckWrapper.lambda()
                .eq(SysUsers::getLoginName, dto.getLoginName());
        
        if (StringUtils.hasText(dto.getUserMail())) {
            duplicateCheckWrapper.lambda()
                    .or()
                    .eq(SysUsers::getUserMail, dto.getUserMail());
        }
        
        if (StringUtils.hasText(dto.getUserPhone())) {
            duplicateCheckWrapper.lambda()
                    .or()
                    .eq(SysUsers::getUserPhone, dto.getUserPhone());
        }
        
        List<SysUsers> existingUsers = list(duplicateCheckWrapper);
        
        // 检查具体的重复类型并给出精确的错误信息
        for (SysUsers existingUser : existingUsers) {
            if (dto.getLoginName().equals(existingUser.getLoginName())) {
                Asserts.fail("账号已存在，请更换其他账号");
            }
            if (StringUtils.hasText(dto.getUserMail()) && 
                dto.getUserMail().equals(existingUser.getUserMail())) {
                Asserts.fail("该邮箱已被注册，请使用其他邮箱");
            }
            if (StringUtils.hasText(dto.getUserPhone()) && 
                dto.getUserPhone().equals(existingUser.getUserPhone())) {
                Asserts.fail("该手机号已被注册，请使用其他手机号");
            }
        }

        // 数据封装
        SysUsers sysUsers = new SysUsers();
        BeanUtils.copyProperties(dto, sysUsers);
        sysUsers.setUserName("用户" + RandomUtil.randomString(8).toUpperCase());
        sysUsers.setCreateTime(System.currentTimeMillis());

        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(sysUsers.getPassword());
        log.info("新用户注册，加密后密码长度: {}", encodePassword.length());

        sysUsers.setPassword(encodePassword);
        baseMapper.insert(sysUsers);
        
        // 记录注册日志
        log.info("新用户注册成功: loginName={}, userMail={}, userPhone={}, userId={}", 
                dto.getLoginName(), dto.getUserMail(), dto.getUserPhone(), sysUsers.getId());
        
        // 不返回敏感数据
        sysUsers.setPassword("");
        
        //实现sysUsers映射到sysUserDTO
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(sysUsers, sysUserDTO);

        return sysUserDTO;
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(tokenHeader);
        // 判断是否为空，并且以 Bearer 开头
        if (StringUtils.hasText(header) && header.startsWith(tokenPrefix)) {
            return header.substring(tokenPrefix.length()).trim();
        }
        return null;
    }
}
