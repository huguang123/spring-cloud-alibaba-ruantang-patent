package com.ruantang.service.prompt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.prom.PromTemplateParameter;
import com.ruantang.mapper.prom.PromTemplateParameterMapper;
import com.ruantang.service.prompt.model.PromTemplateParameterDTO;
import com.ruantang.service.prompt.model.PromTemplateParameterRequest;
import com.ruantang.service.prompt.service.PromTemplateParameterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提示词模板参数Service实现类
 */
@Service
public class PromTemplateParameterServiceImpl extends ServiceImpl<PromTemplateParameterMapper, PromTemplateParameter> implements PromTemplateParameterService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createParameter(PromTemplateParameterRequest request) {
        PromTemplateParameter parameter = new PromTemplateParameter();
        BeanUtils.copyProperties(request, parameter);
        
        // 设置默认值
        long currentTime = System.currentTimeMillis();
        parameter.setCreateTime(currentTime);
        parameter.setUpdateTime(currentTime);

        // 保存
        save(parameter);
        return parameter.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParameter(PromTemplateParameterRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("参数ID不能为空");
        }
        
        PromTemplateParameter parameter = getById(request.getId());
        if (parameter == null) {
            throw new RuntimeException("参数不存在");
        }
        
        BeanUtils.copyProperties(request, parameter);
        parameter.setUpdateTime(System.currentTimeMillis());
        
        return updateById(parameter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteParameter(Long id) {
        return removeById(id);
    }

    @Override
    public PromTemplateParameterDTO getParameter(Long id) {
        PromTemplateParameter parameter = getById(id);
        if (parameter == null) {
            return null;
        }
        
        return convertToDTO(parameter);
    }

    @Override
    public List<PromTemplateParameterDTO> getParametersByTemplateId(Long templateId) {
        if (templateId == null) {
            return new ArrayList<>();
        }
        
        LambdaQueryWrapper<PromTemplateParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromTemplateParameter::getTemplateId, templateId);
        
        List<PromTemplateParameter> parameters = list(queryWrapper);
        return parameters.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveParameters(List<PromTemplateParameterRequest> parameters, Long templateId) {
        if (CollectionUtils.isEmpty(parameters) || templateId == null) {
            return true;
        }
        
        // 删除原有参数
        deleteByTemplateId(templateId);
        
        // 批量保存新参数
        List<PromTemplateParameter> parameterList = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        
        for (PromTemplateParameterRequest request : parameters) {
            PromTemplateParameter parameter = new PromTemplateParameter();
            BeanUtils.copyProperties(request, parameter);
            parameter.setTemplateId(templateId);
            parameter.setCreateTime(currentTime);
            parameter.setUpdateTime(currentTime);
            parameterList.add(parameter);
        }
        
        return saveBatch(parameterList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTemplateId(Long templateId) {
        if (templateId == null) {
            return false;
        }
        
        LambdaQueryWrapper<PromTemplateParameter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromTemplateParameter::getTemplateId, templateId);
        
        return remove(queryWrapper);
    }
    
    /**
     * 将实体转换为DTO
     */
    private PromTemplateParameterDTO convertToDTO(PromTemplateParameter parameter) {
        PromTemplateParameterDTO dto = new PromTemplateParameterDTO();
        BeanUtils.copyProperties(parameter, dto);
        
        // 设置提示词类型描述
        if (parameter.getPromRole() != null) {
            dto.setPromRoleDesc(parameter.getPromRole() == 0 ? "系统提示词" : "用户提示词");
        }
        
        return dto;
    }
} 