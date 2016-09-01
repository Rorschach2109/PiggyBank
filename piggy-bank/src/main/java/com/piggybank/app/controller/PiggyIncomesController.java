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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class PiggyIncomesController implements IPBController {

	@FXML
	private Label monthYearLabel;
	@FXML
	private Label totalIncomesLabel;
	@FXML
	private Pane incomesPane;
	@FXML
	private HBox headerBox;
	@FXML
	private GridPane headerGrid;
	
	private LocalDate currentDate;
	private ListView<PiggyIncome> incomesListView;
	private final PiggyBankIncomes piggyIncomesRemote;
	
	{
		this.currentDate = LocalDate.now();
		this.piggyIncomesRemote = new PiggyBankIncomes();
	}
	
	@Override
	public final void init(double width, double height) {
		createExpenseCategoryListView(width, height);
		insertExpensesList();
		update();
	}
	
	private void update() {
		setIncomesContent();
		setCurrentMonthYear();
		setSummaryLabels();
	}
	
	private void setCurrentMonthYear() {
		this.monthYearLabel.setText(
				LocalDateFormatter.getMonthYearStringFromDate(this.currentDate));
	}
	
	private void setSummaryLabels() {
		double totalIncomesAmount = 0.0;
		
		for (PiggyIncome income : this.incomesListView.getItems()) {
			totalIncomesAmount += income.getAmount();
		}
		
		this.totalIncomesLabel.setText(String.valueOf(totalIncomesAmount));
	}
		
	private void createExpenseCategoryListView(double width, double height) {
		this.incomesListView = new ListView<>();
		
		this.incomesListView.setCellFactory(new Callback<ListView<PiggyIncome>, ListCell<PiggyIncome>>() {
			@Override
			public ListCell<PiggyIncome> call(ListView<PiggyIncome> param) {
				return new PiggyIncomeCell(incomesPane.getWidth(), incomesPane.getHeight());
			}
		});
		
		double listHeight = height - (headerBox.getPrefHeight() + headerGrid.getPrefHeight());
		this.incomesListView.setMinSize(width, listHeight);
		this.incomesListView.setMaxSize(width, listHeight);
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
