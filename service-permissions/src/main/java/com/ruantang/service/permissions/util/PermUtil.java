package com.ruantang.service.permissions.util;

import com.ruantang.entity.perm.Perm;
import com.ruantang.entity.perm.PermDataPolicy;
import com.ruantang.service.permissions.model.dto.PermDTO;
import com.ruantang.service.permissions.model.dto.PermDataPolicyDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限工具类
 */
public class PermUtil {

    /**
     * 获取权限类型名称
     * @param permScope 权限作用域
     * @return 类型名称
     */
    public static String getPermTypeName(String permScope) {
        if (permScope == null) {
            return "";
        }
        
        switch (permScope) {
            case "PLATFORM":
                return "平台权限";
            case "TENANT":
                return "租户权限";
            case "ALL":
                return "通用权限";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 获取条件类型名称
     * @param conditionType 条件类型值
     * @return 条件类型名称
     */
    public static String getConditionTypeName(Integer conditionType) {
        if (conditionType == null) {
            return "";
        }
        
        switch (conditionType) {
            case 1:
                return "SQL片段";
            case 2:
                return "SpEL表达式";
            default:
                return "未知类型";
        }
    }
    
    /**
     * 操作权限实体转DTO
     * @param perm 操作权限实体
     * @return 操作权限DTO
     */
    public static PermDTO convertToPermDTO(Perm perm) {
        if (perm == null) {
            return null;
        }
        
        PermDTO dto = new PermDTO();
        BeanUtils.copyProperties(perm, dto);
        
        return dto;
    }
    
    /**
     * 操作权限实体列表转DTO列表
     * @param perms 操作权限实体列表
     * @return 操作权限DTO列表
     */
    public static List<PermDTO> convertToPermDTOList(List<Perm> perms) {
        List<PermDTO> dtoList = new ArrayList<>();
        if (perms == null || perms.isEmpty()) {
            return dtoList;
        }
        
        for (Perm perm : perms) {
            dtoList.add(convertToPermDTO(perm));
        }
        
        return dtoList;
    }
    
    /**
     * 数据权限策略实体转DTO
     * @param policy 数据权限策略实体
     * @return 数据权限策略DTO
     */
    public static PermDataPolicyDTO convertToPolicyDTO(PermDataPolicy policy) {
        if (policy == null) {
            return null;
        }
        
        PermDataPolicyDTO dto = new PermDataPolicyDTO();
        BeanUtils.copyProperties(policy, dto);
        
        // 设置类型名称
        dto.setConditionTypeName(getConditionTypeName(policy.getConditionType()));
        
        return dto;
    }
    
    /**
     * 数据权限策略实体列表转DTO列表
     * @param policies 数据权限策略实体列表
     * @return 数据权限策略DTO列表
     */
    public static List<PermDataPolicyDTO> convertToPolicyDTOList(List<PermDataPolicy> policies) {
        List<PermDataPolicyDTO> dtoList = new ArrayList<>();
        if (policies == null || policies.isEmpty()) {
            return dtoList;
        }
        
        for (PermDataPolicy policy : policies) {
            dtoList.add(convertToPolicyDTO(policy));
        }
        
        return dtoList;
    }
} 