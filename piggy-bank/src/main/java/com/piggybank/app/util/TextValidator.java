package com.piggybank.app.util;

public final class TextValidator {

	public static boolean containsDoubleNumber(String text) {
		return text.matches("[0-9]+(\\.[0-9]+)?"); 
	}
	
	public static boolean containsOnlyNumber(String text) {
		return text.matches("[0-9]+"); 
	}
	
	private TextValidator() {
		
	}
}
