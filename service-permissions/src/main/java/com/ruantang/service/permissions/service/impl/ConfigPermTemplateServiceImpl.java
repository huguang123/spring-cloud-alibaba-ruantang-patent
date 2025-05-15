package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.ConfigPermModule;
import com.ruantang.entity.perm.ConfigPermNode;
import com.ruantang.entity.perm.ConfigPermTemplate;
import com.ruantang.entity.perm.ConfigPermVersion;
import com.ruantang.service.permissions.model.dto.ConfigPermTemplateDTO;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.dto.PermNodeTreeDTO;
import com.ruantang.service.permissions.model.request.TemplateCreateRequest;
import com.ruantang.service.permissions.model.request.TemplateQueryRequest;
import com.ruantang.service.permissions.model.request.TemplateUpdateRequest;
import com.ruantang.service.permissions.repository.ConfigPermModuleRepository;
import com.ruantang.service.permissions.repository.ConfigPermNodeRepository;
import com.ruantang.service.permissions.repository.ConfigPermTemplateRepository;
import com.ruantang.service.permissions.repository.ConfigPermVersionRepository;
import com.ruantang.service.permissions.service.ConfigPermTemplateService;
import com.ruantang.service.permissions.service.PermDataPolicyService;
import com.ruantang.service.permissions.service.PermService;
import com.ruantang.service.permissions.util.ConfigPermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置权限模板服务实现类
 */
@Service
@RequiredArgsConstructor
public class ConfigPermTemplateServiceImpl implements ConfigPermTemplateService {

    private final ConfigPermTemplateRepository templateRepository;
    private final ConfigPermVersionRepository versionRepository;
    private final ConfigPermModuleRepository moduleRepository;
    private final ConfigPermNodeRepository nodeRepository;
    private final PermService permService;
    private final PermDataPolicyService policyService;

