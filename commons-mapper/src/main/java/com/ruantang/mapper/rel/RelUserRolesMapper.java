package com.ruantang.mapper.rel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruantang.entity.rel.RelUserRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RelUserRolesMapper extends BaseMapper<RelUserRoles> {

    /**
     * 根据角色ID删除用户角色关联
     *
     * @param roleId 角色ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM rel_user_roles WHERE roles_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);
}
