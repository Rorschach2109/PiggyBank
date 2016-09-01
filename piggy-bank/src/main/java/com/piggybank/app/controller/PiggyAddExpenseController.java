package com.piggybank.app.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.function.Predicate;

import com.piggybank.app.util.TextValidator;
import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PiggyAddExpenseController implements IPBTopStageController {

	private Predicate<PiggyExpense> confirmHandler;
	private Runnable discardHandler;
	
	@FXML
	private Label categoryNameLabel;
	@FXML
	private TextField dayField;
	@FXML
	private Label monthNameLabel;
	@FXML
	private Label yearLabel;
	@FXML
	private TextField titleField;
	@FXML
	private TextField amountField;
	@FXML
	private Label errorLabel;
	
	public void setExpenseCategory(PiggyExpenseCategory expenseCategory) {
		this.categoryNameLabel.setText(expenseCategory.getName().getName());
		this.monthNameLabel.setText(expenseCategory.getMonth().name());
		this.yearLabel.setText(String.valueOf(expenseCategory.getYear().getValue()));
	}
	
	@Override
	public void init() {
		setDayFieldHandler();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void setConfirmHandler(Predicate<T> confirmHandler) {
		this.confirmHandler = (Predicate<PiggyExpense>) confirmHandler;
	}

	@Override
	public void setDiscardHandler(Runnable discardHandler) {
		this.discardHandler = discardHandler;
	}

	@Override
	public <T> void setContent(T item) {
		if (null != item) {
			PiggyExpense expense = (PiggyExpense) item;
			LocalDate expenseDate = expense.getDate();
			
			this.categoryNameLabel.setText(expense.getCategory().getName().getName());
			this.dayField.setText(String.valueOf(expenseDate.getDayOfMonth()));
			this.monthNameLabel.setText(expenseDate.getMonth().name());
			this.yearLabel.setText(String.valueOf(expenseDate.getYear()));
			this.titleField.setText(expense.getTitle());
			this.amountField.setText(String.valueOf(expense.getAmount()));
		}
	}
	
	private void setDayFieldHandler() {
		this.dayField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, 
					String oldValue, String newValue) {
				if (newValue.length() > 2) {
					dayField.setText(oldValue);
				}
			}
		});
	}

	@FXML
	private void handleDiscardButtonReleased() {
		this.discardHandler.run();
	}
	
	@FXML
	private void handleConfirmButtonReleased() {
		PiggyExpense expense = new PiggyExpense();
		
		String dayText = this.dayField.getText();
		if (dayText.isEmpty() || 
				false == TextValidator.containsOnlyNumber(dayText)) {
			this.errorLabel.setText("Day cannot be empty");
			return;
		}
		
		try {
			LocalDate expenseDate = LocalDate.of(
					Integer.parseInt(yearLabel.getText()),
					Month.valueOf(monthNameLabel.getText()), 
					Integer.parseInt(dayText));
			expense.setDate(expenseDate);
		} catch (DateTimeException exception) {
			this.errorLabel.setText("Invalid day of month");
			return;
		}
		
		String expenseTitle = this.titleField.getText();
		if (expenseTitle.isEmpty()) {
			this.errorLabel.setText("Title cannot be empty");
			return;
		}
		expense.setTitle(expenseTitle);
		
		String expenseAmountText = this.amountField.getText();
		if (expenseAmountText.isEmpty() || 
				false == TextValidator.containsDoubleNumber(expenseAmountText)) {
			this.errorLabel.setText("Invalid amount");
			return;
		}
		expense.setAmount(Double.parseDouble(expenseAmountText));
		
		if (false == confirmHandler.test(expense)) {
			this.errorLabel.setText("Expense already exists");
		}
	}
}
