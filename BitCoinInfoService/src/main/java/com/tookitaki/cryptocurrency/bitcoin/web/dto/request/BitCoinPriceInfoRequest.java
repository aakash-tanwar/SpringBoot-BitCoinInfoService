package com.tookitaki.cryptocurrency.bitcoin.web.dto.request;

import java.util.Date;

import com.tookitaki.cryptocurrency.bitcoin.domain.Period;

public class BitCoinPriceInfoRequest {

	private Period period;
	private boolean isMovingAverage;
	private Date startDate;
	private Date endDate;
	private int movingWindow;

	public BitCoinPriceInfoRequest() {
	}

	public BitCoinPriceInfoRequest(Period period, boolean isMovingAverage, 
			Date startDate, Date endDate, int movingWindow) {
		super();
		this.period = period;
		this.isMovingAverage = isMovingAverage;
		this.startDate = startDate;
		this.endDate = endDate;
		this.movingWindow = movingWindow;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public boolean isMovingAverage() {
		return isMovingAverage;
	}

	public void setMovingAverage(boolean isMovingAverage) {
		this.isMovingAverage = isMovingAverage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getMovingWindow() {
		return movingWindow;
	}

	public void setMovingWindow(int movingWindow) {
		this.movingWindow = movingWindow;
	}

	@Override
	public String toString() {
		return "BitCoinPriceInfoRequest [period=" + period + ", isMovingAverage=" + isMovingAverage + ", startDate="
				+ startDate + ", endDate=" + endDate + ", movingWindow=" + movingWindow + "]";
	}

}
