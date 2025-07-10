package com.ruantang.service.prompt.service.impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.summary.TextRankSentence;
import com.ruantang.service.prompt.model.*;
import com.ruantang.service.prompt.service.*;
import com.ruantang.service.prompt.util.AiApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.*;
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

    // 新增带上下文处理的方法
    @Override
    public DocGenerateResponse generateDocWithContext(DocGenerateRequest request) {
        // 复用原有的模板获取逻辑
        DocTemplateDTO docTemplate = docTemplateService.getDocTemplate(request.getDocTemplateId());
        // ...（其他校验逻辑保持不变）
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

        // 按顺序处理章节（非并行）
        List<DocGenerateResponse.DocSection> sections = new ArrayList<>();
        StringBuilder contextBuilder = new StringBuilder();

        // 按sortOrder排序
        List<DocSectionTemplateDTO> sortedSections = sectionTemplates.stream()
                .sorted(Comparator.comparing(DocSectionTemplateDTO::getSortOrder))
                .collect(Collectors.toList());

        for (DocSectionTemplateDTO section : sortedSections) {
            // 使用新的语义上下文构建方法
            String semanticContext = buildSemanticContext(sections); // 使用已生成的章节

            DocGenerateResponse.DocSection resultSection = processSectionWithContext(
                    section,
                    request,
                    semanticContext  // 传入语义上下文
            );

            sections.add(resultSection);
        }

        // 构建响应
        DocGenerateResponse response = new DocGenerateResponse();
        response.setTitle(buildDocumentTitle(templateType, request.getTechDescription()));
//        response.setTitle(templateType.getTypeName() + "预览");
        response.setSections(sections);
        return response;
    }

    // 新增带上下文的章节处理方法
    private DocGenerateResponse.DocSection processSectionWithContext(
            DocSectionTemplateDTO sectionTemplate,
            DocGenerateRequest request,
            String context
    ) {
        DocGenerateResponse.DocSection section = new DocGenerateResponse.DocSection();
        section.setSectionName(sectionTemplate.getSectionName());
        section.setSortOrder(sectionTemplate.getSortOrder());

        PromTemplateDTO promTemplate = promTemplateService.getTemplate(sectionTemplate.getPromptId());
        if (promTemplate == null) return section;

        List<PromTemplateParameterDTO> parameters = promTemplateParameterService.getParametersByTemplateId(promTemplate.getId());

        if (request.getUseAiGenerate()) {
            String prompt = generatePrompt(promTemplate.getContent(), parameters, request.getTechDescription());

            // 使用新方法调用带上下文的AI接口
            String content = aiApiUtil.generateContentWithContext(prompt, context);
            section.setContent(content);
        } else {
            section.setContent(getExampleContent(parameters));
        }

        return section;
    }

    // 在DocGenerateServiceImpl中添加
    private String buildSemanticContext(
            List<DocGenerateResponse.DocSection> previousSections
    ) {
        if (CollectionUtils.isEmpty(previousSections)) {
            return "这是文档的第一个章节，请根据用户输入生成内容";
        }

        // 1. 合并所有已生成内容（增强格式处理）
        String fullText = previousSections.stream()
                .map(s -> {
                    // 清理特殊字符
                    String cleaned = s.getContent()
                            .replaceAll("<[^>]+>", "")  // 移除HTML标签
                            .replaceAll("\\[.*?\\]", "") // 移除方括号内容
                            .replaceAll("\\d+%", "百分比"); // 替换百分比符号

                    return s.getSectionName() + ": " + cleaned;
                })
                .collect(Collectors.joining("\n\n"));

        // 2. 确保文本长度足够
        if (fullText.length() < 50) {
            fullText = "技术文档内容: " + fullText; // 添加前缀保证最小长度
        }
//        log.info("Semantic Context: {}", fullText);

        // 3. 使用更健壮的摘要方法
        String summary = getRobustSummary(fullText, 1);
//        String summary = fullText;

        // 4. 关键信息提取（使用HanLP）
        List<String> keywords = extractKeywords(fullText, 10); // 提取前10个关键词

        log.info("Semantic Context: {}", String.format(
                "【文档已生成内容摘要】:\n%s\n\n" +
                        "【技术关键词】: %s\n" +
                        "【当前章节要求】: 请确保内容与已生成部分逻辑连贯，技术术语保持一致",
                summary, // 提取3句核心摘要
                String.join("、", keywords)
        ));

        // 3. 结构化上下文模板
        return String.format(
                "【文档已生成内容摘要】:\n%s\n\n" +
                        "【技术关键词】: %s\n" +
                        "【当前章节要求】: 请确保内容与已生成部分逻辑连贯，技术术语保持一致",
                summary, // 提取3句核心摘要
                String.join("、", keywords)
        );
    }


    // 新增健壮的摘要提取方法
    private String getRobustSummary(String text, int sentenceCount) {
        try {
            // 尝试标准方法
            List<String> sentences = TextRankSentence.getTopSentenceList(text, sentenceCount);
            if (!sentences.isEmpty()) {
                return String.join("。", sentences) + "。";
            }

            // 备用方法：手动分句
            String[] manualSentences = text.split("[。！？；]");
            if (manualSentences.length > 0) {
                int end = Math.min(sentenceCount, manualSentences.length);
                return String.join("。", Arrays.copyOfRange(manualSentences, 0, end)) + "。";
            }

            // 最后手段：截取前100字符
            return text.substring(0, Math.min(100, text.length())) + "...";
        } catch (Exception e) {
            log.error("摘要提取失败: {}", e.getMessage());
            return "技术文档内容摘要";
        }
    }

    // 添加关键词提取方法
    private List<String> extractKeywords(String text, int topN) {
        return HanLP.extractKeyword(text, topN);
    }

    // 在类中添加新的NLP摘要方法
    private String generateTechSummary(String techDescription) {
        if (!StringUtils.hasText(techDescription)) {
            return "";
        }

        // 方案1：提取核心名词短语（更符合技术名称）
        List<String> phrases = HanLP.extractPhrase(techDescription, 3);
        if (!phrases.isEmpty()) {
            // 过滤掉动词和非技术词汇
            String techName = phrases.stream()
                    .filter(phrase -> phrase.matches(".*[\\u4e00-\\u9fa5]{2,}.*")) // 至少2个中文字符
                    .filter(phrase -> !phrase.matches(".*(研究|分析|探讨|基于).*")) // 排除研究类动词
                    .map(phrase -> phrase.replaceAll("的|了|要", "")) // 清理虚词
                    .collect(Collectors.joining("-"));

            if (StringUtils.hasText(techName)) {
                return techName + "方案";
            }
        }

        // 方案2：使用关键词组合（备用方案）
        List<String> keywords = HanLP.extractKeyword(techDescription, 4);
        if (!keywords.isEmpty()) {
            return keywords.stream()
                    .limit(3) // 最多取3个关键词
                    .collect(Collectors.joining("-")) + "方案";
        }

        // 方案3：智能截取首句核心部分
        int endIndex = techDescription.indexOf('。');
        if (endIndex > 0) {
            String firstSentence = techDescription.substring(0, endIndex);
            // 提取核心名词部分
            return firstSentence.replaceAll(".*?(基于|针对|研究)?([\\u4e00-\\u9fa5]{4,20}?)(方案|系统|技术)?.*", "$2");
        }

        return "";
    }

    // 修改标题设置逻辑
    private String buildDocumentTitle(DocTemplateTypeDTO templateType, String techDescription) {
        String summary = generateTechSummary(techDescription);
        log.info("Document Title: {}", summary);
        String baseTitle = templateType.getTypeName() + "预览";

        return StringUtils.hasText(summary) ?
                summary + " - " + baseTitle :
                baseTitle;
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

    // 示例内容提取方法
    private String getExampleContent(List<PromTemplateParameterDTO> parameters) {
        return parameters.stream()
                .filter(p -> "example".equals(p.getParamKey()))
                .findFirst()
                .map(PromTemplateParameterDTO::getDefaultValue)
                .orElse("");
    }

    @Override
    public byte[] exportWordDocument(DocWordExportRequest request) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            log.info("开始生成Word文档，标题：{}", request.getTitle());

            // ==== 1. 创建必要的文档结构 ====
            // 添加文档主体
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            CTPageSz pageSz = sectPr.addNewPgSz();
            pageSz.setW(BigInteger.valueOf(11906)); // A4纸宽度(21cm)
            pageSz.setH(BigInteger.valueOf(16838)); // A4纸高度(29.7cm)

            // ==== 2. 添加标题 ====
            if (StringUtils.hasText(request.getTitle())) {
                XWPFParagraph titlePara = document.createParagraph();
                titlePara.setAlignment(ParagraphAlignment.CENTER);

                XWPFRun titleRun = titlePara.createRun();
                titleRun.setText(request.getTitle());
                titleRun.setBold(true);
                titleRun.setFontSize(20);
                titleRun.setFontFamily("宋体");

                // 添加标题后空行
                document.createParagraph().createRun().addBreak();
            }

            // ==== 3. 添加章节内容 ====
            if (request.getSections() != null && !request.getSections().isEmpty()) {
                List<DocWordExportRequest.DocSection> sortedSections = request.getSections().stream()
                        .sorted(Comparator.comparing(
                                DocWordExportRequest.DocSection::getSortOrder,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ))
                        .collect(Collectors.toList());

                for (DocWordExportRequest.DocSection section : sortedSections) {
                    // 添加章节标题
                    if (StringUtils.hasText(section.getSectionName())) {
                        XWPFParagraph headingPara = document.createParagraph();
                        headingPara.setStyle("Heading2");

                        XWPFRun headingRun = headingPara.createRun();
                        headingRun.setText(section.getSectionName());
                        headingRun.setBold(true);
                        headingRun.setFontSize(16);
                        headingRun.setFontFamily("黑体");
                    }

                    // 添加章节内容
                    if (StringUtils.hasText(section.getContent())) {
                        XWPFParagraph contentPara = document.createParagraph();
                        contentPara.setAlignment(ParagraphAlignment.BOTH);

                        // 设置首行缩进
                        CTPPr ppr = contentPara.getCTP().getPPr();
                        if (ppr == null) ppr = contentPara.getCTP().addNewPPr();
                        CTInd ind = ppr.isSetInd() ? ppr.getInd() : ppr.addNewInd();
                        ind.setFirstLineChars(BigInteger.valueOf(200)); // 首行缩进2字符

                        // 处理内容中的换行
                        String[] paragraphs = section.getContent().split("\n");
                        for (int i = 0; i < paragraphs.length; i++) {
                            if (i > 0) {
                                contentPara.createRun().addBreak(); // 添加换行
                            }
                            XWPFRun contentRun = contentPara.createRun();
                            contentRun.setText(paragraphs[i]);
                            contentRun.setFontSize(12);
                            contentRun.setFontFamily("宋体");
                        }
                    }

                    // 添加章节间空行
                    document.createParagraph().createRun().addBreak();
                }
            }

            // ==== 4. 添加必要的文档结束标记 ====
            document.createParagraph().createRun().addCarriageReturn();

            // ==== 5. 生成文档字节 ====
            document.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            log.error("生成Word文档失败", e);
            throw new RuntimeException("生成Word文档失败: " + e.getMessage());
        }
    }


    /**
     * 异步处理方法
     * @param request 生成请求
     * @return
     */
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
            allFutures.get(299, TimeUnit.SECONDS); // 总超时时间299秒
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

} 