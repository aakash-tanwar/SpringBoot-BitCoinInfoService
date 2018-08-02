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
				startDate = DateUtility.getTrimmedDateAfterNDays(new Date(), -365);
				break;
			case MONTH:
				startDate = DateUtility.getTrimmedDateAfterNDays(new Date(), -30);
				break;
			case WEEK:
				startDate = DateUtility.getTrimmedDateAfterNDays(new Date(), -7);
				break;
			case DAY:
				startDate = DateUtility.getTrimmedDateAfterNDays(new Date(), -1);
				break;
			default:
				break;
			}
			endDate = DateUtility.getTrimmedDateAfterNDays(new Date(), 0);
		}
		baseRequest.setPreriod(Period.YEAR);
		baseRequest.setStartDate(startDate);
		baseRequest.setEndDate(endDate);
		baseRequest.setFilterData(true);
		return baseRequest;
	}

}
