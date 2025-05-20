package com.ruantang.service.organization.util;

import com.ruantang.entity.organ.Organization;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.organization.model.dto.OrganizationDTO;
import com.ruantang.service.organization.model.dto.OrganizationMemberDTO;
import com.ruantang.service.organization.model.dto.OrganizationTreeDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织工具类
 */
public class OrganizationUtil {
    
    /**
     * 将组织实体转换为DTO
     * 
     * @param organization 组织实体
     * @return 组织DTO
     */
    public static OrganizationDTO convertToDTO(Organization organization) {
        if (organization == null) {
            return null;
        }
        
        OrganizationDTO dto = new OrganizationDTO();
        BeanUtils.copyProperties(organization, dto);
        
        // 设置状态名称
        if (dto.getState() != null) {
            dto.setStateName(dto.getState() == 1 ? "启用" : "禁用");
        }
        
        return dto;
    }
    
    /**
     * 将组织DTO转换为实体
     * 
     * @param dto 组织DTO
     * @return 组织实体
     */
    public static Organization convertToEntity(OrganizationDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Organization organization = new Organization();
        BeanUtils.copyProperties(dto, organization);
        
        return organization;
    }
    
    /**
     * 构建组织树
     * 
     * @param organizations 组织列表
     * @param memberCountMap 成员数量Map
     * @return 组织树
     */
    public static List<OrganizationTreeDTO> buildOrganizationTree(List<Organization> organizations, Map<Long, Integer> memberCountMap) {
        if (organizations == null || organizations.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 转换为树节点DTO
        List<OrganizationTreeDTO> nodes = organizations.stream()
                .map(org -> {
                    OrganizationTreeDTO node = new OrganizationTreeDTO();
                    node.setId(org.getId());
                    node.setOrgName(org.getOrgName());
                    node.setOrgCode(org.getOrgCode());
                    node.setMemberCount(memberCountMap.getOrDefault(org.getId(), 0));
                    node.setChildren(new ArrayList<>());
                    return node;
                })
                .collect(Collectors.toList());
        
        // 构建ID到节点的映射
        Map<Long, OrganizationTreeDTO> idToNodeMap = nodes.stream()
                .collect(Collectors.toMap(OrganizationTreeDTO::getId, node -> node));
        
        // 构建树
        List<OrganizationTreeDTO> rootNodes = new ArrayList<>();
        
        for (Organization org : organizations) {
            OrganizationTreeDTO currentNode = idToNodeMap.get(org.getId());
            
            if (org.getParentId() == null || org.getParentId() == 0L) {
                // 根节点
                rootNodes.add(currentNode);
            } else {
                // 添加到父节点的子节点列表
                OrganizationTreeDTO parentNode = idToNodeMap.get(org.getParentId());
                if (parentNode != null) {
                    parentNode.getChildren().add(currentNode);
                } else {
                    // 找不到父节点，视为根节点
                    rootNodes.add(currentNode);
                }
            }
        }
        
        return rootNodes;
    }
    
    /**
     * 将SysUsers转换为OrganizationMemberDTO
     * 
     * @param user 用户实体
     * @param roles 角色列表
     * @param roleIds 角色ID列表
     * @return 组织成员DTO
     */
    public static OrganizationMemberDTO convertToMemberDTO(SysUsers user, List<String> roles, List<Long> roleIds) {
        if (user == null) {
            return null;
        }
        
        OrganizationMemberDTO dto = new OrganizationMemberDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setLoginName(user.getLoginName());
        dto.setUserMail(user.getUserMail());
        dto.setUserPhone(user.getUserPhone());
        dto.setJoinDate(user.getCreateTime());
        dto.setRoles(roles);
        dto.setRoleIds(roleIds);
        
        return dto;
    }
} 