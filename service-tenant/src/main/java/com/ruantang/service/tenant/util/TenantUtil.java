package com.ruantang.service.tenant.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 租户工具类
 */
public class TenantUtil {
    
    /**
     * 租户类型名称映射
     */
    private static final Map<Integer, String> TENANT_TYPE_MAP = new HashMap<>();
    
    /**
     * 数据隔离模式名称映射
     */
    private static final Map<Integer, String> DATA_ISOLATION_MODE_MAP = new HashMap<>();
    
    /**
     * 模板绑定模式名称映射
     */
    private static final Map<Integer, String> TEMPLATE_BIND_MODE_MAP = new HashMap<>();
    
    static {
        // 初始化租户类型
        TENANT_TYPE_MAP.put(1, "平台管理租户");
        TENANT_TYPE_MAP.put(2, "企业商户租户");
        TENANT_TYPE_MAP.put(3, "代理所租户");
        
        // 初始化数据隔离模式
        DATA_ISOLATION_MODE_MAP.put(1, "行级隔离");
        DATA_ISOLATION_MODE_MAP.put(2, "Schema隔离");
        DATA_ISOLATION_MODE_MAP.put(3, "独立库隔离");
        
        // 初始化模板绑定模式
        TEMPLATE_BIND_MODE_MAP.put(1, "继承");
        TEMPLATE_BIND_MODE_MAP.put(2, "快照");
    }
    
    /**
     * 获取租户类型名称
     * 
     * @param type 租户类型
     * @return 类型名称
     */
    public static String getTenantTypeName(Integer type) {
        return TENANT_TYPE_MAP.getOrDefault(type, "未知");
    }
    
    /**
     * 获取数据隔离模式名称
     * 
     * @param mode 数据隔离模式
     * @return 模式名称
     */
    public static String getDataIsolationModeName(Integer mode) {
        return DATA_ISOLATION_MODE_MAP.getOrDefault(mode, "未知");
    }
    
    /**
     * 获取模板绑定模式名称
     * 
     * @param mode 绑定模式
     * @return 模式名称
     */
    public static String getBindModeName(Integer mode) {
        return TEMPLATE_BIND_MODE_MAP.getOrDefault(mode, "未知");
    }
}