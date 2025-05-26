package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 节点更新请求
 */
@Data
@ApiModel(description = "节点更新请求")
public class NodeUpdateRequest {
    
    @NotNull(message = "节点ID不能为空")
    @ApiModelProperty(value = "节点ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    
    @ApiModelProperty(value = "节点类型(1=菜单项 2=操作按钮 3=数据字段)")
    private Integer nodeType;
    
    @ApiModelProperty(value = "绑定权限类型(0:操作权限 1:数据权限)")
    private Integer permType;
    
    @ApiModelProperty(value = "绑定操作权限ID")
    private Long permId;
    
    @ApiModelProperty(value = "绑定数据权限ID")
    private Long dataPolicyId;
    
    @ApiModelProperty(value = "是否基础权限(0=自定义 1=系统预置)")
    private Integer isBasic;
} 