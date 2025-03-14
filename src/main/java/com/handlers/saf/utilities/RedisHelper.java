package com.handlers.saf.utilities;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Service
public class RedisHelper {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisHelper(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * Add a key with custom TTL
	 * 
	 * @param key
	 * @param value
	 * @param ttl
	 * @param unit
	 */
	public boolean addKey(String key, String value, long ttl, TimeUnit unit) {
		try {
			redisTemplate.opsForValue().set(key, value, ttl, unit);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Adds a key with a default TTL of 1 day (24HRS)
	 * 
	 * @param key
	 * @param value
	 */
	public boolean addKey(String key, String value) {
		try {
			redisTemplate.opsForValue().set(key, value, 24, TimeUnit.HOURS);
			return true;
		} catch (Exception e) {
			log.error("Redis Error: {}", e.getMessage());
			return false;
		}
	}

	public String getKey(String key) {
		try {
			Object obj = redisTemplate.opsForValue().get(key);
			return obj != null ? String.valueOf(obj) : null;
		} catch (Exception e) {
			log.error("Redis Error: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * Delete a key from Redis.
	 *
	 * @param key The key to delete.
	 * @return True if the key was deleted, false otherwise.
	 */
	public boolean deleteKey(String key) {
		try {
			return Boolean.TRUE.equals(redisTemplate.delete(key));
		} catch (Exception e) {
			log.error("Redis Error: {}", e.getMessage());
			return false;
		}
	}

}
