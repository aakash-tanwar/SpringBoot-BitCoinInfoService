package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.tookitaki.cryptocurrency.bitcoin.domain.BitCoinInfo;
import com.tookitaki.cryptocurrency.bitcoin.domain.Price;
import com.tookitaki.cryptocurrency.bitcoin.util.DateUtility;

@Component
public class RollingAverageUtility {
	
	public BitCoinInfo getMovingAverage(List<Price> prices, Date startDate, Date endDate, int window) {
		List<Price> result = new ArrayList<Price>();
		int oldIndex = 0;
		Date start = new Date(startDate.getTime());
		Date end = DateUtility.getDateAfterNDays(endDate, 1);
		
		List<Price> filteredList = getFilteredList(prices, start,
				DateUtility.getDateAfterNDays(end, window + 1));

		while (start.before(end)) {
			Double meanPrice = new Double(0);
			for (int i = 0; i < window; i++) {
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
