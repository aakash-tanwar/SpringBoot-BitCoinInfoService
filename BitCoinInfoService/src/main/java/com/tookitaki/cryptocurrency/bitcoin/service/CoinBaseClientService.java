package com.tookitaki.cryptocurrency.bitcoin.service;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.CoinBaseRequest;

public interface CoinBaseClientService {

	public BitCoinInfo get(CoinBaseRequest coinBaseRequest);

}
