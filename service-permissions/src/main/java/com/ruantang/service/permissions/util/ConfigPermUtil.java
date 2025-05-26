package com.ruantang.service.permissions.util;

import com.ruantang.entity.perm.ConfigPermModule;
import com.ruantang.entity.perm.ConfigPermNode;
import com.ruantang.entity.perm.ConfigPermTemplate;
import com.ruantang.entity.perm.ConfigPermVersion;
import com.ruantang.service.permissions.model.dto.ConfigPermModuleDTO;
import com.ruantang.service.permissions.model.dto.ConfigPermNodeDTO;
import com.ruantang.service.permissions.model.dto.ConfigPermTemplateDTO;
import com.ruantang.service.permissions.model.dto.ConfigPermVersionDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置权限工具类
 */
public class ConfigPermUtil {

    /**
     * 获取模板类型名称
     * @param templateType 模板类型值
     * @return 类型名称
     */
    public static String getTemplateTypeName(Integer templateType) {
        if (templateType == null) {
            return "";
        }
        
        switch (templateType) {
            case 1:
                return "平台配置模板";
            case 2:
                return "企业租户配置模板";
            case 3:
                return "代理所配置模板";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取模块类型名称
     * @param moduleType 模块类型值
     * @return 类型名称
     */
    public static String getModuleTypeName(Integer moduleType) {
        if (moduleType == null) {
            return "";
        }
        
        switch (moduleType) {
            case 1:
                return "功能权限模块";
            case 2:
                return "数据权限模块";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取节点类型名称
     * @param nodeType 节点类型值
     * @return 类型名称
     */
    public static String getNodeTypeName(Integer nodeType) {
        if (nodeType == null) {
            return "";
        }
        
        switch (nodeType) {
            case 1:
                return "菜单项";
            case 2:
                return "操作按钮";
            case 3:
                return "数据字段";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取数据权限类型名称
     * @param dataScope 数据权限类型值
     * @return 类型名称
     */
    public static String getDataScopeName(Integer dataScope) {
        if (dataScope == null) {
            return "";
        }
        
        switch (dataScope) {
            case 1:
                return "查看";
            case 2:
                return "编辑";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取权限类型名称
     * @param permType 权限类型值
     * @return 类型名称
     */
    public static String getPermTypeName(Integer permType) {
        if (permType == null) {
            return "";
        }
        
        switch (permType) {
            case 0:
                return "操作权限";
            case 1:
                return "数据权限";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 配置权限模板实体转DTO
     * @param template 配置权限模板实体
     * @return 配置权限模板DTO
     */
    public static ConfigPermTemplateDTO convertToTemplateDTO(ConfigPermTemplate template) {
        if (template == null) {
            return null;
        }
        
        ConfigPermTemplateDTO dto = new ConfigPermTemplateDTO();
        BeanUtils.copyProperties(template, dto);
        
        // 设置类型名称
        dto.setTemplateTypeName(getTemplateTypeName(template.getTemplateType()));
        
        return dto;
    }
    
    /**
     * 配置权限模板实体列表转DTO列表
     * @param templates 配置权限模板实体列表
     * @return 配置权限模板DTO列表
     */
    public static List<ConfigPermTemplateDTO> convertToTemplateDTOList(List<ConfigPermTemplate> templates) {
        List<ConfigPermTemplateDTO> dtoList = new ArrayList<>();
        if (templates == null || templates.isEmpty()) {
            return dtoList;
        }
        
        for (ConfigPermTemplate template : templates) {
            dtoList.add(convertToTemplateDTO(template));
        }
        
        return dtoList;
    }
    
    /**
     * 模板版本配置实体转DTO
     * @param version 模板版本配置实体
     * @return 模板版本配置DTO
     */
    public static ConfigPermVersionDTO convertToVersionDTO(ConfigPermVersion version) {
        if (version == null) {
            return null;
        }
        
        ConfigPermVersionDTO dto = new ConfigPermVersionDTO();
        BeanUtils.copyProperties(version, dto);
        
        return dto;
    }
    
    /**
     * 模板版本配置实体列表转DTO列表
     * @param versions 模板版本配置实体列表
     * @return 模板版本配置DTO列表
     */
    public static List<ConfigPermVersionDTO> convertToVersionDTOList(List<ConfigPermVersion> versions) {
        List<ConfigPermVersionDTO> dtoList = new ArrayList<>();
        if (versions == null || versions.isEmpty()) {
            return dtoList;
        }
        
        for (ConfigPermVersion version : versions) {
            dtoList.add(convertToVersionDTO(version));
        }
        
        return dtoList;
    }
    
    /**
     * 配置权限模块实体转DTO
     * @param module 配置权限模块实体
     * @return 配置权限模块DTO
     */
    public static ConfigPermModuleDTO convertToModuleDTO(ConfigPermModule module) {
        if (module == null) {
            return null;
        }
        
        ConfigPermModuleDTO dto = new ConfigPermModuleDTO();
        BeanUtils.copyProperties(module, dto);
        
        // 设置类型名称
        dto.setModuleTypeName(getModuleTypeName(module.getModuleType()));
        
        return dto;
    }
    
    /**
     * 配置权限模块实体列表转DTO列表
     * @param modules 配置权限模块实体列表
     * @return 配置权限模块DTO列表
     */
    public static List<ConfigPermModuleDTO> convertToModuleDTOList(List<ConfigPermModule> modules) {
        List<ConfigPermModuleDTO> dtoList = new ArrayList<>();
        if (modules == null || modules.isEmpty()) {
            return dtoList;
        }
        
        for (ConfigPermModule module : modules) {
            dtoList.add(convertToModuleDTO(module));
        }
        
        return dtoList;
    }
    
    /**
     * 配置权限节点实体转DTO
     * @param node 配置权限节点实体
     * @return 配置权限节点DTO
     */
    public static ConfigPermNodeDTO convertToNodeDTO(ConfigPermNode node) {
        if (node == null) {
            return null;
        }
        
        ConfigPermNodeDTO dto = new ConfigPermNodeDTO();
        BeanUtils.copyProperties(node, dto);
        
        // 设置类型名称
        dto.setNodeTypeName(getNodeTypeName(node.getNodeType()));
        dto.setPermTypeName(getPermTypeName(node.getPermType()));
        
        return dto;
    }
    
    /**
     * 配置权限节点实体列表转DTO列表
     * @param nodes 配置权限节点实体列表
     * @return 配置权限节点DTO列表
     */
    public static List<ConfigPermNodeDTO> convertToNodeDTOList(List<ConfigPermNode> nodes) {
        List<ConfigPermNodeDTO> dtoList = new ArrayList<>();
        if (nodes == null || nodes.isEmpty()) {
            return dtoList;
        }
        
        for (ConfigPermNode node : nodes) {
            dtoList.add(convertToNodeDTO(node));
        }
        
        return dtoList;
    }
} 