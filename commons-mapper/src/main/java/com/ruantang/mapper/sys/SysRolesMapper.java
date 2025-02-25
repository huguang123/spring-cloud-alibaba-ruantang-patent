package com.ruantang.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.sys.SysRoles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRolesMapper extends BaseMapper<SysRoles> {
}
