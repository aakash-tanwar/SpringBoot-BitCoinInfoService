package com.tookitaki.cryptocurrency.bitcoin.web.dto.request;

import java.util.Date;
import java.util.Objects;

import com.tookitaki.cryptocurrency.bitcoin.domain.Period;
import com.tookitaki.cryptocurrency.bitcoin.util.DateUtility;

public class BitCoinPriceInfoRequest {

	private Period period;
	private Date startDate;
	private Date endDate;
	private int movingWindow;

	public BitCoinPriceInfoRequest() {
	}

	public BitCoinPriceInfoRequest(Period period, Date startDate, Date endDate, int movingWindow) {
		super();
		this.period = period;
		this.startDate = startDate;
		this.endDate = endDate;
		this.movingWindow = movingWindow;
	}

	public static BitCoinPriceInfoRequest toDto(String period, String startDate, String endDate,
			int movingWindow) {
		BitCoinPriceInfoRequest bitCoinPriceInfoRequest = new BitCoinPriceInfoRequest();
		bitCoinPriceInfoRequest.setStartDate(
				Objects.nonNull(startDate) ? DateUtility.convertStringToDate(startDate, "yyyy-MM-dd") : null);
		bitCoinPriceInfoRequest
				.setEndDate(Objects.nonNull(endDate) ? DateUtility.convertStringToDate(endDate, "yyyy-MM-dd") : null);
		bitCoinPriceInfoRequest.setPeriod(Period.to(period));
		bitCoinPriceInfoRequest.setMovingWindow(movingWindow);
		return bitCoinPriceInfoRequest;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
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
		return "BitCoinPriceInfoRequest [period=" + period + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", movingWindow=" + movingWindow + "]";
	}

}
