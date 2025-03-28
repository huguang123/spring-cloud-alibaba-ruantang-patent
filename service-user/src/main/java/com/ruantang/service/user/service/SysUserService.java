package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 * 提供用户相关的业务操作
 */
public interface SysUserService extends IService<SysUsers> {

    /**
     * 获取用户数据（用于Spring Security认证）
     *
     * @param loginName 登录账号
     * @return UserDetails对象
     */
    UserDetails loadUserByUsername(String loginName);

    /**
     * 获取用户的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRoles> getRolesList(String userId);

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息DTO，包含角色和组织信息
     */
    UserDTO getUserById(Long id);
    
    /**
     * 获取所有用户列表
     *
     * @return 用户列表
     */
    List<UserDTO> listUsers();
    
    /**
     * 创建新用户
     *
     * @param userDTO 用户信息
     * @return 新创建的用户ID
     */
    Long createUser(UserDTO userDTO);
    
    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     * @return 是否更新成功
     */
    boolean updateUser(UserDTO userDTO);
    
    /**
     * 删除用户
     * 同时会删除用户的角色关系和组织关系
     *
     * @param id 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long id);
    
    /**
     * 为用户分配角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否分配成功
     */
    boolean assignRole(Long userId, Long roleId);
    
    /**
     * 移除用户的角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否移除成功
     */
    boolean removeRole(Long userId, Long roleId);
    
    /**
     * 为用户分配组织
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否分配成功
     */
    boolean assignOrganization(Long userId, Long orgId);
    
    /**
     * 移除用户的组织
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否移除成功
     */
    boolean removeOrganization(Long userId, Long orgId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息DTO
     */
    UserDTO getUserByUsername(String username);
}
