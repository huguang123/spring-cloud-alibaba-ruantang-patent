package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.PermRelPolicyBinding;
import com.ruantang.entity.perm.RelRolesPerm;
import com.ruantang.entity.rel.RelUserRoles;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.mapper.rel.RelUserRolesMapper;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.dto.PermNodeTreeDTO;
import com.ruantang.service.permissions.model.dto.RolePermissionDTO;
import com.ruantang.service.permissions.model.dto.SysRolesDTO;
import com.ruantang.service.permissions.model.request.RoleCreateRequest;
import com.ruantang.service.permissions.model.request.RolePermissionRequest;
import com.ruantang.service.permissions.model.request.RoleQueryRequest;
import com.ruantang.service.permissions.model.request.RoleUpdateRequest;
import com.ruantang.service.permissions.model.request.UserRoleAssignRequest;
import com.ruantang.service.permissions.repository.PermRelPolicyBindingRepository;
import com.ruantang.service.permissions.repository.RelRolesPermRepository;
import com.ruantang.service.permissions.repository.SysRolesRepository;
import com.ruantang.service.permissions.service.ConfigPermTemplateService;
import com.ruantang.service.permissions.service.PermDataPolicyService;
import com.ruantang.service.permissions.service.PermService;
import com.ruantang.service.permissions.service.SysRoleService;
import com.ruantang.service.permissions.util.RoleUtil;
import com.ruantang.service.permissions.client.TenantTemplateFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    private final RelUserRolesMapper relUserRolesMapper;
    private final TenantTemplateFeignClient tenantTemplateFeignClient;

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

        // 检查角色是否绑定到企业模板
        ApiResult<Boolean> bindingResult = tenantTemplateFeignClient.checkRoleBindingToTemplate(id);
        if (bindingResult == null || bindingResult.getCode() != 200) {
            // Feign调用失败，记录日志或抛出异常，这里简单返回错误信息
            return ApiResult.failed("检查角色绑定状态失败，请稍后再试");
        }
        if (Boolean.TRUE.equals(bindingResult.getData())) {
            return ApiResult.failed("角色已绑定到企业模板，请先解绑后再删除");
        }
        
        // 删除角色的权限关联
        rolesPermRepository.deleteByRoleId(id);
        
        // 删除角色的数据权限策略绑定
        policyBindingRepository.unbindAllPoliciesFromRole(id);
        
        // 删除用户角色关联
        relUserRolesMapper.deleteByRoleId(id);

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
        List<SysRolesDTO> roleDTOList = RoleUtil.convertToRoleDTOList(roles);
        return ApiResult.success(roleDTOList);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> assignUserRoles(UserRoleAssignRequest request) {
        Long userId = request.getUserId();
        List<Long> roleIds = request.getRoleIds();
        
        if (userId == null) {
            return ApiResult.failed("用户ID不能为空");
        }
        
        if (roleIds == null) {
            roleIds = new ArrayList<>();
        }
        
        try {
            // 1. 删除用户当前所有角色关联
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RelUserRoles> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            queryWrapper.eq(RelUserRoles::getUserId, userId);
            relUserRolesMapper.delete(queryWrapper);
            
            // 2. 添加新的角色关联
            if (!roleIds.isEmpty()) {
                long currentTime = System.currentTimeMillis();
                
                for (Long roleId : roleIds) {
                    // 检查角色是否存在
                    SysRoles role = rolesRepository.getRoleById(roleId);
                    if (role == null) {
                        continue; // 跳过不存在的角色
                    }
                    
                    // 创建用户-角色关联记录
                    RelUserRoles userRole = new RelUserRoles();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.setCreateTime(currentTime);
                    
                    // 插入关联记录
                    relUserRolesMapper.insert(userRole);
                }
            }
            
            return ApiResult.success(true);
        } catch (Exception e) {
            return ApiResult.failed("分配用户角色失败：" + e.getMessage());
        }
    }

    @Override
    public ApiResult<List<SysRolesDTO>> getUserRoles(Long userId) {
        if (userId == null) {
            return ApiResult.failed("用户ID不能为空");
        }
        
        try {
            // 查询用户-角色关联
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RelUserRoles> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            queryWrapper.eq(RelUserRoles::getUserId, userId);
            List<RelUserRoles> userRoles = relUserRolesMapper.selectList(queryWrapper);
            
            if (CollectionUtils.isEmpty(userRoles)) {
                return ApiResult.success(Collections.emptyList());
            }
            
            // 获取角色ID列表
            List<Long> roleIds = userRoles.stream()
                    .map(RelUserRoles::getRoleId)
                    .collect(Collectors.toList());
            
            // 查询角色信息
            List<SysRoles> roles = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysRoles role = rolesRepository.getRoleById(roleId);
                if (role != null) {
                    roles.add(role);
                }
            }
            
            // 转换为DTO
            List<SysRolesDTO> roleDTOs = RoleUtil.convertToRoleDTOList(roles);
            
            return ApiResult.success(roleDTOs);
        } catch (Exception e) {
            // 记录日志
            return ApiResult.failed("获取用户角色列表失败：" + e.getMessage());
        }
    }
} 