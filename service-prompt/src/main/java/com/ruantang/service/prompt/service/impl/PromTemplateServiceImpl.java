package com.ruantang.service.prompt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.prom.PromTemplate;
import com.ruantang.mapper.prom.PromTemplateMapper;
import com.ruantang.service.prompt.model.*;
import com.ruantang.service.prompt.service.PromTemplateParameterService;
import com.ruantang.service.prompt.service.PromTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 提示词模板Service实现类
 */
@Service
public class PromTemplateServiceImpl extends ServiceImpl<PromTemplateMapper, PromTemplate> implements PromTemplateService {

    @Autowired
    private PromTemplateParameterService parameterService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTemplate(PromTemplateRequest request) {
        PromTemplate template = new PromTemplate();
        BeanUtils.copyProperties(request, template);
        
        // 设置默认值
        long currentTime = System.currentTimeMillis();
        template.setCreateTime(currentTime);
        template.setUpdateTime(currentTime);

        // 保存模板
        save(template);
        
        // 保存参数
        if (request.getParameters() != null && !request.getParameters().isEmpty()) {
            parameterService.batchSaveParameters(request.getParameters(), template.getId());
        }
        
        return template.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplate(PromTemplateRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("模板ID不能为空");
        }
        PromTemplate template = getById(request.getId());
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        
        BeanUtils.copyProperties(request, template);
        template.setUpdateTime(System.currentTimeMillis());
        
        // 更新模板
        boolean result = updateById(template);
        
        // 更新参数（先删除再新增）
        if (result && request.getParameters() != null) {
            parameterService.batchSaveParameters(request.getParameters(), template.getId());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTemplate(Long id) {
        // 删除模板参数
        parameterService.deleteByTemplateId(id);
        
        // 删除模板
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Boolean enabled) {
        PromTemplate template = getById(id);
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        // 确保 enabled 不为 null，避免潜在的空指针异常
        template.setEnabled(enabled);

        template.setUpdateTime(System.currentTimeMillis());
        
        return updateById(template);
    }

    @Override
    public PromTemplateDTO getTemplate(Long id) {
        PromTemplate template = getById(id);
        if (template == null) {
            return null;
        }
        
        // 转换为DTO
        PromTemplateDTO dto = convertToDTO(template);
        
        // 设置参数列表
        List<PromTemplateParameterDTO> parameters = parameterService.getParametersByTemplateId(id);
        dto.setParameters(parameters);
        
        return dto;
    }

    @Override
    public PageResult<PromTemplateDTO> pageTemplates(PromTemplateQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<PromTemplate> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(request.getSectionType())) {
            queryWrapper.eq(PromTemplate::getSectionType, request.getSectionType());
        }
        
        if (StringUtils.hasText(request.getPromName())) {
            queryWrapper.like(PromTemplate::getPromName, request.getPromName());
        }
        
        if (StringUtils.hasText(request.getVersion())) {
            queryWrapper.eq(PromTemplate::getVersion, request.getVersion());
        }
        

        
        // 按权重降序、创建时间降序排序
        queryWrapper.orderByDesc(PromTemplate::getWeight)
                .orderByDesc(PromTemplate::getCreateTime);
        
        // 分页查询
        Page<PromTemplate> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<PromTemplate> iPage = page(page, queryWrapper);
        
        // 转换结果
        List<PromTemplateDTO> records = iPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 查询参数列表
        for (PromTemplateDTO dto : records) {
            List<PromTemplateParameterDTO> parameters = parameterService.getParametersByTemplateId(dto.getId());
            dto.setParameters(parameters);
        }
        
        return PageResult.of(records, iPage.getTotal(), request.getPageNum(), request.getPageSize());
    }

    @Override
    public PromTemplateDTO getTemplateBySectionType(String sectionType) {
        if (!StringUtils.hasText(sectionType)) {
            return null;
        }
        
        // 查询模板
        LambdaQueryWrapper<PromTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromTemplate::getSectionType, sectionType)
                .orderByDesc(PromTemplate::getWeight)
                .orderByDesc(PromTemplate::getCreateTime)
                .last("LIMIT 1");
        
        PromTemplate template = getOne(queryWrapper);
        if (template == null) {
            return null;
        }
        
        // 转换为DTO
        PromTemplateDTO dto = convertToDTO(template);
        
        // 设置参数列表
        List<PromTemplateParameterDTO> parameters = parameterService.getParametersByTemplateId(template.getId());
        dto.setParameters(parameters);
        
        return dto;
    }
    
    /**
     * 将实体转换为DTO
     */
    private PromTemplateDTO convertToDTO(PromTemplate template) {
        if (template == null) {
            return null;
        }
        
        PromTemplateDTO dto = new PromTemplateDTO();
        BeanUtils.copyProperties(template, dto);
        
        return dto;
    }

    @Override
    public List<PromTemplateDTO> getTemplatesByName(String name) {
        LambdaQueryWrapper<PromTemplate> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            queryWrapper.like(PromTemplate::getPromName, name);
        }
        
        List<PromTemplate> templates = list(queryWrapper);

        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
} 