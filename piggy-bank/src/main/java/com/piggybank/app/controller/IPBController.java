package com.piggybank.app.controller;

public interface IPBController extends IPBMarkController {
	public default void setMainWindowController(PiggyMainWindowController mainWindowController) {
	}
	
	public default boolean onClose() {
		return true;
	}
}
