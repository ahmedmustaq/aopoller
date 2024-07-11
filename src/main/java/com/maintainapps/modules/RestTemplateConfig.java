package com.maintainapps.modules;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {




	 
	@Bean
	public RestTemplate restTemplate() {
	    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
	   // restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(apiHost));
	    return restTemplate;
	}
	 
	@Bean
	@ConditionalOnMissingBean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory 
	                        = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setHttpClient(HttpClientBuilder.create().build());
	    return clientHttpRequestFactory;
	}
	
	
}
