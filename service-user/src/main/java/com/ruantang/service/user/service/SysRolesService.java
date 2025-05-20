package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.entity.sys.SysRoles;

import java.util.List;

public interface SysRolesService extends IService<SysRoles> {

    /**
     * 可访问角色列表
     *
     * @return 所有角色列表
     */
    List<SysRoles> getRolesList();
    
    /**
     * 获取用户的角色列表
     *
     * @param userId 用户ID
     * @return 用户的角色列表
     */
    List<SysRoles> getUserRoles(Long userId);
}
