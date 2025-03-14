package com.handlers.saf.global;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.handlers.saf.exceptions.DuplicateCallbackException;
import com.handlers.saf.utilities.RedisHelper;

@Component
public class Callbacks {

	@Autowired
	RedisHelper redisHelper;

	final String SAF_CALLBACK_PREFIX = "SAF_CALLBACK_";

	protected void duplicatesChecker(String key) throws DuplicateCallbackException {
		// Check if is duplicate callback
		if (Strings.isNotEmpty(redisHelper.getKey(SAF_CALLBACK_PREFIX + key))) {
			throw new DuplicateCallbackException("Duplicate callback request for " + key);
		}

		// Add value in redis
		redisHelper.addKey(SAF_CALLBACK_PREFIX + key, key, 1, TimeUnit.HOURS);
	}

}
