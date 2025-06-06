package com.ruantang.service.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController", description = "认证中心")
//@CrossOrigin(origins = "https://hubhukvice.sealos.run", allowCredentials = "true",
//        allowedHeaders = {"Content-Type", "Authorization"},
//        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {

    @Resource
    private AuthService authService;

    @ApiOperation(value = "login", notes = "用户登录")
    @PostMapping("/login")
    public ApiResult<Object> login(@RequestBody SysUserRegisterDTO sysUserRegisterDTO) {
        Map<String, String> login = authService.login(sysUserRegisterDTO);
        return ApiResult.success(login);
    }

    @ApiOperation(value = "logout",notes = "用户登出")
    @PostMapping("/logout")
    public ApiResult<Boolean> logout(){
        Boolean logout = authService.logout();
        if (logout) {
            return ApiResult.success(true, "注销成功");
        } else {
            return ApiResult.failed("注销失败，用户未登录或token已失效");
        }
    }

    @ApiOperation("用户注册")
    @PostMapping(value = "/register")
    public ApiResult<SysUserDTO> register(@Validated @RequestBody SysUserRegisterDTO dto) {
        SysUserDTO sysUserDTO = authService.register(dto);
        if (sysUserDTO == null) {
            return ApiResult.failed("注册失败");
        }
        return ApiResult.success(sysUserDTO, "注册成功");
    }
}
