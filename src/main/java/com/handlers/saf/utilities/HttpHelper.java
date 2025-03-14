package com.handlers.saf.utilities;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class HttpHelper {

    private final RestClient restClient;

    public HttpHelper() {
//    	var factory = new HttpComponentsClientHttpRequestFactory();
//		factory.setConnectTimeout(5000);
//		factory.setReadTimeout(5000);
		
        this.restClient = RestClient.builder().build(); 
    }

    public HttpHelper(int connectTimeout, int readTimeout) {
        this.restClient = RestClient.builder().build(); 
    }

    public String postRequest(String url, String payload, Map<String, String> headers) {
        if (headers.isEmpty() || !headers.containsKey("Content-Type")) {
            headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }
        var response = restClient.post()
                .uri(url)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .body(payload)
                .retrieve()
                .toEntity(String.class);
        log.info("API Call URL:{} ,\nPAYLOAD:{},\nRESPONSE:{}", url,payload,  response.getBody());
        return response.getBody();
    }

    public String getRequest(String url, Map<String, String> headers) {
        if (headers.isEmpty() || !headers.containsKey("Content-Type")) {
            headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }
        var response = restClient.get()
                .uri(url)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .retrieve()
                .toEntity(String.class);
        
        log.info("API Call, URL: {} ,\nRESPONSE: {}", url, response.getBody());
        return response.getBody();
    }
}
