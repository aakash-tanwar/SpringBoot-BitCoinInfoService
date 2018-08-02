package com.tookitaki.cryptocurrency.bitcoin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateServiceImpl implements RestTemplateService {

	private final HttpEntity<String> jsonEntity;
	private final RestTemplate restTemplate;

	@Autowired
	public RestTemplateServiceImpl(@Qualifier("restTemplate") final RestTemplate restTemplate,
			@Qualifier("jsonEntity") final HttpEntity<String> jsonEntity) {
		this.restTemplate = restTemplate;
		this.jsonEntity = jsonEntity;
	}

	@Override
	@Retryable(value = {
			HttpServerErrorException.class }, maxAttempts = 2, backoff = @Backoff(delay = 100, maxDelay = 500))
	public <T> ResponseEntity<T> get(String url, Class<T> targetEntity) {
		ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, jsonEntity, targetEntity);
		return response;
	}

	@Override
	@Retryable(value = {
			HttpServerErrorException.class }, maxAttempts = 2, backoff = @Backoff(delay = 100, maxDelay = 500))
	public <U, T> T post(String url, U messageBody, Class<T> targetEntity) {
		T response = restTemplate.postForObject(url, messageBody, targetEntity);
		return response;

	}

	@Override
	@Retryable(value = {
			HttpServerErrorException.class }, maxAttempts = 2, backoff = @Backoff(delay = 100, maxDelay = 500))
	public <U> void update(String url, U messageBody) {
		restTemplate.put(url, messageBody);
	}

	@Override
	@Retryable(value = {
			HttpServerErrorException.class }, maxAttempts = 2, backoff = @Backoff(delay = 100, maxDelay = 500))
	public <T> ResponseEntity<T> delete(String url, Class<T> targetEntity) {
		ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.DELETE, jsonEntity, targetEntity);
		return response;
	}

}
