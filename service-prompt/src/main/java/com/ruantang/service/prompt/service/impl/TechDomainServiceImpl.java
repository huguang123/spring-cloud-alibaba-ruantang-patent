package com.ruantang.service.prompt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.prom.TechDomain;
import com.ruantang.mapper.prom.TechDomainMapper;
import com.ruantang.service.prompt.model.DocTemplateDTO;
import com.ruantang.service.prompt.model.TechDomainDTO;
import com.ruantang.service.prompt.model.TechDomainRequest;
import com.ruantang.service.prompt.service.DocTemplateService;
import com.ruantang.service.prompt.service.TechDomainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 技术领域Service实现类
 */
@Service
public class TechDomainServiceImpl extends ServiceImpl<TechDomainMapper, TechDomain> implements TechDomainService {

    @Autowired
    private DocTemplateService docTemplateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTechDomain(TechDomainRequest request) {
        TechDomain techDomain = new TechDomain();
        BeanUtils.copyProperties(request, techDomain);
        
        // 设置层级
        if (request.getParentId() != null) {
            TechDomain parent = getById(request.getParentId());
            if (parent == null) {
                throw new RuntimeException("父级领域不存在");
            }
            techDomain.setLevel(parent.getLevel() + 1);
        } else {
            techDomain.setLevel(1); // 一级领域
        }
        
        // 设置创建时间等信息
        long currentTime = System.currentTimeMillis();
        techDomain.setCreateTime(currentTime);
        techDomain.setUpdateTime(currentTime);
        techDomain.setIsDeleted(0); // 1表示未删除
        
        // 保存
        save(techDomain);
        return techDomain.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTechDomain(TechDomainRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("领域ID不能为空");
        }
        
        TechDomain techDomain = getById(request.getId());
        if (techDomain == null) {
            throw new RuntimeException("领域不存在");
        }
        
        // 设置更新的属性
        if (StringUtils.hasText(request.getDomainName())) {
            techDomain.setDomainName(request.getDomainName());
        }
        if (StringUtils.hasText(request.getDescription())) {
            techDomain.setDescription(request.getDescription());
        }
        
        // 更新父级关系时需要调整level
        if (request.getParentId() != null && !request.getParentId().equals(techDomain.getParentId())) {
            // 不能将自己设为自己的父级
            if (request.getParentId().equals(request.getId())) {
                throw new RuntimeException("不能将自己设为自己的父级");
            }
            
            // 不能将自己的子级设为自己的父级
            List<TechDomain> children = getAllChildren(request.getId());
            boolean isChild = children.stream().anyMatch(child -> child.getId().equals(request.getParentId()));
            if (isChild) {
                throw new RuntimeException("不能将自己的子级设为自己的父级");
            }
            
            TechDomain parent = getById(request.getParentId());
            if (parent == null) {
                throw new RuntimeException("父级领域不存在");
            }
            techDomain.setParentId(request.getParentId());
            techDomain.setLevel(parent.getLevel() + 1);
            
            // 更新所有子级的level
            updateChildrenLevel(techDomain.getId(), techDomain.getLevel());
        }
        
        techDomain.setUpdateTime(System.currentTimeMillis());
        return updateById(techDomain);
    }

    /**
     * 更新所有子级的level
     */
    private void updateChildrenLevel(Long parentId, Integer parentLevel) {
        List<TechDomain> children = list(new LambdaQueryWrapper<TechDomain>()
                .eq(TechDomain::getParentId, parentId));
        
        if (!children.isEmpty()) {
            for (TechDomain child : children) {
                child.setLevel(parentLevel + 1);
                child.setUpdateTime(System.currentTimeMillis());
                updateById(child);
                
                // 递归更新子级的level
                updateChildrenLevel(child.getId(), child.getLevel());
            }
        }
    }

