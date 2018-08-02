package com.tookitaki.cryptocurrency.bitcoin.domain;

import java.util.Date;

public class Price {

	private Double price;
	private Date time;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Price [price=" + price + ", time=" + time + "]";
	}

}
