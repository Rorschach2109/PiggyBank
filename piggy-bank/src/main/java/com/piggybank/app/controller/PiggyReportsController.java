package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyIncome;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class PiggyReportsController implements IPBController {

	@FXML
	private Label monthYearLabel;
	@FXML
	private Label totalOutcomesLabel;
	@FXML
	private Label totalIncomesLabel;
	@FXML
	private Label balanceLabel;
	@FXML
	private HBox headerBox;
	@FXML
	private GridPane headerGrid;
	@FXML
	private AnchorPane reportsAnchor;
	
	private PieChart outcomesPieChart;
	
	private LocalDate currentDate;
	private List<PiggyIncome> incomesList;
	private List<PiggyExpenseCategory> expenseCategoriesList;
	
	private double totalIncomesAmount;
	private double totalExpensesAmount;
	
	private final PiggyBankIncomes piggyIncomesRemote;
	private final PiggyBankExpenses piggyExpensesRemote;
	
	{
		this.currentDate = LocalDate.now();
		this.outcomesPieChart = new PieChart();
		this.totalIncomesAmount = 0.0;
		this.totalExpensesAmount = 0.0;
		this.piggyIncomesRemote = new PiggyBankIncomes();
		this.piggyExpensesRemote = new PiggyBankExpenses();
	}
	
	@Override
	public void init(double width, double height) {
		configurePieChart(width, height);
		update();
	}
	
	private void update() {
		clearSummaryValues();
		setContentData();
		setCurrentMonthYear();
		setSummaryValues();
		setSummaryLabels();
		fillPieChart();
	}

	private void setContentData() {
		Month currentMonth = this.currentDate.getMonth();
		Year currentYear = Year.of(this.currentDate.getYear());
		this.incomesList = this.piggyIncomesRemote.getIncomesByMonthYear(currentMonth, currentYear);
		this.expenseCategoriesList = this.piggyExpensesRemote.getExpensesCategoriesByMonthYear(currentMonth, currentYear);
	}
	
	private void setCurrentMonthYear() {
		this.monthYearLabel.setText(
				LocalDateFormatter.getMonthYearStringFromDate(this.currentDate));
	}
	
	private void clearSummaryValues() {
		this.totalExpensesAmount = 0.0;
		this.totalIncomesAmount = 0.0;
	}
	
	private void setSummaryValues() {
		setTotalOutcomes();
		setTotalIncomes();
	}
	
	private void setTotalOutcomes() {
		for (PiggyExpenseCategory expenseCategory : this.expenseCategoriesList) {
			this.totalExpensesAmount += expenseCategory.getRealAmount();
		}
	}
	
	private void setTotalIncomes() {
		for (PiggyIncome income : this.incomesList) {
			this.totalIncomesAmount += income.getAmount();
		}
	}
	
	private void setSummaryLabels() {
		this.totalOutcomesLabel.setText("-" + String.valueOf(this.totalExpensesAmount));
		this.totalOutcomesLabel.setTextFill(Color.RED);
		this.totalIncomesLabel.setText("+" + String.valueOf(this.totalIncomesAmount));
		this.totalIncomesLabel.setTextFill(Color.GREEN);
		setBalanceLabel();
	}
	
	private void setBalanceLabel() {
		double balance = this.totalIncomesAmount - this.totalExpensesAmount;
		String balanceSign = "";
		Color balanceColor = Color.BLACK;
		
		if (balance > 0.0) {
			balanceSign = "+";
			balanceColor = Color.GREEN;
		} else {
			balanceColor = Color.RED;
		}
		
		this.balanceLabel.setText(balanceSign + String.valueOf(balance));
		this.balanceLabel.setTextFill(balanceColor);
	}
	
	private void configurePieChart(double width, double height) {
		double pieChartHeight = height - (this.headerBox.getPrefHeight() +
				this.headerGrid.getPrefHeight());
		
		this.outcomesPieChart.setLegendSide(Side.TOP);
		this.outcomesPieChart.setTitle("Outcomes");
		this.outcomesPieChart.setMaxSize(width * 0.9, pieChartHeight* 0.9);
		this.reportsAnchor.getChildren().add(this.outcomesPieChart);
	}
	
	private void fillPieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		this.expenseCategoriesList.forEach(expenseCategory -> {
			pieChartData.add(new PieChart.Data(expenseCategory.getName().getName(), 
					expenseCategory.getRealAmount() / totalExpensesAmount));
		});
		this.outcomesPieChart.setData(pieChartData);
	}
	
	@FXML
	private void handlePreviousButtonReleased() {
		this.currentDate = this.currentDate.minusMonths(1);
		update();
	}
	
	@FXML
	private void handleNextButtonReleased() {
		this.currentDate = this.currentDate.plusMonths(1);
		update();
	}
}
