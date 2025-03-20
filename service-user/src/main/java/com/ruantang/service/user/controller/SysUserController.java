package com.ruantang.service.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(value = "SysUserController", description = "用户中心")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;


    @ApiOperation("修改用户信息")
    @PostMapping("/update")
    public ApiResult<Boolean> update(@RequestBody @Validated SysUserDTO sysUserDTO) {
        //将sysUserDTO转换为SysUsers，并调用service层修改用户信息如何
        SysUsers sysUsers = new SysUsers();
        BeanUtil.copyProperties(sysUserDTO, sysUsers);
        return ApiResult.success(sysUserService.updateById(sysUsers));
    }

    @ApiOperation("根据id查询用户信息")
    @GetMapping("/{id}")
    public ApiResult<SysUserDTO> getById(@PathVariable Long id) {
        SysUsers sysUsers = sysUserService.getById(id);
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtil.copyProperties(sysUsers, sysUserDTO);
        return ApiResult.success(sysUserDTO);
    }

    @ApiOperation("根据id删除用户信息")
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        //实现逻辑删除，修改isDeleted字段为true
        return ApiResult.success(sysUserService.update().eq("id", id).set("is_deleted", true).update());
    }

    @ApiOperation("根据id批量删除用户信息")
    @DeleteMapping("/batch/{ids}")
    public ApiResult<Boolean> batchDelete(@PathVariable String ids) {
        //实现逻辑删除，修改isDeleted字段为true
        String[] split = ids.split(",");
        return ApiResult.success(sysUserService.update().in("id", split).set("is_deleted", true).update());
    }

    @ApiOperation("根据id批量恢复用户信息")
    @DeleteMapping("/batch/recover/{ids}")
    public ApiResult<Boolean> batchRecover(@PathVariable String ids) {
        //实现逻辑删除，修改isDeleted字段为false
        String[] split = ids.split(",");
        return ApiResult.success(sysUserService.update().in("id", split).set("is_deleted", false).update());
    }


    @ApiOperation("测试接口")
    @GetMapping("/test")
    public ApiResult<String> test() {
        return ApiResult.success("测试接口成功");
    }


}
