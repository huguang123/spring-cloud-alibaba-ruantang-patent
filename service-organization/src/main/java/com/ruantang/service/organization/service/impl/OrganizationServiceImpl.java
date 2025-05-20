package com.ruantang.service.organization.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.organ.Organization;
import com.ruantang.service.organization.model.dto.OrganizationDTO;
import com.ruantang.service.organization.model.dto.OrganizationTreeDTO;
import com.ruantang.service.organization.model.request.OrganizationCreateRequest;
import com.ruantang.service.organization.model.request.OrganizationQueryRequest;
import com.ruantang.service.organization.model.request.OrganizationUpdateRequest;
import com.ruantang.service.organization.repository.OrganizationHierarchyRepository;
import com.ruantang.service.organization.repository.OrganizationRepository;
import com.ruantang.service.organization.service.OrganizationService;
import com.ruantang.service.organization.util.OrganizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织服务实现类
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final OrganizationHierarchyRepository hierarchyRepository;
    
    @Override
    public ApiResult<List<OrganizationTreeDTO>> getOrganizationTree(Long tenantId) {
        if (tenantId == null) {
            return ApiResult.failed("租户ID不能为空");
        }
        
        // 查询租户下所有组织
        List<Organization> organizations = organizationRepository.listByTenantId(tenantId);
        if (organizations.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        
        // 获取组织ID列表
        List<Long> orgIds = organizations.stream().map(Organization::getId).collect(Collectors.toList());
        
        // 查询组织成员数量
        Map<Long, Integer> memberCountMap = organizationRepository.getMemberCounts(orgIds);
        
        // 构建组织树
        List<OrganizationTreeDTO> tree = OrganizationUtil.buildOrganizationTree(organizations, memberCountMap);
        
        return ApiResult.success(tree);
    }
    
    @Override
    public ApiResult<Page<OrganizationDTO>> queryOrganizationPage(OrganizationQueryRequest request) {
        // 创建分页对象
        Page<Organization> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        // 查询分页数据
        Page<Organization> organizationPage = organizationRepository.queryPage(
                request.getOrgName(),
                request.getOrgCode(),
                request.getTenantId(),
                request.getState(),
                page
        );
        
        // 转换为DTO列表
        Page<OrganizationDTO> resultPage = new Page<>(organizationPage.getCurrent(), organizationPage.getSize(), organizationPage.getTotal());
        List<OrganizationDTO> records = organizationPage.getRecords().stream()
                .map(OrganizationUtil::convertToDTO)
                .collect(Collectors.toList());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }
    
    @Override
    public ApiResult<OrganizationDTO> getOrganizationById(Long id) {
        if (id == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        Organization organization = organizationRepository.getById(id);
        if (organization == null) {
            return ApiResult.failed("组织不存在");
        }
        
        // 转换为DTO
        OrganizationDTO dto = OrganizationUtil.convertToDTO(organization);
        
        // 查询成员数量
        Map<Long, Integer> memberCounts = organizationRepository.getMemberCounts(List.of(id));
        dto.setMemberCount(memberCounts.getOrDefault(id, 0));
        
        return ApiResult.success(dto);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<OrganizationDTO> createOrganization(OrganizationCreateRequest request) {
        // 检查参数
        if (request.getTenantId() == null) {
            return ApiResult.failed("租户ID不能为空");
        }
        
        if (request.getParentId() != null && request.getParentId() > 0) {
            // 检查父组织是否存在
            Organization parent = organizationRepository.getById(request.getParentId());
            if (parent == null) {
                return ApiResult.failed("父组织不存在");
            }
            
            // 检查父组织是否属于同一租户
            if (!parent.getTenantId().equals(request.getTenantId())) {
                return ApiResult.failed("父组织不属于指定租户");
            }
        }
        
        // 检查组织编码是否已存在
        if (StringUtils.hasText(request.getOrgCode())) {
            Organization existOrg = organizationRepository.getByCode(request.getOrgCode(), request.getTenantId());
            if (existOrg != null) {
                return ApiResult.failed("组织编码已存在");
            }
        }
        
        // 创建组织实体
        Organization organization = new Organization();
        BeanUtils.copyProperties(request, organization);
        
        // 设置创建时间等信息
        long currentTime = System.currentTimeMillis();
        organization.setCreateTime(currentTime);
        organization.setUpdateTime(currentTime);
        
        // 如果父组织为空或0，设置为顶级组织
        if (organization.getParentId() == null) {
            organization.setParentId(0L);
        }
        
        // 保存组织
        boolean success = organizationRepository.save(organization);
        if (!success) {
            return ApiResult.failed("创建组织失败");
        }
        
        // 创建组织层级关系
        hierarchyRepository.initHierarchy(organization.getId(), organization.getParentId());
        
        // 构建组织路径
        updateOrgPath(organization);
        
        // 返回创建结果
        OrganizationDTO dto = OrganizationUtil.convertToDTO(organization);
        dto.setMemberCount(0);
        
        return ApiResult.success(dto);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateOrganization(OrganizationUpdateRequest request) {
        // 检查参数
        if (request.getId() == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        // 检查组织是否存在
        Organization existOrg = organizationRepository.getById(request.getId());
        if (existOrg == null) {
            return ApiResult.failed("组织不存在");
        }
        
        // 检查组织编码是否已存在
        if (StringUtils.hasText(request.getOrgCode()) && !request.getOrgCode().equals(existOrg.getOrgCode())) {
            Organization orgByCode = organizationRepository.getByCode(request.getOrgCode(), existOrg.getTenantId());
            if (orgByCode != null && !orgByCode.getId().equals(request.getId())) {
                return ApiResult.failed("组织编码已存在");
            }
        }
        
        // 创建更新对象
        Organization organization = new Organization();
        organization.setId(request.getId());
        
        // 设置需要更新的字段
        if (StringUtils.hasText(request.getOrgName())) {
            organization.setOrgName(request.getOrgName());
        }
        
        if (StringUtils.hasText(request.getOrgCode())) {
            organization.setOrgCode(request.getOrgCode());
        }
        
        if (StringUtils.hasText(request.getOrgPhone())) {
            organization.setOrgPhone(request.getOrgPhone());
        }
        
        if (StringUtils.hasText(request.getOrgMail())) {
            organization.setOrgMail(request.getOrgMail());
        }
        
        if (request.getState() != null) {
            organization.setState(request.getState());
        }
        
        if (StringUtils.hasText(request.getDescription())) {
            organization.setDescription(request.getDescription());
        }
        
        // 设置更新时间
        organization.setUpdateTime(System.currentTimeMillis());
        
        // 更新组织
        boolean success = organizationRepository.update(organization);
        
        return ApiResult.success(success);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteOrganization(Long id) {
        // 检查参数
        if (id == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        // 检查组织是否存在
        Organization organization = organizationRepository.getById(id);
        if (organization == null) {
            return ApiResult.failed("组织不存在");
        }
        
        // 检查是否有子组织
        boolean hasChildren = hierarchyRepository.hasChildren(id);
        if (hasChildren) {
            return ApiResult.failed("该组织下存在子组织，无法删除");
        }
        
        // 检查是否有绑定用户
        boolean hasBindUser = organizationRepository.hasBindUser(id);
        if (hasBindUser) {
            return ApiResult.failed("该组织下存在绑定用户，无法删除");
        }
        
        // 删除组织层级关系
        hierarchyRepository.removeByOrgId(id);
        
        // 删除组织
        boolean success = organizationRepository.remove(id);
        
        return ApiResult.success(success);
    }
    
    /**
     * 更新组织路径
     *
     * @param organization 组织
     */
    private void updateOrgPath(Organization organization) {
        if (organization == null || organization.getId() == null) {
            return;
        }
        
        // 构建组织路径
        StringBuilder pathBuilder = new StringBuilder(".");
        
        // 查询所有祖先节点
        List<Long> ancestors = hierarchyRepository.findAncestors(organization.getId());
        // 按层级深度排序（不包括自己）
        ancestors.removeIf(id -> id.equals(organization.getId()));
        
        for (Long ancestorId : ancestors) {
            pathBuilder.append(ancestorId).append(".");
        }
        
        pathBuilder.append(organization.getId()).append(".");
        
        // 设置组织路径
        organization.setOrgPath(pathBuilder.toString());
        
        // 更新组织路径
        organizationRepository.update(organization);
    }
} 