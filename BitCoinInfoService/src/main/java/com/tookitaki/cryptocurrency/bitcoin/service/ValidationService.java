	package com.tookitaki.cryptocurrency.bitcoin.service;

import com.tookitaki.cryptocurrency.bitcoin.domain.CustomErrorType;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;

public interface ValidationService {
	
	public CustomErrorType validatePriceRequest(BitCoinPriceInfoRequest bitCoinPriceInfoRequest);
	public CustomErrorType validateRollingPriceRequest(BitCoinPriceInfoRequest bitCoinPriceInfoRequest);

}
