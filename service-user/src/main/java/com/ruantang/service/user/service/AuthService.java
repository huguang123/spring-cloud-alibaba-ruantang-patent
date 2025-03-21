package com.ruantang.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.model.dto.SysUserDTO;
import com.ruantang.service.user.model.dto.SysUserRegisterDTO;

import java.util.Map;

public interface AuthService extends IService<SysUsers> {

    /**
     * 用户登录
     * @param sysUserRegisterDTO
     * @return
     */
    Map<String, String> login(SysUserRegisterDTO sysUserRegisterDTO);

    /**
     * 用户注销
     * @return
     */
    Boolean logout();

    /**
     * 用户注册
     *
     * @param dto 注册DTO
     * @return
     */
    SysUserDTO register(SysUserRegisterDTO dto);
}
