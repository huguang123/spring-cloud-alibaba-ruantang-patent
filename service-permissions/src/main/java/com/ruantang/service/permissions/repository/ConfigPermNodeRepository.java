package com.ruantang.service.permissions.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.perm.ConfigPermNode;
import com.ruantang.mapper.perm.ConfigPermNodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 配置权限节点数据访问仓库
 */
@Repository
@RequiredArgsConstructor
public class ConfigPermNodeRepository {

    private final ConfigPermNodeMapper nodeMapper;
    
    /**
     * 分页查询配置权限节点
     */
    public Page<ConfigPermNode> queryNodePage(Long moduleId, String nodeName, Integer nodeType, Integer permType, Page<ConfigPermNode> page) {
        LambdaQueryWrapper<ConfigPermNode> queryWrapper = new LambdaQueryWrapper<>();
        
        if (moduleId != null) {
            queryWrapper.eq(ConfigPermNode::getModuleId, moduleId);
        }
        
        if (StringUtils.hasText(nodeName)) {
            queryWrapper.like(ConfigPermNode::getNodeName, nodeName);
        }
        
        if (nodeType != null) {
            queryWrapper.eq(ConfigPermNode::getNodeType, nodeType);
        }
        
        if (permType != null) {
            queryWrapper.eq(ConfigPermNode::getPermType, permType);
        }
        
        queryWrapper.orderByDesc(ConfigPermNode::getCreateTime);
        return nodeMapper.selectPage(page, queryWrapper);
    }
    
    /**
     * 根据ID查询配置权限节点
     */
    public ConfigPermNode getNodeById(Long id) {
        return nodeMapper.selectById(id);
    }
    
    /**
     * 根据模块ID查询节点列表
     */
    public List<ConfigPermNode> listNodesByModuleId(Long moduleId) {
        LambdaQueryWrapper<ConfigPermNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermNode::getModuleId, moduleId)
                   .orderByDesc(ConfigPermNode::getCreateTime);
        return nodeMapper.selectList(queryWrapper);
    }
    
    /**
     * 保存配置权限节点
     */
    public boolean saveNode(ConfigPermNode node) {
        return nodeMapper.insert(node) > 0;
    }
    
    /**
     * 更新配置权限节点
     */
    public boolean updateNode(ConfigPermNode node) {
        return nodeMapper.updateById(node) > 0;
    }
    
    /**
     * 删除配置权限节点
     */
    public boolean deleteNodeById(Long id) {
        return nodeMapper.deleteById(id) > 0;
    }
    
    /**
     * 根据模块ID删除所有节点
     */
    public boolean deleteNodesByModuleId(Long moduleId) {
        LambdaQueryWrapper<ConfigPermNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermNode::getModuleId, moduleId);
        return nodeMapper.delete(queryWrapper) >= 0;
    }
    
    /**
     * 检查权限是否绑定到配置权限节点
     */
    public boolean checkPermBindingToNode(Long permId) {
        LambdaQueryWrapper<ConfigPermNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConfigPermNode::getPermId, permId);
        return nodeMapper.selectCount(queryWrapper) > 0;
    }
}
