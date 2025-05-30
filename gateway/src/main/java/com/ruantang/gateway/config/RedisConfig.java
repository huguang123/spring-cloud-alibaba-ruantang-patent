package com.ruantang.gateway.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 *
 * @author knox
 * @since 2020/3/2
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
