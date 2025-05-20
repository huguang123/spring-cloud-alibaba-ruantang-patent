package com.ruantang.service.organization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.organization.model.dto.OrganizationMemberDTO;
import com.ruantang.service.organization.model.request.OrgMemberQueryRequest;
import com.ruantang.service.organization.model.request.OrgMemberUpdateRequest;
import com.ruantang.service.organization.model.request.UserOrgBindRequest;

/**
 * 组织成员服务接口
 */
public interface OrganizationMemberService {
    
    /**
     * 分页查询组织成员
     *
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<OrganizationMemberDTO>> queryMemberPage(OrgMemberQueryRequest request);
    
    /**
     * 绑定用户到组织
     *
     * @param request 绑定请求
     * @return 绑定结果
     */
    ApiResult<Boolean> bindUserToOrg(UserOrgBindRequest request);
    
    /**
     * 解绑用户与组织
     *
     * @param orgId 组织ID
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param unbindTenant 是否同时解绑租户关系
     * @return 解绑结果
     */
    ApiResult<Boolean> unbindUserFromOrg(Long orgId, Long tenantId, Long userId, Boolean unbindTenant);
    
    /**
     * 更新组织成员信息
     *
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updateMember(OrgMemberUpdateRequest request);
} 