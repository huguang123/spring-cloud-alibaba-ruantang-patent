package com.ruantang.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruantang.entity.rel.RelUserOrganization;
import com.ruantang.entity.rel.RelUserRoles;
import com.ruantang.entity.sys.SysOrganization;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.mapper.rel.RelUserOrganizationMapper;
import com.ruantang.mapper.rel.RelUserRolesMapper;
import com.ruantang.mapper.sys.SysOrganizationMapper;
import com.ruantang.mapper.sys.SysRolesMapper;
import com.ruantang.mapper.sys.SysUsersMapper;
import com.ruantang.service.user.model.dto.OrganizationDTO;
import com.ruantang.service.user.model.dto.RoleDTO;
import com.ruantang.service.user.model.dto.UserDTO;
import com.ruantang.service.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑，包括用户管理、角色分配、组织分配等功能
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUsersMapper, SysUsers> implements SysUserService {

    @Autowired
    private RelUserRolesMapper userRolesMapper;
    
    @Autowired
    private RelUserOrganizationMapper userOrganizationMapper;
    
    @Autowired
    private SysRolesMapper rolesMapper;
    
    @Autowired
    private SysOrganizationMapper organizationMapper;

    @Override
    public UserDetails loadUserByUsername(String loginName) {
        return null;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        // 根据用户名查询用户
        SysUsers user = getOne(new LambdaQueryWrapper<SysUsers>()
                .eq(SysUsers::getLoginName, username));
        
        if (user == null) {
            return null;
        }
        
        return convertToDTO(user);
    }
    
    /**
     * 将用户实体转换为DTO
     * 包含角色和组织信息的转换
     *
     * @param user 用户实体
     * @return 用户DTO
     */
    private UserDTO convertToDTO(SysUsers user) {
        if (user == null) {
            return null;
        }
        
        // 转换基本信息
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        
        // 获取并转换用户角色信息
        List<RelUserRoles> userRoles = userRolesMapper.selectList(
                new LambdaQueryWrapper<RelUserRoles>()
                        .eq(RelUserRoles::getUserId, user.getId()));
        
        if (!userRoles.isEmpty()) {
            // 获取角色详细信息
            List<Long> roleIds = userRoles.stream()
                    .map(RelUserRoles::getRoleId)
                    .collect(Collectors.toList());
            
            List<SysRoles> roles = rolesMapper.selectBatchIds(roleIds);
            dto.setRoles(roles.stream().map(role -> {
                RoleDTO roleDTO = new RoleDTO();
                BeanUtils.copyProperties(role, roleDTO);
                return roleDTO;
            }).collect(Collectors.toList()));
        } else {
            dto.setRoles(new ArrayList<>());
        }
        
        // 获取并转换用户组织信息
        List<RelUserOrganization> userOrgs = userOrganizationMapper.selectList(
                new LambdaQueryWrapper<RelUserOrganization>()
                        .eq(RelUserOrganization::getUserId, user.getId()));
        
        if (!userOrgs.isEmpty()) {
            // 获取组织详细信息
            List<Long> orgIds = userOrgs.stream()
                    .map(RelUserOrganization::getOrgId)
                    .collect(Collectors.toList());
            
            List<SysOrganization> orgs = organizationMapper.selectBatchIds(orgIds);
            dto.setOrganizations(orgs.stream().map(org -> {
                OrganizationDTO orgDTO = new OrganizationDTO();
                BeanUtils.copyProperties(org, orgDTO);
                return orgDTO;
            }).collect(Collectors.toList()));
        } else {
            dto.setOrganizations(new ArrayList<>());
        }
        
        return dto;
    }
}
