package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.PermRelPolicyBinding;
import com.ruantang.entity.perm.RelRolesPerm;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.dto.PermNodeTreeDTO;
import com.ruantang.service.permissions.model.dto.RolePermissionDTO;
import com.ruantang.service.permissions.model.dto.SysRolesDTO;
import com.ruantang.service.permissions.model.request.RoleCreateRequest;
import com.ruantang.service.permissions.model.request.RolePermissionRequest;
import com.ruantang.service.permissions.model.request.RoleQueryRequest;
import com.ruantang.service.permissions.model.request.RoleUpdateRequest;
import com.ruantang.service.permissions.repository.PermRelPolicyBindingRepository;
import com.ruantang.service.permissions.repository.RelRolesPermRepository;
import com.ruantang.service.permissions.repository.SysRolesRepository;
import com.ruantang.service.permissions.service.ConfigPermTemplateService;
import com.ruantang.service.permissions.service.PermDataPolicyService;
import com.ruantang.service.permissions.service.PermService;
import com.ruantang.service.permissions.service.SysRoleService;
import com.ruantang.service.permissions.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRolesRepository rolesRepository;
    private final RelRolesPermRepository rolesPermRepository;
    private final PermRelPolicyBindingRepository policyBindingRepository;
    private final PermService permService;
    private final PermDataPolicyService policyService;
    private final ConfigPermTemplateService templateService;

    @Override
    public ApiResult<Page<SysRolesDTO>> queryRolePage(RoleQueryRequest request) {
        Page<SysRoles> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<SysRoles> rolePage = rolesRepository.queryRolesPage(
                request.getRoleName(),
                request.getRoleCode(),
                request.getRoleType(),
                page
        );
        
        // 转换为DTO
        Page<SysRolesDTO> resultPage = new Page<>(rolePage.getCurrent(), rolePage.getSize(), rolePage.getTotal());
        List<SysRolesDTO> records = RoleUtil.convertToRoleDTOList(rolePage.getRecords());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<SysRolesDTO> getRoleById(Long id) {
        SysRoles role = rolesRepository.getRoleById(id);
        if (role == null) {
            return ApiResult.failed("角色不存在");
        }
        
        SysRolesDTO roleDTO = RoleUtil.convertToRoleDTO(role);
        return ApiResult.success(roleDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<SysRolesDTO> createRole(RoleCreateRequest request) {
        // 检查角色编码是否已存在
        SysRoles existRole = rolesRepository.getRoleByCode(request.getRolesCode());
        if (existRole != null) {
            return ApiResult.failed("角色编码已存在");
        }
        
        SysRoles role = new SysRoles();
        BeanUtils.copyProperties(request, role);
        
        // 设置创建时间和更新时间
        role.setCreateTime(System.currentTimeMillis());
        role.setUpdateTime(System.currentTimeMillis());
        
        boolean success = rolesRepository.saveRole(role);
        if (!success) {
            return ApiResult.failed("创建角色失败");
        }
        
        SysRolesDTO roleDTO = RoleUtil.convertToRoleDTO(role);
        return ApiResult.success(roleDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateRole(RoleUpdateRequest request) {
        // 检查角色是否存在
        SysRoles role = rolesRepository.getRoleById(request.getId());
        if (role == null) {
            return ApiResult.failed("角色不存在");
        }
        
        // 仅允许更新角色名称和描述
        if (StringUtils.hasText(request.getRolesName())) {
            role.setRolesName(request.getRolesName());
        }
        
        if (StringUtils.hasText(request.getRolesDescription())) {
            role.setRolesDescription(request.getRolesDescription());
        }
        
        // 更新时间
        role.setUpdateTime(System.currentTimeMillis());
        
        boolean success = rolesRepository.updateRole(role);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteRole(Long id) {
        // 检查角色是否存在
        SysRoles role = rolesRepository.getRoleById(id);
        if (role == null) {
            return ApiResult.failed("角色不存在");
        }
        
        // 删除角色的权限关联
        rolesPermRepository.deleteByRoleId(id);
        
        // 删除角色的数据权限策略绑定
        policyBindingRepository.unbindAllPoliciesFromRole(id);
        
        // 删除角色
        boolean success = rolesRepository.deleteRoleById(id);
        return ApiResult.success(success);
    }

    @Override
    public ApiResult<PermNodeTreeDTO> getRolePermissionTree(Long roleId) {
        // 检查角色是否存在
        SysRoles role = rolesRepository.getRoleById(roleId);
        if (role == null) {
            throw new ApiException("角色不存在");
        }
        
        // 获取角色类型
        Integer roleType = role.getRolesType();
        
        // 根据角色类型获取权限树
        ApiResult<PermNodeTreeDTO> treeResult = templateService.getPermTreeByRoleType(roleType);
        if (treeResult.getCode() != 200 || treeResult.getData() == null) {
            return ApiResult.failed("获取权限模板树失败: " + treeResult.getMessage());
        }
        
        PermNodeTreeDTO treeDTO = treeResult.getData();
        
        // 获取角色已分配的权限
        ApiResult<RolePermissionDTO> permResult = getRolePermissions(roleId);
        if (permResult.getCode() != 200 || permResult.getData() == null) {
            return ApiResult.success(treeDTO);
        }
        
        RolePermissionDTO rolePermDTO = permResult.getData();
        List<Long> permIds = rolePermDTO.getPermIds();
        List<Long> dataPolicyIds = rolePermDTO.getDataPolicyIds();
        
        // 标记已选择的节点
        markSelectedNodes(treeDTO.getOperationModules(), permIds, true);
        markSelectedNodes(treeDTO.getDataModules(), dataPolicyIds, false);
        
        return ApiResult.success(treeDTO);
    }
    
    /**
     * 标记已选择的节点
     */
    private void markSelectedNodes(List<PermNodeTreeDTO.PermModuleDTO> modules, List<Long> selectedIds, boolean isOperation) {
        if (modules == null || modules.isEmpty() || selectedIds == null || selectedIds.isEmpty()) {
            return;
        }
        
        for (PermNodeTreeDTO.PermModuleDTO module : modules) {
            if (module.getNodes() == null || module.getNodes().isEmpty()) {
                continue;
            }
            
            for (PermNodeTreeDTO.PermNodeDTO node : module.getNodes()) {
                Long idToCheck = isOperation ? node.getPermId() : node.getDataPolicyId();
                if (idToCheck != null && selectedIds.contains(idToCheck)) {
                    node.setSelected(true);
                }
            }
        }
    }

    @Override
    public ApiResult<RolePermissionDTO> getRolePermissions(Long roleId) {
        // 检查角色是否存在
        SysRoles role = rolesRepository.getRoleById(roleId);
        if (role == null) {
            return ApiResult.failed("角色不存在");
        }
        
        // 获取角色的操作权限ID列表
        List<RelRolesPerm> rolePerms = rolesPermRepository.listPermsByRoleId(roleId);
        List<Long> permIds = rolePerms.stream()
                .map(RelRolesPerm::getPermsId)
                .collect(Collectors.toList());
        
        // 获取角色的数据权限策略ID列表
        List<PermRelPolicyBinding> policyBindings = policyBindingRepository.listRolePolicyBindings(roleId);
        List<Long> dataPolicyIds = policyBindings.stream()
                .map(PermRelPolicyBinding::getPolicyId)
                .collect(Collectors.toList());
        
        // 构建返回结果
        RolePermissionDTO result = new RolePermissionDTO();
        result.setRoleId(roleId);
        result.setRoleCode(role.getRolesCode());
        result.setRoleName(role.getRolesName());
        result.setPermIds(permIds);
        result.setDataPolicyIds(dataPolicyIds);
        
        return ApiResult.success(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> assignRolePermissions(RolePermissionRequest request) {
        // 检查角色是否存在
        SysRoles role = rolesRepository.getRoleById(request.getRoleId());
        if (role == null) {
            return ApiResult.failed("角色不存在");
        }
        
        // 先删除角色的所有操作权限关联
        rolesPermRepository.deleteByRoleId(request.getRoleId());
        
        // 添加新的操作权限关联
        if (request.getPermIds() != null && !request.getPermIds().isEmpty()) {
            List<RelRolesPerm> rolePerms = new ArrayList<>();
            for (Long permId : request.getPermIds()) {
                // 验证权限是否存在
                ApiResult<PermDTO> permResult = permService.getPermById(permId);
                if (permResult.getCode() != 200 || permResult.getData() == null) {
                    continue; // 跳过不存在的权限
                }
                
                RelRolesPerm rolePerm = new RelRolesPerm();
                rolePerm.setRolesId(request.getRoleId());
                rolePerm.setPermsId(permId);
                rolePerm.setCreateTime(System.currentTimeMillis());
                rolePerms.add(rolePerm);
            }
            
            rolesPermRepository.batchSaveRolePerms(rolePerms);
        }
        
        // 先删除角色的所有数据权限策略绑定
        policyBindingRepository.unbindAllPoliciesFromRole(request.getRoleId());
        
        // 添加新的数据权限策略绑定
        if (request.getDataPolicyIds() != null && !request.getDataPolicyIds().isEmpty()) {
            for (Long policyId : request.getDataPolicyIds()) {
                // 验证策略是否存在
                ApiResult<PermDataPolicyDTO> policyResult = policyService.getPolicyById(policyId);
                if (policyResult.getCode() != 200 || policyResult.getData() == null) {
                    continue; // 跳过不存在的策略
                }
                
                policyBindingRepository.bindPolicyToRole(policyId, request.getRoleId());
            }
        }
        
        return ApiResult.success(true);
    }

    @Override
    public ApiResult<List<SysRolesDTO>> listRolesByType(Integer roleType) {
        List<SysRoles> roles = rolesRepository.listRolesByType(roleType);
        List<SysRolesDTO> roleDTOs = RoleUtil.convertToRoleDTOList(roles);
        return ApiResult.success(roleDTOs);
    }
} 