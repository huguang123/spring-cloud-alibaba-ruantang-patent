package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.Perm;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.request.PermCreateRequest;
import com.ruantang.service.permissions.model.request.PermQueryRequest;
import com.ruantang.service.permissions.model.request.PermUpdateRequest;
import com.ruantang.service.permissions.repository.PermRepository;
import com.ruantang.service.permissions.service.PermService;
import com.ruantang.service.permissions.util.PermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作权限服务实现类
 */
@Service
@RequiredArgsConstructor
public class PermServiceImpl implements PermService {

    private final PermRepository permRepository;

    @Override
    public ApiResult<Page<PermDTO>> queryPermPage(PermQueryRequest request) {
        Page<Perm> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<Perm> permPage = permRepository.queryPermPage(
                request.getPermsName(),
                request.getPermsCode(),
                request.getApiMethod(),
                request.getPermType(),
                page
        );
        
        // 转换为DTO
        Page<PermDTO> resultPage = new Page<>(permPage.getCurrent(), permPage.getSize(), permPage.getTotal());
        List<PermDTO> records = PermUtil.convertToPermDTOList(permPage.getRecords());
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<PermDTO> getPermById(Long id) {
        Perm perm = permRepository.getPermById(id);
        if (perm == null) {
            return ApiResult.failed("操作权限不存在");
        }
        
        return ApiResult.success(PermUtil.convertToPermDTO(perm));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<PermDTO> createPerm(PermCreateRequest request) {
        // 检查权限编码是否已存在
        Perm existPerm = permRepository.getPermByCode(request.getPermsCode());
        if (existPerm != null) {
            throw new ApiException("权限编码已存在");
        }
        
        Perm perm = new Perm();
        BeanUtils.copyProperties(request, perm);
        
        // 设置创建时间
        perm.setCreateTime(System.currentTimeMillis());
        perm.setUpdateTime(System.currentTimeMillis());
        
        // 生成ID (这里使用时间戳，实际应用中可以使用雪花算法等)
//        perm.setId(System.currentTimeMillis());
        
        boolean success = permRepository.savePerm(perm);
        if (!success) {
            return ApiResult.failed("创建操作权限失败");
        }
        
        return ApiResult.success(PermUtil.convertToPermDTO(perm));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updatePerm(PermUpdateRequest request) {
        // 检查权限是否存在
        Perm existPerm = permRepository.getPermById(request.getId());
        if (existPerm == null) {
            return ApiResult.failed("操作权限不存在");
        }
        
        // 检查权限编码是否重复
        if (request.getPermsCode() != null && !request.getPermsCode().equals(existPerm.getPermsCode())) {
            Perm codeExistPerm = permRepository.getPermByCode(request.getPermsCode());
            if (codeExistPerm != null) {
                return ApiResult.failed("权限编码已存在");
            }
        }
        
        Perm perm = new Perm();
        BeanUtils.copyProperties(request, perm);
        
        // 设置更新时间
        perm.setUpdateTime(System.currentTimeMillis());
        
        boolean success = permRepository.updatePerm(perm);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deletePermById(Long id) {
        // 检查权限是否存在
        Perm existPerm = permRepository.getPermById(id);
        if (existPerm == null) {
            return ApiResult.failed("操作权限不存在");
        }
        
        boolean success = permRepository.deletePermById(id);
        return ApiResult.success(success);
    }
} 
 