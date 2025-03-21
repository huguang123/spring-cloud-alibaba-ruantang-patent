package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocTemplateTypeDTO;
import com.ruantang.service.prompt.model.DocTemplateTypeRequest;
import com.ruantang.service.prompt.service.DocTemplateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档模板类型Controller
 */
@RestController
@RequestMapping("/api/doc-template-types")
public class DocTemplateTypeController {

    @Autowired
    private DocTemplateTypeService docTemplateTypeService;

    /**
     * 创建文档模板类型
     */
    @PostMapping
    public ApiResult<Long> create(@RequestBody DocTemplateTypeRequest request) {
        Long id = docTemplateTypeService.createDocTemplateType(request);
        return ApiResult.success(id, "文档模板类型创建成功");
    }

    /**
     * 更新文档模板类型
     */
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable("id") Long id, @RequestBody DocTemplateTypeRequest request) {
        request.setId(id);
        boolean success = docTemplateTypeService.updateDocTemplateType(request);
        if (success) {
            return ApiResult.success(null, "文档模板类型更新成功");
        } else {
            return ApiResult.failed("文档模板类型更新失败");
        }
    }

    /**
     * 删除文档模板类型
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable("id") Long id) {
        boolean success = docTemplateTypeService.deleteDocTemplateType(id);
        if (success) {
            return ApiResult.success(null, "文档模板类型删除成功");
        } else {
            return ApiResult.failed("文档模板类型删除失败");
        }
    }

    /**
     * 获取文档模板类型详情
     */
    @GetMapping("/{id}")
    public ApiResult<DocTemplateTypeDTO> getById(@PathVariable("id") Long id) {
        DocTemplateTypeDTO docTemplateType = docTemplateTypeService.getDocTemplateType(id);
        if (docTemplateType != null) {
            return ApiResult.success(docTemplateType, "文档模板类型获取成功");
        } else {
            return ApiResult.failed("文档模板类型不存在");
        }
    }

    /**
     * 获取所有文档模板类型
     */
    @GetMapping
    public ApiResult<List<DocTemplateTypeDTO>> getAll() {
        List<DocTemplateTypeDTO> types = docTemplateTypeService.getAllDocTemplateTypes();
        return ApiResult.success(types, "文档模板类型列表获取成功");
    }
} 