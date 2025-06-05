package com.ruantang.service.user.util;

import com.ruantang.service.user.model.dto.PermDataPolicyDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 默认权限工具类
 * 为新注册用户提供基础权限
 */
public class DefaultPermissionUtil {
    
    /**
     * 新用户默认的API权限
     * 包含用户基本操作权限
     */
    public static final Set<String> DEFAULT_API_PERMISSIONS = new HashSet<>(Arrays.asList(
            // 用户个人信息相关API
//            "GET:/ums/api/user/users/info",           // 获取当前用户信息
            "GET:/ums/api/user/users/profile",        // 获取个人资料
            "PUT:/ums/api/user/users/profile",        // 更新个人资料
            "PUT:/ums/api/user/users/update-password", // 修改密码
            
            // 认证相关API  
//            "POST:/auth/logout",                   // 用户注销（退出登录）
            
            // 账户管理API
            "POST:/ums/api/user/users/deactivate-account" // 账户注销（删除账户）
            
            // 基础查询权限
//            "GET:/api/user/users/{id}"            // 查询用户详情（仅限自己）
    ));
    
    /**
     * 新用户默认的按钮权限
     * 包含基本的页面操作权限
     */
    public static final Set<String> DEFAULT_BUTTON_PERMISSIONS = new HashSet<>(Arrays.asList(
            // 个人中心相关按钮
            "profile:view",                        // 查看个人资料
            "profile:edit",                        // 编辑个人资料
            "profile:password",                    // 修改密码
            
            // 基础操作按钮
//            "user:logout",                         // 注销登录（退出登录）
            "account:deactivate"                   // 账户注销（删除账户）
    ));
    
    /**
     * 新用户默认数据权限策略
     * 确保用户只能访问自己的数据
     */
    public static final String DEFAULT_USER_DATA_POLICY_CODE = "default_user_self_only";
    public static final String DEFAULT_USER_DATA_POLICY_NAME = "新用户默认数据权限-仅自己";
    public static final String DEFAULT_USER_DATA_CONDITION = "AND user_id = #{currentUserId}";
    public static final String DEFAULT_USER_DATA_TABLES = "sys_users";
    
    /**
     * 判断用户是否为新用户（无角色无权限）
     * 
     * @param roleIds 用户角色ID列表
     * @param buttons 用户按钮权限列表
     * @param apis 用户API权限列表
     * @return true表示是新用户
     */
    public static boolean isNewUser(java.util.List<Long> roleIds, Set<String> buttons, Set<String> apis) {
        // 如果没有角色，且没有任何权限，则认为是新用户
        return (roleIds == null || roleIds.isEmpty()) && 
               (buttons == null || buttons.isEmpty()) && 
               (apis == null || apis.isEmpty());
    }
    
    /**
     * 为新用户分配默认权限
     * 
     * @param buttons 当前按钮权限集合
     * @param apis 当前API权限集合
     * @param dataPolicies 当前数据权限策略映射
     */
    public static void assignDefaultPermissions(Set<String> buttons, Set<String> apis, Map<String, PermDataPolicyDTO> dataPolicies) {
        buttons.addAll(DEFAULT_BUTTON_PERMISSIONS);
        apis.addAll(DEFAULT_API_PERMISSIONS);
        
        // 添加默认数据权限策略
        PermDataPolicyDTO defaultDataPolicy = new PermDataPolicyDTO();
        defaultDataPolicy.setPolicyCode(DEFAULT_USER_DATA_POLICY_CODE);
        defaultDataPolicy.setPolicyName(DEFAULT_USER_DATA_POLICY_NAME);
        defaultDataPolicy.setConditionType(1); // SQL片段类型
        defaultDataPolicy.setConditionExpression(DEFAULT_USER_DATA_CONDITION);
        defaultDataPolicy.setEffectTables(DEFAULT_USER_DATA_TABLES);
        defaultDataPolicy.setPriority(999); // 最高优先级
        
        dataPolicies.put(DEFAULT_USER_DATA_TABLES, defaultDataPolicy);
    }
    
    /**
     * 为新用户分配默认权限（兼容原有方法）
     * 
     * @param buttons 当前按钮权限集合
     * @param apis 当前API权限集合
     */
    public static void assignDefaultPermissions(Set<String> buttons, Set<String> apis) {
        buttons.addAll(DEFAULT_BUTTON_PERMISSIONS);
        apis.addAll(DEFAULT_API_PERMISSIONS);
    }
} 