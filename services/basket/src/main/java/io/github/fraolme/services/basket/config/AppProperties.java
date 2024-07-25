package io.github.fraolme.services.basket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties
@Component
public class AppProperties {
    @Value("${redis.server}")
    private String redisServer;

    @Value("${redis.port}")
    private int redisPort;

    public int getRedisPort() {
        return redisPort;
    }

    public String getRedisServer() {
        return redisServer;
    }
}
