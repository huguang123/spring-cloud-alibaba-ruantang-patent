package com.ruantang.service.user.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户信息数据传输对象
 */
@Data
public class UserDTO {
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 登录账号
     */
    private String loginName;
    
    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * 手机号码
     */
    private String userPhone;
    
    /**
     * 电子邮箱
     */
    private String userMail;
    
    /**
     * 性别（0：男，1：女）
     */
    private Integer gender;
    
    /**
     * 微信联系方式
     */
    private String weixin;
    
    /**
     * QQ联系方式
     */
    private String qq;
    
    /**
     * 职级
     */
    private Integer level;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;
    
    /**
     * 用户拥有的角色列表
     */
    private List<RoleDTO> roles;
    
    /**
     * 用户所属的组织列表
     */
    private List<OrganizationDTO> organizations;
} 