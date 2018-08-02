package com.tookitaki.cryptocurrency.bitcoin.web.rest;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.CustomErrorType;
import com.tookitaki.cryptocurrency.bitcoin.service.BeanBuilder;
import com.tookitaki.cryptocurrency.bitcoin.service.BitCoinInfoService;
import com.tookitaki.cryptocurrency.bitcoin.service.ValidationService;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.response.BitCoinInfoResponse;

@RestController
@RequestMapping(value = "/bitcoin")
public class BitCoinInfoResource {

	public static final Logger LOGGER = LoggerFactory.getLogger(BitCoinInfoResource.class);

	private final BitCoinInfoService bitCoinInfoService;
	private final ValidationService validationService;
	private final BeanBuilder beanBuilder;

	@Autowired
	public BitCoinInfoResource(final BitCoinInfoService bitCoinInfoService, final ValidationService validationService,
			final BeanBuilder beanBuilder) {
		this.bitCoinInfoService = bitCoinInfoService;
		this.validationService = validationService;
		this.beanBuilder = beanBuilder;
	}

	@GetMapping("/price")
	public ResponseEntity<?> getPriceTrend(@RequestParam(name = "period") String period,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate) {
		LOGGER.info("Received request to fetch price trend with Period : {}, startDate : {}, endDate : {}", period,
				startDate, endDate);
		BitCoinPriceInfoRequest bitCoinPriceInfoRequest = beanBuilder.buildPriceRequest(period, false, startDate,
				endDate, 0);
		CustomErrorType errorMap = validationService.validatePriceRequest(bitCoinPriceInfoRequest);
		if (errorMap.isError()) {
			return new ResponseEntity<Object>(errorMap, HttpStatus.BAD_REQUEST);
		}
		BitCoinInfo bitCoinInfo = bitCoinInfoService.getPriceTrend(bitCoinPriceInfoRequest);

		if (Objects.isNull(bitCoinInfo) || Objects.isNull(bitCoinInfo.getPrices())) {
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		BitCoinInfoResponse bitCoinInfoResponse = BitCoinInfoResponse.toDto(bitCoinInfo);
		return new ResponseEntity<BitCoinInfoResponse>(bitCoinInfoResponse, HttpStatus.OK);
	}

	@GetMapping("/rollingprice")
	public ResponseEntity<?> getRollingTPriceTrend(@RequestParam(name = "startDate", required = true) String startDate,
			@RequestParam(name = "endDate", required = true) String endDate,
			@RequestParam(name = "window", required = true) int movingWindow) {

		LOGGER.info("Received request to fetch price trend with window : {}, startDate : {}, endDate : {}",
				movingWindow, startDate, endDate);

		BitCoinPriceInfoRequest bitCoinPriceInfoRequest = beanBuilder.buildPriceRequest(null, true, startDate, endDate,
				movingWindow);
		CustomErrorType errorMap = validationService.validateRollingPriceRequest(bitCoinPriceInfoRequest);
		if (errorMap.isError()) {
			return new ResponseEntity<Object>(errorMap, HttpStatus.BAD_REQUEST);
		}
		BitCoinInfo bitCoinInfo = bitCoinInfoService.getMovingAveragePriceTrend(bitCoinPriceInfoRequest);

		if (Objects.isNull(bitCoinInfo) || Objects.isNull(bitCoinInfo.getPrices())) {
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		BitCoinInfoResponse bitCoinInfoResponse = BitCoinInfoResponse.toDto(bitCoinInfo);
		return new ResponseEntity<BitCoinInfoResponse>(bitCoinInfoResponse, HttpStatus.OK);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handleTodoNotFound() {
	}

}