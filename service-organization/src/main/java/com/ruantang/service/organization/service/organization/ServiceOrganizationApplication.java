package com.ruantang.service.organization.service.organization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.ruantang")
@MapperScan(basePackages = "com.ruantang.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) // 新增此行
public class ServiceOrganizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOrganizationApplication.class, args);
    }

}
