package com.piggybank.app;

import com.piggybank.app.controller.PiggyBankAppController;
import com.piggybank.app.remote.PiggyRemoteProxy;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PiggyBankApp extends Application {
	
	private final String iconPath = "images/Icon.png";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (PiggyRemoteProxy.initialize()) {
			setApplicationIcon(primaryStage);
			new PiggyBankAppController(primaryStage).start();
		} else {
			System.out.println("JNDI Error");
		}
	}
	
	private void setApplicationIcon(Stage primaryStage) {
		Image iconImage = new Image(getClass().getClassLoader()
				.getResourceAsStream(this.iconPath));
		primaryStage.getIcons().add(iconImage);
	}

}
