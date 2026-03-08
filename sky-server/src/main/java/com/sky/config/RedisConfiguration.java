package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j

public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建Redis模板对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置redis的key的序列化器
        /**
         * 功能： 指定 Redis 存储数据时，将 key 从 Java 对象转换为字符串格式。
         * 使用 StringRedisSerializer 可以确保 key 以可读的字符串形式存储在 Redis 中，而不是默认的 JDK 序列化（会产生二进制乱码）。
         */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
