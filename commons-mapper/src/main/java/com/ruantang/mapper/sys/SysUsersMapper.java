package com.ruantang.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SysUsersMapper extends BaseMapper<SysUsers> {


}
