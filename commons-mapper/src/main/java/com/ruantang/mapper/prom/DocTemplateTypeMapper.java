package com.ruantang.mapper.prom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.prom.DocTemplateType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文档模板类型Mapper接口
 */
@Mapper
@Repository
public interface DocTemplateTypeMapper extends BaseMapper<DocTemplateType> {
} 