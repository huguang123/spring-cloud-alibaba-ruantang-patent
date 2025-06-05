package com.ruantang.service.permissions.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 权限节点树DTO
 */
@Data
@ApiModel(description = "权限节点树DTO")
public class PermNodeTreeDTO {
    
    @ApiModelProperty(value = "操作权限模块列表")
    private List<PermModuleDTO> operationModules;
    
    @ApiModelProperty(value = "数据权限模块列表")
    private List<PermModuleDTO> dataModules;
    
    /**
     * 权限模块DTO
     */
    @Data
    @ApiModel(description = "权限模块DTO")
    public static class PermModuleDTO {
        
        @ApiModelProperty(value = "模块ID")
        private Long id;
        
        @ApiModelProperty(value = "模块名称")
        private String moduleName;
        
        @ApiModelProperty(value = "模块类型(1=功能权限模块 2=数据权限模块)")
        private Integer moduleType;
        
        @ApiModelProperty(value = "模块类型名称")
        private String moduleTypeName;
        
        @ApiModelProperty(value = "权限节点列表")
        private List<PermNodeDTO> nodes;
    }
    
    /**
     * 权限节点DTO
     */
    @Data
    @ApiModel(description = "权限节点DTO")
    public static class PermNodeDTO {
        
        @ApiModelProperty(value = "节点ID")
        private Long id;
        
        @ApiModelProperty(value = "节点名称")
        private String nodeName;
        
        @ApiModelProperty(value = "节点类型(1=菜单项 2=操作按钮 3=数据字段)")
        private Integer nodeType;
        
        @ApiModelProperty(value = "节点类型名称")
        private String nodeTypeName;
        
        @ApiModelProperty(value = "权限类型(0:操作权限 1:数据权限)")
        private Integer permType;
        
        @ApiModelProperty(value = "权限类型名称")
        private String permTypeName;
        
        @ApiModelProperty(value = "绑定操作权限ID")
        private Long permId;
        
        @ApiModelProperty(value = "绑定操作权限信息")
        private PermDTO permDetail;
        
        @ApiModelProperty(value = "绑定数据权限ID")
        private Long dataPolicyId;

        @ApiModelProperty(value = "排序号")
        private int sort;
        
        @ApiModelProperty(value = "绑定数据权限信息")
        private PermDataPolicyDTO dataPolicyDetail;
        
        @ApiModelProperty(value = "是否已选择(前端使用)")
        private Boolean selected = false;
    }
} 