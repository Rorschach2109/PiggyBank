package com.piggybank.server.model.query;

public final class PiggySavingNamedQuery {

	public final static String SAVINGS_BY_YEAR = 
			"SELECT saving FROM PiggySaving saving WHERE saving.year = :year";
	public final static String SAVING_BY_MONTH_YEAR = 
			"SELECT saving FROM PiggySaving saving WHERE saving.month = :month AND saving.year = :year";
	public final static String SAVINGS_TILL_MONTH_YEAR = 
			"SELECT saving FROM PiggySaving saving WHERE (saving.month < :month AND saving.year <= :year) OR saving.year < :year";
	
	private PiggySavingNamedQuery() {
		
	}
}
