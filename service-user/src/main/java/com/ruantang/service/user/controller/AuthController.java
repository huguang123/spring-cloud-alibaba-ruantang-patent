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
@RequestMapping("/api/auth")
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
//        try {
        Map<String, String> login = authService.login(sysUserRegisterDTO);
        return ApiResult.success(login);
//        }catch (Exception e){
//            return ApiResult.failed("用户名或密码错误");
//        }
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
    public ApiResult<SysUserDTO> register(@Validated @RequestBody SysUserRegisterDTO dto) {
        SysUserDTO sysUserDTO = authService.register(dto);
        if (sysUserDTO == null) {
            return ApiResult.failed("注册失败");
        }
        return ApiResult.success(sysUserDTO, "注册成功");
    }
}
