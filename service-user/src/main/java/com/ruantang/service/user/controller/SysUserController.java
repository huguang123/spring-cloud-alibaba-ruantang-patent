package com.ruantang.service.user.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(value = "SysUserController", description = "用户中心")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @ApiOperation("测试接口")
    @GetMapping("/test")
    public ApiResult<String> test() {
        return ApiResult.success("测试接口成功");
    }




}
