package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.PromTemplateParameterDTO;
import com.ruantang.service.prompt.model.PromTemplateParameterRequest;
import com.ruantang.service.prompt.service.PromTemplateParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提示词模板参数Controller
 */
@RestController
@RequestMapping("/api/prom-template-parameters")
public class PromTemplateParameterController {

    @Autowired
    private PromTemplateParameterService promTemplateParameterService;

    /**
     * 创建参数
     */
    @PostMapping
    public ApiResult<Long> create(@RequestBody PromTemplateParameterRequest request) {
        Long id = promTemplateParameterService.createParameter(request);
        return ApiResult.success(id, "创建成功");
    }

    /**
     * 更新参数
     */
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody PromTemplateParameterRequest request) {
        request.setId(id);
        boolean success = promTemplateParameterService.updateParameter(request);
        return success ? ApiResult.success(null, "更新成功") : ApiResult.failed("更新失败");
    }

    /**
     * 删除参数
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        boolean success = promTemplateParameterService.deleteParameter(id);
        return success ? ApiResult.success(null, "删除成功") : ApiResult.failed("删除失败");
    }

    /**
     * 获取参数详情
     */
    @GetMapping("/{id}")
    public ApiResult<PromTemplateParameterDTO> getById(@PathVariable Long id) {
        PromTemplateParameterDTO dto = promTemplateParameterService.getParameter(id);
        return dto != null ? ApiResult.success(dto) : ApiResult.failed("参数不存在");
    }

    /**
     * 根据模板ID获取参数列表
     */
    @GetMapping("/by-template")
    public ApiResult<List<PromTemplateParameterDTO>> getByTemplateId(@RequestParam("templateId") Long templateId) {
        List<PromTemplateParameterDTO> list = promTemplateParameterService.getParametersByTemplateId(templateId);
        return ApiResult.success(list);
    }
    
    /**
     * 批量保存参数
     */
    @PostMapping("/batch")
    public ApiResult<Void> batchSave(@RequestParam("templateId") Long templateId, @RequestBody List<PromTemplateParameterRequest> parameters) {
        boolean success = promTemplateParameterService.batchSaveParameters(parameters, templateId);
        return success ? ApiResult.success(null, "保存成功") : ApiResult.failed("保存失败");
    }
} 