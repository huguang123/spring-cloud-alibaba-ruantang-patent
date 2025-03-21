package com.ruantang.service.prompt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.ruantang")
@MapperScan(basePackages = "com.ruantang.mapper")
public class ServicePromptApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePromptApplication.class, args);
    }

}
