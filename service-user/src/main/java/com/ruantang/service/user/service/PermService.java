package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.user.model.dto.PermDTO;
import com.ruantang.service.user.model.request.PermCreateRequest;
import com.ruantang.service.user.model.request.PermQueryRequest;
import com.ruantang.service.user.model.request.PermUpdateRequest;

import java.util.List;

/**
 * 操作权限服务接口
 */
public interface PermService {
    
    /**
     * 分页查询操作权限
     * 
     * @param request 查询请求
     * @return 分页结果
     */
    ApiResult<Page<PermDTO>> queryPermPage(PermQueryRequest request);
    
    /**
     * 根据ID查询操作权限
     * 
     * @param id 权限ID
     * @return 操作权限详情
     */
    ApiResult<PermDTO> getPermById(Long id);
    
    /**
     * 创建操作权限
     * 
     * @param request 创建请求
     * @return 创建结果
     */
    ApiResult<PermDTO> createPerm(PermCreateRequest request);
    
    /**
     * 更新操作权限
     * 
     * @param request 更新请求
     * @return 更新结果
     */
    ApiResult<Boolean> updatePerm(PermUpdateRequest request);
    
    /**
     * 删除操作权限
     * 
     * @param id 权限ID
     * @return 删除结果
     */
    ApiResult<Boolean> deletePermById(Long id);
    
    /**
     * 根据角色ID列表查询操作权限列表
     * 
     * @param roleIds 角色ID列表
     * @return 操作权限列表
     */
    List<PermDTO> getPermsByRoleIds(List<Long> roleIds);
    
    /**
     * 获取用户所有操作权限按钮标识列表
     * 
     * @param userId 用户ID
     * @return 权限按钮标识列表
     */
    List<String> getUserPermButtons(Long userId);
    
    /**
     * 获取用户所有API权限列表
     * 
     * @param userId 用户ID
     * @return API权限列表
     */
    List<String> getUserApiPerms(Long userId);
} 