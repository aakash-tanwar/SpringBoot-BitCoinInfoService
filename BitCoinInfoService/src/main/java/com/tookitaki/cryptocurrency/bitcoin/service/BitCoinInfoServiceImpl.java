package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tookitaki.cryptocurrency.bitcoin.common.DecisionStatus;
import com.tookitaki.cryptocurrency.bitcoin.common.DecisionStrategy;
import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.Period;
import com.tookitaki.cryptocurrency.bitcoin.domain.Price;
import com.tookitaki.cryptocurrency.bitcoin.domain.RecommendedDecision;
import com.tookitaki.cryptocurrency.bitcoin.util.DateUtility;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.CoinBaseRequest;

@Service
public class BitCoinInfoServiceImpl implements BitCoinInfoService {

	public static final Logger LOGGER = LoggerFactory.getLogger(BitCoinInfoServiceImpl.class);

	private final CoinBaseClientService coinBaseClientService;
	private final PricePridictionService pricePridictionService;
	private final RollingAverageUtility rollingAverageUtility;
	private final Environment environment;
	private final BeanBuilder beanBuilder;

	@Autowired
	public BitCoinInfoServiceImpl(final CoinBaseClientService coinBaseClientService,
			final PricePridictionService pricePridictionService, final RollingAverageUtility rollingAverageUtility,
			final Environment environment, final BeanBuilder beanBuilder) {
		this.coinBaseClientService = coinBaseClientService;
		this.pricePridictionService = pricePridictionService;
		this.rollingAverageUtility = rollingAverageUtility;
		this.environment = environment;
		this.beanBuilder = beanBuilder;

	}

	@Override
	public BitCoinInfo getPriceTrend(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		LOGGER.debug("Fetching price trends for request " + bitCoinPriceInfoRequest);
		CoinBaseRequest coinBaseRequest = beanBuilder.getCoinBaseRequest(bitCoinPriceInfoRequest);
		BitCoinInfo bitCoinInfo = coinBaseClientService.get(coinBaseRequest);
		LOGGER.debug("Bit Coin info fethced is " + bitCoinInfo);
		return bitCoinInfo;
	}

	@Override
	public BitCoinInfo getMovingAveragePriceTrend(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		LOGGER.debug("Fetching Moving price trends for request " + bitCoinPriceInfoRequest);
		CoinBaseRequest coinBaseRequest = CoinBaseRequest.from(bitCoinPriceInfoRequest);
		BitCoinInfo bitCoinInfo = coinBaseClientService.get(coinBaseRequest);
		BitCoinInfo movingBitCoinInfo = rollingAverageUtility.getMovingAverage(bitCoinInfo.getPrices(),
				bitCoinPriceInfoRequest.getStartDate(), bitCoinPriceInfoRequest.getEndDate(),
				bitCoinPriceInfoRequest.getMovingWindow());
		LOGGER.debug("Moving price trend is " + movingBitCoinInfo);
		return bitCoinInfo;
	}

	@Override
	public BitCoinInfo getPridictedPriceTrend(int days) {
		LOGGER.debug("Fetching predicted price trends for days " + days);
		BitCoinInfo bitCoinInfo = new BitCoinInfo();
		List<Price> prices = new ArrayList<>();
		Date startDate = DateUtility.getTrimmedDateAfterNDays(new Date(), 0);
		IntStream.range(1, days + 1).forEachOrdered(day -> {
			Price price = pricePridictionService.predict(DateUtility.getTrimmedDateAfterNDays(startDate, day));
			prices.add(price);
		});
		Collections.sort(prices, (p1, p2) -> p1.getTime().compareTo(p2.getTime()));
		bitCoinInfo.setPrices(prices);
		LOGGER.debug("Predicted price trends for days " + bitCoinInfo);
		return bitCoinInfo;
	}

	@Override
	public List<RecommendedDecision> getTradingDecision() {
		LOGGER.debug("Fetching trading decisions for date " + new Date());
		CoinBaseRequest baseRequest = getCoinBaseRequest();
		BitCoinInfo bitCoinInfo = coinBaseClientService.get(baseRequest);
		Price pridictedPriceData = pricePridictionService.predict(DateUtility.trimDate(new Date()));
		List<RecommendedDecision> decisions = getRecommendedDecision(bitCoinInfo, pridictedPriceData);
		LOGGER.debug("Cooked Trading decisions for date " + new Date() + " is  " + decisions);
		return decisions;
	}

	private List<RecommendedDecision> getRecommendedDecision(BitCoinInfo bitCoinInfo, Price pridictedPriceData) {
		List<RecommendedDecision> recommendedDecisions = new ArrayList<>();

		if (Objects.nonNull(bitCoinInfo) && !CollectionUtils.isEmpty(bitCoinInfo.getPrices())
				&& Objects.nonNull(pridictedPriceData)) {
			Price todayPriceData = null;
			todayPriceData = bitCoinInfo.getPrices().get(0);
			if (todayPriceData.getTime().equals(pridictedPriceData.getTime())) {
				double todayPrice = todayPriceData.getPrice();
				double pridictedPrice = pridictedPriceData.getPrice();
				LOGGER.info("Today price : " + todayPrice);
				LOGGER.info("Forcasted price : " + pridictedPrice);
				recommendedDecisions.add(getOptimisticStrategy(pridictedPrice, todayPrice));
				recommendedDecisions.add(getSafeDecision(pridictedPrice, todayPrice));
			}
		}
		return recommendedDecisions;
	}

	private CoinBaseRequest getCoinBaseRequest() {
		Date startDate = DateUtility.getTrimmedDateAfterNDays(new Date(), -1);
		Date endDate = DateUtility.getTrimmedDateAfterNDays(new Date(), 1);
		CoinBaseRequest baseRequest = new CoinBaseRequest(Period.YEAR, true, startDate, endDate);
		return baseRequest;
	}

	private RecommendedDecision getOptimisticStrategy(double pridictedPrice, double todayPrice) {
		RecommendedDecision optimisticDecision = new RecommendedDecision();
		optimisticDecision.setDecisionStrategy(DecisionStrategy.OPTIMISTIC_STRATEGY);
		optimisticDecision.setDecisionStatus(pridictedPrice > todayPrice ? DecisionStatus.BUY : DecisionStatus.HOLD);
		return optimisticDecision;
	}

	private RecommendedDecision getSafeDecision(double pridictedPrice, double todayPrice) {
		RecommendedDecision safeDecision = new RecommendedDecision();
		float buyThreshold = Float.parseFloat(environment.getProperty("pridicted.price.loss.threshold"));
		float holdThreshold = Float.parseFloat(environment.getProperty("pridicted.price.gain.threshold"));

		if (pridictedPrice == (buyThreshold * todayPrice) + todayPrice) {
			safeDecision.setDecisionStatus(DecisionStatus.BUY);
		} else if (pridictedPrice - todayPrice >= (holdThreshold * todayPrice)) {
			safeDecision.setDecisionStatus(DecisionStatus.HOLD);
		} else {
			safeDecision.setDecisionStatus(DecisionStatus.SELL);
		}
		safeDecision.setDecisionStrategy(DecisionStrategy.SAFE_STRATEGY);
		return safeDecision;
	}

}
