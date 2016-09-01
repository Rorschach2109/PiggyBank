package com.piggybank.app.util;

import java.io.IOException;
import java.net.URL;

import com.piggybank.app.controller.IPBMarkController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public final class LayoutManager {

	private IPBMarkController currentController;
	
	public IPBMarkController getCurrentController() {
		return this.currentController;
	}
	
	public Scene loadScene(String sceneResourcePath) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		Scene scene = null;
		
		try {
			Pane pane = loadLayout(fxmlLoader, sceneResourcePath);
			scene = new Scene(pane);
			setCurrentController(fxmlLoader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return scene;
	}
	
	private Pane loadLayout(FXMLLoader fxmlLoader, String sceneResourcePath) throws IOException {
		URL resourcePath = getClass().getClassLoader().getResource(sceneResourcePath);
		fxmlLoader.setLocation(resourcePath); 

		return fxmlLoader.load();
	}
	
	private void setCurrentController(FXMLLoader fxmlLoader) {
		this.currentController = fxmlLoader.getController();
	}
}
