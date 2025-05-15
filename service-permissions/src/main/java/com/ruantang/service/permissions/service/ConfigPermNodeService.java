package com.ruantang.service.permissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.permissions.model.dto.ConfigPermNodeDTO;
import com.ruantang.service.permissions.model.request.NodeCreateRequest;
import com.ruantang.service.permissions.model.request.NodeQueryRequest;
import com.ruantang.service.permissions.model.request.NodeUpdateRequest;

import java.util.List;

/**
 * 配置权限节点服务接口
 */
public interface ConfigPermNodeService {
    
    /**
     * 分页查询配置权限节点
     * @param request 查询请求参数
     * @return 分页结果
     */
    ApiResult<Page<ConfigPermNodeDTO>> queryNodePage(NodeQueryRequest request);
    
    /**
     * 根据ID获取配置权限节点详情
     * @param id 节点ID
     * @return 节点详情
     */
    ApiResult<ConfigPermNodeDTO> getNodeById(Long id);
    
    /**
     * 根据模块ID获取节点列表
     * @param moduleId 模块ID
     * @return 节点列表
     */
    ApiResult<List<ConfigPermNodeDTO>> listNodesByModuleId(Long moduleId);
    
    /**
     * 创建配置权限节点
     * @param request 创建请求参数
     * @return 创建的节点信息
     */
    ApiResult<ConfigPermNodeDTO> createNode(NodeCreateRequest request);
    
    /**
     * 更新配置权限节点
     * @param request 更新请求参数
     * @return 更新结果
     */
    ApiResult<Boolean> updateNode(NodeUpdateRequest request);
    
    /**
     * 删除配置权限节点
     * @param id 节点ID
     * @return 删除结果
     */
    ApiResult<Boolean> deleteNodeById(Long id);
} 