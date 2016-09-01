package com.piggybank.app.util;

import java.time.LocalDate;
import java.time.YearMonth;

public final class LocalDateFormatter {

	public static String getMonthYearStringFromDate(LocalDate date) {
		String currentMonthName = date.getMonth().toString();
		String currentYearString = String.valueOf(date.getYear());
		return new StringBuilder()
				.append(currentMonthName)
				.append(", ")
				.append(currentYearString)
				.toString();
	}
	
	public static String getMonthYearStringFromYearMonth(YearMonth yearMonth) {
		String currentMonthName = yearMonth.getMonth().toString();
		String currentYearString = String.valueOf(yearMonth.getYear());
		return new StringBuilder()
				.append(currentMonthName)
				.append(", ")
				.append(currentYearString)
				.toString();
	}
		
	private LocalDateFormatter() {
		
	}
}
