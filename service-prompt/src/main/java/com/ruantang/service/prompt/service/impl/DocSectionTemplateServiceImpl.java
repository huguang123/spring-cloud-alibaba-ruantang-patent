package com.ruantang.service.prompt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.prom.DocSectionTemplate;
import com.ruantang.mapper.prom.DocSectionTemplateMapper;
import com.ruantang.service.prompt.model.DocSectionTemplateDTO;
import com.ruantang.service.prompt.model.DocSectionTemplateRequest;
import com.ruantang.service.prompt.model.PromTemplateDTO;
import com.ruantang.service.prompt.service.DocSectionTemplateService;
import com.ruantang.service.prompt.service.PromTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档分项模板Service实现类
 */
@Service
public class DocSectionTemplateServiceImpl extends ServiceImpl<DocSectionTemplateMapper, DocSectionTemplate> implements DocSectionTemplateService {

    @Autowired
    private PromTemplateService promTemplateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDocSectionTemplate(DocSectionTemplateRequest request) {
        DocSectionTemplate docSectionTemplate = new DocSectionTemplate();
        BeanUtils.copyProperties(request, docSectionTemplate);
        
        // 设置创建时间等信息
        long currentTime = System.currentTimeMillis();
        docSectionTemplate.setCreateTime(currentTime);
        docSectionTemplate.setUpdateTime(currentTime);
        
        // 保存
        save(docSectionTemplate);
        return docSectionTemplate.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDocSectionTemplate(DocSectionTemplateRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("分项模板ID不能为空");
        }
        
        DocSectionTemplate docSectionTemplate = getById(request.getId());
        if (docSectionTemplate == null) {
            throw new RuntimeException("分项模板不存在");
        }
        
        BeanUtils.copyProperties(request, docSectionTemplate);
        docSectionTemplate.setUpdateTime(System.currentTimeMillis());
        
        return updateById(docSectionTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDocSectionTemplate(Long id) {
        return removeById(id);
    }

    @Override
    public DocSectionTemplateDTO getDocSectionTemplate(Long id) {
        DocSectionTemplate docSectionTemplate = getById(id);
        if (docSectionTemplate == null) {
            return null;
        }
        
        DocSectionTemplateDTO dto = convertToDTO(docSectionTemplate);
        
        // 设置关联的提示词模板信息
        if (docSectionTemplate.getPromptId() != null) {
            PromTemplateDTO promptTemplate = promTemplateService.getTemplate(docSectionTemplate.getPromptId());
            dto.setPromptTemplate(promptTemplate);
        }
        
        return dto;
    }

    @Override
    public List<DocSectionTemplateDTO> getByDocTemplateId(Long docTemplateId) {
        LambdaQueryWrapper<DocSectionTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DocSectionTemplate::getDocTemplateId, docTemplateId);
        queryWrapper.orderByAsc(DocSectionTemplate::getSortOrder);
        
        List<DocSectionTemplate> list = list(queryWrapper);
        return list.stream().map(entity -> {
            DocSectionTemplateDTO dto = convertToDTO(entity);
            
            // 设置关联的提示词模板信息
            if (entity.getPromptId() != null) {
                PromTemplateDTO promptTemplate = promTemplateService.getTemplate(entity.getPromptId());
                dto.setPromptTemplate(promptTemplate);
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * 将实体转换为DTO
     */
    private DocSectionTemplateDTO convertToDTO(DocSectionTemplate entity) {
        DocSectionTemplateDTO dto = new DocSectionTemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
} 