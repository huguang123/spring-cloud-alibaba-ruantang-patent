package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 账户注销请求
 * 用户主动删除自己的账户
 */
@Data
@ApiModel(description = "账户注销请求")
public class AccountDeactivateRequest {
    
    @ApiModelProperty(value = "当前密码", required = true, notes = "用于验证用户身份")
    @NotBlank(message = "请输入当前密码以确认身份")
    private String currentPassword;
    
    @ApiModelProperty(value = "注销原因", required = true, notes = "用户注销账户的原因")
    @NotBlank(message = "请填写注销原因")
    private String reason;
    
    @ApiModelProperty(value = "确认注销", required = true, notes = "用户需要确认注销操作")
    @NotBlank(message = "请输入'确认注销'以确认操作")
    private String confirmText;
} 