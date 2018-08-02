package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.tookitaki.cryptocurrency.bitcoin.domain.Period;
import com.tookitaki.cryptocurrency.bitcoin.util.DateUtility;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.CoinBaseRequest;

@Component
public class BeanBuilder {

	public CoinBaseRequest getCoinBaseRequest(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		CoinBaseRequest baseRequest = new CoinBaseRequest();
		Period period = bitCoinPriceInfoRequest.getPeriod();

		Date startDate = bitCoinPriceInfoRequest.getStartDate();
		Date endDate = bitCoinPriceInfoRequest.getEndDate();

		if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
			switch (period) {

			case YEAR:
				startDate = DateUtility.getDateAfterNDays(new Date(), -365);
				break;
			case MONTH:
				startDate = DateUtility.getDateAfterNDays(new Date(), -30);
				break;
			case WEEK:
				startDate = DateUtility.getDateAfterNDays(new Date(), -7);
				break;
			case DAY:
				startDate = DateUtility.getDateAfterNDays(new Date(), -1);
				break;
			default:
				break;
			}
			endDate = DateUtility.getDateAfterNDays(new Date(), 0);
		}
		baseRequest.setPreriod(period);
		baseRequest.setStartDate(startDate);
		baseRequest.setEndDate(endDate);
		baseRequest.setFilterData(true);
		return baseRequest;
	}
	
	public CoinBaseRequest getCoinBaseRollingRequest(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		CoinBaseRequest baseRequest = new CoinBaseRequest();
		baseRequest.setPreriod(Period.YEAR);
		baseRequest.setStartDate(bitCoinPriceInfoRequest.getStartDate());
		baseRequest.setEndDate(bitCoinPriceInfoRequest.getEndDate());
		baseRequest.setFilterData(false);
		return baseRequest;
	}

	public BitCoinPriceInfoRequest buildPriceRequest(String period, boolean isMovingAvg, String startDate,
			String endDate, int movingWindow) {
		BitCoinPriceInfoRequest bitCoinPriceInfoRequest = new BitCoinPriceInfoRequest();
		bitCoinPriceInfoRequest.setStartDate(Objects.nonNull(startDate) ? DateUtility.convertStringToDate(startDate, "yyyy-MM-dd") : null);
		bitCoinPriceInfoRequest.setEndDate(Objects.nonNull(endDate) ? DateUtility.convertStringToDate(endDate, "yyyy-MM-dd") : null);
		bitCoinPriceInfoRequest.setPeriod(Period.to(period));
		bitCoinPriceInfoRequest.setMovingAverage(isMovingAvg);
		bitCoinPriceInfoRequest.setMovingWindow(movingWindow);
		return bitCoinPriceInfoRequest;
	}

}
