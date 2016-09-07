package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;

import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.server.model.PiggySaving;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PiggySavingsController implements IPBController {

	@FXML
	private Label headerLabel;
	@FXML
	private Label totalSavingsLabel;
	@FXML
	private Label yearlySavingsLabel;
	@FXML
	private AnchorPane savingsAnchor;
	
	private LineChart<String, Number> savingsChart;
	
	private Year currentYear;
	private List<PiggySaving> currentYearSavings;
	private final PiggyBankIncomes piggyIncomesRemote;
	private final String headerText;
	
	{
		this.currentYear = Year.now();
		this.piggyIncomesRemote = new PiggyBankIncomes();
		this.headerText = "Savings report ";
	}
	
	@Override
	public void init() {
		configureChart();
		setTotalSavingsLabel();
		update();
	}
	
	private void update() {
		setContentData();
		setHeaderLabels();
		setCurrentYearSavingsLabel();
		fillChart();
	}
	
	private void setContentData() {
		this.currentYearSavings = this.piggyIncomesRemote.getSavingsByYear(this.currentYear);
		Collections.sort(this.currentYearSavings);
	}
	
	private void configureChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Month");
		yAxis.setLabel("Savings");
		
		this.savingsChart = new LineChart<>(xAxis, yAxis);
		this.savingsChart.setMaxSize(this.savingsAnchor.getPrefWidth() * 0.9, 
				this.savingsAnchor.getPrefHeight()* 0.9);
		this.savingsAnchor.getChildren().add(this.savingsChart);
	}
	
	private void fillChart() {
		this.savingsChart.setTitle("Savings, " + this.currentYear.getValue());
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		
		for (PiggySaving saving : this.currentYearSavings) {
			series.getData().add(new XYChart.Data<>(saving.getMonth().name().substring(0, 3), 
					saving.getAmount()));
		}
		this.savingsChart.setLegendVisible(false);
		this.savingsChart.getData().clear();
		this.savingsChart.getData().add(series);
		
	}
	
	private void setHeaderLabels() {
		this.headerLabel.setText(this.headerText + 
				String.valueOf(this.currentYear.getValue()));
	}
	
	private void setTotalSavingsLabel() {
		this.totalSavingsLabel.setText(String.valueOf(getTotalSavings()));
	}
	
	private void setCurrentYearSavingsLabel() {
		this.yearlySavingsLabel.setText(String.valueOf(getSavingsByYear()));
	}
	
	private double getTotalSavings() {
		double totalSavingsAmount = 0.0;
		
		List<PiggySaving> savings = this.piggyIncomesRemote.getSavingsTillMonthYear(
				LocalDate.now().getMonth(), Year.now());
		for (PiggySaving saving : savings) {
			totalSavingsAmount += saving.getAmount();
		}
		
		return totalSavingsAmount;
	}
	
	private double getSavingsByYear() {
		double yearlySavingsAmount = 0.0;
		
		for (PiggySaving saving : this.currentYearSavings) {
			yearlySavingsAmount += saving.getAmount();
		}
		
		return yearlySavingsAmount;
	}
	
	@FXML
	private void handlePreviousButtonReleased() {
		this.currentYear = Year.of(this.currentYear.getValue() - 1);
		update();
	}
	
	@FXML
	private void handleNextButtonReleased() {
		this.currentYear = Year.of(this.currentYear.getValue() + 1);
		update();
	}
}
