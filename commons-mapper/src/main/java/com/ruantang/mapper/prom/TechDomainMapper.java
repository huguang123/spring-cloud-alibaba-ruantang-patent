package com.ruantang.mapper.prom;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.prom.TechDomain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 技术领域Mapper接口
 */
@Mapper
@Repository
public interface TechDomainMapper extends BaseMapper<TechDomain> {
} 