package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocTemplateDTO;
import com.ruantang.service.prompt.model.DocTemplateRequest;
import com.ruantang.service.prompt.service.DocTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档模板Controller
 */
@RestController
@RequestMapping("/api/doc-templates")
public class DocTemplateController {

    @Autowired
    private DocTemplateService docTemplateService;

    /**
     * 创建文档模板
     */
    @PostMapping
    public ApiResult<Long> create(@RequestBody DocTemplateRequest request) {
        Long id = docTemplateService.createDocTemplate(request);
        return ApiResult.success(id, "文档模板创建成功");
    }

    /**
     * 更新文档模板
     */
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable("id") Long id, @RequestBody DocTemplateRequest request) {
        request.setId(id);
        boolean success = docTemplateService.updateDocTemplate(request);
        if (success) {
            return ApiResult.success(null, "文档模板更新成功");
        } else {
            return ApiResult.failed("文档模板更新失败");
        }
    }

    /**
     * 删除文档模板
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable("id") Long id) {
        boolean success = docTemplateService.deleteDocTemplate(id);
        if (success) {
            return ApiResult.success(null, "文档模板删除成功");
        } else {
            return ApiResult.failed("文档模板删除失败");
        }
    }

    /**
     * 获取文档模板详情
     */
    @GetMapping("/{id}")
    public ApiResult<DocTemplateDTO> getById(@PathVariable("id") Long id) {
        DocTemplateDTO docTemplate = docTemplateService.getDocTemplate(id);
        if (docTemplate != null) {
            return ApiResult.success(docTemplate, "文档模板获取成功");
        } else {
            return ApiResult.failed("文档模板不存在");
        }
    }

    /**
     * 获取所有文档模板
     */
    @GetMapping
    public ApiResult<List<DocTemplateDTO>> getAll() {
        List<DocTemplateDTO> templates = docTemplateService.getAllDocTemplates();
        return ApiResult.success(templates, "文档模板列表获取成功");
    }
    
    /**
     * 根据技术领域ID获取文档模板
     */
    @GetMapping("/by-domain")
    public ApiResult<List<DocTemplateDTO>> getByDomainId(@RequestParam("domainId") Long domainId) {
        List<DocTemplateDTO> templates = docTemplateService.getByDomainId(domainId);
        return ApiResult.success(templates, "文档模板列表获取成功");
    }
} 