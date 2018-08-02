package com.tookitaki.cryptocurrency.bitcoin.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableRetry
@PropertySources({ @PropertySource(value = "classpath:bitCoinInfo.properties"),
		@PropertySource(value = "classpath:linearRegressionWeights.properties") })
public class AppConfig {

	@Autowired
	private Environment env;

	public AppConfig() {
		System.out.println("Loading App configuration for Bit coin info service !!");
	}

	@Bean(name = "restTemplate")

	public RestTemplate getAutocompleteSolrRestTemplate() {
		int readTimeout = Integer.parseInt(env.getProperty("template.read.timeout"));
		int connectTimeout = Integer.parseInt(env.getProperty("template.connect.timeout"));
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				getHttpClient());
		requestFactory.setReadTimeout(readTimeout);
		requestFactory.setConnectTimeout(connectTimeout);
		RestTemplate template = new RestTemplate(requestFactory);

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

		// Add the Jackson Message converter
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
		messageConverters.add(converter);
		template.setMessageConverters(messageConverters);
		return template;
	}

	@Bean
	public HttpClient getHttpClient() {
		final PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
		poolingConnectionManager.setMaxTotal(Integer.parseInt(env.getProperty("http.threads.max")));
		poolingConnectionManager.setDefaultMaxPerRoute(Integer.parseInt(env.getProperty("http.threads.per.host")));
		final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolingConnectionManager)
				.build();
		return httpClient;
	}

	@Bean
	@Qualifier("jsonEntity")
	public HttpEntity<String> jsonHttpEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return objectMapper;
	}
}
