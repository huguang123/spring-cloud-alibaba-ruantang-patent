package com.ruantang.service.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.commons.exception.Asserts;
import com.ruantang.commons.service.RedisService;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.security.util.JwtTokenUtil;
import com.ruantang.service.user.domain.SysUserDetails;
import com.ruantang.service.user.model.dto.PermDataPolicyDTO;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.service.AuthService;
import com.ruantang.service.user.service.PermDataPolicyService;
import com.ruantang.service.user.service.PermService;
import com.ruantang.service.user.service.SysRolesService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
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
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> login(SysUserRegisterDTO sysUserRegisterDTO) {

        //检查是否存在账号对应用户
        SysUsers sysUsers = sysUsersMapper.selectOne(new LambdaQueryWrapper<SysUsers>().eq(SysUsers::getLoginName, sysUserRegisterDTO.getLoginName()));
        System.out.println("sysUsers="+sysUsers);
        System.out.println("测试日志更新时间提醒");
        if (Objects.isNull(sysUsers)) {
            System.out.println("怪事件");
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        //进行用户认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(sysUserRegisterDTO.getLoginName(), sysUserRegisterDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println("authenticate="+authenticate);
        //认证未通过
        if (Objects.isNull(authenticate)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        //通过验证，生成jwt
        UserDetails userDetails = new SysUserDetails(sysUsers, sysRolesService.getRolesList());
        String token = jwtTokenUtil.generateToken(userDetails);
        System.out.println("token="+token);
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
        
        // 获取用户操作权限按钮标识列表
        List<String> buttons = permService.getUserPermButtons(sysUsers.getId());
        
        // 获取用户API权限列表
        List<String> apis = permService.getUserApiPerms(sysUsers.getId());
        
        // 获取用户数据权限策略
        Map<String, PermDataPolicyDTO> dataPolicies = policyService.getUserDataPolicies(sysUsers.getId());
        
        // 构建用户权限信息
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("roles", roleNames);
        authMap.put("role_ids", roleIds);
        authMap.put("apis", apis);
        authMap.put("buttons", buttons);
        authMap.put("data_perms", dataPolicies);

        // 将用户权限信息存储到redis缓存，设置失效时间
        redisService.set("AUTH:PERMISSION:" + userDetails.getUsername(), authMap, jwtTokenUtil.getExpiration());

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
        // 查询是否有相同用户名的用户
        QueryWrapper<SysUsers> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUsers::getLoginName, dto.getLoginName());
        List<SysUsers> sysUsersList = list(wrapper);
        if (sysUsersList.size() > 0) {
            Asserts.fail("帐号已存在");
        }

        // 数据封装
        SysUsers sysUsers = new SysUsers();
        BeanUtils.copyProperties(dto, sysUsers);
        sysUsers.setUserName("用户" + RandomUtil.randomString(8).toUpperCase());
        sysUsers.setCreateTime(System.currentTimeMillis());

        // 将密码进行加密操作
//        passwordEncoder.
        String encodePassword = passwordEncoder.encode(sysUsers.getPassword());
        System.out.println(encodePassword);

        sysUsers.setPassword(encodePassword);
        baseMapper.insert(sysUsers);
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
