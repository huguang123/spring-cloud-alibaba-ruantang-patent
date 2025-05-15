package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.request.PermCreateRequest;
import com.ruantang.service.permissions.model.request.PermQueryRequest;
import com.ruantang.service.permissions.model.request.PermUpdateRequest;

/**
 * 操作权限服务接口
 */
public interface PermService {
    
    /**
     * 分页查询操作权限
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<PermDTO>> queryPermPage(PermQueryRequest request);
    
    /**
     * 根据ID查询操作权限
     * @param id 权限ID
     * @return 权限信息
     */
    ApiResult<PermDTO> getPermById(Long id);
    
    /**
     * 创建操作权限
     * @param request 创建请求
     * @return 创建结果
     */
    ApiResult<PermDTO> createPerm(PermCreateRequest request);
    
    /**
     * 更新操作权限
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updatePerm(PermUpdateRequest request);
    
    /**
     * 删除操作权限
     * @param id 权限ID
     * @return 删除结果
     */
    ApiResult<Boolean> deletePermById(Long id);
} 