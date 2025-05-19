package com.ruantang.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruantang.entity.tenant.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 租户数据访问接口
 */
@Repository
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
    
    /**
     * 分页查询租户
     * 
     * @param tenantName 租户名称（支持模糊查询）
     * @param tenantCode 租户编码
     * @param tenantType 租户类型
     * @param page 分页对象
     * @return 分页结果
     */
    Page<Tenant> queryTenantPage(
            @Param("tenantName") String tenantName,
            @Param("tenantCode") String tenantCode,
            @Param("tenantType") Integer tenantType,
            @Param("page") Page<Tenant> page
    );
} 