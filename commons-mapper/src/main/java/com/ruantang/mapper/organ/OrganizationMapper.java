package com.ruantang.mapper.organ;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.organ.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {
} 