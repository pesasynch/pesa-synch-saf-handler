package com.handlers.saf.oauth;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handlers.saf.utilities.HttpHelper;
import com.handlers.saf.utilities.RedisHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SafaricomOauth {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	HttpHelper httpHelper;

	@Autowired
	RedisHelper redisHelper;

	@Value("${app.safaricom.base.url}")
	private String baseURL;

	final String URL_POSTFIX = "/oauth/v1/generate?grant_type=client_credentials";

	final String SAF_AUTH_TOKEN_PREFIX = "SAF_AUTH_TOKEN_";

	public String getOAuthToken(String consumerKey, String consumerSecret) {

		final String redisKey = SAF_AUTH_TOKEN_PREFIX + consumerKey;

		// First check on redis
		String token = redisHelper.getKey(redisKey);
		if (token == null) {
			System.out.println();

			log.info("SAF token not found on Redis !");

			token = fetchSafaricomAuthToken(consumerKey, consumerSecret);

			if (Strings.isNotBlank(token)) {
				// Save to Redis
				redisHelper.addKey(redisKey, token, 55, TimeUnit.MINUTES);
			}

		}

		return token;

	}

	private String fetchSafaricomAuthToken(String consumerKey, String consumerSecret) {

		log.info("SAF fetching from Saf Auth API !");

		String authUrl = baseURL + URL_POSTFIX;
		String authHeader = Base64.getEncoder().encodeToString((consumerKey + ":" + consumerSecret).getBytes());

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic " + authHeader);
		headers.put("Content-Type", MediaType.APPLICATION_JSON.toString());

		try {
			var response = httpHelper.getRequest(authUrl, headers);
			JsonNode jsonNode = objectMapper.readTree(response);
			return jsonNode.get("access_token").asText();
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse access token", e);
		}

	}

}
