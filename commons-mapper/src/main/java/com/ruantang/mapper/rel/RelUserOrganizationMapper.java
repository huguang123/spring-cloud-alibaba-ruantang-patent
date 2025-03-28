package com.ruantang.mapper.rel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.rel.RelUserOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RelUserOrganizationMapper extends BaseMapper<RelUserOrganization> {
}
