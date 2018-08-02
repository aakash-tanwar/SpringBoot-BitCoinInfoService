package com.tookitaki.cryptocurrency.bitcoin.web.dto.response;

import com.tookitaki.cryptocurrency.bitcoin.domain.CoinBaseBitCoinInfo;

public class CoinBaseBitCoinInfoResponse {

	private CoinBaseBitCoinInfo data;

	public CoinBaseBitCoinInfo getData() {
		return data;
	}

	public void setData(CoinBaseBitCoinInfo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CoinBaseBitCoinInfoResponse [data=" + data + "]";
	}

}
