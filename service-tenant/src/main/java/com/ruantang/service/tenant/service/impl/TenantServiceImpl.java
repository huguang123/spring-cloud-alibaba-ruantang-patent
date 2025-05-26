package com.ruantang.service.tenant.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.tenant.Tenant;
import com.ruantang.entity.tenant.TenantRelTenantTemplate;
import com.ruantang.entity.tenant.TenantRelTemplateRole;
import com.ruantang.entity.tenant.TenantTemplate;
import com.ruantang.mapper.tenant.TenantTemplateMapper;
import com.ruantang.service.tenant.client.SysRoleFeignClient;
import com.ruantang.service.tenant.model.dto.SysRolesDTO;
import com.ruantang.service.tenant.model.dto.TenantDTO;
import com.ruantang.service.tenant.model.dto.TenantRoleDTO;
import com.ruantang.service.tenant.model.dto.TenantRolePermissionDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateBindDTO;
import com.ruantang.service.tenant.model.request.TenantBindTemplatesRequest;
import com.ruantang.service.tenant.model.request.TenantCreateRequest;
import com.ruantang.service.tenant.model.request.TenantQueryRequest;
import com.ruantang.service.tenant.model.request.TenantRoleVerifyRequest;
import com.ruantang.service.tenant.model.request.TenantUpdateRequest;
import com.ruantang.service.tenant.repository.TenantRelTemplateRoleRepository;
import com.ruantang.service.tenant.repository.TenantRepository;
import com.ruantang.service.tenant.service.TenantService;
import com.ruantang.service.tenant.service.TenantTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 租户服务实现类
 */
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    
    private final TenantRepository tenantRepository;
    private final TenantTemplateMapper tenantTemplateMapper;
    private final TenantRelTemplateRoleRepository templateRoleRepository;
    private final SysRoleFeignClient roleFeignClient;
    private final TenantTemplateService tenantTemplateService;

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
    
    @Override
    public ApiResult<List<TenantTemplateBindDTO>> getTenantTemplates(Long tenantId) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(tenantId);
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 查询关联的模板
        List<TenantRelTenantTemplate> tenantTemplates = tenantRepository.getTenantTemplates(tenantId);
        if (tenantTemplates.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        
        // 获取所有模板ID
        List<Long> templateIds = tenantTemplates.stream()
                .map(TenantRelTenantTemplate::getTemplateId)
                .collect(Collectors.toList());
        
        // 批量查询模板信息
        List<TenantTemplate> templates = tenantTemplateMapper.selectBatchIds(templateIds);
        Map<Long, TenantTemplate> templateMap = templates.stream()
                .collect(Collectors.toMap(TenantTemplate::getId, template -> template));
        
        // 组装模板绑定信息
        List<TenantTemplateBindDTO> templateBindDTOs = new ArrayList<>();
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
                templateBindDTOs.add(bindDTO);
            }
        }
        
        return ApiResult.success(templateBindDTOs);
    }
    
    @Override
    public ApiResult<List<TenantRoleDTO>> getTenantRoles(Long tenantId) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(tenantId);
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 查询租户绑定的模板
        List<TenantRelTenantTemplate> tenantTemplates = tenantRepository.getTenantTemplates(tenantId);
        if (tenantTemplates.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        
        // 获取所有模板ID
        List<Long> templateIds = tenantTemplates.stream()
                .map(TenantRelTenantTemplate::getTemplateId)
                .collect(Collectors.toList());
        
        // 批量查询模板信息
        List<TenantTemplate> templates = tenantTemplateMapper.selectBatchIds(templateIds);
        Map<Long, TenantTemplate> templateMap = templates.stream()
                .collect(Collectors.toMap(TenantTemplate::getId, template -> template));
        
        // 保存结果
        List<TenantRoleDTO> resultRoles = new ArrayList<>();
        
        // 遍历模板，查询每个模板的角色
        for (TenantRelTenantTemplate tenantTemplate : tenantTemplates) {
            TenantTemplate template = templateMap.get(tenantTemplate.getTemplateId());
            if (template == null) {
                continue;
            }
            
            // 获取模板关联的角色
            List<TenantRelTemplateRole> templateRoles = templateRoleRepository.listRolesByTemplateId(template.getId());
            if (templateRoles.isEmpty()) {
                continue;
            }
            
            // 遍历角色，构建返回数据
            for (TenantRelTemplateRole templateRole : templateRoles) {
                ApiResult<SysRolesDTO> roleResult = roleFeignClient.getRoleById(templateRole.getRoleId());
                if (roleResult == null || roleResult.getCode() != 200 || roleResult.getData() == null) {
                    continue;
                }
                
                SysRolesDTO role = roleResult.getData();
                
                TenantRoleDTO tenantRoleDTO = new TenantRoleDTO();
                tenantRoleDTO.setRoleId(role.getId());
                tenantRoleDTO.setRolesCode(role.getRolesCode());
                tenantRoleDTO.setRolesName(role.getRolesName());
                tenantRoleDTO.setRolesType(role.getRolesType());
                tenantRoleDTO.setRolesTypeName(role.getRolesTypeName());
                tenantRoleDTO.setRolesDescription(role.getRolesDescription());
                tenantRoleDTO.setTemplateId(template.getId());
                tenantRoleDTO.setTemplateName(template.getTemplateName());
                tenantRoleDTO.setIsInherit(templateRole.getIsInherit());
                
                resultRoles.add(tenantRoleDTO);
            }
        }
        
        return ApiResult.success(resultRoles);
    }
    
    @Override
    public ApiResult<List<TenantRolePermissionDTO>> verifyTenantRolePermissions(TenantRoleVerifyRequest request) {
        // 检查租户是否存在
        Tenant existingTenant = tenantRepository.getTenantById(request.getTenantId());
        if (existingTenant == null) {
            return ApiResult.failed("租户不存在");
        }
        
        // 查询租户绑定的模板
        List<TenantRelTenantTemplate> tenantTemplates = tenantRepository.getTenantTemplates(request.getTenantId());
        if (tenantTemplates.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        
        // 获取所有模板ID
        List<Long> templateIds = tenantTemplates.stream()
                .map(TenantRelTenantTemplate::getTemplateId)
                .collect(Collectors.toList());
        
        // 批量查询模板信息
        List<TenantTemplate> templates = tenantTemplateMapper.selectBatchIds(templateIds);
        Map<Long, TenantTemplate> templateMap = templates.stream()
                .collect(Collectors.toMap(TenantTemplate::getId, template -> template));
        
        // 保存结果
        List<TenantRolePermissionDTO> resultRoles = new ArrayList<>();
        // 已处理的角色ID，避免重复添加
        Set<Long> processedRoleIds = new HashSet<>();
        
        // 遍历模板，查询每个模板的角色
        for (TenantRelTenantTemplate tenantTemplate : tenantTemplates) {
            TenantTemplate template = templateMap.get(tenantTemplate.getTemplateId());
            if (template == null) {
                continue;
            }
            
            // 查询模板绑定的所有角色关联
            List<TenantRelTemplateRole> allTemplateRoles = templateRoleRepository.listRolesByTemplateId(template.getId());
            if (allTemplateRoles.isEmpty()) {
                continue;
            }
            
            // 筛选请求中包含的角色
            Map<Long, TenantRelTemplateRole> roleMap = allTemplateRoles.stream()
                    .filter(role -> request.getRoleIds().contains(role.getRoleId()))
                    .collect(Collectors.toMap(TenantRelTemplateRole::getRoleId, role -> role));
            
            // 遍历请求中的角色ID，查询角色信息
            for (Long roleId : request.getRoleIds()) {
                // 如果该角色已处理或模板中不包含该角色，跳过
                if (processedRoleIds.contains(roleId) || !roleMap.containsKey(roleId)) {
                    continue;
                }
                
                TenantRelTemplateRole templateRole = roleMap.get(roleId);
                
                // 查询角色基本信息
                ApiResult<SysRolesDTO> roleResult = roleFeignClient.getRoleById(roleId);
                if (roleResult == null || roleResult.getCode() != 200 || roleResult.getData() == null) {
                    continue;
                }
                
                SysRolesDTO role = roleResult.getData();
                
                // 创建返回对象
                TenantRolePermissionDTO permissionDTO = new TenantRolePermissionDTO();
                permissionDTO.setRoleId(role.getId());
                permissionDTO.setRolesCode(role.getRolesCode());
                permissionDTO.setRolesName(role.getRolesName());
                permissionDTO.setTemplateId(template.getId());
                permissionDTO.setTemplateName(template.getTemplateName());
                permissionDTO.setIsInherit(templateRole.getIsInherit());
                // 只有当不继承权限变更时，才设置权限快照
                if (templateRole.getIsInherit() != null && templateRole.getIsInherit() == 0) {
                    permissionDTO.setPermissionSnapshot(templateRole.getPermissionSnapshot());
                }
                
                resultRoles.add(permissionDTO);
                processedRoleIds.add(roleId);
            }
        }
        
        return ApiResult.success(resultRoles);
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