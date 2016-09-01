package com.piggybank.server.model.query;

public final class PiggyFutureExpenseNamedQuery {

	public static final String FUTURE_EXPENSES = 
			"SELECT future FROM PiggyFutureExpense future";
	
	public static final String FUTURE_EXPENSES_BY_CATEGORY_NAME = 
			"SELECT future FROM PiggyFutureExpense future JOIN FETCH future.categoryName category WHERE category.name = :categoryName";
	
	private PiggyFutureExpenseNamedQuery() {
		
	}
}
