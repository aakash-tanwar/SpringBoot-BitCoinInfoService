package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.Price;
import com.tookitaki.cryptocurrency.bitcoin.util.DateUtility;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.BitCoinPriceInfoRequest;
import com.tookitaki.cryptocurrency.bitcoin.web.dto.request.CoinBaseRequest;

@Service
public class BitCoinInfoServiceImpl implements BitCoinInfoService {

	private final CoinBaseClientService coinBaseClientService;
	private final BeanBuilder beanBuilder;

	@Autowired
	public BitCoinInfoServiceImpl(final CoinBaseClientService coinBaseClientService, final BeanBuilder beanBuilder) {
		this.coinBaseClientService = coinBaseClientService;
		this.beanBuilder = beanBuilder;

	}

	@Override
	public BitCoinInfo getPriceTrend(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		CoinBaseRequest coinBaseRequest = beanBuilder.getCoinBaseRequest(bitCoinPriceInfoRequest);
		BitCoinInfo bitCoinInfo = coinBaseClientService.get(coinBaseRequest);
		return bitCoinInfo;
	}

	@Override
	public BitCoinInfo getMovingAveragePriceTrend(BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		CoinBaseRequest coinBaseRequest = beanBuilder.getCoinBaseRequest(bitCoinPriceInfoRequest);
		BitCoinInfo bitCoinInfo = coinBaseClientService.get(coinBaseRequest);
		return getMovingAverage(bitCoinInfo.getPrices(), bitCoinPriceInfoRequest);
	}

	private BitCoinInfo getMovingAverage(List<Price> prices, BitCoinPriceInfoRequest bitCoinPriceInfoRequest) {
		List<Price> result = new ArrayList<Price>();
		int oldIndex = 0;
		Date start = bitCoinPriceInfoRequest.getStartDate();
		Date end = DateUtility.getDateAfterNDays(bitCoinPriceInfoRequest.getEndDate(), 1);
		int window = bitCoinPriceInfoRequest.getMovingWindow();

		List<Price> filteredList = getFilteredList(prices, start,
				DateUtility.getDateAfterNDays(end, bitCoinPriceInfoRequest.getMovingWindow() + 1));

		while (start.before(end)) {
			Double meanPrice = new Double(0);
			for (int i = 0; i < bitCoinPriceInfoRequest.getMovingWindow(); i++) {
				if (i + oldIndex < filteredList.size()) {
					Price price = filteredList.get(i + oldIndex);
					meanPrice += price.getPrice();
				}
			}
			oldIndex++;
			Price price = new Price();
			price.setTime(start);
			price.setPrice(meanPrice / window);
			result.add(price);
			start = DateUtility.getDateAfterNDays(start, 1);
		}

		BitCoinInfo info = new BitCoinInfo();
		info.setPrices(result);
		return info;
	}

	private Price trimDate(Price price) {
		Date date = price.getTime();
		price.setTime(DateUtility.trimDate(date));
		return price;
	}

	private List<Price> getFilteredList(List<Price> prices, Date startDate, Date endDate) {
		Map<Date, Double> groupedOnDatePriceMap = new HashMap<>();
		List<Price> groupedOnDatePriceList = new ArrayList<Price>();
		groupedOnDatePriceMap = prices.stream().map(price -> trimDate(price))
				.collect(Collectors.groupingBy(Price::getTime, Collectors.averagingDouble(Price::getPrice)));

		groupedOnDatePriceMap.entrySet().forEach(entry -> {
			Price price = new Price();
			price.setPrice(entry.getValue());
			price.setTime(entry.getKey());
			groupedOnDatePriceList.add(price);
		});

		List<Price> priceList = groupedOnDatePriceList.stream()
				.filter(coinBasePrice -> coinBasePrice.getTime().after(startDate))
				.filter(coinBasePrice -> coinBasePrice.getTime().before(endDate))
				.sorted((p1, p2) -> p1.getTime().compareTo(p2.getTime())).collect(Collectors.toList());

		return priceList;
	}

}
