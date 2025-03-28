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
    public List<SysRoles> getRolesList(String userId) {
        return List.of();
    }

    @Override
    public UserDTO getUserById(Long id) {
        // 获取用户基本信息
        SysUsers user = getById(id);
        if (user == null) {
            return null;
        }
        
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> listUsers() {
        // 获取所有用户并转换为DTO
        List<SysUsers> users = list();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserDTO userDTO) {
        // 转换DTO为实体
        SysUsers user = new SysUsers();
        BeanUtils.copyProperties(userDTO, user);
        
        // 设置创建时间等基本信息
        long currentTime = System.currentTimeMillis();
        user.setCreateTime(currentTime);
        user.setUpdateTime(currentTime);
        
        // 保存用户信息
        save(user);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserDTO userDTO) {
        // 检查用户是否存在
        SysUsers user = getById(userDTO.getId());
        if (user == null) {
            return false;
        }
        
        try {
            // 更新用户信息
            BeanUtils.copyProperties(userDTO, user);
            user.setUpdateTime(System.currentTimeMillis());
            return updateById(user);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        try {
            // 删除用户角色关系
            LambdaQueryWrapper<RelUserRoles> roleWrapper = new LambdaQueryWrapper<>();
            roleWrapper.eq(RelUserRoles::getUserId, id);
            userRolesMapper.delete(roleWrapper);
            
            // 删除用户组织关系
            LambdaQueryWrapper<RelUserOrganization> orgWrapper = new LambdaQueryWrapper<>();
            orgWrapper.eq(RelUserOrganization::getUserId, id);
            userOrganizationMapper.delete(orgWrapper);
            
            // 删除用户基本信息
            return removeById(id);
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRole(Long userId, Long roleId) {
        try {
            // 检查用户和角色是否存在
            if (getById(userId) == null || rolesMapper.selectById(roleId) == null) {
                return false;
            }
            
            // 检查是否已分配该角色
            Long count = userRolesMapper.selectCount(new LambdaQueryWrapper<RelUserRoles>()
                    .eq(RelUserRoles::getUserId, userId)
                    .eq(RelUserRoles::getRoleId, roleId));
                    
            // 如果未分配，则创建关联关系
            if (count == 0) {
                RelUserRoles userRole = new RelUserRoles();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRolesMapper.insert(userRole) > 0;
            }
            return true;
        } catch (Exception e) {
            log.error("分配角色失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRole(Long userId, Long roleId) {
        try {
            return userRolesMapper.delete(new LambdaQueryWrapper<RelUserRoles>()
                    .eq(RelUserRoles::getUserId, userId)
                    .eq(RelUserRoles::getRoleId, roleId)) > 0;
        } catch (Exception e) {
            log.error("移除角色失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignOrganization(Long userId, Long orgId) {
        try {
            // 检查用户和组织是否存在
            if (getById(userId) == null || organizationMapper.selectById(orgId) == null) {
                return false;
            }
            
            // 检查是否已分配该组织
            LambdaQueryWrapper<RelUserOrganization> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RelUserOrganization::getUserId, userId)
                  .eq(RelUserOrganization::getOrgId, orgId);
            Long count = userOrganizationMapper.selectCount(wrapper);
                    
            // 如果未分配，则创建关联关系
            if (count == 0) {
                RelUserOrganization userOrg = new RelUserOrganization();
                userOrg.setUserId(userId);
                userOrg.setOrgId(orgId);
                return userOrganizationMapper.insert(userOrg) > 0;
            }
            return true;
        } catch (Exception e) {
            log.error("分配组织失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeOrganization(Long userId, Long orgId) {
        try {
            LambdaQueryWrapper<RelUserOrganization> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RelUserOrganization::getUserId, userId)
                  .eq(RelUserOrganization::getOrgId, orgId);
            return userOrganizationMapper.delete(wrapper) > 0;
        } catch (Exception e) {
            log.error("移除组织失败", e);
            return false;
        }
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