    @Override
    public ApiResult<Page<ConfigPermTemplateDTO>> queryTemplatePage(TemplateQueryRequest request) {
        Page<ConfigPermTemplate> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<ConfigPermTemplate> templatePage = templateRepository.queryTemplatePage(
                request.getTemplateCode(),
                request.getTemplateType(),
                page
        );
        
        // 转换为DTO
        Page<ConfigPermTemplateDTO> resultPage = new Page<>(templatePage.getCurrent(), templatePage.getSize(), templatePage.getTotal());
        List<ConfigPermTemplateDTO> records = ConfigPermUtil.convertToTemplateDTOList(templatePage.getRecords());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<ConfigPermTemplateDTO> getTemplateById(Long id) {
        ConfigPermTemplate template = templateRepository.getTemplateById(id);
        if (template == null) {
            return ApiResult.failed("配置权限模板不存在");
        }
        
        return ApiResult.success(ConfigPermUtil.convertToTemplateDTO(template));
    }

    @Override
    public ApiResult<ConfigPermTemplateDTO> getTemplateByType(Integer templateType) {
        if (templateType == null) {
            return ApiResult.failed("模板类型不能为空");
        }
        
        // 查询指定类型的模板
        ConfigPermTemplate template = templateRepository.getTemplateByType(templateType);
        if (template == null) {
            return ApiResult.failed("指定类型的模板不存在");
        }
        
        return ApiResult.success(ConfigPermUtil.convertToTemplateDTO(template));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<ConfigPermTemplateDTO> createTemplate(TemplateCreateRequest request) {
        // 检查模板编码是否已存在
        ConfigPermTemplate existTemplate = templateRepository.getTemplateByCode(request.getTemplateCode());
        if (existTemplate != null) {
            throw new ApiException("模板编码已存在");
        }
        
        ConfigPermTemplate template = new ConfigPermTemplate();
        BeanUtils.copyProperties(request, template);
        
        // 设置创建时间
        template.setCreateTime(System.currentTimeMillis());
        template.setUpdateTime(System.currentTimeMillis());
        
        boolean success = templateRepository.saveTemplate(template);
        if (!success) {
            return ApiResult.failed("创建配置权限模板失败");
        }
        
        return ApiResult.success(ConfigPermUtil.convertToTemplateDTO(template));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateTemplate(TemplateUpdateRequest request) {
        // 检查模板是否存在
        ConfigPermTemplate existTemplate = templateRepository.getTemplateById(request.getId());
        if (existTemplate == null) {
            return ApiResult.failed("配置权限模板不存在");
        }
        
        // 检查模板编码是否重复
        if (request.getTemplateCode() != null && !request.getTemplateCode().equals(existTemplate.getTemplateCode())) {
            ConfigPermTemplate codeExistTemplate = templateRepository.getTemplateByCode(request.getTemplateCode());
            if (codeExistTemplate != null) {
                return ApiResult.failed("模板编码已存在");
            }
        }
        
        ConfigPermTemplate template = new ConfigPermTemplate();
        BeanUtils.copyProperties(request, template);
        
        // 设置更新时间
        template.setUpdateTime(System.currentTimeMillis());
        
        boolean success = templateRepository.updateTemplate(template);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteTemplateById(Long id) {
        // 检查模板是否存在
        ConfigPermTemplate existTemplate = templateRepository.getTemplateById(id);
        if (existTemplate == null) {
            return ApiResult.failed("配置权限模板不存在");
        }
        
        boolean success = templateRepository.deleteTemplateById(id);
        return ApiResult.success(success);
    }
    
    @Override
    public ApiResult<ConfigPermVersion> getDefaultVersionByRoleType(Integer roleType) {
        if (roleType == null) {
            return ApiResult.failed("角色类型不能为空");
        }
        
        // 将角色类型映射到模板类型
        // 角色类型(1:平台角色 2:企业角色 3:代理所角色)
        // 模板类型(1=平台配置模板 2=企业租户配置模板 3=代理所配置模板)
        Integer templateType = mapRoleTypeToTemplateType(roleType);
        
        // 获取指定类型的模板
        ConfigPermTemplate template = templateRepository.getTemplateByType(templateType);
        if (template == null) {
            return ApiResult.failed("指定类型的模板不存在");
        }
        
        // 获取该模板的默认版本
        ConfigPermVersion defaultVersion = versionRepository.getDefaultVersionByTemplateId(template.getId());
        if (defaultVersion == null) {
            return ApiResult.failed("指定模板的默认版本不存在");
        }
        
        return ApiResult.success(defaultVersion);
    }
    
    /**
     * 将角色类型映射到模板类型
     */
    private Integer mapRoleTypeToTemplateType(Integer roleType) {
        switch (roleType) {
            case 1: // 平台角色
                return 1; // 平台配置模板
            case 2: // 企业角色
                return 2; // 企业租户配置模板
            case 3: // 代理所角色
                return 3; // 代理所配置模板
            default:
                throw new ApiException("无效的角色类型");
        }
    }
    
    @Override
    public ApiResult<PermNodeTreeDTO> getPermTreeByRoleType(Integer roleType) {
        // 获取角色类型对应的默认模板版本
        ApiResult<ConfigPermVersion> versionResult = getDefaultVersionByRoleType(roleType);
        if (versionResult.getCode() != 200 || versionResult.getData() == null) {
            return ApiResult.failed(versionResult.getMessage());
        }
        
        ConfigPermVersion version = versionResult.getData();
        
        // 构建权限树
        PermNodeTreeDTO treeDTO = buildPermissionTree(version.getId());
        
        return ApiResult.success(treeDTO);
    }
    
    /**
     * 构建权限树
     */
    private PermNodeTreeDTO buildPermissionTree(Long versionId) {
        PermNodeTreeDTO treeDTO = new PermNodeTreeDTO();
        
        // 获取所有模块
        List<ConfigPermModule> allModules = moduleRepository.listModulesByVersionId(versionId);
        
        // 筛选操作权限模块(moduleType=1)
        List<ConfigPermModule> opModules = allModules.stream()
                .filter(module -> module.getModuleType() != null && module.getModuleType() == 1)
                .collect(Collectors.toList());
        List<PermNodeTreeDTO.PermModuleDTO> opModuleDTOs = opModules.stream()
                .map(this::convertToPermModuleDTO)
                .collect(Collectors.toList());
        treeDTO.setOperationModules(opModuleDTOs);
        
        // 筛选数据权限模块(moduleType=2)
        List<ConfigPermModule> dataModules = allModules.stream()
                .filter(module -> module.getModuleType() != null && module.getModuleType() == 2)
                .collect(Collectors.toList());
        List<PermNodeTreeDTO.PermModuleDTO> dataModuleDTOs = dataModules.stream()
                .map(this::convertToPermModuleDTO)
                .collect(Collectors.toList());
        treeDTO.setDataModules(dataModuleDTOs);
        
        return treeDTO;
    }
    
    /**
     * 将权限模块转换为DTO
     */
    private PermNodeTreeDTO.PermModuleDTO convertToPermModuleDTO(ConfigPermModule module) {
        PermNodeTreeDTO.PermModuleDTO moduleDTO = new PermNodeTreeDTO.PermModuleDTO();
        moduleDTO.setId(module.getId());
        moduleDTO.setModuleName(module.getModuleName());
        moduleDTO.setModuleType(module.getModuleType());
        moduleDTO.setModuleTypeName(ConfigPermUtil.getModuleTypeName(module.getModuleType()));
        
        // 获取模块下的节点
        List<ConfigPermNode> nodes = nodeRepository.listNodesByModuleId(module.getId());
        List<PermNodeTreeDTO.PermNodeDTO> nodeDTOs = nodes.stream()
                .map(this::convertToPermNodeDTO)
                .collect(Collectors.toList());
        moduleDTO.setNodes(nodeDTOs);
        
        return moduleDTO;
    }
    
    /**
     * 将权限节点转换为DTO
     */
    private PermNodeTreeDTO.PermNodeDTO convertToPermNodeDTO(ConfigPermNode node) {
        PermNodeTreeDTO.PermNodeDTO nodeDTO = new PermNodeTreeDTO.PermNodeDTO();
        nodeDTO.setId(node.getId());
        nodeDTO.setNodeName(node.getNodeName());
        nodeDTO.setNodeType(node.getNodeType());
        nodeDTO.setNodeTypeName(ConfigPermUtil.getNodeTypeName(node.getNodeType()));
        nodeDTO.setPermType(node.getPermType());
        nodeDTO.setPermTypeName(ConfigPermUtil.getPermTypeName(node.getPermType()));
        nodeDTO.setPermId(node.getPermId());
        nodeDTO.setDataPolicyId(node.getDataPolicyId());
        nodeDTO.setSelected(false);
        
        // 填充权限详情
        if (node.getPermType() != null && node.getPermType() == 0 && node.getPermId() != null) {
            ApiResult<PermDTO> permResult = permService.getPermById(node.getPermId());
            if (permResult.getCode() == 200 && permResult.getData() != null) {
                nodeDTO.setPermDetail(permResult.getData());
            }
        }
        
        // 填充数据权限详情
        if (node.getPermType() != null && node.getPermType() == 1 && node.getDataPolicyId() != null) {
            ApiResult<PermDataPolicyDTO> policyResult = policyService.getPolicyById(node.getDataPolicyId());
            if (policyResult.getCode() == 200 && policyResult.getData() != null) {
                nodeDTO.setDataPolicyDetail(policyResult.getData());
            }
        }
        
        return nodeDTO;
    }
}
