package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.mapper.sys.SysRolesMapper;
import com.ruantang.service.user.service.SysRolesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRolesServiceImpl extends ServiceImpl<SysRolesMapper,SysRoles> implements SysRolesService {
    @Override
    public List<SysRoles> getRolesList() {
        List<SysRoles> sysRoles = new ArrayList<>();
        return sysRoles;
    }
}
