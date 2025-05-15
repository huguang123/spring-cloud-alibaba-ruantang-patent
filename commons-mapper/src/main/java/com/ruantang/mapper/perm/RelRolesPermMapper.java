package com.ruantang.mapper.perm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.perm.RelRolesPerm;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联Mapper接口
 */
@Mapper
public interface RelRolesPermMapper extends BaseMapper<RelRolesPerm> {
} 