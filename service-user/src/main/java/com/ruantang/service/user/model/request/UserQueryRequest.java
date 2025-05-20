package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询请求
 */
@Data
@ApiModel(value = "用户查询请求", description = "用户查询请求参数")
public class UserQueryRequest {
    
    @ApiModelProperty(value = "关键字(支持用户名、手机号、邮箱模糊查询)")
    private String keyword;
    
    @ApiModelProperty(value = "用户名(精确查询)")
    private String userName;
    
    @ApiModelProperty(value = "手机号(精确查询)")
    private String userPhone;
    
    @ApiModelProperty(value = "邮箱(精确查询)")
    private String userMail;
    
    @ApiModelProperty(value = "性别(0:男 1:女)")
    private Integer gender;
    
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    
    @ApiModelProperty(value = "组织ID")
    private Long orgId;
    
    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;
} 