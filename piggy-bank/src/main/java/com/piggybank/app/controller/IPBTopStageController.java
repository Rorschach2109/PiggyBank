package com.piggybank.app.controller;

import java.time.LocalDate;
import java.util.function.Predicate;

public interface IPBTopStageController extends IPBMarkController {
	public default void setDate(LocalDate date) {
		
	}
	
	public void init();
	public <T> void setContent(T item);
	public <T> void setConfirmHandler(Predicate<T> confirmHandler);
	public void setDiscardHandler(Runnable discardHandler);
}
