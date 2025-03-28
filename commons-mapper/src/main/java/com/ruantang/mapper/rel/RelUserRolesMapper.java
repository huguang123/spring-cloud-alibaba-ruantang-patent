package com.ruantang.mapper.rel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.rel.RelUserRoles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RelUserRolesMapper extends BaseMapper<RelUserRoles> {
}
