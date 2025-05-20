package com.ruantang.service.organization.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组织DTO
 */
@Data
@ApiModel(description = "组织DTO")
public class OrganizationDTO {
    
    @ApiModelProperty(value = "组织ID")
    private Long id;
    
    @ApiModelProperty(value = "组织名称")
    private String orgName;
    
    @ApiModelProperty(value = "父组织ID")
    private Long parentId;
    
    @ApiModelProperty(value = "所属租户ID")
    private Long tenantId;
    
    @ApiModelProperty(value = "组织编码")
    private String orgCode;
    
    @ApiModelProperty(value = "层级路径")
    private String orgPath;
    
    @ApiModelProperty(value = "联系电话")
    private String orgPhone;
    
    @ApiModelProperty(value = "联系邮箱")
    private String orgMail;
    
    @ApiModelProperty(value = "状态（0：禁用、1：启用）")
    private Integer state;
    
    @ApiModelProperty(value = "状态名称")
    private String stateName;
    
    @ApiModelProperty(value = "备注说明")
    private String description;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
    
    @ApiModelProperty(value = "子组织")
    private List<OrganizationDTO> children;
    
    @ApiModelProperty(value = "成员数量")
    private Integer memberCount;
} 