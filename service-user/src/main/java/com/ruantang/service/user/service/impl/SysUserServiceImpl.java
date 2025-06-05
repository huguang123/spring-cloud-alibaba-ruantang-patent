package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.service.user.client.OrganizationFeignClient;
import com.ruantang.service.user.client.TenantFeignClient;
import com.ruantang.service.user.model.dto.OrganizationDTO;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.dto.TenantDTO;
import com.ruantang.service.user.model.request.UserQueryRequest;
import com.ruantang.service.user.model.request.UserUpdateRequest;
import com.ruantang.service.user.model.request.PasswordUpdateRequest;
import com.ruantang.service.user.model.request.AccountDeactivateRequest;
import com.ruantang.service.user.service.SysUserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers> implements SysUserService {

    @Resource
    @Lazy
    private PasswordEncoder passwordEncoder;
    
    @Resource
    private TenantFeignClient tenantFeignClient;
    
    @Resource
    private OrganizationFeignClient organizationFeignClient;
    
    @Override
    public SysUserDTO getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        // 查询用户
        LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUsers::getLoginName, username);
        SysUsers user = baseMapper.selectOne(queryWrapper);
        
        if (user == null) {
            return null;
        }
        
        // 转换为DTO
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 不返回敏感信息
        userDTO.setPassword(null);
        
        return userDTO;
    }
    
    @Override
    public ApiResult<Page<SysUserDTO>> queryUserPage(UserQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
        
        // 关键字搜索（用户名、手机号、邮箱模糊查询）
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                    .like(SysUsers::getUserName, request.getKeyword())
                    .or()
                    .like(SysUsers::getUserPhone, request.getKeyword())
                    .or()
                    .like(SysUsers::getUserMail, request.getKeyword())
            );
        }
        
        // 精确查询条件
        if (StringUtils.hasText(request.getUserName())) {
            queryWrapper.eq(SysUsers::getUserName, request.getUserName());
        }
        
        if (StringUtils.hasText(request.getUserPhone())) {
            queryWrapper.eq(SysUsers::getUserPhone, request.getUserPhone());
        }
        
        if (StringUtils.hasText(request.getUserMail())) {
            queryWrapper.eq(SysUsers::getUserMail, request.getUserMail());
        }
        
        if (request.getGender() != null) {
            queryWrapper.eq(SysUsers::getGender, request.getGender());
        }
        
        if (request.getTenantId() != null) {
            queryWrapper.eq(SysUsers::getTenantId, request.getTenantId());
        }
        
        if (request.getOrgId() != null) {
            queryWrapper.eq(SysUsers::getOrgId, request.getOrgId());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(SysUsers::getCreateTime);
        
        // 分页查询
        Page<SysUsers> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<SysUsers> userPage = baseMapper.selectPage(page, queryWrapper);
        
        // 转换结果
        Page<SysUserDTO> resultPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<SysUserDTO> userDTOList = userPage.getRecords().stream()
                .map(this::convertToDTOPage)
                .collect(Collectors.toList());
        resultPage.setRecords(userDTOList);
        
        return ApiResult.success(resultPage);
    }
    
    @Override
    public ApiResult<SysUserDTO> getUserById(Long id) {
        SysUsers user = baseMapper.selectById(id);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        SysUserDTO userDTO = convertToDTO(user);
        
        // 获取租户名称
        if (user.getTenantId() != null) {
            try {
                ApiResult<TenantDTO> tenantResult = tenantFeignClient.getTenantById(user.getTenantId());
                if (tenantResult != null && tenantResult.getCode() == 200 && tenantResult.getData() != null) {
                    userDTO.setTenantName(tenantResult.getData().getTenantName());
                } else {
                    log.warn("获取租户信息失败，租户ID: {}", user.getTenantId());
                }
            } catch (Exception e) {
                log.error("调用租户服务失败，租户ID: {}, error: {}", user.getTenantId(), e.getMessage(), e);
            }
        }
        
        // 获取组织名称
        if (user.getOrgId() != null) {
            try {
                ApiResult<OrganizationDTO> orgResult = organizationFeignClient.getOrganizationById(user.getOrgId());
                if (orgResult != null && orgResult.getCode() == 200 && orgResult.getData() != null) {
                    userDTO.setOrgName(orgResult.getData().getOrgName());
                } else {
                    log.warn("获取组织信息失败，组织ID: {}", user.getOrgId());
                }
            } catch (Exception e) {
                log.error("调用组织服务失败，组织ID: {}, error: {}", user.getOrgId(), e.getMessage(), e);
            }
        }
        
        return ApiResult.success(userDTO);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteUserById(Long id) {
        SysUsers user = baseMapper.selectById(id);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        baseMapper.deleteById(id);
        return ApiResult.success(true);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateUserInfo(UserUpdateRequest request) {
        // 验证用户是否存在
        SysUsers existingUser = baseMapper.selectById(request.getId());
        if (existingUser == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 只更新允许修改的字段
        SysUsers updateUser = new SysUsers();
        updateUser.setId(request.getId());
        
        if (StringUtils.hasText(request.getUserName())) {
            updateUser.setUserName(request.getUserName());
        }
        
        if (request.getLevel() != null) {
            updateUser.setLevel(request.getLevel());
        }
        
        if (request.getGender() != null) {
            updateUser.setGender(request.getGender());
        }
        
        if (StringUtils.hasText(request.getWeixin())) {
            updateUser.setWeixin(request.getWeixin());
        }
        
        if (StringUtils.hasText(request.getQq())) {
            updateUser.setQq(request.getQq());
        }
        
        // 设置更新时间
        updateUser.setUpdateTime(System.currentTimeMillis());
        
        // 执行更新
        int result = baseMapper.updateById(updateUser);
        if (result > 0) {
            return ApiResult.success(true);
        } else {
            return ApiResult.failed("更新失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updatePassword(PasswordUpdateRequest request, Long currentUserId) {
        // 验证用户是否存在
        SysUsers user = baseMapper.selectById(currentUserId);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 验证当前密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ApiResult.failed("当前密码错误");
        }
        
        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ApiResult.failed("两次输入的新密码不一致");
        }
        
        // 验证新密码不能与当前密码相同
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return ApiResult.failed("新密码不能与当前密码相同");
        }
        
        // 加密新密码
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        
        // 更新密码
        SysUsers updateUser = new SysUsers();
        updateUser.setId(currentUserId);
        updateUser.setPassword(encodedPassword);
        updateUser.setUpdateTime(System.currentTimeMillis());
        
        int result = baseMapper.updateById(updateUser);
        if (result > 0) {
            return ApiResult.success(true);
        } else {
            return ApiResult.failed("密码修改失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deactivateAccount(AccountDeactivateRequest request, Long currentUserId) {
        // 验证用户是否存在
        SysUsers user = baseMapper.selectById(currentUserId);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 验证当前密码
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ApiResult.failed("密码错误，无法注销账户");
        }
        
        // 验证确认文本
        if (!"确认注销".equals(request.getConfirmText())) {
            return ApiResult.failed("请输入'确认注销'以确认操作");
        }
        
        // 验证注销原因不能为空
        if (!StringUtils.hasText(request.getReason()) || request.getReason().trim().length() < 5) {
            return ApiResult.failed("注销原因至少需要5个字符");
        }
        
        try {
            // 软删除：更新账号状态，而不是直接删除记录
            // 1. 将登录名改为已注销状态（防止重复注册）
            String deactivatedLoginName = user.getLoginName() + "_DEACTIVATED_" + System.currentTimeMillis();
            
            // 2. 清空或标记敏感信息
            SysUsers updateUser = new SysUsers();
            updateUser.setId(currentUserId);
            updateUser.setLoginName(deactivatedLoginName);
            updateUser.setPassword("DEACTIVATED"); // 密码置为无效值
            updateUser.setUserName("已注销用户");
            updateUser.setUserPhone(null);
            updateUser.setUserMail(null);
            updateUser.setWeixin(null);
            updateUser.setQq(null);
            updateUser.setUpdateTime(System.currentTimeMillis());
            
            // 可以添加一个状态字段来标记账户已注销（如果表结构支持）
            // updateUser.setStatus(9); // 9表示已注销
            
            // 执行更新
            int result = baseMapper.updateById(updateUser);
            
            if (result > 0) {
                // 记录注销日志（可以考虑插入到操作日志表）
                log.info("用户账户注销成功: userId={}, loginName={}, reason={}", 
                        currentUserId, user.getLoginName(), request.getReason());
                
                // 可以在这里添加其他清理逻辑，如：
                // 1. 清理用户角色关联
                // 2. 清理用户权限缓存
                // 3. 发送注销确认邮件等
                
                return ApiResult.success(true, "账户注销成功");
            } else {
                return ApiResult.failed("账户注销失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("账户注销失败: userId={}, error={}", currentUserId, e.getMessage(), e);
            return ApiResult.failed("账户注销失败，系统异常");
        }
    }
    
    /**
     * 将用户实体转换为DTO
     * 
     * @param user 用户实体
     * @return 用户DTO
     */
    private SysUserDTO convertToDTO(SysUsers user) {
        if (user == null) {
            return null;
        }
        
        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 不返回敏感信息
        userDTO.setPassword(null);
        
        return userDTO;
    }

    /**
     * 将用户实体转换为DTO
     *
     * @param user 用户实体
     * @return 用户DTO
     */
    private SysUserDTO convertToDTOPage(SysUsers user) {
        if (user == null) {
            return null;
        }

        SysUserDTO userDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 不返回敏感信息
        userDTO.setPassword(null);
        userDTO.setUserMail(null);
        userDTO.setUserPhone(null);
        userDTO.setGender(null);
        userDTO.setLevel(null);
        userDTO.setCreateBy(null);
        userDTO.setCreateTime(null);
        userDTO.setUpdateTime(null);
        userDTO.setTenantId(null);
        userDTO.setOrgId(null);
        userDTO.setWeixin(null);
        userDTO.setQq(null);
        userDTO.setLoginName(null);


        return userDTO;
    }
} 