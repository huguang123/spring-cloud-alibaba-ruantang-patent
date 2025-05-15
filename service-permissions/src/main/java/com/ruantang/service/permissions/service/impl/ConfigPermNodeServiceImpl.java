package com.ruantang.service.permissions.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.commons.api.ApiResult;
import com.ruantang.commons.exception.ApiException;
import com.ruantang.entity.perm.ConfigPermModule;
import com.ruantang.entity.perm.ConfigPermNode;
import com.ruantang.service.permissions.model.dto.ConfigPermNodeDTO;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import com.ruantang.service.permissions.model.request.NodeCreateRequest;
import com.ruantang.service.permissions.model.request.NodeQueryRequest;
import com.ruantang.service.permissions.model.request.NodeUpdateRequest;
import com.ruantang.service.permissions.repository.ConfigPermModuleRepository;
import com.ruantang.service.permissions.repository.ConfigPermNodeRepository;
import com.ruantang.service.permissions.service.ConfigPermNodeService;
import com.ruantang.service.permissions.service.PermDataPolicyService;
import com.ruantang.service.permissions.service.PermService;
import com.ruantang.service.permissions.util.ConfigPermUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置权限节点服务实现类
 */
@Service
@RequiredArgsConstructor
public class ConfigPermNodeServiceImpl implements ConfigPermNodeService {

    private final ConfigPermNodeRepository nodeRepository;
    private final ConfigPermModuleRepository moduleRepository;
    private final PermService permService;
    private final PermDataPolicyService permDataPolicyService;

    /**
     * 填充节点的权限详情
     * @param nodeDTO 节点DTO
     */
    private void fillPermissionDetails(ConfigPermNodeDTO nodeDTO) {
        if (nodeDTO == null) {
            return;
        }
        
        // 如果是操作权限类型且有绑定权限ID，获取操作权限详情
        if (nodeDTO.getPermType() != null && nodeDTO.getPermType() == 0 && nodeDTO.getPermId() != null) {
            ApiResult<PermDTO> permResult = permService.getPermById(nodeDTO.getPermId());
            if (permResult.getCode() == 200 && permResult.getData() != null) {
                nodeDTO.setPermDetail(permResult.getData());
            }
        }
        
        // 如果是数据权限类型且有绑定策略ID，获取数据权限策略详情
        if (nodeDTO.getPermType() != null && nodeDTO.getPermType() == 1 && nodeDTO.getDataPolicyId() != null) {
            ApiResult<PermDataPolicyDTO> policyResult = permDataPolicyService.getPolicyById(nodeDTO.getDataPolicyId());
            if (policyResult.getCode() == 200 && policyResult.getData() != null) {
                nodeDTO.setDataPolicyDetail(policyResult.getData());
            }
        }
    }
    
    /**
     * 填充节点列表的权限详情
     * @param nodeDTOList 节点DTO列表
     */
    private void fillPermissionDetailsList(List<ConfigPermNodeDTO> nodeDTOList) {
        if (nodeDTOList == null || nodeDTOList.isEmpty()) {
            return;
        }
        
        for (ConfigPermNodeDTO nodeDTO : nodeDTOList) {
            fillPermissionDetails(nodeDTO);
        }
    }

    @Override
    public ApiResult<Page<ConfigPermNodeDTO>> queryNodePage(NodeQueryRequest request) {
        Page<ConfigPermNode> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<ConfigPermNode> nodePage = nodeRepository.queryNodePage(
                request.getModuleId(),
                request.getNodeName(),
                request.getNodeType(),
                request.getPermType(),
                page
        );
        
        // 转换为DTO
        Page<ConfigPermNodeDTO> resultPage = new Page<>(nodePage.getCurrent(), nodePage.getSize(), nodePage.getTotal());
        List<ConfigPermNodeDTO> records = ConfigPermUtil.convertToNodeDTOList(nodePage.getRecords());
        
        // 填充权限详情
        fillPermissionDetailsList(records);
        
        resultPage.setRecords(records);
        
        return ApiResult.success(resultPage);
    }

    @Override
    public ApiResult<ConfigPermNodeDTO> getNodeById(Long id) {
        ConfigPermNode node = nodeRepository.getNodeById(id);
        if (node == null) {
            return ApiResult.failed("配置权限节点不存在");
        }
        
        ConfigPermNodeDTO nodeDTO = ConfigPermUtil.convertToNodeDTO(node);
        
        // 填充权限详情
        fillPermissionDetails(nodeDTO);
        
        return ApiResult.success(nodeDTO);
    }

    @Override
    public ApiResult<List<ConfigPermNodeDTO>> listNodesByModuleId(Long moduleId) {
        // 检查模块是否存在
        ConfigPermModule module = moduleRepository.getModuleById(moduleId);
        if (module == null) {
            return ApiResult.failed("配置权限模块不存在");
        }
        
        List<ConfigPermNode> nodes = nodeRepository.listNodesByModuleId(moduleId);
        List<ConfigPermNodeDTO> dtoList = ConfigPermUtil.convertToNodeDTOList(nodes);
        
        // 填充权限详情
        fillPermissionDetailsList(dtoList);
        
        return ApiResult.success(dtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<ConfigPermNodeDTO> createNode(NodeCreateRequest request) {
        // 检查模块是否存在
        ConfigPermModule module = moduleRepository.getModuleById(request.getModuleId());
        if (module == null) {
            throw new ApiException("配置权限模块不存在");
        }
        
        ConfigPermNode node = new ConfigPermNode();
        BeanUtils.copyProperties(request, node);
        
        // 设置创建时间
        node.setCreateTime(System.currentTimeMillis());
        node.setUpdateTime(System.currentTimeMillis());
        
        boolean success = nodeRepository.saveNode(node);
        if (!success) {
            return ApiResult.failed("创建配置权限节点失败");
        }
        
        ConfigPermNodeDTO nodeDTO = ConfigPermUtil.convertToNodeDTO(node);
        
        // 填充权限详情
        fillPermissionDetails(nodeDTO);
        
        return ApiResult.success(nodeDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> updateNode(NodeUpdateRequest request) {
        // 检查节点是否存在
        ConfigPermNode existNode = nodeRepository.getNodeById(request.getId());
        if (existNode == null) {
            return ApiResult.failed("配置权限节点不存在");
        }
        
        ConfigPermNode node = new ConfigPermNode();
        BeanUtils.copyProperties(request, node);
        
        // 设置更新时间
        node.setUpdateTime(System.currentTimeMillis());
        
        boolean success = nodeRepository.updateNode(node);
        return ApiResult.success(success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteNodeById(Long id) {
        // 检查节点是否存在
        ConfigPermNode existNode = nodeRepository.getNodeById(id);
        if (existNode == null) {
            return ApiResult.failed("配置权限节点不存在");
        }
        
        boolean success = nodeRepository.deleteNodeById(id);
        return ApiResult.success(success);
    }
} 