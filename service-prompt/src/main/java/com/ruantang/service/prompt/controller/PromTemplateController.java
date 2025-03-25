package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.PageResult;
import com.ruantang.service.prompt.model.PromTemplateDTO;
import com.ruantang.service.prompt.model.PromTemplateQueryRequest;
import com.ruantang.service.prompt.model.PromTemplateRequest;
import com.ruantang.service.prompt.service.PromTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller
 */
@RestController
@RequestMapping("/api/prom-templates")
public class PromTemplateController {

    @Autowired
    private PromTemplateService promTemplateService;

    /**
     * 创建提示词模板
     * @param request 请求对象
     * @return 模板ID
     */
    @PostMapping
    public ApiResult<Long> create(@RequestBody PromTemplateRequest request) {
        Long id = promTemplateService.createTemplate(request);
        return ApiResult.success(id, "创建成功");
    }

    /**
     * 更新提示词模板
     * @param id 模板ID
     * @param request 请求对象
     * @return 是否成功
     */
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable("id") Long id, @RequestBody PromTemplateRequest request) {
        request.setId(id);
        boolean success = promTemplateService.updateTemplate(request);
        return success ? ApiResult.success(null, "更新成功") : ApiResult.failed("更新失败");
    }

    /**
     * 删除提示词模板
     * @param id 模板ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable("id") Long id) {
        boolean success = promTemplateService.deleteTemplate(id);
        return success ? ApiResult.success(null, "删除成功") : ApiResult.failed("删除失败");
    }

    /**
     * 启用/禁用提示词模板
     * @param id 模板ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    @PatchMapping("/{id}/status")
    public ApiResult<Void> updateStatus(@PathVariable("id") Long id, @RequestParam("enabled") Boolean enabled) {
        boolean success = promTemplateService.updateStatus(id, enabled);
        return success ? ApiResult.success(null, "状态更新成功") : ApiResult.failed("状态更新失败");
    }

    /**
     * 获取提示词模板详情
     * @param id 模板ID
     * @return 模板DTO（包含参数列表）
     */
    @GetMapping("/{id}")
    public ApiResult<PromTemplateDTO> getById(@PathVariable("id") Long id) {
        PromTemplateDTO dto = promTemplateService.getTemplate(id);
        return dto != null ? ApiResult.success(dto) : ApiResult.failed("模板不存在");
    }

    /**
     * 分页查询提示词模板
     * @param request 查询条件
     * @return 分页结果
     */
    @GetMapping
    public ApiResult<PageResult<PromTemplateDTO>> page(PromTemplateQueryRequest request) {
        PageResult<PromTemplateDTO> result = promTemplateService.pageTemplates(request);
        return ApiResult.success(result);
    }

    /**
     * 根据分项类型获取提示词模板
     * @param sectionType 分项类型
     * @return 模板DTO（包含参数列表）
     */
    @GetMapping("/by-section-type")
    public ApiResult<PromTemplateDTO> getBySectionType(@RequestParam String sectionType) {
        PromTemplateDTO dto = promTemplateService.getTemplateBySectionType(sectionType);
        return dto != null ? ApiResult.success(dto) : ApiResult.failed("模板不存在");
    }

    /**
     * 根据名称模糊查询提示词模板列表
     * @param name 模板名称
     * @return 模板DTO列表
     */
    @GetMapping("/by-name")
    public ApiResult<List<PromTemplateDTO>> getTemplatesByName(@RequestParam(name = "name", required = false) String name) {
        List<PromTemplateDTO> dtos = promTemplateService.getTemplatesByName(name);
        return ApiResult.success(dtos);
    }
}
