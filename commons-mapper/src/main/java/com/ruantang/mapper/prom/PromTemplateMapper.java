package com.ruantang.mapper.prom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.prom.PromTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 提示词模板Mapper接口
 */
@Mapper
@Repository
public interface PromTemplateMapper extends BaseMapper<PromTemplate> {
} 