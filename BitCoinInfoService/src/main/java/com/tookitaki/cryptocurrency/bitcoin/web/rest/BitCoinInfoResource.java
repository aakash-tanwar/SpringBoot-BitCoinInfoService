package com.tookitaki.cryptocurrency.bitcoin.web.rest;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.CustomErrorType;
import com.tookitaki.cryptocurrency.bitcoin.domain.RecommendedDecision;
import com.tookitaki.cryptocurrency.bitcoin.service.BitCoinInfoService;
import com.tookitaki.cryptocurrency.bitcoin.service.ValidationService;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.response.BitCoinDecisionResponse;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.response.BitCoinInfoResponse;

@RestController
@RequestMapping(value = "/bitcoin")
public class BitCoinInfoResource {

	public static final Logger LOGGER = LoggerFactory.getLogger(BitCoinInfoResource.class);

	private final BitCoinInfoService bitCoinInfoService;
	private final ValidationService validationService;

	@Autowired
	public BitCoinInfoResource(final BitCoinInfoService bitCoinInfoService, final ValidationService validationService) {
		this.bitCoinInfoService = bitCoinInfoService;
		this.validationService = validationService;
	}

	@GetMapping("/price")
	public ResponseEntity<?> getPriceTrend(@RequestParam(name = "period") String period,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate) {
		LOGGER.info("Received request to fetch price trend with Period : {}, startDate : {}, endDate : {}", period,
				startDate, endDate);
		BitCoinPriceInfoRequest bitCoinPriceInfoRequest = BitCoinPriceInfoRequest.toDto(period, startDate, endDate, 0);
		CustomErrorType errorMap = validationService.validatePriceRequest(bitCoinPriceInfoRequest);
		if (errorMap.isError()) {
			return new ResponseEntity<Object>(errorMap, HttpStatus.BAD_REQUEST);
		}
		BitCoinInfo bitCoinInfo = bitCoinInfoService.getPriceTrend(bitCoinPriceInfoRequest);

		if (Objects.isNull(bitCoinInfo) || CollectionUtils.isEmpty(bitCoinInfo.getPrices())) {
			LOGGER.error("Price Trend found empty for request "+bitCoinPriceInfoRequest);
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		BitCoinInfoResponse bitCoinInfoResponse = BitCoinInfoResponse.toDto(bitCoinInfo);
		LOGGER.info("Price trends :  " + bitCoinInfoResponse);
		return new ResponseEntity<BitCoinInfoResponse>(bitCoinInfoResponse, HttpStatus.OK);
	}

	@GetMapping("/rolling-price")
	public ResponseEntity<?> getRollingTPriceTrend(@RequestParam(name = "startDate", required = true) String startDate,
			@RequestParam(name = "endDate", required = true) String endDate,
			@RequestParam(name = "window", required = true) int movingWindow) {

		LOGGER.info("Received request to fetch price trend with window : {}, startDate : {}, endDate : {}",
				movingWindow, startDate, endDate);

		BitCoinPriceInfoRequest bitCoinPriceInfoRequest = BitCoinPriceInfoRequest.toDto(null, startDate, endDate,
				movingWindow);
		CustomErrorType errorMap = validationService.validateRollingPriceRequest(bitCoinPriceInfoRequest);
		if (errorMap.isError()) {
			return new ResponseEntity<Object>(errorMap, HttpStatus.BAD_REQUEST);
		}
		BitCoinInfo bitCoinInfo = bitCoinInfoService.getMovingAveragePriceTrend(bitCoinPriceInfoRequest);

		if (Objects.isNull(bitCoinInfo) || CollectionUtils.isEmpty(bitCoinInfo.getPrices())) {
			LOGGER.error("Moving Price Trend found empty for request "+bitCoinPriceInfoRequest);
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		BitCoinInfoResponse bitCoinInfoResponse = BitCoinInfoResponse.toDto(bitCoinInfo);
		LOGGER.info("Moving price trends :  " + bitCoinInfoResponse);
		return new ResponseEntity<BitCoinInfoResponse>(bitCoinInfoResponse, HttpStatus.OK);
	}

	@GetMapping("/pridicted-price")
	public ResponseEntity<?> getPredictedPriceTrend(@RequestParam(name = "days", required = true) int days) {
		LOGGER.info("Received request to fetch predicted price trend for window : {}", days);

		CustomErrorType errorMap = validationService.validatePredictedPriceRequest(days);
		if (errorMap.isError()) {
			return new ResponseEntity<Object>(errorMap, HttpStatus.BAD_REQUEST);
		}
		BitCoinInfo bitCoinInfo = bitCoinInfoService.getPridictedPriceTrend(days);

		if (Objects.isNull(bitCoinInfo) || CollectionUtils.isEmpty((bitCoinInfo.getPrices()))) {
			LOGGER.error("Prdicted Price Trend found empty.");
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		BitCoinInfoResponse bitCoinInfoResponse = BitCoinInfoResponse.toDto(bitCoinInfo);
		LOGGER.info("Predicted price trends :  " + bitCoinInfoResponse);
		return new ResponseEntity<BitCoinInfoResponse>(bitCoinInfoResponse, HttpStatus.OK);
	}

	@GetMapping("/trading-decision")
	public ResponseEntity<?> getTradingDecision() {
		LOGGER.info("Received request to get the decision ");
		List<RecommendedDecision> recommendedDecisions = bitCoinInfoService.getTradingDecision();

		if (CollectionUtils.isEmpty((recommendedDecisions))) {
			LOGGER.error("Recommendation decision found empty.");
			return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
		}
		BitCoinDecisionResponse bitCoinDecisionResponse = BitCoinDecisionResponse.toDto(recommendedDecisions);

		LOGGER.info("Decision computed : " + bitCoinDecisionResponse);
		return new ResponseEntity<BitCoinDecisionResponse>(bitCoinDecisionResponse, HttpStatus.OK);
	}

}