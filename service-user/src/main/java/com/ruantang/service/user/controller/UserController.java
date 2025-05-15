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
} 