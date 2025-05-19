package com.ruantang.service.tenant.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.tenant.Tenant;
import com.ruantang.entity.tenant.TenantRelTenantTemplate;
import com.ruantang.entity.tenant.TenantTemplate;
import com.ruantang.mapper.tenant.TenantTemplateMapper;
import com.ruantang.service.tenant.model.dto.TenantDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateBindDTO;
import com.ruantang.service.tenant.model.request.TenantBindTemplatesRequest;
import com.ruantang.service.tenant.model.request.TenantCreateRequest;
import com.ruantang.service.tenant.model.request.TenantQueryRequest;
import com.ruantang.service.tenant.model.request.TenantUpdateRequest;
import com.ruantang.service.tenant.repository.TenantRepository;
import com.ruantang.service.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户服务实现类
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    
    private final TenantRepository tenantRepository;
    private final TenantTemplateMapper tenantTemplateMapper;

    @Override
    public ApiResult<Page<TenantDTO>> queryTenantPage(TenantQueryRequest request) {
        Page<Tenant> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Tenant> tenantPage = tenantRepository.queryTenantPage(
                request.getTenantName(),
                request.getTenantCode(),
                request.getTenantType(),
                page
        );
        
        Page<TenantDTO> resultPage = new Page<>(tenantPage.getCurrent(), tenantPage.getSize(), tenantPage.getTotal());
        List<TenantDTO> tenantDTOList = tenantPage.getRecords().stream().map(this::convertToDTO).collect(Collectors.toList());
        resultPage.setRecords(tenantDTOList);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<TenantDTO> getTenantById(Long id) {
        Tenant tenant = tenantRepository.getTenantById(id);
        if (tenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        TenantDTO tenantDTO = convertToDTO(tenant);
        // 查询关联的模板
        List<TenantRelTenantTemplate> tenantTemplates = tenantRepository.getTenantTemplates(id);
        if (!tenantTemplates.isEmpty()) {
            // 获取所有模板ID
            List<Long> templateIds = tenantTemplates.stream()
                    .map(TenantRelTenantTemplate::getTemplateId)
                    .collect(Collectors.toList());
            
            // 批量查询模板信息
            List<TenantTemplate> templates = tenantTemplateMapper.selectBatchIds(templateIds);
            Map<Long, TenantTemplate> templateMap = templates.stream()
                    .collect(Collectors.toMap(TenantTemplate::getId, template -> template));
            
            // 组装模板绑定信息
            List<TenantTemplateBindDTO> templateBindDTOS = new ArrayList<>();
            for (TenantRelTenantTemplate relTemplate : tenantTemplates) {
                TenantTemplate template = templateMap.get(relTemplate.getTemplateId());
                if (template != null) {
                    TenantTemplateBindDTO bindDTO = new TenantTemplateBindDTO();
                    bindDTO.setId(relTemplate.getId());
                    bindDTO.setTemplateId(template.getId());
                    bindDTO.setTemplateName(template.getTemplateName());
                    bindDTO.setTemplateCode(template.getTemplateCode());
                    bindDTO.setBindMode(relTemplate.getBindMode());
                    bindDTO.setBindModeName(relTemplate.getBindMode() == 1 ? "继承" : "快照");
                    bindDTO.setEffectiveTime(relTemplate.getEffectiveTime());
                    bindDTO.setCreateTime(relTemplate.getCreateTime());
                    bindDTO.setUpdateTime(relTemplate.getUpdateTime());
                    templateBindDTOS.add(bindDTO);
                }
            }
            tenantDTO.setTemplates(templateBindDTOS);
        }
        
        return ApiResult.success(tenantDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<TenantDTO> createTenant(TenantCreateRequest request) {
        // 检查租户编码是否存在
        Tenant existingTenant = tenantRepository.getTenantByCode(request.getTenantCode());
        if (existingTenant != null) {
            return ApiResult.failed("租户编码已存在");
        }
        
        // 创建租户
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(request, tenant);
        tenant.setCreateTime(System.currentTimeMillis());
        tenant.setUpdateTime(System.currentTimeMillis());
        
        boolean success = tenantRepository.createTenant(tenant);
        if (!success) {
            return ApiResult.failed("创建租户失败");
        }
        
        // 绑定模板(如果有)
        if (request.getTemplates() != null && !request.getTemplates().isEmpty()) {
            List<TenantRelTenantTemplate> tenantRelTemplates = new ArrayList<>();
            request.getTemplates().forEach(templateBind -> {
                TenantRelTenantTemplate relTemplate = new TenantRelTenantTemplate();
                relTemplate.setTenantId(tenant.getId());
                relTemplate.setTemplateId(templateBind.getTemplateId());
                relTemplate.setBindMode(templateBind.getBindMode());
                relTemplate.setEffectiveTime(templateBind.getEffectiveTime());
                relTemplate.setCreateTime(System.currentTimeMillis());
                relTemplate.setUpdateTime(System.currentTimeMillis());
                tenantRelTemplates.add(relTemplate);
            });
            
            tenantRepository.bindTenantTemplates(tenantRelTemplates);
        }
        
        return ApiResult.success(convertToDTO(tenant));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateTenant(TenantUpdateRequest request) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(request.getId());
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 如果租户编码已更改，检查是否与其他租户冲突
        if (!existingTenant.getTenantCode().equals(request.getTenantCode())) {
            Tenant tenantByCode = tenantRepository.getTenantByCode(request.getTenantCode());
            if (tenantByCode != null && !tenantByCode.getId().equals(request.getId())) {
                return ApiResult.failed("租户编码已被其他租户使用");
            }
        }
        
        // 更新租户信息
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(request, tenant);
        tenant.setUpdateTime(System.currentTimeMillis());
        
        boolean success = tenantRepository.updateTenant(tenant);
        if (!success) {
            return ApiResult.failed("更新租户失败");
        }
        
        return ApiResult.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteTenantById(Long id) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(id);
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 删除租户
        boolean success = tenantRepository.deleteTenantById(id);
        if (!success) {
            return ApiResult.failed("删除租户失败");
        }
        
        return ApiResult.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> bindTenantTemplates(TenantBindTemplatesRequest request) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(request.getTenantId());
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 将绑定请求转换为实体
        List<TenantRelTenantTemplate> tenantRelTemplates = new ArrayList<>();
        request.getTemplates().forEach(templateBind -> {
            TenantRelTenantTemplate relTemplate = new TenantRelTenantTemplate();
            relTemplate.setTenantId(request.getTenantId());
            relTemplate.setTemplateId(templateBind.getTemplateId());
            relTemplate.setBindMode(templateBind.getBindMode());
            relTemplate.setEffectiveTime(templateBind.getEffectiveTime());
            relTemplate.setCreateTime(System.currentTimeMillis());
            relTemplate.setUpdateTime(System.currentTimeMillis());
            tenantRelTemplates.add(relTemplate);
        });
        
        // 绑定模板
        boolean success = tenantRepository.bindTenantTemplates(tenantRelTemplates);
        if (!success) {
            return ApiResult.failed("绑定模板失败");
        }
        
        return ApiResult.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> unbindTenantTemplate(Long tenantId, Long templateId) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(tenantId);
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 解绑模板
        boolean success = tenantRepository.unbindTenantTemplate(tenantId, templateId);
        if (!success) {
            return ApiResult.failed("解绑模板失败");
        }
        
        return ApiResult.success(true);
    }
    
    /**
     * 将租户实体转换为DTO
     * 
     * @param tenant 租户实体
     * @return 租户DTO
     */
    private TenantDTO convertToDTO(Tenant tenant) {
        TenantDTO tenantDTO = new TenantDTO();
        BeanUtils.copyProperties(tenant, tenantDTO);
        
        // 设置租户类型名称
        Map<Integer, String> tenantTypeMap = new HashMap<>();
        tenantTypeMap.put(1, "平台管理租户");
        tenantTypeMap.put(2, "企业商户租户");
        tenantTypeMap.put(3, "代理所租户");
        tenantDTO.setTenantTypeName(tenantTypeMap.getOrDefault(tenant.getTenantType(), "未知"));
        
        // 设置数据隔离模式名称
        Map<Integer, String> isolationModeMap = new HashMap<>();
        isolationModeMap.put(1, "行级隔离");
        isolationModeMap.put(2, "Schema隔离");
        isolationModeMap.put(3, "独立库隔离");
        tenantDTO.setDataIsolationModeName(isolationModeMap.getOrDefault(tenant.getDataIsolationMode(), "未知"));
        
        return tenantDTO;
    }
}