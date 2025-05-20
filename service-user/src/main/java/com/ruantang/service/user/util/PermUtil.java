package com.ruantang.service.user.util;

import com.ruantang.entity.perm.Perm;
import com.ruantang.entity.perm.PermDataPolicy;
import com.ruantang.service.user.model.dto.PermDTO;
import com.ruantang.service.user.model.dto.PermDataPolicyDTO;
import org.springframework.beans.BeanUtils;

/**
 * 权限工具类
 */
public class PermUtil {
    
    /**
     * 操作权限实体转DTO
     * 
     * @param perm 操作权限实体
     * @return 操作权限DTO
     */
    public static PermDTO convertToDTO(Perm perm) {
        if (perm == null) {
            return null;
        }
        PermDTO permDTO = new PermDTO();
        BeanUtils.copyProperties(perm, permDTO);
        return permDTO;
    }
    
    /**
     * 操作权限DTO转实体
     * 
     * @param permDTO 操作权限DTO
     * @return 操作权限实体
     */
    public static Perm convertToEntity(PermDTO permDTO) {
        if (permDTO == null) {
            return null;
        }
        Perm perm = new Perm();
        BeanUtils.copyProperties(permDTO, perm);
        return perm;
    }
    
    /**
     * 数据权限策略实体转DTO
     * 
     * @param policy 数据权限策略实体
     * @return 数据权限策略DTO
     */
    public static PermDataPolicyDTO convertToDTO(PermDataPolicy policy) {
        if (policy == null) {
            return null;
        }
        PermDataPolicyDTO policyDTO = new PermDataPolicyDTO();
        BeanUtils.copyProperties(policy, policyDTO);
        return policyDTO;
    }
    
    /**
     * 数据权限策略DTO转实体
     * 
     * @param policyDTO 数据权限策略DTO
     * @return 数据权限策略实体
     */
    public static PermDataPolicy convertToEntity(PermDataPolicyDTO policyDTO) {
        if (policyDTO == null) {
            return null;
        }
        PermDataPolicy policy = new PermDataPolicy();
        BeanUtils.copyProperties(policyDTO, policy);
        return policy;
    }
} 