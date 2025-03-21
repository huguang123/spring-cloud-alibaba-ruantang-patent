package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocSectionTemplateDTO;
import com.ruantang.service.prompt.model.DocSectionTemplateRequest;
import com.ruantang.service.prompt.service.DocSectionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档分项模板Controller
 */
@RestController
@RequestMapping("/api/doc-section-templates")
public class DocSectionTemplateController {

    @Autowired
    private DocSectionTemplateService docSectionTemplateService;

    /**
     * 创建文档分项模板
     */
    @PostMapping
    public ApiResult<Long> create(@RequestBody DocSectionTemplateRequest request) {
        Long id = docSectionTemplateService.createDocSectionTemplate(request);
        return ApiResult.success(id, "文档分项模板创建成功");
    }

    /**
     * 更新文档分项模板
     */
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable("id") Long id, @RequestBody DocSectionTemplateRequest request) {
        request.setId(id);
        boolean success = docSectionTemplateService.updateDocSectionTemplate(request);
        if (success) {
            return ApiResult.success(null, "文档分项模板更新成功");
        } else {
            return ApiResult.failed("文档分项模板更新失败");
        }
    }

    /**
     * 删除文档分项模板
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable("id") Long id) {
        boolean success = docSectionTemplateService.deleteDocSectionTemplate(id);
        if (success) {
            return ApiResult.success(null, "文档分项模板删除成功");
        } else {
            return ApiResult.failed("文档分项模板删除失败");
        }
    }

    /**
     * 获取文档分项模板详情
     */
    @GetMapping("/{id}")
    public ApiResult<DocSectionTemplateDTO> getById(@PathVariable("id") Long id) {
        DocSectionTemplateDTO docSectionTemplate = docSectionTemplateService.getDocSectionTemplate(id);
        if (docSectionTemplate != null) {
            return ApiResult.success(docSectionTemplate, "文档分项模板获取成功");
        } else {
            return ApiResult.failed("文档分项模板不存在");
        }
    }

    /**
     * 根据文档模板ID获取所有分项模板
     */
    @GetMapping("/by-template")
    public ApiResult<List<DocSectionTemplateDTO>> getByTemplateId(@RequestParam("docTemplateId") Long docTemplateId) {
        List<DocSectionTemplateDTO> sections = docSectionTemplateService.getByDocTemplateId(docTemplateId);
        return ApiResult.success(sections, "文档分项模板列表获取成功");
    }
} 