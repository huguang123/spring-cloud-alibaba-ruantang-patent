package com.ruantang.service.organization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.ruantang.service.organization",
        "com.ruantang.entity",
        "com.ruantang.mapper",
        "com.ruantang.commons"
})
@MapperScan("com.ruantang.mapper")
@EnableFeignClients("com.ruantang.service.organization.client")
public class ServiceOrganizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOrganizationApplication.class, args);
    }

}
