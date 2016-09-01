package com.piggybank.server.model.query;

public final class PiggyIncomeNamedQuery {

	public static final String INCOMES = 
			"SELECT income FROM PiggyIncome income";
	public static final String INCOMES_BY_MONTH_YEAR = 
			"SELECT income FROM PiggyIncome income WHERE income.month = :month AND income.year = :year";
	
	private PiggyIncomeNamedQuery() {
		
	}
}
