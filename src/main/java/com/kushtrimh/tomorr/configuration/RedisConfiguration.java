package com.kushtrimh.tomorr.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.kushtrimh.tomorr.properties.RedisProperties;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
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

import java.time.Duration;
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
        GenericJackson2JsonRedisSerializer serializer = newGenericJacksonSerializer();
        // Cache configurations
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = Map.of(
                "default", RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .computePrefixWith(CacheKeyPrefix.prefixed("default"))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)),
                "artistsSearch", RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .computePrefixWith(CacheKeyPrefix.prefixed("artist:search"))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .entryTtl(Duration.ofMinutes(5))
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
    public RedisTemplate<String, Task<ArtistTaskData>> redisArtistSyncTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Task<ArtistTaskData>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(newGenericJacksonSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return template;
    }


    private GenericJackson2JsonRedisSerializer newGenericJacksonSerializer() {
        var typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.kushtrimh.tomorr")
                .allowIfSubType("java")
                .allowIfSubTypeIsArray()
                .build();
        var mapper = new ObjectMapper();
        mapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParameterNamesModule());
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}
