package com.tookitaki.cryptocurrency.bitcoin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {

	public static Date convertStringToDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date convertStringToDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long getDaysBetweenTwoDates(Date startDate, Date endDate) {
		return ChronoUnit.DAYS.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public static long getDaysDifferenceFromToday(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate startDate = LocalDate.parse(date, formatter);
		LocalDate todayDate = LocalDate.now();

		Period period = startDate.until(todayDate);
		return period.getDays();
	}

	public static Date getDateAfterNDays(Date date, int n) {
		Date modifiedDate = new Date(date.getTime() + (n * 24 * 3600 * 1000));
		return modifiedDate;
	}

	public static Date getTrimmedDateAfterNDays(Date date, int n) {
		Date parsedDate = trimDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(parsedDate);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Date trimDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		date = cal.getTime();
		return date;
	}

	public static void main(String[] args) {
		String date = "2017-11-26T00:00:00Z";
		System.out.println(convertStringToDate(date, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
	}
}
