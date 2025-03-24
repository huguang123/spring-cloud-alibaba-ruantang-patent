package com.ruantang.mapper.prom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.prom.PromTemplateParameter;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 提示词模板参数Mapper接口
 */
@Mapper
@Repository
public interface PromTemplateParameterMapper extends BaseMapper<PromTemplateParameter> {
} 