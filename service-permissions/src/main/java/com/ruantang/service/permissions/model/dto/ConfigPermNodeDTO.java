package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配置权限节点DTO
 */
@Data
@ApiModel(description = "配置权限节点DTO")
public class ConfigPermNodeDTO {
    
    @ApiModelProperty(value = "节点ID")
    private Long id;
    
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    
    @ApiModelProperty(value = "所属模块ID")
    private Long moduleId;
    
    @ApiModelProperty(value = "所属模块名称")
    private String moduleName;
    
    @ApiModelProperty(value = "节点类型(1=菜单项 2=操作按钮 3=数据字段)")
    private Integer nodeType;
    
    @ApiModelProperty(value = "节点类型名称")
    private String nodeTypeName;
    
    @ApiModelProperty(value = "数据权限类型(1=查看 2=编辑)")
    private Integer dataScope;
    
    @ApiModelProperty(value = "数据权限类型名称")
    private String dataScopeName;
    
    @ApiModelProperty(value = "绑定权限类型(0:操作权限 1:数据权限)")
    private Integer permType;
    
    @ApiModelProperty(value = "绑定权限类型名称")
    private String permTypeName;
    
    @ApiModelProperty(value = "绑定操作权限ID")
    private Long permId;
    
    @ApiModelProperty(value = "绑定操作权限详情")
    private PermDTO permDetail;
    
    @ApiModelProperty(value = "绑定数据权限ID")
    private Long dataPolicyId;
    
    @ApiModelProperty(value = "绑定数据权限详情")
    private PermDataPolicyDTO dataPolicyDetail;
    
    @ApiModelProperty(value = "是否基础权限(0=自定义 1=系统预置)")
    private Integer isBasic;
    
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
} 