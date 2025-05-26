package com.ruantang.service.tenant.repository;

import com.ruantang.entity.tenant.TenantRelTemplateRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 租户模板角色关联数据访问仓库接口
 */
public interface TenantRelTemplateRoleRepository {

    /**
     * 根据模板ID查询关联角色
     *
     * @param templateId 模板ID
     * @return 角色关联列表
     */
    List<TenantRelTemplateRole> listRolesByTemplateId(Long templateId);

    /**
     * 根据角色ID查询关联的模板
     *
     * @param roleId 角色ID
     * @return 模板关联列表
     */
    List<TenantRelTemplateRole> listTemplatesByRoleId(Long roleId);

    /**
     * 根据模板ID和角色ID查询关联
     *
     * @param templateId 模板ID
     * @param roleId 角色ID
     * @return 角色关联对象
     */
    TenantRelTemplateRole getByTemplateIdAndRoleId(Long templateId, Long roleId);

    /**
     * 根据模板ID删除所有角色关联
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(Long templateId);
    
    /**
     * 根据模板ID和角色ID删除关联
     * 
     * @param templateId 模板ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteByTemplateIdAndRoleId(Long templateId, Long roleId);

    /**
     * 保存模板角色关联
     *
     * @param relation 关联对象
     * @return 是否成功
     */
    boolean saveRelation(TenantRelTemplateRole relation);

    /**
     * 更新模板角色关联
     *
     * @param relation 关联对象
     * @return 是否成功
     */
    boolean updateRelation(TenantRelTemplateRole relation);

    /**
     * 批量保存模板角色关联
     *
     * @param relations 关联列表
     * @return 是否成功
     */
    boolean batchSaveRelations(List<TenantRelTemplateRole> relations);

    /**
     * 检查角色是否绑定到任何模板
     *
     * @param roleId 角色ID
     * @return 是否绑定
     */
    boolean checkRoleBindingToTemplate(Long roleId);
} 