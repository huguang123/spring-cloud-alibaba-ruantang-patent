package com.ruantang.service.user.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.user.model.dto.UserDTO;
import com.ruantang.service.user.service.SysUserService;
import com.ruantang.security.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供用户相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private SysUserService userService;
    
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    
    @Resource
    private HttpServletRequest request;

    /**
     * 获取当前登录用户信息
     * 
     * @return 当前登录用户的详细信息
     */
    @GetMapping("/info")
    public ApiResult<UserDTO> getCurrentUserInfo() {
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 从token中获取用户名
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (username == null) {
            return ApiResult.failed("无效的token");
        }
        
        // 根据用户名获取用户信息
        UserDTO userInfo = userService.getUserByUsername(username);
        if (userInfo == null) {
            return ApiResult.failed("用户不存在");
        }
        
        return ApiResult.success(userInfo);
    }

    /**
     * 根据ID获取用户详情
     * 
     * @param id 用户ID
     * @return 用户详细信息，包含角色和组织信息
     */
    @GetMapping("/{id}")
    public ApiResult<UserDTO> getUserById(@PathVariable("id") Long id) {
        return ApiResult.success(userService.getUserById(id));
    }

    /**
     * 获取所有用户列表
     * 
     * @return 用户列表
     */
    @GetMapping
    public ApiResult<List<UserDTO>> listUsers() {
        return ApiResult.success(userService.listUsers());
    }

    /**
     * 创建新用户
     * 
     * @param userDTO 用户信息
     * @return 新创建的用户ID
     */
    @PostMapping
    public ApiResult<Long> createUser(@RequestBody UserDTO userDTO) {
        return ApiResult.success(userService.createUser(userDTO));
    }

    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param userDTO 更新的用户信息
     * @return 是否更新成功
     */
    @PutMapping("/{id}")
    public ApiResult<Boolean> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        boolean success = userService.updateUser(userDTO);
        return success ? ApiResult.success(true, "更新成功") : ApiResult.failed("更新失败");
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> deleteUser(@PathVariable("id") Long id) {
        boolean success = userService.deleteUser(id);
        return success ? ApiResult.success(true, "删除成功") : ApiResult.failed("删除失败");
    }

    /**
     * 为用户分配角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否分配成功
     */
    @PostMapping("/{userId}/roles/{roleId}")
    public ApiResult<Boolean> assignRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId) {
        boolean success = userService.assignRole(userId, roleId);
        return success ? ApiResult.success(true, "角色分配成功") : ApiResult.failed("角色分配失败");
    }

    /**
     * 移除用户的角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否移除成功
     */
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ApiResult<Boolean> removeRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId) {
        boolean success = userService.removeRole(userId, roleId);
        return success ? ApiResult.success(true, "角色移除成功") : ApiResult.failed("角色移除失败");
    }

    /**
     * 为用户分配组织
     * 
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否分配成功
     */
    @PostMapping("/{userId}/organizations/{orgId}")
    public ApiResult<Boolean> assignOrganization(@PathVariable("userId") Long userId, @PathVariable("orgId") Long orgId) {
        boolean success = userService.assignOrganization(userId, orgId);
        return success ? ApiResult.success(true, "组织分配成功") : ApiResult.failed("组织分配失败");
    }

    /**
     * 移除用户的组织
     * 
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否移除成功
     */
    @DeleteMapping("/{userId}/organizations/{orgId}")
    public ApiResult<Boolean> removeOrganization(@PathVariable("userId") Long userId, @PathVariable("orgId") Long orgId) {
        boolean success = userService.removeOrganization(userId, orgId);
        return success ? ApiResult.success(true, "组织移除成功") : ApiResult.failed("组织移除失败");
    }
} 