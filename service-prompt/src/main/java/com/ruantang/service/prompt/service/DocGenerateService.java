package com.ruantang.service.prompt.service;

import com.ruantang.service.prompt.model.DocGenerateRequest;
import com.ruantang.service.prompt.model.DocGenerateResponse;
import com.ruantang.service.prompt.model.DocSectionTemplateDTO;
import com.ruantang.service.prompt.model.DocWordExportRequest;

import java.util.concurrent.CompletableFuture;

/**
 * 文档生成Service接口
 */
public interface DocGenerateService {
    
    /**
     * 生成文档
     *
     * @param request 生成请求
     * @return 生成结果
     */
    DocGenerateResponse generateDoc(DocGenerateRequest request);

    CompletableFuture<DocGenerateResponse.DocSection> processSectionAsync(DocSectionTemplateDTO sectionTemplate,
                                                                          DocGenerateRequest request);
    
    /**
     * 导出Word文档
     *
     * @param request Word导出请求
     * @return Word文档字节数组
     */
    byte[] exportWordDocument(DocWordExportRequest request);

    /**
     * 生成文档（包含上下文）
     *
     * @param request 带上下文的生成请求
     * @return 带上下文的生成结果
     */
    DocGenerateResponse generateDocWithContext(DocGenerateRequest request);
} 