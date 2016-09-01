package com.piggybank.server.model.query;

public final class PiggyExpenseCategoryNameNamedQuery {

	public final static String CATEGORY_NAMES = 
			"SELECT categoryName FROM PiggyExpenseCategoryName categoryName";
	public final static String CATEGORY_NAME_BY_NAME = 
			"SELECT categoryName FROM PiggyExpenseCategoryName categoryName WHERE categoryName.name = :categoryName";
	public final static String CATEGORY_NAMES_ACTIVE = 
			"SELECT categoryName FROM PiggyExpenseCategoryName categoryName WHERE categoryName.active = 'T' ORDER BY categoryName.name";
	
	private PiggyExpenseCategoryNameNamedQuery() {
		
	}
}
