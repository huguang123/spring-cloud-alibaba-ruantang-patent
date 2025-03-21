package com.ruantang.service.prompt.service;

import com.ruantang.service.prompt.model.DocSectionTemplateDTO;
import com.ruantang.service.prompt.model.DocSectionTemplateRequest;

import java.util.List;

/**
 * 文档分项模板Service接口
 */
public interface DocSectionTemplateService {

    /**
     * 创建文档分项模板
     * @param request 请求对象
     * @return 文档分项模板ID
     */
    Long createDocSectionTemplate(DocSectionTemplateRequest request);

    /**
     * 更新文档分项模板
     * @param request 请求对象
     * @return 是否成功
     */
    boolean updateDocSectionTemplate(DocSectionTemplateRequest request);

    /**
     * 删除文档分项模板
     * @param id 文档分项模板ID
     * @return 是否成功
     */
    boolean deleteDocSectionTemplate(Long id);

    /**
     * 获取文档分项模板详情
     * @param id 文档分项模板ID
     * @return 文档分项模板DTO
     */
    DocSectionTemplateDTO getDocSectionTemplate(Long id);

    /**
     * 根据文档模板ID获取所有分项模板
     * @param docTemplateId 文档模板ID
     * @return 文档分项模板列表
     */
    List<DocSectionTemplateDTO> getByDocTemplateId(Long docTemplateId);
} 