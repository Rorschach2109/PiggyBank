package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.function.Predicate;

import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.app.util.TextValidator;
import com.piggybank.server.model.PiggyIncome;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PiggyPlanIncomeController implements IPBTopStageController {
		
	@FXML
	private Label headerLabel;
	@FXML
	private TextField incomeTitleField;
	@FXML
	private TextField incomeAmountField;
	@FXML
	private Label errorLabel;

	private Predicate<PiggyIncome> confirmHandler;
	private Runnable discardHandler;
	private LocalDate currentDate ;
	
	private final String headerText;
	
	{
		this.headerText = "Plan Income ";
	}
	
	@Override
	public void setDate(LocalDate date) {
		this.currentDate = date;
		this.headerLabel.setText(this.headerText + 
				LocalDateFormatter.getMonthYearStringFromDate(this.currentDate));
	}
	
	@Override
	public <T> void setContent(T item) {
		if (null != item) {
			PiggyIncome income = (PiggyIncome) item;
			this.incomeTitleField.setText(income.getName());
			this.incomeAmountField.setText(String.valueOf(income.getAmount()));
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> void setConfirmHandler(Predicate<T> confirmHandler) {
		this.confirmHandler = (Predicate<PiggyIncome>) confirmHandler;
	}
	
	@Override
	public void setDiscardHandler(Runnable discardHandler) {
		this.discardHandler = discardHandler;
	}
	
	@Override
	public void init() {
	}
	
	@FXML
	private void handleOnKeyReleased(final KeyEvent keyEvent) {
		KeyCode eventKeyCode = keyEvent.getCode();
		
		switch (eventKeyCode) {
			case ESCAPE:
				handleDiscardButtonReleased();
				break;
			case ENTER:
				handleConfirmButtonReleased();
				break;
			default:
				return;
		}
	}

	@FXML
	private void handleDiscardButtonReleased() {
		this.discardHandler.run();
	}
	
	@FXML
	private void handleConfirmButtonReleased() {
		String incomeTitle = this.incomeTitleField.getText();
		if (incomeTitle.isEmpty()) {
			errorLabel.setText("Title cannot be empty");
			return;
		}
		
		String incomeAmountString = this.incomeAmountField.getText();
		if (incomeAmountString.isEmpty() || 
				false == TextValidator.containsDoubleNumber(incomeAmountString)) {
			errorLabel.setText("Invalid amount");
			return;
		}
		
		PiggyIncome income = new PiggyIncome(incomeTitle, 
				Double.parseDouble(incomeAmountString),
				this.currentDate.getMonth(), 
				Year.of(this.currentDate.getYear()));
		
		if (false == confirmHandler.test(income)) {
			this.errorLabel.setText("Income already exists");
		}
	}
}
