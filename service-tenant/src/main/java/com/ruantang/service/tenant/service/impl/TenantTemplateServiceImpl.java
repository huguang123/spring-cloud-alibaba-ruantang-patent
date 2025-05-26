package com.ruantang.service.tenant.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.tenant.TenantRelTemplateRole;
import com.ruantang.entity.tenant.TenantTemplate;
import com.ruantang.service.tenant.client.SysRoleFeignClient;
import com.ruantang.service.tenant.model.dto.RolePermissionDTO;
import com.ruantang.service.tenant.model.dto.SysRolesDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateDTO;
import com.ruantang.service.tenant.model.dto.TenantTemplateRoleDTO;
import com.ruantang.service.tenant.model.dto.TemplateRoleDTO;
import com.ruantang.service.tenant.model.request.TemplateCreateRequest;
import com.ruantang.service.tenant.model.request.TemplateQueryRequest;
import com.ruantang.service.tenant.model.request.TemplateRoleBindRequest;
import com.ruantang.service.tenant.model.request.TemplateUpdateRequest;
import com.ruantang.service.tenant.repository.TenantRelTemplateRoleRepository;
import com.ruantang.service.tenant.repository.TenantTemplateRepository;
import com.ruantang.service.tenant.service.TenantTemplateService;
import com.ruantang.service.tenant.util.TemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户模板服务实现类
 */
@Service
@RequiredArgsConstructor
public class TenantTemplateServiceImpl implements TenantTemplateService {

    private final TenantTemplateRepository templateRepository;
    private final TenantRelTemplateRoleRepository templateRoleRepository;
    private final SysRoleFeignClient roleFeignClient;

