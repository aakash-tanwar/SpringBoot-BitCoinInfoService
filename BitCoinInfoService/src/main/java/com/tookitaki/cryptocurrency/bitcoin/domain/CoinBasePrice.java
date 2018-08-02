package com.tookitaki.cryptocurrency.bitcoin.domain;

public class CoinBasePrice {

	private String price;
	private String time;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Price [price=" + price + ", time=" + time + "]";
	}

}
