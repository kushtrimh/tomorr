package com.kushtrimh.tomorr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.kushtrimh.tomorr.properties.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
@EnableCaching
public class RedisConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort());
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Configure the validator
        var typeValidator = BasicPolymorphicTypeValidator.builder().build();
        var mapper = new ObjectMapper();
        mapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        var serializer = new GenericJackson2JsonRedisSerializer(mapper);
        // Cache configurations
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = Map.of(
                "default", RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .computePrefixWith(CacheKeyPrefix.prefixed("default"))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
        );
        // Configure Redis cache manager
        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactor) {
        return new StringRedisTemplate(redisConnectionFactor);
    }

    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return template;
    }
}
