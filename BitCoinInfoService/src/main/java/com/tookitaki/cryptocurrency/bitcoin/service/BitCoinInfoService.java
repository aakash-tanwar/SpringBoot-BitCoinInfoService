package com.tookitaki.cryptocurrency.bitcoin.service;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;

public interface BitCoinInfoService {
	
	public BitCoinInfo getPriceTrend(BitCoinPriceInfoRequest bitCoinPriceInfoRequest);
	
	public BitCoinInfo getMovingAveragePriceTrend(BitCoinPriceInfoRequest bitCoinPriceInfoRequest);

}
