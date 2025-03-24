package com.ruantang.service.prompt.service;

import com.ruantang.service.prompt.model.PageResult;
import com.ruantang.service.prompt.model.PromTemplateDTO;
import com.ruantang.service.prompt.model.PromTemplateQueryRequest;
import com.ruantang.service.prompt.model.PromTemplateRequest;

import java.util.List;

/**
 * 提示词模板Service接口
 */
public interface PromTemplateService {

    /**
     * 创建提示词模板
     * @param request 请求对象
     * @return 模板ID
     */
    Long createTemplate(PromTemplateRequest request);

    /**
     * 更新提示词模板
     * @param request 请求对象
     * @return 是否成功
     */
    boolean updateTemplate(PromTemplateRequest request);

    /**
     * 删除提示词模板
     * @param id 模板ID
     * @return 是否成功
     */
    boolean deleteTemplate(Long id);
    
    /**
     * 启用/禁用提示词模板
     * @param id 模板ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean updateStatus(Long id, Boolean enabled);

    /**
     * 获取提示词模板详情
     * @param id 模板ID
     * @return 模板DTO（包含参数列表）
     */
    PromTemplateDTO getTemplate(Long id);

    /**
     * 分页查询提示词模板
     * @param request 查询条件
     * @return 分页结果
     */
    PageResult<PromTemplateDTO> pageTemplates(PromTemplateQueryRequest request);
    
    /**
     * 根据分项类型获取提示词模板
     * @param sectionType 分项类型
     * @return 模板DTO（包含参数列表）
     */
    PromTemplateDTO getTemplateBySectionType(String sectionType);

    /**
     * 根据名称模糊查询提示词模板列表
     * @param name 模板名称
     * @return 模板DTO列表
     */
    List<PromTemplateDTO> getTemplatesByName(String name);

} 