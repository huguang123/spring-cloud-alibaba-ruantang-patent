package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocGenerateRequest;
import com.ruantang.service.prompt.model.DocGenerateResponse;
import com.ruantang.service.prompt.service.DocGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档生成Controller
 */
@RestController
@RequestMapping("/api/doc-generate")
public class DocGenerateController {

    @Autowired
    private DocGenerateService docGenerateService;

    /**
     * 生成文档
     */
    @PostMapping
    public ApiResult<DocGenerateResponse> generateDoc(@RequestBody DocGenerateRequest request) {
        DocGenerateResponse response = docGenerateService.generateDoc(request);
        return ApiResult.success(response, "文档生成成功");
    }
} 