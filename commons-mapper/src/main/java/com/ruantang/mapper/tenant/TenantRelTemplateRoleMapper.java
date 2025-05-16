package com.ruantang.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.tenant.TenantRelTemplateRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 租户模板角色关联数据访问接口
 */
@Repository
@Mapper
public interface TenantRelTemplateRoleMapper extends BaseMapper<TenantRelTemplateRole> {

    /**
     * 根据模板ID查询所有关联的角色
     *
     * @param templateId 模板ID
     * @return 角色关联列表
     */
    List<TenantRelTemplateRole> listRolesByTemplateId(@Param("templateId") Long templateId);
    
    /**
     * 根据模板ID删除所有角色关联
     *
     * @param templateId 模板ID
     * @return 影响行数
     */
    int deleteByTemplateId(@Param("templateId") Long templateId);
    
    /**
     * 批量保存模板角色关联
     *
     * @param relations 关联列表
     * @return 是否成功
     */
    int batchSaveRelations(@Param("list") List<TenantRelTemplateRole> relations);
} 