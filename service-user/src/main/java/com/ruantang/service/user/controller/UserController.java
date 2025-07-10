package com.ruantang.service.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.security.util.JwtTokenUtil;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.request.UserQueryRequest;
import com.ruantang.service.user.model.request.UserUpdateRequest;
import com.ruantang.service.user.model.request.PasswordUpdateRequest;
import com.ruantang.service.user.model.request.AccountDeactivateRequest;
import com.ruantang.service.user.service.SysRolesService;
import com.ruantang.service.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 * 提供用户相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/user/users")
@Api(value = "用户管理", tags = "用户管理接口")
public class UserController {

    @Resource
    private SysUserService SysUserService;
    
    @Resource
    private SysRolesService sysRolesService;
    
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    
    @Resource
    private HttpServletRequest request;
    
    @Value("${security.jwt.tokenHeader:Authorization}")
    private String tokenHeader;

    @Value("${security.jwt.tokenPrefix:Bearer }")
    private String tokenPrefix;

    /**
     * 获取当前登录用户信息
     * 
     * @return 当前登录用户的详细信息
     */
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info")
    public ApiResult<Map<String, Object>> getCurrentUserInfo() {
        // 从请求头获取token
        String token = request.getHeader(tokenHeader);
        if (token != null && token.startsWith(tokenPrefix)) {
            token = token.substring(tokenPrefix.length());
        }

        // 从token中获取用户名
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (username == null) {
            return ApiResult.failed("无效的token");
        }

        // 根据用户名获取用户信息
        SysUserDTO userInfo = SysUserService.getUserByUsername(username);
        if (userInfo == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 获取用户角色
        List<SysRoles> roles = sysRolesService.getUserRoles(userInfo.getId());
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("user", userInfo);
        result.put("roles", roles);
        
        return ApiResult.success(result);
    }
    
    /**
     * 分页查询用户列表
     * 
     * @param request 查询请求
     * @return 分页用户列表
     */
    @ApiOperation("分页查询用户列表")
    @PostMapping("/page")
    public ApiResult<Page<SysUserDTO>> getUserPage(@RequestBody UserQueryRequest request) {
        return SysUserService.queryUserPage(request);
    }
    
    /**
     * 根据ID查询用户详情
     * 
     * @param id 用户ID
     * @return 用户详情
     */
    @ApiOperation("根据ID查询用户详情")
    @GetMapping("/{id}")
    public ApiResult<SysUserDTO> getUserById(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable("id") Long id) {
        return SysUserService.getUserById(id);
    }
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> deleteUserById(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable("id") Long id) {
        return SysUserService.deleteUserById(id);
    }
    
    /**
     * 更新用户基本信息(这个接口允许其他用户更新对应用户信息【暂时不暴露到前端使用】)
     * 只允许修改非敏感的基本信息，如用户名、职级、性别、微信、QQ等
     * 
     * @param request 用户信息更新请求
     * @return 更新结果
     */
    @ApiOperation("更新用户基本信息")
    @PutMapping("/update-info")
    public ApiResult<Boolean> updateUserInfo(@Valid @RequestBody UserUpdateRequest request) {
        return SysUserService.updateUserInfo(request);
    }
    
    /**
     * 修改当前用户密码
     * 
     * @param request 密码修改请求
     * @return 修改结果
     */
    @ApiOperation("修改当前用户密码")
    @PutMapping("/update-password")
    public ApiResult<Boolean> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        // 获取当前用户ID
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return ApiResult.failed("无法获取当前用户信息");
        }
        
        return SysUserService.updatePassword(request, currentUserId);
    }
    
    /**
     * 获取当前用户的个人资料
     * 
     * @return 个人资料信息
     */
    @ApiOperation("获取当前用户的个人资料")
    @GetMapping("/profile")
    public ApiResult<SysUserDTO> getUserProfile() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return ApiResult.failed("无法获取当前用户信息");
        }
        
        return SysUserService.getUserById(currentUserId);
    }
    
    /**
     * 更新当前用户的个人资料
     * 
     * @param request 用户信息更新请求
     * @return 更新结果
     */
    @ApiOperation("更新当前用户的个人资料")
    @PutMapping("/profile")
    public ApiResult<Boolean> updateUserProfile(@Valid @RequestBody UserUpdateRequest request) {
        // 获取当前用户ID，确保只能修改自己的信息
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return ApiResult.failed("无法获取当前用户信息");
        }
        
        // 设置为当前用户ID，防止越权修改
        request.setId(currentUserId);
        
        return SysUserService.updateUserInfo(request);
    }
    
    /**
     * 查询未绑定租户和组织的用户列表
     * 用于组织绑定用户时查询可用的注册用户
     * 
     * @param request 查询请求
     * @return 未绑定用户的分页列表
     */
    @ApiOperation("查询未绑定租户和组织的用户列表")
    @PostMapping("/unbound")
    public ApiResult<Page<SysUserDTO>> getUnboundUsers(@RequestBody UserQueryRequest request) {
        return SysUserService.queryUnboundUsers(request);
    }
    
    /**
     * 账户注销 - 用户主动删除自己的账户
     * 这是一个危险操作，需要用户确认身份和意图
     * 
     * @param request 账户注销请求
     * @return 注销结果
     */
    @ApiOperation("账户注销 - 用户主动删除自己的账户")
    @PostMapping("/deactivate-account")
    public ApiResult<Boolean> deactivateAccount(@Valid @RequestBody AccountDeactivateRequest request) {
        // 获取当前用户ID
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return ApiResult.failed("无法获取当前用户信息");
        }
        
        return SysUserService.deactivateAccount(request, currentUserId);
    }
    
    /**
     * 从Token中获取当前用户ID
     * 
     * @return 当前用户ID
     */
    private Long getCurrentUserId() {
        try {
            // 从请求头获取token
            String token = request.getHeader(tokenHeader);
            if (token != null && token.startsWith(tokenPrefix)) {
                token = token.substring(tokenPrefix.length());
            }

            // 从token中获取用户名
            String username = jwtTokenUtil.getUserNameFromToken(token);
            if (username == null) {
                return null;
            }

            // 根据用户名获取用户信息
            SysUserDTO userInfo = SysUserService.getUserByUsername(username);
            return userInfo != null ? userInfo.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
} 