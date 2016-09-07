package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import com.piggybank.app.layout.PiggyIncomeCell;
import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.server.model.PiggyIncome;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class PiggyIncomesController implements IPBController {

	@FXML
	private Label headerLabel;
	@FXML
	private Label totalIncomesLabel;
	@FXML
	private Pane incomesPane;
	
	private LocalDate currentDate;
	private ListView<PiggyIncome> incomesListView;
	private final PiggyBankIncomes piggyIncomesRemote;
	private final String headerText;
	
	{
		this.currentDate = LocalDate.now();
		this.piggyIncomesRemote = new PiggyBankIncomes();
		this.headerText = "Incomes in ";
	}
	
	@Override
	public final void init() {
		createExpenseCategoryListView();
		insertExpensesList();
		update();
	}
	
	private void update() {
		setIncomesContent();
		setCurrentMonthYear();
		setSummaryLabels();
	}
	
	private void setCurrentMonthYear() {
		this.headerLabel.setText(this.headerText + 
				LocalDateFormatter.getMonthYearStringFromDate(this.currentDate));
	}
	
	private void setSummaryLabels() {
		double totalIncomesAmount = 0.0;
		
		for (PiggyIncome income : this.incomesListView.getItems()) {
			totalIncomesAmount += income.getAmount();
		}
		
		this.totalIncomesLabel.setText(String.valueOf(totalIncomesAmount));
	}
		
	private void createExpenseCategoryListView() {
		this.incomesListView = new ListView<>();
		
		this.incomesListView.setCellFactory(new Callback<ListView<PiggyIncome>, ListCell<PiggyIncome>>() {
			@Override
			public ListCell<PiggyIncome> call(ListView<PiggyIncome> param) {
				return new PiggyIncomeCell(incomesPane.getWidth(), incomesPane.getHeight());
			}
		});
		
		this.incomesListView.setMinSize(incomesPane.getPrefWidth(), incomesPane.getPrefHeight());
		this.incomesListView.setMaxSize(incomesPane.getPrefWidth(), incomesPane.getPrefHeight());
	}
	
	private void insertExpensesList() {
		this.incomesPane.getChildren().clear();
		this.incomesPane.getChildren().add(this.incomesListView);
	}
	
	private void setIncomesContent() {
		List<PiggyIncome> incomesList = this.piggyIncomesRemote.getIncomesByMonthYear(
				this.currentDate.getMonth(), Year.of(this.currentDate.getYear()));
		this.incomesListView.setItems(FXCollections.observableArrayList(incomesList));
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
