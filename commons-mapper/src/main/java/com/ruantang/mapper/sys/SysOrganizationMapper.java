package com.ruantang.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.sys.SysOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {
}
