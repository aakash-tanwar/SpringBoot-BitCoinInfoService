package com.tookitaki.cryptocurrency.bitcoin.web.dto.response;

import java.util.List;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.Price;

public class BitCoinInfoResponse {

	private List<Price> data;

	public static BitCoinInfoResponse toDto(BitCoinInfo bitCoinInfo) {
		BitCoinInfoResponse response = new BitCoinInfoResponse();
		response.setData(bitCoinInfo.getPrices());
		return response;
	}

	public List<Price> getData() {
		return data;
	}

	public void setData(List<Price> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BitCoinInfo [data=" + data + "]";
	}
}
