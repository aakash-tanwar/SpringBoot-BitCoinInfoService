package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.Date;

import com.tookitaki.cryptocurrency.bitcoin.domain.Price;

public interface PricePridictionService {
	
	public Price predict(Date startDate);

}
