package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult; // 假设 ApiResult 类在该包下
import com.ruantang.service.prompt.model.TechDomainDTO;
import com.ruantang.service.prompt.model.TechDomainRequest;
import com.ruantang.service.prompt.service.TechDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 技术领域Controller
 */
@RestController
@RequestMapping("/api/tech-domains")
public class TechDomainController {

    @Autowired
    private TechDomainService techDomainService;

    /**
     * 创建技术领域
     */
    @PostMapping
    public ApiResult<Long> create(@RequestBody TechDomainRequest request) {
        Long id = techDomainService.createTechDomain(request);
        return ApiResult.success(id, "技术领域创建成功");
    }

    /**
     * 更新技术领域
     */
    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable("id") Long id, @RequestBody TechDomainRequest request) {
        System.out.println("进入更新");
        request.setId(id);
        boolean success = techDomainService.updateTechDomain(request);
        if (success) {
            return ApiResult.success(null, "技术领域更新成功");
        } else {
            return ApiResult.failed("技术领域更新失败");
        }
    }

    /**
     * 删除技术领域
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable("id") Long id) {
        System.out.println("进入删除");
        boolean success = techDomainService.deleteTechDomain(id);
        if (success) {
            return ApiResult.success(null, "技术领域删除成功");
        } else {
            return ApiResult.failed("技术领域删除失败");
        }
    }

    /**
     * 获取技术领域详情
     */
    @GetMapping("/{id}")
    public ApiResult<TechDomainDTO> getById(@PathVariable("id") Long id) {
        System.out.println("进入获取");
        TechDomainDTO techDomain = techDomainService.getTechDomain(id);
        if (techDomain != null) {
            return ApiResult.success(techDomain, "技术领域获取成功");
        } else {
            return ApiResult.failed("技术领域不存在");
        }
    }

    /**
     * 获取技术领域树
     */
    @GetMapping("/tree")
    public ApiResult<List<TechDomainDTO>> getTree() {
        List<TechDomainDTO> tree = techDomainService.getTechDomainTree();
        return ApiResult.success(tree, "技术领域树获取成功");
    }

    /**
     * 获取指定父级ID下的子技术领域
     */
    @GetMapping("/children")
    public ApiResult<List<TechDomainDTO>> getChildren(@RequestParam(name = "parentId",required = false) Long parentId) {
        List<TechDomainDTO> children = techDomainService.getChildrenByParentId(parentId);
        return ApiResult.success(children, "子技术领域获取成功");
    }

    /**
     * 根据技术领域名称查询技术领域及其上级技术领域树
     */
    @GetMapping("/search-with-parents")
    public ApiResult<List<TechDomainDTO>> searchWithParents(@RequestParam("domainName") String domainName) {
        List<TechDomainDTO> techDomains = techDomainService.searchWithParents(domainName);
        if (techDomains != null && !techDomains.isEmpty()) {
            return ApiResult.success(techDomains, "技术领域查询成功");
        } else {
            return ApiResult.failed("技术领域不存在");
        }
    }
}
