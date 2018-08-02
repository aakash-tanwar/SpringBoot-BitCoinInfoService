package com.tookitaki.cryptocurrency.bitcoin.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.tookitaki.cryptocurrency.bitcoin.domain.Price;

@Service
public class LinearRegressionPricePridictionServiceImpl implements PricePridictionService {

	private final Environment environment;

	@Autowired
	public LinearRegressionPricePridictionServiceImpl(final Environment environment) {
		this.environment = environment;
	}

	@Override
	public Price predict(Date date) {
		double priceValue = pridictPrice(date);
		Price price = new Price();
		price.setTime(date);
		price.setPrice(priceValue);
		return price;
	}

	private Double pridictPrice(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		double intercept = Double.parseDouble(environment.getProperty("lr.intercept.weight"));
		double yearWeight = getYearWeight(calendar.get(Calendar.YEAR));
		double monthWeight = getMonthWeight(calendar.get(Calendar.MONTH) + 1);
		double dayOfWeekWeight = getDayWeight(calendar.get(Calendar.DAY_OF_WEEK));
		double weekOfYearWeight = getweekOfYearWeight(calendar.get(Calendar.WEEK_OF_YEAR));
		
		Double pridictedPriceValue = intercept + yearWeight + monthWeight + 
				dayOfWeekWeight + weekOfYearWeight;
		
		return pridictedPriceValue;
	}

	private double getweekOfYearWeight(int weekOfYear) {
		double weight = 0;
		if (weekOfYear > 1 && weekOfYear <= 52) {
			weight = Double.parseDouble(environment.getProperty("lr.weight.week.of.year."+weekOfYear));
		}
		return weight;
	}

	private double getDayWeight(int dayOfWeek) {
		double weight = 0;
		if (dayOfWeek >= 1 && dayOfWeek <= 7) {
			weight = Double.parseDouble(environment.getProperty("lr.weight.day.of.week."+dayOfWeek));
		}
		return weight;
	}

	private double getMonthWeight(int monthWeight) {
		double weight = 0;
		if (monthWeight >= 1 && monthWeight <= 12) {
			weight = Double.parseDouble(environment.getProperty("lr.weight.month.of.year."+monthWeight));
		}
		return weight;
	}

	private double getYearWeight(int Year) {
		double weight = 0;
		if (Year == 2018 || Year == 2019) {
			weight = Double.parseDouble(environment.getProperty("lr.year.weight."+Year));
		}
		return weight;
	}

}
