package com.ruantang.service.permissions.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 节点创建请求
 */
@Data
@ApiModel(description = "节点创建请求")
public class NodeCreateRequest {
    
    @NotBlank(message = "节点名称不能为空")
    @ApiModelProperty(value = "节点名称", required = true)
    private String nodeName;
    
    @NotNull(message = "所属模块ID不能为空")
    @ApiModelProperty(value = "所属模块ID", required = true)
    private Long moduleId;
    
    @NotNull(message = "节点类型不能为空")
    @ApiModelProperty(value = "节点类型(1=菜单项 2=操作按钮 3=数据字段)", required = true)
    private Integer nodeType;
    
    @NotNull(message = "绑定权限类型不能为空")
    @ApiModelProperty(value = "绑定权限类型(0:操作权限 1:数据权限)", required = true)
    private Integer permType;
    
    @ApiModelProperty(value = "绑定操作权限ID(当permType=0时必填)")
    private Long permId;
    
    @ApiModelProperty(value = "绑定数据权限ID(当permType=1时必填)")
    private Long dataPolicyId;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;
    
    @ApiModelProperty(value = "是否基础权限(0=自定义 1=系统预置)")
    private Integer isBasic = 0;
} 