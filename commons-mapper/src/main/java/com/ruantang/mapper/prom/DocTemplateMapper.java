package com.ruantang.mapper.prom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.prom.DocTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 文档模板Mapper接口
 */
@Mapper
@Repository
public interface DocTemplateMapper extends BaseMapper<DocTemplate> {
} 