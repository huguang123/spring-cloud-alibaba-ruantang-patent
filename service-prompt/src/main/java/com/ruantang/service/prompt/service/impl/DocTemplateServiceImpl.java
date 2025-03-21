package com.ruantang.service.prompt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.prom.DocTemplate;
import com.ruantang.entity.prom.DocTemplateType;
import com.ruantang.entity.prom.TechDomain;
import com.ruantang.mapper.prom.DocTemplateMapper;
import com.ruantang.mapper.prom.DocTemplateTypeMapper;
import com.ruantang.service.prompt.model.DocSectionTemplateDTO;
import com.ruantang.service.prompt.model.DocTemplateDTO;
import com.ruantang.service.prompt.model.DocTemplateRequest;
import com.ruantang.service.prompt.model.TechDomainDTO;
import com.ruantang.service.prompt.service.DocSectionTemplateService;
import com.ruantang.service.prompt.service.DocTemplateService;
import com.ruantang.service.prompt.service.TechDomainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档模板Service实现类
 */
@Service
public class DocTemplateServiceImpl extends ServiceImpl<DocTemplateMapper, DocTemplate> implements DocTemplateService {

    @Autowired
    private DocSectionTemplateService docSectionTemplateService;
    
    @Autowired
    private TechDomainService techDomainService;
    
    @Autowired
    private DocTemplateTypeMapper docTemplateTypeMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDocTemplate(DocTemplateRequest request) {
        DocTemplate docTemplate = new DocTemplate();
        BeanUtils.copyProperties(request, docTemplate);
        
        // 设置创建时间等信息
        long currentTime = System.currentTimeMillis();
        docTemplate.setCreateTime(currentTime);
        docTemplate.setUpdateTime(currentTime);
        
        // 保存
        save(docTemplate);
        return docTemplate.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDocTemplate(DocTemplateRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("模板ID不能为空");
        }
        
        DocTemplate docTemplate = getById(request.getId());
        if (docTemplate == null) {
            throw new RuntimeException("模板不存在");
        }
        
        BeanUtils.copyProperties(request, docTemplate);
        docTemplate.setUpdateTime(System.currentTimeMillis());
        
        return updateById(docTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDocTemplate(Long id) {
        // 删除关联的分项模板
        List<DocSectionTemplateDTO> sections = docSectionTemplateService.getByDocTemplateId(id);
        for (DocSectionTemplateDTO section : sections) {
            docSectionTemplateService.deleteDocSectionTemplate(section.getId());
        }
        
        return removeById(id);
    }

    @Override
    public DocTemplateDTO getDocTemplate(Long id) {
        DocTemplate docTemplate = getById(id);
        if (docTemplate == null) {
            return null;
        }
        
        // 转换为DTO
        DocTemplateDTO dto = convertToDTO(docTemplate);
        
        // 设置分项信息
        List<DocSectionTemplateDTO> sections = docSectionTemplateService.getByDocTemplateId(id);
        dto.setSections(sections);
        
        return dto;
    }

    @Override
    public List<DocTemplateDTO> getAllDocTemplates() {
        List<DocTemplate> templates = list();
        return templates.stream().map(this::convertToFullDTO).collect(Collectors.toList());
    }

    @Override
    public List<DocTemplateDTO> getByDomainId(Long domainId) {
        if (domainId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<DocTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DocTemplate::getDomainId, domainId);
        
        List<DocTemplate> templates = list(queryWrapper);
        return templates.stream().map(this::convertToFullDTO).collect(Collectors.toList());
    }
    
    /**
     * 将实体转换为基本DTO（不包含分项信息）
     */
    private DocTemplateDTO convertToDTO(DocTemplate entity) {
        if (entity == null) {
            return null;
        }
        
        DocTemplateDTO dto = new DocTemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        
        // 设置技术领域名称
        if (entity.getDomainId() != null) {
            TechDomainDTO techDomain = techDomainService.getTechDomain(entity.getDomainId());
            if (techDomain != null) {
                dto.setDomainName(techDomain.getDomainName());
            }
        }
        
        // 设置模板类型名称
        if (entity.getTemplateTypeId() != null) {
            DocTemplateType templateType = docTemplateTypeMapper.selectById(entity.getTemplateTypeId());
            if (templateType != null) {
                dto.setTemplateTypeName(templateType.getTypeName());
            }
        }
        
        return dto;
    }
    
    /**
     * 将实体转换为完整DTO（包含分项信息）
     */
    private DocTemplateDTO convertToFullDTO(DocTemplate entity) {
        if (entity == null) {
            return null;
        }
        
        DocTemplateDTO dto = convertToDTO(entity);
        
        // 设置分项信息
        List<DocSectionTemplateDTO> sections = docSectionTemplateService.getByDocTemplateId(entity.getId());
        dto.setSections(sections);
        
        return dto;
    }
} 