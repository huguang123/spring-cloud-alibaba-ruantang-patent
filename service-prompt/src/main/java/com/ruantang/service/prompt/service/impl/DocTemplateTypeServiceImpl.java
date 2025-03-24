package com.ruantang.service.prompt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.prom.DocTemplate;
import com.ruantang.entity.prom.DocTemplateType;
import com.ruantang.mapper.prom.DocTemplateMapper;
import com.ruantang.mapper.prom.DocTemplateTypeMapper;
import com.ruantang.service.prompt.model.DocTemplateTypeDTO;
import com.ruantang.service.prompt.model.DocTemplateTypeRequest;
import com.ruantang.service.prompt.service.DocTemplateTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档模板类型Service实现类
 */
@Service
public class DocTemplateTypeServiceImpl extends ServiceImpl<DocTemplateTypeMapper, DocTemplateType> implements DocTemplateTypeService {

    @Autowired
    private DocTemplateMapper docTemplateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDocTemplateType(DocTemplateTypeRequest request) {
        DocTemplateType docTemplateType = new DocTemplateType();
        BeanUtils.copyProperties(request, docTemplateType);
        
        // 设置创建时间等信息
        long currentTime = System.currentTimeMillis();
        docTemplateType.setCreateTime(currentTime);
        docTemplateType.setUpdateTime(currentTime);
        
        // 保存
        save(docTemplateType);
        return docTemplateType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDocTemplateType(DocTemplateTypeRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("模板类型ID不能为空");
        }
        DocTemplateType docTemplateType = getById(request.getId());
        if (docTemplateType == null) {
            throw new RuntimeException("模板类型不存在");
        }
        
        BeanUtils.copyProperties(request, docTemplateType);
        docTemplateType.setUpdateTime(System.currentTimeMillis());
        
        return updateById(docTemplateType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDocTemplateType(Long id) {
        // 检查是否有关联的模板
        LambdaQueryWrapper<DocTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DocTemplate::getTemplateTypeId, id);
        long count = docTemplateMapper.selectCount(queryWrapper);
        
        if (count > 0) {
            throw new RuntimeException("该模板类型下存在模板，不能直接删除");
        }
        
        return removeById(id);
    }

    @Override
    public DocTemplateTypeDTO getDocTemplateType(Long id) {
        DocTemplateType docTemplateType = getById(id);
        if (docTemplateType == null) {
            return null;
        }
        
        return convertToDTO(docTemplateType);
    }

    @Override
    public List<DocTemplateTypeDTO> getAllDocTemplateTypes() {
        List<DocTemplateType> types = list();
        return types.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * 将实体转换为DTO
     */
    private DocTemplateTypeDTO convertToDTO(DocTemplateType entity) {
        DocTemplateTypeDTO dto = new DocTemplateTypeDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
} 