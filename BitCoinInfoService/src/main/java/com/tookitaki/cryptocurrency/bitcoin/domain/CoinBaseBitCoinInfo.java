package com.tookitaki.cryptocurrency.bitcoin.domain;

import java.util.List;

public class CoinBaseBitCoinInfo {

	private String base;
	private String currency;
	private List<CoinBasePrice> prices;

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<CoinBasePrice> getPrices() {
		return prices;
	}

	public void setPrices(List<CoinBasePrice> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return "CoinBaseBitCoinInfo [base=" + base + ", currency=" + currency + ", prices=" + prices + "]";
	}

}
