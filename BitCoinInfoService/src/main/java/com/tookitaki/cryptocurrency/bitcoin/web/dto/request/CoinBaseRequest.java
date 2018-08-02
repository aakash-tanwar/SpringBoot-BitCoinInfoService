package com.tookitaki.cryptocurrency.bitcoin.web.dto.request;

import java.util.Date;

import com.tookitaki.cryptocurrency.bitcoin.domain.Period;

public class CoinBaseRequest {

	private Period preriod;
	private boolean isFilterData;
	private Date startDate;
	private Date endDate;

	public Period getPreriod() {
		return preriod;
	}

	public void setPreriod(Period preriod) {
		this.preriod = preriod;
	}

	public boolean isFilterData() {
		return isFilterData;
	}

	public void setFilterData(boolean isFilterData) {
		this.isFilterData = isFilterData;
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

	@Override
	public String toString() {
		return "CoinBaseRequest [preriod=" + preriod + ", isFilterData=" + isFilterData + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

}