    @Override
    public ApiResult<Page<TenantTemplateDTO>> queryTemplatePage(TemplateQueryRequest request) {
        // 构建分页对象
        Page<TenantTemplate> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        // 调用仓库查询分页数据
        Page<TenantTemplate> templatePage = templateRepository.queryTemplatePage(
                request.getTemplateName(),
                request.getTemplateCode(),
                request.getTemplateType(),
                request.getIndustryType(),
                request.getIsSystem(),
                page
        );
        
        // 转换为DTO对象
        Page<TenantTemplateDTO> resultPage = new Page<>(
                templatePage.getCurrent(),
                templatePage.getSize(),
                templatePage.getTotal()
        );
        
        List<TenantTemplateDTO> records = TemplateUtil.convertToTemplateDTOList(templatePage.getRecords());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<TenantTemplateDTO> getTemplateById(Long id) {
        // 查询模板基本信息
        TenantTemplate template = templateRepository.getTemplateById(id);
        if (template == null) {
            return ApiResult.failed("模板不存在");
        }
        
        // 转换为DTO
        TenantTemplateDTO templateDTO = TemplateUtil.convertToTemplateDTO(template);
        
        // 查询绑定的角色信息
        List<TenantRelTemplateRole> roleRelations = templateRoleRepository.listRolesByTemplateId(id);
        if (roleRelations == null || roleRelations.isEmpty()) {
            templateDTO.setRoles(new ArrayList<>());
            return ApiResult.success(templateDTO);
        }
        
        // 转换角色关联为DTO
        List<TenantTemplateRoleDTO> roleRelDTOs = TemplateUtil.convertToTemplateRoleDTOList(roleRelations);
        
        // 填充角色详情
        for (TenantTemplateRoleDTO roleRelDTO : roleRelDTOs) {
            ApiResult<SysRolesDTO> roleResult = roleFeignClient.getRoleById(roleRelDTO.getRoleId());
            if (roleResult != null && roleResult.getCode() == 200 && roleResult.getData() != null) {
                roleRelDTO.setRole(roleResult.getData());
            }
        }
        
        templateDTO.setRoles(roleRelDTOs);
        return ApiResult.success(templateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<TenantTemplateDTO> createTemplate(TemplateCreateRequest request) {
        // 验证模板编码是否已存在
        TenantTemplate existTemplate = templateRepository.getTemplateByCode(request.getTemplateCode());
        if (existTemplate != null) {
            return ApiResult.failed("模板编码已存在");
        }
        
        // 转换为实体对象
        TenantTemplate template = TemplateUtil.convertToEntity(request);
        
        // 保存模板
        boolean success = templateRepository.saveTemplate(template);
        if (!success) {
            return ApiResult.failed("创建模板失败");
        }
        
        // 转换为DTO返回
        TenantTemplateDTO templateDTO = TemplateUtil.convertToTemplateDTO(template);
        templateDTO.setRoles(new ArrayList<>());
        
        return ApiResult.success(templateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateTemplate(TemplateUpdateRequest request) {
        // 验证模板是否存在
        TenantTemplate existTemplate = templateRepository.getTemplateById(request.getId());
        if (existTemplate == null) {
            return ApiResult.failed("模板不存在");
        }
        
        // 验证模板编码是否重复
//        if (StringUtils.hasText(request.getTemplateCode())
//                && !request.getTemplateCode().equals(existTemplate.getTemplateCode())) {
//            TenantTemplate codeExistTemplate = templateRepository.getTemplateByCode(request.getTemplateCode());
//            if (codeExistTemplate != null) {
//                return ApiResult.failed("模板编码已存在");
//            }
//        }
        
        // 转换为实体对象
        TenantTemplate template = TemplateUtil.convertToEntity(request);
        
        // 更新模板
        boolean success = templateRepository.updateTemplate(template);
        
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteTemplateById(Long id) {
        // 验证模板是否存在
        TenantTemplate existTemplate = templateRepository.getTemplateById(id);
        if (existTemplate == null) {
            return ApiResult.failed("模板不存在");
        }
        
        // 系统内置模板不允许删除
        if (existTemplate.getIsSystem() != null && existTemplate.getIsSystem() == 1) {
            return ApiResult.failed("系统内置模板不允许删除");
        }
        
        // 删除模板关联的角色
        templateRoleRepository.deleteByTemplateId(id);
        
        // 删除模板
        boolean success = templateRepository.deleteTemplateById(id);
        
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> bindTemplateRoles(TemplateRoleBindRequest request) {
        // 验证模板是否存在
        TenantTemplate existTemplate = templateRepository.getTemplateById(request.getTemplateId());
        if (existTemplate == null) {
            return ApiResult.failed("模板不存在");
        }
        
        // 获取当前模板的所有角色关联
        List<TenantRelTemplateRole> currentRoles = templateRoleRepository.listRolesByTemplateId(request.getTemplateId());
        Map<Long, TenantRelTemplateRole> currentRoleMap = new HashMap<>();
        if (currentRoles != null && !currentRoles.isEmpty()) {
            currentRoleMap = currentRoles.stream()
                .collect(Collectors.toMap(TenantRelTemplateRole::getRoleId, relation -> relation));
        }
        
        // 新请求中的角色ID集合
        List<Long> requestRoleIds = request.getRoleBinds() != null ? 
            request.getRoleBinds().stream().map(TemplateRoleBindRequest.RoleBindItem::getRoleId).collect(Collectors.toList()) : 
            new ArrayList<>();
        
        // 处理角色绑定请求
        if (request.getRoleBinds() != null && !request.getRoleBinds().isEmpty()) {
            for (TemplateRoleBindRequest.RoleBindItem item : request.getRoleBinds()) {
                Long roleId = item.getRoleId();
                
                // 检查角色是否存在
                ApiResult<SysRolesDTO> roleResult = roleFeignClient.getRoleById(roleId);
                if (roleResult == null || roleResult.getCode() != 200 || roleResult.getData() == null) {
                    continue; // 跳过不存在的角色
                }
                
                // 1. 检查角色是否已经绑定
                TenantRelTemplateRole existRelation = currentRoleMap.get(roleId);
                
                if (existRelation != null) {
                    // 已绑定角色，检查继承状态是否变更
                    if (existRelation.getIsInherit().equals(item.getIsInherit())) {
                        // 继承状态没变，不做处理，从当前角色映射中移除，表示已处理
                        currentRoleMap.remove(roleId);
                    } else {
                        // 继承状态变更，无论是从继承变为不继承，还是从不继承变为继承
                        // 都采用先删除再创建的方式，确保数据正确更新
                        templateRoleRepository.deleteByTemplateIdAndRoleId(request.getTemplateId(), roleId);
                        
                        // 创建新的关联
                        TenantRelTemplateRole newRelation = new TenantRelTemplateRole();
                        newRelation.setTemplateId(request.getTemplateId());
                        newRelation.setRoleId(roleId);
                        newRelation.setIsInherit(item.getIsInherit());
                        
                        // 如果是不继承权限变更，需要创建权限快照
                        if (item.getIsInherit() == 0) {
                            // 获取角色的权限信息
                            ApiResult<RolePermissionDTO> permResult = roleFeignClient.getRolePermissions(roleId);
                            if (permResult != null && permResult.getCode() == 200 && permResult.getData() != null) {
                                // 将权限信息转为JSON存储
                                newRelation.setPermissionSnapshot(JSON.toJSONString(permResult.getData()));
                            }
                        }
                        // 如果是继承权限变更，permissionSnapshot默认为null，不需要特殊处理
                        
                        newRelation.setCreateTime(System.currentTimeMillis());
                        newRelation.setUpdateTime(System.currentTimeMillis());
                        templateRoleRepository.saveRelation(newRelation);
                        
                        currentRoleMap.remove(roleId);
                    }
                } else {
                    // 新绑定角色
                    TenantRelTemplateRole newRelation = new TenantRelTemplateRole();
                    newRelation.setTemplateId(request.getTemplateId());
                    newRelation.setRoleId(roleId);
                    newRelation.setIsInherit(item.getIsInherit());
                    
                    // 如果不继承权限变更，需要创建权限快照
                    if (item.getIsInherit() == 0) {
                        // 获取角色的权限信息
                        ApiResult<RolePermissionDTO> permResult = roleFeignClient.getRolePermissions(roleId);
                        if (permResult != null && permResult.getCode() == 200 && permResult.getData() != null) {
                            // 将权限信息转为JSON存储
                            newRelation.setPermissionSnapshot(JSON.toJSONString(permResult.getData()));
                        }
                    }
                    
                    newRelation.setCreateTime(System.currentTimeMillis());
                    newRelation.setUpdateTime(System.currentTimeMillis());
                    templateRoleRepository.saveRelation(newRelation);
                }
            }
        }
        
        // 删除未在请求中的角色关联
        if (!currentRoleMap.isEmpty()) {
            for (TenantRelTemplateRole relation : currentRoleMap.values()) {
                templateRoleRepository.deleteByTemplateIdAndRoleId(request.getTemplateId(), relation.getRoleId());
            }
        }
        
        return ApiResult.success(true);
    }

    @Override
    public ApiResult<List<TemplateRoleDTO>> getTemplateRoles(Long id) {
        // 验证模板是否存在
        TenantTemplate template = templateRepository.getTemplateById(id);
        if (template == null) {
            return ApiResult.failed("模板不存在");
        }
        
        // 获取模板绑定的角色关联
        List<TenantRelTemplateRole> roleRelations = templateRoleRepository.listRolesByTemplateId(id);
        if (roleRelations == null || roleRelations.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        
        // 构建返回结果
        List<TemplateRoleDTO> templateRoleDTOs = new ArrayList<>();
        
        for (TenantRelTemplateRole relation : roleRelations) {
            TemplateRoleDTO templateRoleDTO = new TemplateRoleDTO();
            templateRoleDTO.setId(relation.getId());
            templateRoleDTO.setTemplateId(relation.getTemplateId());
            templateRoleDTO.setRoleId(relation.getRoleId());
            templateRoleDTO.setIsInherit(relation.getIsInherit());
//            templateRoleDTO.setPermissionSnapshot(relation.getPermissionSnapshot());
            
            // 查询角色信息
            ApiResult<SysRolesDTO> roleResult = roleFeignClient.getRoleById(relation.getRoleId());
            if (roleResult != null && roleResult.getCode() == 200 && roleResult.getData() != null) {
                templateRoleDTO.setRole(roleResult.getData());
            }
            
            templateRoleDTOs.add(templateRoleDTO);
        }
        
        return ApiResult.success(templateRoleDTOs);
    }
}