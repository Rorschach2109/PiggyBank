package com.piggybank.app.controller;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.util.TextValidator;
import com.piggybank.server.model.PiggyExpenseCategoryName;
import com.piggybank.server.model.PiggyFutureExpense;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;

public class PiggyAddFutureExpenseController implements IPBTopStageController {

	@FXML
	private ChoiceBox<PiggyExpenseCategoryName> expenseCategoryChoiceBox;
	@FXML
	private TextField titleField;
	@FXML
	private TextField amountField;
	@FXML
	private Label errorLabel;
	
	private Predicate<PiggyFutureExpense> confirmHandler;
	private Runnable discardHandler;
	private final PiggyBankExpenses piggyExpensesRemote;
	
	{
		this.piggyExpensesRemote = new PiggyBankExpenses();
	}
	
	@Override
	public void init() {
		configureExpenseCategoryChoiceBox();
	}

	@Override
	public <T> void setContent(T item) {
		if (null != item) {
			PiggyFutureExpense futureExpense = (PiggyFutureExpense) item;
			
			this.expenseCategoryChoiceBox.setValue(futureExpense.getCategoryName());
			this.titleField.setText(futureExpense.getTitle());
			this.amountField.setText(String.valueOf(futureExpense.getAmount()));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void setConfirmHandler(Predicate<T> confirmHandler) {
		this.confirmHandler = (Predicate<PiggyFutureExpense>) confirmHandler;		
	}

	@Override
	public void setDiscardHandler(Runnable discardHandler) {
		this.discardHandler = discardHandler;		
	}
	
	private void configureExpenseCategoryChoiceBox() {
		List<PiggyExpenseCategoryName> expensesCategoryNameList = this.piggyExpensesRemote.getExpenseCategoryNames();
		Collections.sort(expensesCategoryNameList);
		this.expenseCategoryChoiceBox.setItems(FXCollections.observableArrayList(expensesCategoryNameList));
	}
	
	@FXML
	private void handleDiscardButtonReleased() {
		this.discardHandler.run();
	}
	
	@FXML
	private void handleConfirmButtonReleased() {
		PiggyFutureExpense futureExpense = new PiggyFutureExpense();
		
		SingleSelectionModel<PiggyExpenseCategoryName> selectionModel = 
				this.expenseCategoryChoiceBox.getSelectionModel();
		
		if (selectionModel.isEmpty()) {
			this.errorLabel.setText("Category cannot be empty");
			return;
		}
		futureExpense.setCategoryName(selectionModel.getSelectedItem());
		
		String expenseTitle = this.titleField.getText();
		if (expenseTitle.isEmpty()) {
			this.errorLabel.setText("Title cannot be empty");
			return;
		}
		futureExpense.setTitle(expenseTitle);
		
		String futureExpenseAmountText = this.amountField.getText();
		if (futureExpenseAmountText.isEmpty() || 
				false == TextValidator.containsDoubleNumber(futureExpenseAmountText)) {
			this.errorLabel.setText("Invalid amount");
			return;
		}
		futureExpense.setAmount(Double.parseDouble(futureExpenseAmountText));
		
		if (false == confirmHandler.test(futureExpense)) {
			this.errorLabel.setText("Expense already exists");
		}
	}

}
