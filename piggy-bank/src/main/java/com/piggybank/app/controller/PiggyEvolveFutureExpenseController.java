package com.piggybank.app.controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Predicate;

import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.app.util.TextValidator;
import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyExpenseCategoryName;
import com.piggybank.server.model.PiggyFutureExpense;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PiggyEvolveFutureExpenseController implements IPBTopStageController {

	@FXML
	private Label categoryNameLabel;
	@FXML
	private TextField dayField;
	@FXML
	private ChoiceBox<String> monthYearChoiceBox;
	@FXML
	private TextField titleField;
	@FXML
	private TextField amountField;
	@FXML
	private Label errorLabel;
	
	private Predicate<PiggyExpense> confirmHandler;
	private Runnable discardHandler;
	
	private final PiggyBankExpenses piggyExpensesRemote;
	
	{
		this.piggyExpensesRemote = new PiggyBankExpenses();
	}
	
	@Override
	public void init() {
		configureMonthYearChoiceBox();
		setDayFieldHandler();
	}

	@Override
	public <T> void setContent(T item) {
		if (null != item) {
			PiggyFutureExpense futureExpense = (PiggyFutureExpense) item;
			
			this.categoryNameLabel.setText(futureExpense.getCategoryName().getName());
			this.titleField.setText(futureExpense.getTitle());
			this.amountField.setText(String.valueOf(futureExpense.getAmount()));
		}
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
	
	private void configureMonthYearChoiceBox() {
		YearMonth yearMonth = YearMonth.now();
		ObservableList<String> yearMonthList = FXCollections.observableArrayList();
		
		for (int yearMonthOffset = 0; yearMonthOffset < 3; ++yearMonthOffset) {
			yearMonthList.add(LocalDateFormatter.getMonthYearStringFromYearMonth(yearMonth));
			yearMonth = yearMonth.plusMonths(1);
		}
		
		this.monthYearChoiceBox.setItems(yearMonthList);
		this.monthYearChoiceBox.getSelectionModel().select(0);
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

	private LocalDate getSelectedDate() {
		String monthYearString = this.monthYearChoiceBox.getValue();
		String[] monthYearTable = monthYearString.split(", ");
		
		Month selectedMonth = Month.valueOf(monthYearTable[0]);
		int selectedYearInt = Integer.parseInt(monthYearTable[1]);
		
		return LocalDate.of(selectedYearInt, selectedMonth, 1);
	}
	
	private List<PiggyExpenseCategory> getExpenseCategory(LocalDate expenseDate) {
		String categoryNameString = this.categoryNameLabel.getText();
		return this.piggyExpensesRemote.getExpensesCategoriesByMonthYearName(expenseDate.getMonth(),
				Year.of(expenseDate.getYear()), categoryNameString);
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
		discardHandler.run();
	}
	
	@FXML
	private void handleConfirmButtonReleased() {
		PiggyExpense expense = new PiggyExpense();
		
		LocalDate expenseDate = getSelectedDate();
		String dayString = this.dayField.getText();
		if (dayString.isEmpty() || false == TextValidator.containsOnlyNumber(dayString)) {
			this.errorLabel.setText("Invalid day");
			return;
		}
		
		try {
			expenseDate = LocalDate.of(expenseDate.getYear(), expenseDate.getMonth(), 
					Integer.parseInt(dayString));
		} catch (DateTimeException exception) {
			this.errorLabel.setText("Invalid day");
			return;
		}
		expense.setDate(expenseDate);
		
		String title = this.titleField.getText();
		if (title.isEmpty()) {
			this.errorLabel.setText("Title cannot be empty");
			return;
		}
		expense.setTitle(title);
		
		String amountString = this.amountField.getText();
		if (amountString.isEmpty() || false == TextValidator.containsDoubleNumber(amountString)) {
			this.errorLabel.setText("Invalid amount");
			return;
		}
		expense.setAmount(Double.parseDouble(amountString));
		
		List<PiggyExpenseCategory> expenseCategories = getExpenseCategory(expenseDate);
		if (expenseCategories.isEmpty()) {
			PiggyExpenseCategory newExpenseCategory = new PiggyExpenseCategory();
			PiggyExpenseCategoryName expenseCategoryName = 
					this.piggyExpensesRemote.getCategoryNameByName(this.categoryNameLabel.getText());
			
			newExpenseCategory.setName(expenseCategoryName);
			newExpenseCategory.setPredictedAmount(expense.getAmount());
			newExpenseCategory.setRealAmount(expense.getAmount());
			newExpenseCategory.setMonth(expenseDate.getMonth());
			newExpenseCategory.setYear(Year.of(expenseDate.getYear()));
			expense.setCategory(newExpenseCategory);
		} else {
			expense.setCategory(expenseCategories.get(0));
		}
		
		if (false == confirmHandler.test(expense)) {
			this.errorLabel.setText("Expense already exist");
		}
	}
}
