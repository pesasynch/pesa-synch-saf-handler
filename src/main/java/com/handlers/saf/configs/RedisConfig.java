package com.handlers.saf.configs;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	String host;

	@Value("${spring.data.redis.port}")
	Integer port;

	@Value("${spring.data.redis.connect-timeout}")
	int connectionTimeout;

	@Value("${spring.data.redis.jedis.pool.max-wait}")
	int commandTimeout;

	@Bean(destroyMethod = "shutdown")
	public ClientResources clientResources() {
		return DefaultClientResources.create();
	}

	@Bean
	public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) {
		// Configure your Redis server settings
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);

		// Build the client configuration with a 2000ms command timeout
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(commandTimeout))
				.clientOptions(ClientOptions.builder()
						.timeoutOptions(TimeoutOptions.enabled(Duration.ofMillis(connectionTimeout))).build())
				.clientResources(clientResources).build();

		return new LettuceConnectionFactory(redisConfig, clientConfig);
	}
	
	
	@Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Use String serializers for keys and hash keys
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // Optionally, configure value serializers as needed
        return template;
    }

}
