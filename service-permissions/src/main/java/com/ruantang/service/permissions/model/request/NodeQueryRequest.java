package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 节点查询请求
 */
@Data
@ApiModel(description = "节点查询请求")
public class NodeQueryRequest {
    
    @ApiModelProperty(value = "所属模块ID")
    private Long moduleId;
    
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    
    @ApiModelProperty(value = "节点类型(1=菜单项 2=操作按钮 3=数据字段)")
    private Integer nodeType;
    
    @ApiModelProperty(value = "绑定权限类型(0:操作权限 1:数据权限)")
    private Integer permType;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;
    
    @ApiModelProperty(value = "每页记录数", example = "10")
    private Integer pageSize = 10;
} 