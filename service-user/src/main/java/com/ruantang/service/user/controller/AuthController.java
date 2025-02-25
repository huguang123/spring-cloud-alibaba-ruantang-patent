package com.ruantang.service.user.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController", description = "认证中心")
public class AuthController {

    @Resource
    private AuthService authService;

    @ApiOperation(value = "login", notes = "用户登录")
    @PostMapping("/login")
    public ApiResult<Object> login(@RequestBody SysUserRegisterDTO sysUserRegisterDTO) {
        try {
        Map<String, String> login = authService.login(sysUserRegisterDTO);
        return ApiResult.success(login);
        }catch (Exception e){
            return ApiResult.failed("用户名或密码错误");
        }
    }

    @ApiOperation(value = "logout",notes = "用户登出")
    @PostMapping("/logout")
    public ApiResult<Object> logout(){
        Boolean logout = authService.logout();

        if (logout){
            return ApiResult.success(logout);
        }else {
            return ApiResult.failed();
        }
    }

    @ApiOperation("用户注册")
    @PostMapping(value = "/register")
    public ApiResult<SysUsers> register(@Validated @RequestBody SysUserRegisterDTO dto) {
        SysUsers sysUsers  = authService.register(dto);
        if (sysUsers == null) {
            return ApiResult.failed("注册失败");
        }
        return ApiResult.success(sysUsers, "注册成功");
    }
}
