package com.ruantang.mapper.perm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.perm.Perm;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作权限数据访问层
 */
@Mapper
public interface PermMapper extends BaseMapper<Perm> {
} 