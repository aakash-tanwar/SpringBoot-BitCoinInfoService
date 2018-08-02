package com.tookitaki.cryptocurrency.bitcoin.domain;

import org.springframework.util.StringUtils;

public enum Period {

	YEAR("year"), MONTH("month"), WEEK("week"), DAY("day");

	private final String unit;

	private Period(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public static Period to(String period) {
		Period periodValue = Period.YEAR;
		if (!StringUtils.isEmpty(period)) {
			if (period.equalsIgnoreCase("YEAR")) {
				periodValue = Period.YEAR;
			} else if (period.equalsIgnoreCase("MONTH")) {
				periodValue = Period.MONTH;
			} else if (period.equalsIgnoreCase("WEEK")) {
				periodValue = Period.WEEK;
			} else if (period.equalsIgnoreCase("DAY")) {
				periodValue = Period.DAY;
			}
		}
		return periodValue;
	}
}
