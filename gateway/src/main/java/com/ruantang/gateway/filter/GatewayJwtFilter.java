package com.ruantang.gateway.filter;

import com.ruantang.gateway.config.IgnoreUrlsConfig;
import com.ruantang.gateway.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

// 新建 GatewayJwtFilter.java
@Component
public class GatewayJwtFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GatewayJwtFilter.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Value("${security.jwt.tokenHeader:Authorization}")
    private String tokenHeader;

    @Value("${security.jwt.tokenPrefix:Bearer }")
    private String tokenPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("进入统一验证平台");
        ServerHttpRequest request = exchange.getRequest();

        // 1. 白名单路径放行

        if (isAllowPath(request.getPath().toString())) {
            log.info("白名单路径放行: {}", request.getPath());

            return chain.filter(exchange);
        }

        // 2. 获取Token
        String token = resolveToken(request);
        System.out.println("token:"+token);
        if (token == null) {
            return writeUnauthResponse(exchange, "缺失认证凭证");
        }

        // 3. 验证Token有效性
        try {
            String username = jwtTokenUtil.getUserNameFromToken(token);

            // 4. Redis校验（核心增强）
            String redisKey = "AUTH:TOKEN:" + username;
            String validToken = redisTemplate.opsForValue().get(redisKey);

            if (validToken == null) {
                return writeUnauthResponse(exchange, "凭证已过期");
            }
            validToken=validToken.replace("\"","");
            if (!validToken.equals(token)) {
                return writeUnauthResponse(exchange, "凭证无效");
            }




            // 5. 传递用户信息
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-AUTH-USER", username)
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());

        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage());
            return writeUnauthResponse(exchange, "凭证验证失败");
        }
    }

    private String resolveToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(tokenHeader);
        // 判断是否为空，并且以 Bearer 开头
        if (StringUtils.hasText(header) && header.startsWith(tokenPrefix)) {
            return header.substring(tokenPrefix.length()).trim();
        }
        return null;
    }

    private boolean isAllowPath(String path) {
        // 配置白名单路径（示例）`
        List<String> urls = ignoreUrlsConfig.getUrls();
        log.info("白名单路径放行: {}", path);
        log.info("白名单路径放行: {}", urls);
        for (String url : urls){
            if (path.startsWith(url)){
                return true;
            }
        }
        return false;
    }

    private Mono<Void> writeUnauthResponse(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = String.format("{\"code\":401,\"message\":\"%s\"}", msg);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100; // 最高优先级
    }
}

