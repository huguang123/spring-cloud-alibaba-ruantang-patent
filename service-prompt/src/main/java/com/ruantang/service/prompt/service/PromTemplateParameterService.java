package com.ruantang.service.prompt.service;

import com.ruantang.entity.prom.PromTemplateParameter;
import com.ruantang.service.prompt.model.PromTemplateParameterDTO;
import com.ruantang.service.prompt.model.PromTemplateParameterRequest;

import java.util.List;

/**
 * 提示词模板参数Service接口
 */
public interface PromTemplateParameterService {

    /**
     * 创建提示词模板参数
     * @param request 请求对象
     * @return 参数ID
     */
    Long createParameter(PromTemplateParameterRequest request);

    /**
     * 更新提示词模板参数
     * @param request 请求对象
     * @return 是否成功
     */
    boolean updateParameter(PromTemplateParameterRequest request);

    /**
     * 删除提示词模板参数
     * @param id 参数ID
     * @return 是否成功
     */
    boolean deleteParameter(Long id);

    /**
     * 获取提示词模板参数详情
     * @param id 参数ID
     * @return 参数DTO
     */
    PromTemplateParameterDTO getParameter(Long id);

    /**
     * 根据模板ID查询参数列表
     * @param templateId 模板ID
     * @return 参数列表
     */
    List<PromTemplateParameterDTO> getParametersByTemplateId(Long templateId);
    
    /**
     * 批量保存参数
     * @param parameters 参数列表
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean batchSaveParameters(List<PromTemplateParameterRequest> parameters, Long templateId);
    
    /**
     * 删除模板下所有参数
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteByTemplateId(Long templateId);
} 