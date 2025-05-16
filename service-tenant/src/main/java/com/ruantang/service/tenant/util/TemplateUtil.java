package com.ruantang.service.tenant.util;

import com.ruantang.entity.tenant.TenantRelTemplateRole;
import com.ruantang.entity.tenant.TenantTemplate;
import com.ruantang.service.tenant.model.dto.TenantTemplateDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateRoleDTO;
import com.ruantang.service.tenant.model.request.TemplateCreateRequest;
import com.ruantang.service.tenant.model.request.TemplateUpdateRequest;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板工具类
 */
public class TemplateUtil {

    /**
     * 将实体转换为DTO
     */
    public static TenantTemplateDTO convertToTemplateDTO(TenantTemplate template) {
        if (template == null) {
            return null;
        }
        TenantTemplateDTO dto = new TenantTemplateDTO();
        BeanUtils.copyProperties(template, dto);
        
        // 设置类型名称
        dto.setTemplateTypeName(getTemplateTypeName(template.getTemplateType()));
        dto.setIndustryTypeName(getIndustryTypeName(template.getIndustryType()));
        
        return dto;
    }
    
    /**
     * 将DTO列表转换为实体列表
     */
    public static List<TenantTemplateDTO> convertToTemplateDTOList(List<TenantTemplate> templates) {
        if (templates == null || templates.isEmpty()) {
            return new ArrayList<>();
        }
        return templates.stream()
                .map(TemplateUtil::convertToTemplateDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建请求转换为实体
     */
    public static TenantTemplate convertToEntity(TemplateCreateRequest request) {
        if (request == null) {
            return null;
        }
        TenantTemplate template = new TenantTemplate();
        BeanUtils.copyProperties(request, template);
        
        // 设置时间
        long currentTime = System.currentTimeMillis();
        template.setCreateTime(currentTime);
        template.setUpdateTime(currentTime);
        
        return template;
    }
    
    /**
     * 更新请求转换为实体
     */
    public static TenantTemplate convertToEntity(TemplateUpdateRequest request) {
        if (request == null) {
            return null;
        }
        TenantTemplate template = new TenantTemplate();
        BeanUtils.copyProperties(request, template);
        
        // 设置更新时间
        template.setUpdateTime(System.currentTimeMillis());
        
        return template;
    }
    
    /**
     * 将角色关联实体转换为DTO
     */
    public static TenantTemplateRoleDTO convertToTemplateRoleDTO(TenantRelTemplateRole relation) {
        if (relation == null) {
            return null;
        }
        TenantTemplateRoleDTO dto = new TenantTemplateRoleDTO();
        BeanUtils.copyProperties(relation, dto);
        return dto;
    }
    
    /**
     * 将角色关联DTO列表转换为实体列表
     */
    public static List<TenantTemplateRoleDTO> convertToTemplateRoleDTOList(List<TenantRelTemplateRole> relations) {
        if (relations == null || relations.isEmpty()) {
            return new ArrayList<>();
        }
        return relations.stream()
                .map(TemplateUtil::convertToTemplateRoleDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取模板类型名称
     */
    public static String getTemplateTypeName(Integer templateType) {
        if (templateType == null) {
            return "";
        }
        switch (templateType) {
            case 1:
                return "平台租户模板";
            case 2:
                return "企业租户模板";
            case 3:
                return "代理所租户模板";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取行业类型名称
     */
    public static String getIndustryTypeName(Integer industryType) {
        if (industryType == null) {
            return "";
        }
        switch (industryType) {
            case 0:
                return "教育";
            case 1:
                return "医疗";
            case 2:
                return "金融";
            default:
                return "未知行业";
        }
    }
} 