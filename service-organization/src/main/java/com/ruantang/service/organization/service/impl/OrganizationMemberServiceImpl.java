package com.ruantang.service.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.organ.Organization;
import com.ruantang.entity.rel.RelUserRoles;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.rel.RelUserRolesMapper;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.service.organization.client.SysRoleFeignClient;
import com.ruantang.service.organization.model.dto.OrganizationMemberDTO;
import com.ruantang.service.organization.model.dto.SysRolesDTO;
import com.ruantang.service.organization.model.request.OrgMemberQueryRequest;
import com.ruantang.service.organization.model.request.OrgMemberUpdateRequest;
import com.ruantang.service.organization.model.request.UserOrgBindRequest;
import com.ruantang.service.organization.model.request.UserRoleAssignRequest;
import com.ruantang.service.organization.repository.OrganizationRepository;
import com.ruantang.service.organization.service.OrganizationMemberService;
import com.ruantang.service.organization.util.OrganizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织成员服务实现类
 */
@Service
@RequiredArgsConstructor
public class OrganizationMemberServiceImpl implements OrganizationMemberService {
    
    private final SysUsersMapper sysUsersMapper;
    private final OrganizationRepository organizationRepository;
    private final SysRoleFeignClient sysRoleFeignClient;

    @Override
    public ApiResult<Page<OrganizationMemberDTO>> queryMemberPage(OrgMemberQueryRequest request) {
        if (request.getOrgId() == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        if (request.getTenantId() == null) {
            return ApiResult.failed("租户ID不能为空");
        }
        
        // 检查组织是否存在
        Organization organization = organizationRepository.getById(request.getOrgId());
        if (organization == null) {
            return ApiResult.failed("组织不存在");
        }
        
        // 创建分页对象
        Page<SysUsers> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<SysUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUsers::getOrgId, request.getOrgId());
        queryWrapper.eq(SysUsers::getTenantId, request.getTenantId());
        
        // 按用户名查询
        if (StringUtils.hasText(request.getUserName())) {
            queryWrapper.like(SysUsers::getUserName, request.getUserName());
        }
        
        // 按账号查询
        if (StringUtils.hasText(request.getLoginName())) {
            queryWrapper.like(SysUsers::getLoginName, request.getLoginName());
        }
        
        // 排序
        queryWrapper.orderByDesc(SysUsers::getCreateTime);
        
        // 执行分页查询
        Page<SysUsers> usersPage = sysUsersMapper.selectPage(page, queryWrapper);
        
        // 转换为DTO结果
        Page<OrganizationMemberDTO> resultPage = new Page<>(usersPage.getCurrent(), usersPage.getSize(), usersPage.getTotal());
        if (CollectionUtils.isEmpty(usersPage.getRecords())) {
            resultPage.setRecords(Collections.emptyList());
            return ApiResult.success(resultPage);
        }
        
        // 查询用户角色信息
        List<OrganizationMemberDTO> memberDTOList = new ArrayList<>();
        for (SysUsers user : usersPage.getRecords()) {
            // 使用FeignClient获取用户角色
            ApiResult<List<SysRolesDTO>> rolesResult = sysRoleFeignClient.getUserRoles(user.getId());
            List<String> roleNames = Collections.emptyList();
            List<Long> roleIds = Collections.emptyList();
            
            if (rolesResult.getCode() == 200 && rolesResult.getData() != null) {
                List<SysRolesDTO> roles = rolesResult.getData();
                roleNames = roles.stream().map(SysRolesDTO::getRolesName).collect(Collectors.toList());
                roleIds = roles.stream().map(SysRolesDTO::getId).collect(Collectors.toList());
            }
            
            // 转换为DTO
            OrganizationMemberDTO memberDTO = OrganizationUtil.convertToMemberDTO(user, roleNames, roleIds);
            memberDTOList.add(memberDTO);
        }
        
        resultPage.setRecords(memberDTOList);
        return ApiResult.success(resultPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> bindUserToOrg(UserOrgBindRequest request) {
        if (request.getOrgId() == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        if (request.getTenantId() == null) {
            return ApiResult.failed("租户ID不能为空");
        }
        
        if (request.getUserId() == null) {
            return ApiResult.failed("用户ID不能为空");
        }
        
        // 检查组织是否存在
        Organization organization = organizationRepository.getById(request.getOrgId());
        if (organization == null) {
            return ApiResult.failed("组织不存在");
        }
        
        // 检查用户是否存在
        SysUsers user = sysUsersMapper.selectById(request.getUserId());
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 检查用户是否已经绑定到其他组织
        if (user.getOrgId() != null && !user.getOrgId().equals(request.getOrgId())) {
            // 已经绑定到其他组织，需要先解绑
            return ApiResult.failed("用户已绑定到其他组织，请先解绑");
        }
        
        try {
            // 绑定用户到组织，同时更新租户ID
            user.setOrgId(request.getOrgId());
            user.setTenantId(request.getTenantId());
            sysUsersMapper.updateById(user);
            
            // 处理角色分配
            if (!CollectionUtils.isEmpty(request.getRoleIds())) {
                // 使用FeignClient为用户分配角色
                UserRoleAssignRequest roleRequest = new UserRoleAssignRequest();
                roleRequest.setUserId(request.getUserId());
                roleRequest.setRoleIds(request.getRoleIds());
                
                ApiResult<Boolean> assignResult = sysRoleFeignClient.assignUserRoles(roleRequest);
                if (assignResult.getCode() != 200 || !Boolean.TRUE.equals(assignResult.getData())) {
                    return ApiResult.failed("分配用户角色失败：" + assignResult.getMessage());
                }
            }
            
            return ApiResult.success(true);
        } catch (Exception e) {
            return ApiResult.failed("绑定用户到组织失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> unbindUserFromOrg(Long orgId, Long tenantId, Long userId, Boolean unbindTenant) {
        if (orgId == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        if (tenantId == null) {
            return ApiResult.failed("租户ID不能为空");
        }
        
        if (userId == null) {
            return ApiResult.failed("用户ID不能为空");
        }
        
        // 检查用户是否存在
        SysUsers user = sysUsersMapper.selectById(userId);
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 检查用户是否属于指定组织
        if (user.getOrgId() == null || !user.getOrgId().equals(orgId)) {
            return ApiResult.failed("用户不属于指定组织");
        }
        
        // 检查用户是否属于指定租户
        if (user.getTenantId() == null || !user.getTenantId().equals(tenantId)) {
            return ApiResult.failed("用户不属于指定租户");
        }
        
        try {
            // 使用UpdateWrapper强制更新null值
            UpdateWrapper<SysUsers> updateWrapper = new UpdateWrapper<SysUsers>()
                    .eq("id", userId)
                    .set("org_id", null)
                    // 根据unbindTenant条件动态设置租户ID
                    .set(unbindTenant, "tenant_id", null);

            // 执行更新操作
            int rows = sysUsersMapper.update(null, updateWrapper);

            // 清除用户角色（保持原有逻辑）
            UserRoleAssignRequest roleRequest = new UserRoleAssignRequest();
            roleRequest.setUserId(userId);
            roleRequest.setRoleIds(Collections.emptyList());
            sysRoleFeignClient.assignUserRoles(roleRequest);

            return ApiResult.success(rows > 0);
        } catch (Exception e) {
            return ApiResult.failed("解绑用户失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateMember(OrgMemberUpdateRequest request) {
        if (request.getOrgId() == null) {
            return ApiResult.failed("组织ID不能为空");
        }
        
        if (request.getTenantId() == null) {
            return ApiResult.failed("租户ID不能为空");
        }
        
        if (request.getUserId() == null) {
            return ApiResult.failed("用户ID不能为空");
        }
        
        // 检查用户是否存在
        SysUsers user = sysUsersMapper.selectById(request.getUserId());
        if (user == null) {
            return ApiResult.failed("用户不存在");
        }
        
        // 检查用户是否属于指定组织
        if (user.getOrgId() == null || !user.getOrgId().equals(request.getOrgId())) {
            return ApiResult.failed("用户不属于指定组织");
        }
        
        // 检查用户是否属于指定租户
        if (user.getTenantId() == null || !user.getTenantId().equals(request.getTenantId())) {
            return ApiResult.failed("用户不属于指定租户");
        }
        
        try {
            // 更新用户基本信息
            boolean needUpdate = false;
            
            if (StringUtils.hasText(request.getUserName()) && !request.getUserName().equals(user.getUserName())) {
                user.setUserName(request.getUserName());
                needUpdate = true;
            }
            
            if (StringUtils.hasText(request.getUserPhone()) && !request.getUserPhone().equals(user.getUserPhone())) {
                user.setUserPhone(request.getUserPhone());
                needUpdate = true;
            }
            
            if (StringUtils.hasText(request.getUserMail()) && !request.getUserMail().equals(user.getUserMail())) {
                user.setUserMail(request.getUserMail());
                needUpdate = true;
            }
            
            if (needUpdate) {
                user.setUpdateTime(System.currentTimeMillis());
                sysUsersMapper.updateById(user);
            }
            
            // 处理角色更新
            if (!CollectionUtils.isEmpty(request.getRoleIds())) {
                // 使用FeignClient为用户分配角色
                UserRoleAssignRequest roleRequest = new UserRoleAssignRequest();
                roleRequest.setUserId(request.getUserId());
                roleRequest.setRoleIds(request.getRoleIds());
                
                ApiResult<Boolean> assignResult = sysRoleFeignClient.assignUserRoles(roleRequest);
                if (assignResult.getCode() != 200 || !Boolean.TRUE.equals(assignResult.getData())) {
                    return ApiResult.failed("更新用户角色失败：" + assignResult.getMessage());
                }
            }
            
            return ApiResult.success(true);
        } catch (Exception e) {
            return ApiResult.failed("更新组织成员信息失败：" + e.getMessage());
        }
    }
} 