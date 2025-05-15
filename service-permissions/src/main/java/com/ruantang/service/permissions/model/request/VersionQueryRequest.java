package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 版本查询请求
 */
@Data
@ApiModel(description = "版本查询请求")
public class VersionQueryRequest {
    
    @ApiModelProperty(value = "模板ID")
    private Long templateId;
    
    @ApiModelProperty(value = "是否默认版本(0:否 1:是)")
    private Integer isDefault;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页记录数", example = "10")
    private Integer pageSize = 10;
}
