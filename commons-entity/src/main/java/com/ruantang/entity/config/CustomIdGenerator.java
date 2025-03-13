package com.ruantang.entity.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.ruantang.entity.util.SnowflakeIdWorker;
import org.springframework.stereotype.Component;

@Component
public class CustomIdGenerator implements IdentifierGenerator {

    private final SnowflakeIdWorker snowflakeIdWorker;

    public CustomIdGenerator() {
        // 数据中心ID和机器ID可以根据实际情况配置
        this.snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
    }

    @Override
    public Number nextId(Object entity) {
        return snowflakeIdWorker.nextId();
    }
}
