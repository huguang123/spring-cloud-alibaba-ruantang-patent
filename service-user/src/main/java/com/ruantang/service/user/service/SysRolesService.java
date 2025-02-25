package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.entity.sys.SysRoles;

import java.util.List;

public interface SysRolesService extends IService<SysRoles> {

    /**
     * 可访问角色列表
     *
     * @return
     */
    List<SysRoles> getRolesList();
}
