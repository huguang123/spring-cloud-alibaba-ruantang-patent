package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.request.UserQueryRequest;
import com.ruantang.service.user.model.request.UserUpdateRequest;
import com.ruantang.service.user.model.request.PasswordUpdateRequest;
import com.ruantang.service.user.model.request.AccountDeactivateRequest;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUsers> {
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户DTO
     */
    SysUserDTO getUserByUsername(String username);
    
    /**
     * 分页查询用户
     * 
     * @param request 查询请求
     * @return 分页用户列表
     */
    ApiResult<Page<SysUserDTO>> queryUserPage(UserQueryRequest request);
    
    /**
     * 查询未绑定租户和组织的用户
     * 
     * @param request 查询请求
     * @return 未绑定用户的分页列表
     */
    ApiResult<Page<SysUserDTO>> queryUnboundUsers(UserQueryRequest request);
    
    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户详情
     */
    ApiResult<SysUserDTO> getUserById(Long id);
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteUserById(Long id);
    
    /**
     * 更新用户基本信息
     * 
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updateUserInfo(UserUpdateRequest request);
    
    /**
     * 修改用户密码
     * 
     * @param request 密码修改请求
     * @param currentUserId 当前用户ID
     * @return 修改结果
     */
    ApiResult<Boolean> updatePassword(PasswordUpdateRequest request, Long currentUserId);
    
    /**
     * 账户注销 - 用户主动删除自己的账户
     * 
     * @param request 注销请求
     * @param currentUserId 当前用户ID
     * @return 注销结果
     */
    ApiResult<Boolean> deactivateAccount(AccountDeactivateRequest request, Long currentUserId);
} 