    /**
     * 获取所有子级领域
     */
    private List<TechDomain> getAllChildren(Long parentId) {
        List<TechDomain> result = new ArrayList<>();
        List<TechDomain> children = list(new LambdaQueryWrapper<TechDomain>()
                .eq(TechDomain::getParentId, parentId));
        
        if (!children.isEmpty()) {
            result.addAll(children);
            for (TechDomain child : children) {
                result.addAll(getAllChildren(child.getId()));
            }
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTechDomain(Long id) {
        // 检查是否有子级领域
        Long count = count(new LambdaQueryWrapper<TechDomain>()
                .eq(TechDomain::getParentId, id));
        
        if (count > 0) {
            throw new RuntimeException("该领域下存在子级领域，不能直接删除");
        }
        
        // 获取该技术领域下的所有文档模板
        List<DocTemplateDTO> docTemplates = docTemplateService.getByDomainId(id);
        
        // 删除每个文档模板（文档模板服务中会级联删除其关联的分项）
        for (DocTemplateDTO docTemplate : docTemplates) {
            docTemplateService.deleteDocTemplate(docTemplate.getId());
        }
        
        // 删除技术领域
        return removeById(id);
    }

    @Override
    public TechDomainDTO getTechDomain(Long id) {
        System.out.println(id+"");
        TechDomain techDomain = getById(id);
        System.out.println(techDomain);
        if (techDomain == null) {
            return null;
        }
        
        TechDomainDTO dto = convertToDTO(techDomain);
        
        // 如果有父级，设置父级名称
        if (techDomain.getParentId() != null) {
            TechDomain parent = getById(techDomain.getParentId());
            if (parent != null) {
                dto.setParentName(parent.getDomainName());
            }
        }
        
        return dto;
    }

    @Override
    public List<TechDomainDTO> getTechDomainTree() {
        // 获取所有领域
        List<TechDomain> allDomains = list();
        
        // 转换为DTO
        List<TechDomainDTO> allDomainDTOs = allDomains.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 构建父子关系映射
        Map<Long, List<TechDomainDTO>> childrenMap = allDomainDTOs.stream()
                .filter(dto -> dto.getParentId() != null)
                .collect(Collectors.groupingBy(TechDomainDTO::getParentId));
        
        // 设置子级
        for (TechDomainDTO dto : allDomainDTOs) {
            if (childrenMap.containsKey(dto.getId())) {
                dto.setChildren(childrenMap.get(dto.getId()));
            } else {
                dto.setChildren(Collections.emptyList());
            }
        }
        
        // 获取一级领域
        return allDomainDTOs.stream()
                .filter(dto -> dto.getParentId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechDomainDTO> getChildrenByParentId(Long parentId) {
        // 查询直接子级
        List<TechDomain> children;
        if (parentId == null) {
            // 查询所有一级领域
            children = list(new LambdaQueryWrapper<TechDomain>()
                    .isNull(TechDomain::getParentId));
        } else {
            children = list(new LambdaQueryWrapper<TechDomain>()
                    .eq(TechDomain::getParentId, parentId));
        }
        
        // 转换为DTO并设置父级名称
        return children.stream().map(domain -> {
            TechDomainDTO dto = convertToDTO(domain);
            
            // 如果有父级，设置父级名称
            if (domain.getParentId() != null) {
                TechDomain parent = getById(domain.getParentId());
                if (parent != null) {
                    dto.setParentName(parent.getDomainName());
                }
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * 将实体转换为DTO
     */
    private TechDomainDTO convertToDTO(TechDomain domain) {
        if (domain == null) {
            return null;
        }
        
        TechDomainDTO dto = new TechDomainDTO();
        BeanUtils.copyProperties(domain, dto);
        
        return dto;
    }

    @Override
    public List<TechDomainDTO> searchWithParents(String domainName) {
        // 查询匹配的技术领域
        List<TechDomain> matchedDomains = list(new LambdaQueryWrapper<TechDomain>()
                .eq(TechDomain::getDomainName, domainName));

        if (matchedDomains == null || matchedDomains.isEmpty()) {
            return new ArrayList<>(); // 返回空列表
        }

        // 构建结果列表
        List<TechDomainDTO> result = new ArrayList<>();

        for (TechDomain domain : matchedDomains) {
            // 转换为DTO并递归获取上级技术领域
            TechDomainDTO dto = convertToDTO(domain);
            buildParentTree(dto);
            result.add(dto);
        }

        return result;
    }

    /**
     * 递归构建上级技术领域树
     */
    private void buildParentTree(TechDomainDTO current) {
        if (current.getParentId() == null) {
            return; // 如果没有父级，直接返回
        }

        // 查询父级技术领域
        TechDomain parent = getById(current.getParentId());
        if (parent == null) {
            return; // 父级不存在，直接返回
        }

        // 转换为DTO并设置到当前节点
        TechDomainDTO parentDto = convertToDTO(parent);
        current.setParentName(parent.getDomainName()); // 设置父级名称
        current.setParent(parentDto); // 设置父级节点

        // 递归构建父级树
        buildParentTree(parentDto);
    }

    /**
     * 将实体转换为DTO
     */

} 