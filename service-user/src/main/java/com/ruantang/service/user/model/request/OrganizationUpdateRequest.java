package com.ruantang.service.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新组织请求
 */
@Data
@ApiModel(description = "更新组织请求")
public class OrganizationUpdateRequest {
    
    @NotNull(message = "组织ID不能为空")
    @ApiModelProperty(value = "组织ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "组织名称")
    private String orgName;
    
    @ApiModelProperty(value = "组织编码")
    private String orgCode;
    
    @ApiModelProperty(value = "联系电话")
    private String orgPhone;
    
    @ApiModelProperty(value = "联系邮箱")
    private String orgMail;
    
    @ApiModelProperty(value = "状态（0：禁用、1：启用）")
    private Integer state;
    
    @ApiModelProperty(value = "备注说明")
    private String description;
} 