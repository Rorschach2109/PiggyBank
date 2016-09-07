package com.piggybank.app.controller;

public interface IPBMarkController {
	public default void init() {
		throw new UnsupportedOperationException();
	}
}
