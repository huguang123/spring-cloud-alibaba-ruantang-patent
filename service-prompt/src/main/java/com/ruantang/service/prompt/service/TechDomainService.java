package com.ruantang.service.prompt.service;

import com.ruantang.entity.prom.TechDomain;
import com.ruantang.service.prompt.model.TechDomainDTO;
import com.ruantang.service.prompt.model.TechDomainRequest;

import java.util.List;

/**
 * 技术领域Service接口
 */
public interface TechDomainService {

    /**
     * 创建技术领域
     * @param request 技术领域请求对象
     * @return 技术领域ID
     */
    Long createTechDomain(TechDomainRequest request);

    /**
     * 更新技术领域
     * @param request 技术领域请求对象
     * @return 是否成功
     */
    boolean updateTechDomain(TechDomainRequest request);

    /**
     * 删除技术领域
     * @param id 技术领域ID
     * @return 是否成功
     */
    boolean deleteTechDomain(Long id);

    /**
     * 获取技术领域详情
     * @param id 技术领域ID
     * @return 技术领域DTO
     */
    TechDomainDTO getTechDomain(Long id);

    /**
     * 获取技术领域树
     * @return 技术领域树
     */
    List<TechDomainDTO> getTechDomainTree();

    /**
     * 获取指定父级ID下的子技术领域
     * @param parentId 父级ID
     * @return 子技术领域列表
     */
    List<TechDomainDTO> getChildrenByParentId(Long parentId);

    /**
     * 根据技术领域名称查询技术领域及其上级技术领域树
     * @param domainName 技术领域名称
     * @return 包含技术领域及其上级技术领域树的列表
     */
    List<TechDomainDTO> searchWithParents(String domainName);
} 