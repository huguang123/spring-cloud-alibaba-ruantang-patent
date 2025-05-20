package com.ruantang.mapper.organ;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.organ.OrganizationHierarchy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrganizationHierarchyMapper extends BaseMapper<OrganizationHierarchy> {
    
    /**
     * 批量插入组织层级关系
     * 
     * @param hierarchies 组织层级关系列表
     * @return 插入行数
     */
    int batchInsert(@Param("hierarchies") List<OrganizationHierarchy> hierarchies);
    
    /**
     * 查询某个组织的所有祖先节点
     * 
     * @param orgId 组织ID
     * @return 祖先组织ID列表
     */
    List<Long> findAncestors(@Param("orgId") Long orgId);
    
    /**
     * 查询某个组织的所有子孙节点
     * 
     * @param orgId 组织ID
     * @return 子孙组织ID列表
     */
    List<Long> findDescendants(@Param("orgId") Long orgId);
    
    /**
     * 查询某个组织的直接子节点
     * 
     * @param orgId 组织ID
     * @return 直接子节点组织ID列表
     */
    List<Long> findDirectChildren(@Param("orgId") Long orgId);
    
    /**
     * 删除与指定组织相关的所有层级关系
     * 
     * @param orgId 组织ID
     * @return 删除行数
     */
    int deleteByOrgId(@Param("orgId") Long orgId);
} 