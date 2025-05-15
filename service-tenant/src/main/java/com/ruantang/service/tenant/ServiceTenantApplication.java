package com.ruantang.service.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 租户服务应用
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ruantang.service.tenant",
    "com.ruantang.entity",
    "com.ruantang.mapper",
    "com.ruantang.commons"
})
public class ServiceTenantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTenantApplication.class, args);
    }
}
