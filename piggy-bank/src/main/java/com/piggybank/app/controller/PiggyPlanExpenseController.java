package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.app.util.TextValidator;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyExpenseCategoryName;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PiggyPlanExpenseController implements IPBTopStageController {
	
	private Predicate<PiggyExpenseCategory> confirmHandler;
	private Runnable discardHandler;
	private LocalDate currentDate;
	
	private final PiggyBankExpenses piggyExpensesRemote;
	
	@FXML
	private Label monthYearLabel;
	@FXML
	private ChoiceBox<PiggyExpenseCategoryName> categoryNameChoiceBox;
	@FXML
	private TextField predictedAmountField;
	@FXML
	private Label errorLabel;
	
	{
		this.piggyExpensesRemote = new PiggyBankExpenses();
	}
	
	@Override
	public void setDate(LocalDate date) {
		this.currentDate = date;
		this.monthYearLabel.setText(LocalDateFormatter.getMonthYearStringFromDate(this.currentDate));
	}
	
	@Override
	public <T> void setContent(T item) {
		if (null != item) {
			PiggyExpenseCategory expenseCategory = (PiggyExpenseCategory) item;
			
			PiggyExpenseCategoryName expenseCategoryName = expenseCategory.getName();
			List<PiggyExpenseCategoryName> categoryNamesList = 
					this.categoryNameChoiceBox.getItems();
			categoryNamesList.add(expenseCategoryName);
			Collections.sort(categoryNamesList);
			
			this.categoryNameChoiceBox.setValue(expenseCategoryName);
			this.categoryNameChoiceBox.setDisable(true);
			
			this.predictedAmountField.setText(String.valueOf(expenseCategory.getPredictedAmount()));
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> void setConfirmHandler(Predicate<T> confirmHandler) {
		this.confirmHandler = (Predicate<PiggyExpenseCategory>) confirmHandler;
	}
	
	@Override
	public void setDiscardHandler(Runnable discardHandler) {
		this.discardHandler = discardHandler;
	}
	
	@Override
	public void init() {
		update();
	}
	
	private void update() {
		setCategoryChoiceBoxItems();
	}
	
	private void setCategoryChoiceBoxItems() {
		List<PiggyExpenseCategoryName> categoryNames = this.piggyExpensesRemote.getActiveExpenseCategoryNames();
		this.categoryNameChoiceBox.setItems(
				FXCollections.observableArrayList(categoryNames));
	}
	
	@FXML
	private void handleDiscardButtonReleased() {
		this.discardHandler.run();
	}
	
	@FXML
	private void handleConfirmButtonReleased() {
		if (this.categoryNameChoiceBox.getSelectionModel().isEmpty()) {
			errorLabel.setText("Category cannot be empty");
			return;
		}

		String expensePredictedAmountString = this.predictedAmountField.getText();
		if (false == TextValidator.containsDoubleNumber(expensePredictedAmountString)) {
			errorLabel.setText("Invalid amount");
			return;
		}
		
		PiggyExpenseCategoryName expenseCategoryName = this.categoryNameChoiceBox.getValue();
		PiggyExpenseCategory expenseCategory = new PiggyExpenseCategory(
				expenseCategoryName, 
				Double.parseDouble(expensePredictedAmountString), 
				this.currentDate.getMonth(), 
				Year.of(this.currentDate.getYear()));
		
		confirmHandler.test(expenseCategory);
		piggyExpensesRemote.setPiggyCategoryActiveFlag(expenseCategoryName, false);
	}
}
