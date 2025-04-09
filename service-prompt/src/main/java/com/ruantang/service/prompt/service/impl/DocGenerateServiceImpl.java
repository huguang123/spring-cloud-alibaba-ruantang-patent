package com.ruantang.service.prompt.service.impl;

import com.ruantang.service.prompt.model.*;
import com.ruantang.service.prompt.service.*;
import com.ruantang.service.prompt.util.AiApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 文档生成Service实现类
 */
@Service
@Slf4j
public class DocGenerateServiceImpl implements DocGenerateService {

    @Autowired
    private DocTemplateService docTemplateService;

    // 新增自注入代理对象
    @Autowired
    @Lazy
    private DocGenerateService selfProxy; // 必须通过代理调用
    
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

        // 异步处理每个section（使用spring代理方式）
        List<CompletableFuture<DocGenerateResponse.DocSection>> futures = sectionTemplates.stream()
                .map(st -> selfProxy.processSectionAsync(st, request))
                .collect(Collectors.toList());

        // 合并所有异步任务（带超时控制）
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allFutures.get(180, TimeUnit.SECONDS); // 总超时时间60秒
        } catch (TimeoutException e) {
            log.warn("AI生成任务部分超时，已完成的{}个分项将被返回",
                    futures.stream().filter(CompletableFuture::isDone).count());
        } catch (Exception e) {
            throw new RuntimeException("文档生成失败", e);
        }

        // 收集结果
        List<DocGenerateResponse.DocSection> sections = futures.stream()
                .filter(f -> f.isDone() && !f.isCompletedExceptionally())
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 排序并设置响应
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

    // 新增异步处理方法
    @Async("aiExecutor")
    @Override
    public CompletableFuture<DocGenerateResponse.DocSection> processSectionAsync(DocSectionTemplateDTO sectionTemplate,
                                                                                 DocGenerateRequest request) {
        // 添加线程验证日志
        log.debug("异步线程：{} 处理章节：{}",
                Thread.currentThread().getName(),
                sectionTemplate.getSectionName());

        // 直接返回处理结果，@Async会自动管理异步执行
        DocGenerateResponse.DocSection section = processSection(sectionTemplate, request);
        return CompletableFuture.completedFuture(section);
    }

    // 提取同步处理逻辑到独立方法
    private DocGenerateResponse.DocSection processSection(DocSectionTemplateDTO sectionTemplate,
                                                          DocGenerateRequest request) {
        log.info("当前线程：{} | 章节：{}",
                Thread.currentThread().getName(),
                sectionTemplate.getSectionName());
        DocGenerateResponse.DocSection section = new DocGenerateResponse.DocSection();
        section.setSectionName(sectionTemplate.getSectionName());
        section.setSortOrder(sectionTemplate.getSortOrder());

        PromTemplateDTO promTemplate = promTemplateService.getTemplate(sectionTemplate.getPromptId());
        if (promTemplate == null) return section;

        List<PromTemplateParameterDTO> parameters = promTemplateParameterService.getParametersByTemplateId(promTemplate.getId());

        if (request.getUseAiGenerate()) {
            String prompt = generatePrompt(promTemplate.getContent(), parameters, request.getTechDescription());
            log.info("生成提示词完成，准备调用AI接口：{}", prompt);

            // 记录AI接口调用开始时间
            long startTime = System.currentTimeMillis();
            String content = aiApiUtil.generateContent(prompt);
            long duration = System.currentTimeMillis() - startTime;
            log.info("AI接口调用完成，耗时{}ms，章节内容长度：{}", duration, content.length());


            section.setContent(content);
        } else {
            section.setContent(getExampleContent(parameters));
        }
        return section;
    }

    // 示例内容提取方法
    private String getExampleContent(List<PromTemplateParameterDTO> parameters) {
        return parameters.stream()
                .filter(p -> "example".equals(p.getParamKey()))
                .findFirst()
                .map(PromTemplateParameterDTO::getDefaultValue)
                .orElse("");
    }

} 