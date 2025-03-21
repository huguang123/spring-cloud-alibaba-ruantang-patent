package com.ruantang.mapper.prom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.prom.DocSectionTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文档分项模板Mapper接口
 */
@Mapper
@Repository
public interface DocSectionTemplateMapper extends BaseMapper<DocSectionTemplate> {
} 