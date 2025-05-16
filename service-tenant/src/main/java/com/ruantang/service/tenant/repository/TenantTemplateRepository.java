package com.ruantang.service.tenant.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.tenant.TenantTemplate;

/**
 * 租户模板数据访问仓库接口
 */
public interface TenantTemplateRepository {

    /**
     * 分页查询租户模板
     *
     * @param templateName 模板名称（支持模糊查询）
     * @param templateCode 模板编码
     * @param templateType 模板类型
     * @param industryType 行业类型
     * @param isSystem     是否系统内置
     * @param page         分页对象
     * @return 分页结果
     */
    Page<TenantTemplate> queryTemplatePage(String templateName, String templateCode, 
                                          Integer templateType, Integer industryType, 
                                          Integer isSystem, Page<TenantTemplate> page);

    /**
     * 根据ID查询租户模板
     *
     * @param id 模板ID
     * @return 租户模板
     */
    TenantTemplate getTemplateById(Long id);

    /**
     * 根据编码查询租户模板
     *
     * @param templateCode 模板编码
     * @return 租户模板
     */
    TenantTemplate getTemplateByCode(String templateCode);

    /**
     * 保存租户模板
     *
     * @param template 租户模板
     * @return 是否成功
     */
    boolean saveTemplate(TenantTemplate template);

    /**
     * 更新租户模板
     *
     * @param template 租户模板
     * @return 是否成功
     */
    boolean updateTemplate(TenantTemplate template);

    /**
     * 删除租户模板
     *
     * @param id 模板ID
     * @return 是否成功
     */
    boolean deleteTemplateById(Long id);
} 