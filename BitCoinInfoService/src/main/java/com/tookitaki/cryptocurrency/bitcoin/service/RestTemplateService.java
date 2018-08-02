package com.tookitaki.cryptocurrency.bitcoin.service;

import org.springframework.http.ResponseEntity;

public interface RestTemplateService {

    public <T> ResponseEntity<T> get(String url, Class<T> targetEntity);

    public <U, T> T post(String url, U messageBody, Class<T> targetEntity);

    public <U> void update(String url, U messageBody);

    public <T> ResponseEntity<T> delete(String url, Class<T> targetEntity);
}
