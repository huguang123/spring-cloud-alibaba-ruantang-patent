package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;
import com.ruantang.service.user.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 * 提供用户相关的业务操作
 */
public interface SysUserService extends IService<SysUsers> {

    /**
     * 获取用户数据（用于Spring Security认证）
     *
     * @param loginName 登录账号
     * @return UserDetails对象
     */
    UserDetails loadUserByUsername(String loginName);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息DTO
     */
    UserDTO getUserByUsername(String username);
}
