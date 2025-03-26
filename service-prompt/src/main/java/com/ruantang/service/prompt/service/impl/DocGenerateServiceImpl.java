package com.ruantang.service.prompt.service.impl;

import com.ruantang.entity.prom.*;
import com.ruantang.service.prompt.model.*;
import com.ruantang.service.prompt.service.*;
import com.ruantang.service.prompt.util.AiApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文档生成Service实现类
 */
@Service
public class DocGenerateServiceImpl implements DocGenerateService {

    @Autowired
    private DocTemplateService docTemplateService;
    
    @Autowired
    private DocTemplateTypeService docTemplateTypeService;
    
    @Autowired
    private DocSectionTemplateService docSectionTemplateService;
    
    @Autowired
    private PromTemplateService promTemplateService;
    
    @Autowired
    private PromTemplateParameterService promTemplateParameterService;
    
    @Autowired
    private AiApiUtil aiApiUtil;

    @Override
    public DocGenerateResponse generateDoc(DocGenerateRequest request) {
        // 1. 获取文档模板
        DocTemplateDTO docTemplate = docTemplateService.getDocTemplate(request.getDocTemplateId());
        if (docTemplate == null) {
            throw new RuntimeException("文档模板不存在");
        }
        
        // 2. 获取文档模板类型
        DocTemplateTypeDTO templateType = docTemplateTypeService.getDocTemplateType(docTemplate.getTemplateTypeId());
        if (templateType == null) {
            throw new RuntimeException("文档模板类型不存在");
        }
        
        // 3. 获取文档分项模板列表
        List<DocSectionTemplateDTO> sectionTemplates = docSectionTemplateService.getByDocTemplateId(docTemplate.getId());
        if (CollectionUtils.isEmpty(sectionTemplates)) {
            throw new RuntimeException("文档分项模板不存在");
        }
        
        // 4. 构建响应对象
        DocGenerateResponse response = new DocGenerateResponse();
        response.setTitle(templateType.getTypeName() + "预览");
        
        // 5. 处理每个分项
        List<DocGenerateResponse.DocSection> sections = new ArrayList<>();
        for (DocSectionTemplateDTO sectionTemplate : sectionTemplates) {
            DocGenerateResponse.DocSection section = new DocGenerateResponse.DocSection();
            section.setSectionName(sectionTemplate.getSectionName());
            section.setSortOrder(sectionTemplate.getSortOrder());
            
            // 获取提示词模板
            PromTemplateDTO promTemplate = promTemplateService.getTemplate(sectionTemplate.getPromptId());
            if (promTemplate == null) {
                continue;
            }
            
            // 获取提示词参数列表
            List<PromTemplateParameterDTO> parameters = promTemplateParameterService.getParametersByTemplateId(promTemplate.getId());
            
            if (request.getUseAiGenerate()) {
                // 使用AI生成内容
                String prompt = generatePrompt(promTemplate.getContent(), parameters, request.getTechDescription());
                String content = aiApiUtil.generateContent(prompt);
                section.setContent(content);
            } else {
                // 使用示例内容
                String exampleContent = parameters.stream()
                        .filter(p -> "example".equals(p.getParamKey()))
                        .findFirst()
                        .map(PromTemplateParameterDTO::getDefaultValue)
                        .orElse("");
                section.setContent(exampleContent);
            }
            
            sections.add(section);
        }
        
        // 6. 按排序字段排序
        sections.sort(Comparator.comparing(DocGenerateResponse.DocSection::getSortOrder));
        response.setSections(sections);
        
        return response;
    }
    
    /**
     * 生成完整的提示词
     */
    private String generatePrompt(String template, List<PromTemplateParameterDTO> parameters, String userInput) {
        if (CollectionUtils.isEmpty(parameters)) {
            return template;
        }
        
        // 将参数转换为Map
        Map<String, String> paramMap = parameters.stream()
                .collect(Collectors.toMap(
                        PromTemplateParameterDTO::getParamKey,
                        PromTemplateParameterDTO::getDefaultValue,
                        (v1, v2) -> v1
                ));
        
        // 添加用户输入
        paramMap.put("user_input", userInput);
        
        // 替换模板中的参数
        String prompt = template;
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            prompt = prompt.replace(key, value);
        }
        
        return prompt;
    }
} 