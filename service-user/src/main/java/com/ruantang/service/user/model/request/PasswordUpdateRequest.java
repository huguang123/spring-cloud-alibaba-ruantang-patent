package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 密码修改请求
 */
@Data
@ApiModel(description = "密码修改请求")
public class PasswordUpdateRequest {
    
    @ApiModelProperty(value = "当前密码", required = true)
    @NotBlank(message = "当前密码不能为空")
    private String oldPassword;
    
    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String newPassword;
    
    @ApiModelProperty(value = "确认新密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
} 