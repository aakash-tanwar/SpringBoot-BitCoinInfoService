package com.tookitaki.cryptocurrency.bitcoin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.CoinBaseRequest;

@Component
public class UrlBuilder {

	@Autowired
	private Environment environment;

	public String getCoinBaseApiUrl(CoinBaseRequest baseRequest) {
		String baseUrl = environment.getProperty("base.url.coinbase.api");
		StringBuilder coinBaseUrl = new StringBuilder(baseUrl)
				.append("?period=")
				.append(baseRequest.getPreriod().toString().toLowerCase());
		
		return coinBaseUrl.toString();
	}

}
