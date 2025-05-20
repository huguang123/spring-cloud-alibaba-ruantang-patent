package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.rel.RelUserRoles;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.mapper.rel.RelUserRolesMapper;
import com.ruantang.mapper.sys.SysRolesMapper;
import com.ruantang.service.user.service.SysRolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRolesServiceImpl extends ServiceImpl<SysRolesMapper, SysRoles> implements SysRolesService {
    
    private final RelUserRolesMapper relUserRolesMapper;
    
    @Override
    public List<SysRoles> getRolesList() {
        // 查询所有角色
        return baseMapper.selectList(null);
    }
    
    @Override
    public List<SysRoles> getUserRoles(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        
        // 查询用户-角色关联
        LambdaQueryWrapper<RelUserRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RelUserRoles::getUserId, userId);
        List<RelUserRoles> userRoles = relUserRolesMapper.selectList(queryWrapper);
        
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }
        
        // 获取角色ID列表
        List<Long> roleIds = userRoles.stream()
                .map(RelUserRoles::getRoleId)
                .collect(Collectors.toList());
        
        // 查询角色信息
        return baseMapper.selectBatchIds(roleIds);
    }
}
