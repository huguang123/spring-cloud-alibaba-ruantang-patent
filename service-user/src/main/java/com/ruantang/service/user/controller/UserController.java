package com.ruantang.service.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.security.util.JwtTokenUtil;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.request.UserQueryRequest;
import com.ruantang.service.user.service.SysRolesService;
import com.ruantang.service.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
} 