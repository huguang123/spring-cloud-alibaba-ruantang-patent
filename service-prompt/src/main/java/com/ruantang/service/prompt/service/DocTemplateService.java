package com.ruantang.service.prompt.service;

import com.ruantang.service.prompt.model.DocTemplateDTO;
import com.ruantang.service.prompt.model.DocTemplateRequest;

import java.util.List;

/**
 * 文档模板Service接口
 */
public interface DocTemplateService {

    /**
     * 创建文档模板
     * @param request 请求对象
     * @return 文档模板ID
     */
    Long createDocTemplate(DocTemplateRequest request);

    /**
     * 更新文档模板
     * @param request 请求对象
     * @return 是否成功
     */
    boolean updateDocTemplate(DocTemplateRequest request);

    /**
     * 删除文档模板
     * @param id 文档模板ID
     * @return 是否成功
     */
    boolean deleteDocTemplate(Long id);

    /**
     * 获取文档模板详情
     * @param id 文档模板ID
     * @return 文档模板DTO（包含类型和分项信息）
     */
    DocTemplateDTO getDocTemplate(Long id);

    /**
     * 获取所有文档模板
     * @return 文档模板列表
     */
    List<DocTemplateDTO> getAllDocTemplates();
    
    /**
     * 根据技术领域ID获取文档模板
     * @param domainId 技术领域ID
     * @return 文档模板列表
     */
    List<DocTemplateDTO> getByDomainId(Long domainId);
} 