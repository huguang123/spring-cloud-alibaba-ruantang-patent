package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.request.UserQueryRequest;

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
} 