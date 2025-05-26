package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.entity.perm.Perm;
import com.ruantang.entity.rel.RelUserRoles;
import com.ruantang.mapper.rel.RelUserRolesMapper;
import com.ruantang.service.user.model.dto.PermDTO;
import com.ruantang.service.user.model.request.PermCreateRequest;
import com.ruantang.service.user.model.request.PermQueryRequest;
import com.ruantang.service.user.model.request.PermUpdateRequest;
import com.ruantang.service.user.repository.PermRepository;
import com.ruantang.service.user.service.PermService;
import com.ruantang.service.user.util.PermUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作权限服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermServiceImpl implements PermService {
    
    private final PermRepository permRepository;
    private final RelUserRolesMapper relUserRolesMapper;
    
    @Override
    public ApiResult<Page<PermDTO>> queryPermPage(PermQueryRequest request) {
        Page<Perm> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Perm> permPage = permRepository.queryPermPage(
                request.getPermsCode(),
                request.getPermsName(),
                request.getPermScope(),
                page
        );
        
        Page<PermDTO> resultPage = new Page<>(permPage.getCurrent(), permPage.getSize(), permPage.getTotal());
        List<PermDTO> permDTOList = permPage.getRecords().stream()
                .map(PermUtil::convertToDTO)
                .collect(Collectors.toList());
        resultPage.setRecords(permDTOList);
        
        return ApiResult.success(resultPage);
    }
    
    @Override
    public ApiResult<PermDTO> getPermById(Long id) {
        Perm perm = permRepository.getPermById(id);
        if (perm == null) {
            return ApiResult.failed("权限不存在");
        }
        
        return ApiResult.success(PermUtil.convertToDTO(perm));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<PermDTO> createPerm(PermCreateRequest request) {
        // 检查权限标识是否存在
        Perm existingPerm = permRepository.getPermByCode(request.getPermsCode());
        if (existingPerm != null) {
            return ApiResult.failed("权限标识已存在");
        }
        
        // 创建权限
        Perm perm = new Perm();
        BeanUtils.copyProperties(request, perm);
        perm.setCreateTime(System.currentTimeMillis());
        perm.setUpdateTime(System.currentTimeMillis());
        
        boolean success = permRepository.createPerm(perm);
        if (!success) {
            return ApiResult.failed("创建权限失败");
        }
        
        return ApiResult.success(PermUtil.convertToDTO(perm));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updatePerm(PermUpdateRequest request) {
        // 检查权限是否存在
        Perm existingPerm = permRepository.getPermById(request.getId());
        if (existingPerm == null) {
            return ApiResult.failed("权限不存在");
        }
        
        // 如果更新权限标识，检查是否与其他权限冲突
        if (request.getPermsCode() != null && !request.getPermsCode().equals(existingPerm.getPermsCode())) {
            Perm permByCode = permRepository.getPermByCode(request.getPermsCode());
            if (permByCode != null && !permByCode.getId().equals(request.getId())) {
                return ApiResult.failed("权限标识已被其他权限使用");
            }
        }
        
        // 更新权限
        Perm perm = new Perm();
        BeanUtils.copyProperties(request, perm);
        perm.setUpdateTime(System.currentTimeMillis());
        
        boolean success = permRepository.updatePerm(perm);
        if (!success) {
            return ApiResult.failed("更新权限失败");
        }
        
        return ApiResult.success(true);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deletePermById(Long id) {
        // 检查权限是否存在
        Perm existingPerm = permRepository.getPermById(id);
        if (existingPerm == null) {
            return ApiResult.failed("权限不存在");
        }
        
        // 删除权限
        boolean success = permRepository.deletePermById(id);
        if (!success) {
            return ApiResult.failed("删除权限失败");
        }
        
        return ApiResult.success(true);
    }
    
    @Override
    public List<PermDTO> getPermsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        List<Perm> perms = permRepository.getPermsByRoleIds(roleIds);
        return perms.stream()
                .map(PermUtil::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getUserPermButtons(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        
        // 获取用户角色
        List<Long> roleIds = getUserRoleIds(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        // 获取角色权限
        List<Perm> perms = permRepository.getPermsByRoleIds(roleIds);
        
        // 过滤出按钮权限（权限标识格式为xxx:xxx的权限）
        return perms.stream()
                .filter(perm -> perm.getPermsCode() != null && perm.getPermsCode().contains(":"))
                .map(Perm::getPermsCode)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getUserApiPerms(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        
        // 获取用户角色
        List<Long> roleIds = getUserRoleIds(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        // 获取角色权限
        List<Perm> perms = permRepository.getPermsByRoleIds(roleIds);
        
        // 过滤出API权限（同时拥有HTTP方法和接口路径的权限）
        return perms.stream()
                .filter(perm -> perm.getApiMethod() != null && perm.getApiPath() != null)
                .map(perm -> perm.getApiMethod() + ":" + perm.getApiPath())
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户的角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    private List<Long> getUserRoleIds(Long userId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RelUserRoles> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(RelUserRoles::getUserId, userId);
        
        List<RelUserRoles> userRoles = relUserRolesMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }
        
        return userRoles.stream()
                .map(RelUserRoles::getRoleId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getButtonsByPermIds(List<Long> permIds) {
        if (CollectionUtils.isEmpty(permIds)) {
            return Collections.emptyList();
        }
        
        // 根据权限ID获取权限信息
        List<Perm> perms = permRepository.getPermsByIds(permIds);
        if (CollectionUtils.isEmpty(perms)) {
            return Collections.emptyList();
        }
        
        // 筛选按钮类型权限并提取按钮标识
        return perms.stream()
                .filter(perm -> "BUTTON".equals(perm.getPermType()))
                .map(Perm::getPermsCode)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getApisByPermIds(List<Long> permIds) {
        if (CollectionUtils.isEmpty(permIds)) {
            return Collections.emptyList();
        }
        
        // 根据权限ID获取权限信息
        List<Perm> perms = permRepository.getPermsByIds(permIds);
        if (CollectionUtils.isEmpty(perms)) {
            return Collections.emptyList();
        }
        
        // 筛选API类型权限并提取API地址
        return perms.stream()
                .filter(perm -> "API".equals(perm.getPermType()))
                .map(perm -> perm.getApiMethod() + ":" + perm.getApiPath())
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getButtonsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        // 根据角色ID获取权限信息
        List<Perm> perms = permRepository.getPermsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(perms)) {
            return Collections.emptyList();
        }
        
        // 筛选按钮类型权限并提取按钮标识
        return perms.stream()
                .filter(perm -> "BUTTON".equals(perm.getPermType()))
                .map(Perm::getPermsCode)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getApisByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        
        // 根据角色ID获取权限信息
        List<Perm> perms = permRepository.getPermsByRoleIds(roleIds);
        log.info("perms:{}",perms);
        if (CollectionUtils.isEmpty(perms)) {
            return Collections.emptyList();
        }
        
        // 筛选API类型权限并提取API地址
        return perms.stream()
                .filter(perm -> "API".equals(perm.getPermType()))
                .map(perm -> perm.getApiMethod() + ":" + perm.getApiPath())
                .collect(Collectors.toList());
    }
} 