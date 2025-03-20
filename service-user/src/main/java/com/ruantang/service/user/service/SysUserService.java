package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUsers> {

    /**
     * 获取用户数据
     *
     * @param loginName 用户名
     * @return
     */
    UserDetails loadUserByUsername(String loginName);

    /**
     * 获取可访问资源列表
     *
     * @param userId
     * @return 用户ID
     */
    List<SysRoles> getRolesList(String userId);



}
