package com.tookitaki.cryptocurrency.bitcoin.domain;

import java.util.List;

public class BitCoinInfo {

	private List<Price> prices;
	
	public List<Price> getPrices() {
		return prices;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return "BitCoinInfo [prices=" + prices + "]";
	}

}
