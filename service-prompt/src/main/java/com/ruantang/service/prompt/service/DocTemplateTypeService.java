package com.ruantang.service.prompt.service;

import com.ruantang.service.prompt.model.DocTemplateTypeDTO;
import com.ruantang.service.prompt.model.DocTemplateTypeRequest;

import java.util.List;

/**
 * 文档模板类型Service接口
 */
public interface DocTemplateTypeService {

    /**
     * 创建文档模板类型
     * @param request 请求对象
     * @return 文档模板类型ID
     */
    Long createDocTemplateType(DocTemplateTypeRequest request);

    /**
     * 更新文档模板类型
     * @param request 请求对象
     * @return 是否成功
     */
    boolean updateDocTemplateType(DocTemplateTypeRequest request);

    /**
     * 删除文档模板类型
     * @param id 文档模板类型ID
     * @return 是否成功
     */
    boolean deleteDocTemplateType(Long id);

    /**
     * 获取文档模板类型详情
     * @param id 文档模板类型ID
     * @return 文档模板类型DTO
     */
    DocTemplateTypeDTO getDocTemplateType(Long id);

    /**
     * 获取所有文档模板类型
     * @return 文档模板类型列表
     */
    List<DocTemplateTypeDTO> getAllDocTemplateTypes();
} 