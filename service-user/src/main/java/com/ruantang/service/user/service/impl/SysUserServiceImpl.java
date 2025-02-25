package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.service.user.service.SysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers> implements SysUserService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    @Override
    public List<SysRoles> getRolesList(String userId) {
        return null;
    }


}
