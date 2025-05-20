package com.ruantang.service.organization.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组织成员DTO
 */
@Data
@ApiModel(description = "组织成员DTO")
public class OrganizationMemberDTO {
    
    @ApiModelProperty(value = "用户ID")
    private Long id;
    
    @ApiModelProperty(value = "姓名")
    private String userName;
    
    @ApiModelProperty(value = "账号")
    private String loginName;
    
    @ApiModelProperty(value = "角色")
    private List<String> roles;
    
    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIds;
    
    @ApiModelProperty(value = "邮箱")
    private String userMail;
    
    @ApiModelProperty(value = "电话")
    private String userPhone;
    
    @ApiModelProperty(value = "入职日期")
    private Long joinDate;
} 