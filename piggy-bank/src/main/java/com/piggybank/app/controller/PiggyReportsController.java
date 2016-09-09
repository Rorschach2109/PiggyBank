package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
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

public class PiggyReportsController implements IPBController {

	@FXML
	private Label headerLabel;
	@FXML
	private Label totalOutcomesLabel;
	@FXML
	private Label totalIncomesLabel;
	@FXML
	private Label balanceLabel;
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
	private final String headerText;
	
	{
		this.currentDate = LocalDate.now();
		this.outcomesPieChart = new PieChart();
		this.totalIncomesAmount = 0.0;
		this.totalExpensesAmount = 0.0;
		this.piggyIncomesRemote = new PiggyBankIncomes();
		this.piggyExpensesRemote = new PiggyBankExpenses();
		this.headerText = "Report from ";
	}
	
	@Override
	public void init() {
		configurePieChart();
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
		Collections.sort(this.expenseCategoriesList);
	}
	
	private void setCurrentMonthYear() {
		this.headerLabel.setText(this.headerText + 
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
		this.totalOutcomesLabel.setText("-" + String.format("%.2f", this.totalExpensesAmount));
		this.totalIncomesLabel.setText("+" + String.format("%.2f", this.totalIncomesAmount));
		setBalanceLabel();
	}
	
	private void setBalanceLabel() {
		double balance = this.totalIncomesAmount - this.totalExpensesAmount;
		String balanceSign = "";
		
		if (balance > 0.0) {
			balanceSign = "+";
		}
		
		this.balanceLabel.setText(balanceSign + String.format("%.2f", balance));
	}
	
	private void configurePieChart() {
		this.outcomesPieChart.setLegendSide(Side.TOP);
		this.outcomesPieChart.setTitle("Outcomes");
		this.outcomesPieChart.setMaxSize(this.reportsAnchor.getPrefWidth() * 0.9, 
				this.reportsAnchor.getPrefHeight() * 0.9);
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
