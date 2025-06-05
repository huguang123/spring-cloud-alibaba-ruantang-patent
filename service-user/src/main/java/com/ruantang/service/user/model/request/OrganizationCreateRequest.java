package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建组织请求
 */
@Data
@ApiModel(description = "创建组织请求")
public class OrganizationCreateRequest {
    
    @NotBlank(message = "组织名称不能为空")
    @ApiModelProperty(value = "组织名称", required = true)
    private String orgName;
    
    @ApiModelProperty(value = "父组织ID（默认为0表示顶级组织）")
    private Long parentId = 0L;
    
    @NotNull(message = "所属租户ID不能为空")
    @ApiModelProperty(value = "所属租户ID", required = true)
    private Long tenantId;
    
    @NotBlank(message = "组织编码不能为空")
    @ApiModelProperty(value = "组织编码", required = true)
    private String orgCode;
    
    @ApiModelProperty(value = "联系电话")
    private String orgPhone;
    
    @ApiModelProperty(value = "联系邮箱")
    private String orgMail;
    
    @ApiModelProperty(value = "状态（0：禁用、1：启用）", required = true)
    private Integer state = 1;
    
    @ApiModelProperty(value = "备注说明")
    private String description;
} 