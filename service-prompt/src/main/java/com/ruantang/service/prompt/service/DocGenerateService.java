package com.ruantang.service.prompt.service;

import com.ruantang.service.prompt.model.DocGenerateRequest;
import com.ruantang.service.prompt.model.DocGenerateResponse;

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
} 