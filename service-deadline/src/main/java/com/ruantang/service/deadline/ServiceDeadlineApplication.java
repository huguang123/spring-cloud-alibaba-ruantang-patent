package com.ruantang.service.deadline;

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
@EnableFeignClients("com.ruantang.service.deadline.client")
public class ServiceDeadlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDeadlineApplication.class, args);
    }

}
