package com.ruantang.service.tenant.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户模板查询请求
 */
@Data
@ApiModel(value = "租户模板查询请求", description = "租户模板查询参数")
public class TemplateQueryRequest {
    
    @ApiModelProperty("模板名称（支持模糊查询）")
    private String templateName;
    
    @ApiModelProperty("模板编码")
    private String templateCode;
    
    @ApiModelProperty("模板类型（1：平台租户模板、2：企业租户模板、3：代理所租户模板）")
    private Integer templateType;
    
    @ApiModelProperty("行业类型(0:教育 1:医疗 2:金融)")
    private Integer industryType;
    
    @ApiModelProperty("是否系统内置")
    private Integer isSystem;
    
    @ApiModelProperty("当前页码")
    private Integer pageNum = 1;
    
    @ApiModelProperty("每页记录数")
    private Integer pageSize = 10;
} 