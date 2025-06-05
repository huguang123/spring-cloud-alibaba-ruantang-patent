package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 用户信息更新请求
 * 只允许修改非敏感的基本信息
 */
@Data
@ApiModel(description = "用户信息更新请求")
public class UserUpdateRequest {
    
    @ApiModelProperty(value = "用户ID", required = true)
//    @NotNull(message = "用户ID不能为空")
    private Long id;
    
    @ApiModelProperty(value = "用户名")
    private String userName;
    
    @ApiModelProperty(value = "职级")
    private Integer level;
    
    @ApiModelProperty(value = "性别（0：男，1：女）")
    private Integer gender;
    
    @ApiModelProperty(value = "微信联系方式")
    private String weixin;
    
    @ApiModelProperty(value = "QQ联系方式")  
    private String qq;
} 