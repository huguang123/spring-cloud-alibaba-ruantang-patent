package com.ruantang.service.permissions.util;

import com.ruantang.entity.sys.SysRoles;
import com.ruantang.service.permissions.model.dto.SysRolesDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色工具类
 */
public class RoleUtil {
    
    /**
     * 获取角色类型名称
     */
    public static String getRoleTypeName(Integer roleType) {
        if (roleType == null) {
            return "";
        }
        
        switch (roleType) {
            case 1:
                return "平台角色";
            case 2:
                return "企业角色";
            case 3:
                return "代理所角色";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 角色实体转DTO
     */
    public static SysRolesDTO convertToRoleDTO(SysRoles role) {
        if (role == null) {
            return null;
        }
        
        SysRolesDTO dto = new SysRolesDTO();
        BeanUtils.copyProperties(role, dto);
        
        // 设置角色类型名称
        dto.setRolesTypeName(getRoleTypeName(role.getRolesType()));
        
        return dto;
    }
    
    /**
     * 角色实体列表转DTO列表
     */
    public static List<SysRolesDTO> convertToRoleDTOList(List<SysRoles> roles) {
        List<SysRolesDTO> dtoList = new ArrayList<>();
        if (roles == null || roles.isEmpty()) {
            return dtoList;
        }
        
        for (SysRoles role : roles) {
            dtoList.add(convertToRoleDTO(role));
        }
        
        return dtoList;
    }
} 