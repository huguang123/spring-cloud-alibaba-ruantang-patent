package com.ruantang.service.permissions;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.ruantang")
@MapperScan(basePackages = "com.ruantang.mapper")
@EnableAspectJAutoProxy(exposeProxy = true) // 新增此行
@EnableFeignClients("com.ruantang.service.permissions.client")
public class ServicePermissionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePermissionsApplication.class, args);
    }

}
