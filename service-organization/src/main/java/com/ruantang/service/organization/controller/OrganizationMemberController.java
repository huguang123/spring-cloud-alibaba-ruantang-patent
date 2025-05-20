package com.ruantang.service.organization.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.organization.model.dto.OrganizationMemberDTO;
import com.ruantang.service.organization.model.request.OrgMemberQueryRequest;
import com.ruantang.service.organization.model.request.OrgMemberUpdateRequest;
import com.ruantang.service.organization.model.request.UserOrgBindRequest;
import com.ruantang.service.organization.service.OrganizationMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 组织成员控制器
 */
@RestController
@RequestMapping("/api/v1/org/members")
@Api(tags = "组织成员管理接口")
@RequiredArgsConstructor
public class OrganizationMemberController {
    
    private final OrganizationMemberService organizationMemberService;
    
    @GetMapping("/page")
    @ApiOperation("分页查询组织成员")
    public ApiResult<Page<OrganizationMemberDTO>> queryMemberPage(@Valid OrgMemberQueryRequest request) {
        return organizationMemberService.queryMemberPage(request);
    }
    
    @PostMapping("/bind")
    @ApiOperation("绑定用户到组织")
    public ApiResult<Boolean> bindUserToOrg(@Valid @RequestBody UserOrgBindRequest request) {
        return organizationMemberService.bindUserToOrg(request);
    }
    
    @DeleteMapping("/unbind")
    @ApiOperation("解绑用户与组织")
    public ApiResult<Boolean> unbindUserFromOrg(
            @ApiParam(value = "组织ID", required = true) @RequestParam Long orgId,
            @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId,
            @ApiParam(value = "是否同时解绑租户关系", defaultValue = "false") @RequestParam(defaultValue = "false") Boolean unbindTenant) {
        return organizationMemberService.unbindUserFromOrg(orgId, tenantId, userId, unbindTenant);
    }
    
    @PutMapping("/update")
    @ApiOperation("更新组织成员信息")
    public ApiResult<Boolean> updateMember(@Valid @RequestBody OrgMemberUpdateRequest request) {
        return organizationMemberService.updateMember(request);
    }
} 