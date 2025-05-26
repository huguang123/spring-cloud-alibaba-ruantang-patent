package com.ruantang.gateway.filter;

import com.alibaba.fastjson.JSON;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

// 新建 GatewayJwtFilter.java
@Component
public class GatewayJwtFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GatewayJwtFilter.class);
    
    private static final String SUPER_ADMIN_ROLE = "ROLE_SUPER_ADMIN"; // 超级管理员角色标识
    private static final String ADMIN_ROLE_PREFIX = "ROLE_ADMIN"; // 管理员角色前缀
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

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
        log.info("进入统一验证平台");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        // 1. 白名单路径放行
        if (isAllowPath(path)) {
            log.info("白名单路径放行: {}", path);
            return chain.filter(exchange);
        }

        // 2. 获取Token
        String token = resolveToken(request);
        log.info("token: {}", token);
        if (token == null) {
            return writeUnauthResponse(exchange, "缺失认证凭证");
        }

        // 3. 验证Token有效性
        try {
            String username = jwtTokenUtil.getUserNameFromToken(token);

            // 4. Redis校验Token（核心增强）
            String redisTokenKey = "AUTH:TOKEN:" + username;
            String validToken = redisTemplate.opsForValue().get(redisTokenKey);

            if (validToken == null) {
                return writeUnauthResponse(exchange, "凭证已过期");
            }
            
            validToken = validToken.replace("\"", "");
            if (!validToken.equals(token)) {
                return writeUnauthResponse(exchange, "凭证无效");
            }
            
            // 5. 获取用户权限信息
            String redisPermKey = "AUTH:PERMISSION:" + username;
            String permJson = redisTemplate.opsForValue().get(redisPermKey);
            if (permJson == null) {
                return writeUnauthResponse(exchange, "无法获取权限信息");
            }
            
            Map<String, Object> permMap;
            try {
                permMap = JSON.parseObject(permJson, Map.class);
            } catch (Exception e) {
                log.error("解析权限数据异常: {}", e.getMessage());
                return writeUnauthResponse(exchange, "权限数据解析失败");
            }
            
            // 6. 验证是否是超级平台管理员
            List<String> roles = (List<String>) permMap.get("roles");
            if (roles != null && roles.contains(SUPER_ADMIN_ROLE)) {
                log.info("超级平台管理员权限验证通过: {}", username);
                return passRequest(exchange, chain, username);
            }
            
            // 7. TODO: 检查是否跨系统接口访问
            // 这部分需要在nginx配置完成后实现
            // 思路：从请求头获取X-Platform-Type，与用户的user_platform_type进行比对
            // Integer requestPlatformType = getRequestPlatformType(request);
            // Integer userPlatformType = (Integer) permMap.get("user_platform_type");
            // if (!Objects.equals(requestPlatformType, userPlatformType)) {
            //     return writeUnauthResponse(exchange, "不允许跨平台访问");
            // }
            
            // 8. 管理员角色验证
            boolean isAdmin = false;
            if (roles != null) {
                for (String role : roles) {
                    if (role.startsWith(ADMIN_ROLE_PREFIX)) {
                        isAdmin = true;
                        break;
                    }
                }
            }
            
            if (isAdmin) {
                log.info("管理员角色验证通过: {}", username);
                return passRequest(exchange, chain, username);
            }
            
            // 9. API权限匹配检查
            List<String> apis = (List<String>) permMap.get("apis");
            if (apis == null || apis.isEmpty()) {
                return writeUnauthResponse(exchange, "无API访问权限");
            }
            
            if (!hasApiPermission(path, apis)) {
                return writeUnauthResponse(exchange, "无访问权限");
            }

            // 权限验证通过
            return passRequest(exchange, chain, username);

        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage());
            return writeUnauthResponse(exchange, "凭证验证失败");
        }
    }
    
    /**
     * 检查用户是否有API访问权限
     */
    private boolean hasApiPermission(String requestPath, List<String> permittedApis) {
        for (String api : permittedApis) {
            if (pathMatcher.match(api, requestPath)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证通过，构建新请求并放行
     */
    private Mono<Void> passRequest(ServerWebExchange exchange, GatewayFilterChain chain, String username) {
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header("X-AUTH-USER", username)
                .build();
        return chain.filter(exchange.mutate().request(newRequest).build());
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
        List<String> urls = ignoreUrlsConfig.getUrls();
        log.info("检查白名单: {}", path);
        for (String url : urls) {
            if (path.startsWith(url)) {
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

