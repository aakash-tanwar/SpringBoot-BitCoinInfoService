package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tookitaki.cryptocurrency.bitcoin.domain.CustomErrorType;
import com.tookitaki.cryptocurrency.bitcoin.domain.Period;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;

@Service
public class ValidationServiceImpl implements ValidationService {

	@Override
	public CustomErrorType validatePriceRequest(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		CustomErrorType errorObject = new CustomErrorType();
		Map<String, String> map = new HashMap<>();
		Period period = bitCoinPriceInfoRequest.getPeriod();
		if (StringUtils.isEmpty(period)) {
			map.put("Period", "Set correct period Type(YEAR, MONTH, DAY)");
		}

		if (!StringUtils.isEmpty(period) && period.equals(Period.DAY)) {
			if (Objects.isNull(bitCoinPriceInfoRequest.getStartDate())) {
				map.put("startDate", "StartDate cann't be null (yyyy-MM-dd)");
			}

			if (Objects.isNull(bitCoinPriceInfoRequest.getEndDate())) {
				map.put("endDate", "EndDate cann't be null (yyyy-MM-dd)");
			}
		}
		errorObject.setErrorMessage(map);
		return errorObject;
	}

	@Override
	public CustomErrorType validateRollingPriceRequest(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		CustomErrorType errorObject = new CustomErrorType();
		Map<String, String> map = new HashMap<>();

		if (Objects.isNull(bitCoinPriceInfoRequest.getStartDate())) {
			map.put("startDate", "StartDate cann't be null (yyyy-MM-dd)");
		}
		if (Objects.isNull(bitCoinPriceInfoRequest.getEndDate())) {
			map.put("endDate", "EndDate cann't be null (yyyy-MM-dd)");
		}
		if (bitCoinPriceInfoRequest.getMovingWindow() <= 0) {
			map.put("moving window", "Moving window shoild be greater than 0.");
		}
		errorObject.setErrorMessage(map);
		return errorObject;
	}

	@Override
	public CustomErrorType validatePredictedPriceRequest(int days) {
		CustomErrorType errorObject = new CustomErrorType();
		Map<String, String> map = new HashMap<>();

		if (days <= 7 || days >= 365) {
			map.put("days", "days should be between [7,365]");
		}

		errorObject.setErrorMessage(map);
		return errorObject;
	}

}
