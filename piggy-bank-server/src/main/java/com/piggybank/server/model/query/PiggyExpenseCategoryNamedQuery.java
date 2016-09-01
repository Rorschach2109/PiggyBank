package com.piggybank.server.model.query;

public final class PiggyExpenseCategoryNamedQuery {

	public final static String EXPENSE_CATEGORIES = 
			"SELECT category FROM PiggyExpenseCategory category";
	
	public final static String EXPENSE_CATEGORIES_BY_MONTH_YEAR = 
			"SELECT category FROM PiggyExpenseCategory category WHERE category.month = :month AND category.year = :year";
	
	public final static String EXPENSE_CATEGORIES_BY_MONTH_YEAR_NAME = 
			"SELECT category FROM PiggyExpenseCategory category JOIN FETCH category.name name WHERE category.month = :month AND category.year = :year AND name.name = :name";
	
	private PiggyExpenseCategoryNamedQuery() {
		
	}
}
