package com.piggybank.app;

import com.piggybank.app.controller.PiggyBankAppController;
import com.piggybank.app.remote.PiggyRemoteProxy;

import javafx.application.Application;
import javafx.stage.Stage;

public class PiggyBankApp extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (PiggyRemoteProxy.initialize()) {
			new PiggyBankAppController(primaryStage).start();
		} else {
			System.out.println("JNDI Error");
		}
	}

}
