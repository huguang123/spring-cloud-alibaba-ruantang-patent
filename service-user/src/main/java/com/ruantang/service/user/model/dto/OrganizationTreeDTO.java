package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组织树DTO
 */
@Data
@ApiModel(description = "组织树DTO")
public class OrganizationTreeDTO {
    
    @ApiModelProperty(value = "组织ID")
    private Long id;
    
    @ApiModelProperty(value = "组织名称")
    private String orgName;
    
    @ApiModelProperty(value = "组织编码")
    private String orgCode;
    
    @ApiModelProperty(value = "成员数量")
    private Integer memberCount;
    
    @ApiModelProperty(value = "子组织")
    private List<OrganizationTreeDTO> children;
} 