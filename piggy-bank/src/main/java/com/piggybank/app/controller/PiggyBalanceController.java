package com.piggybank.app.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyIncome;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class PiggyBalanceController implements IPBController {

	private PiggyBankIncomes piggyIncomesRemote;
	private PiggyBankExpenses piggyExpensesRemote;
	
	@FXML
	private Label monthYearLabel;
	@FXML
	private Label expensesLabel;
	@FXML
	private Label incomesLabel;
	@FXML
	private Label balanceLabel;
	@FXML
	private Label currentSavingsLabel;

	public PiggyBalanceController() {
		this.piggyIncomesRemote = new PiggyBankIncomes();
		this.piggyExpensesRemote = new PiggyBankExpenses();
	}
	
	public void setCurrentMonthName() {
		this.monthYearLabel.setText(
				LocalDateFormatter.getMonthYearStringFromDate(LocalDate.now()));
	}
	
	@Override
	public final void init(double width, double height) {
		update();
	}
	
	private void update() {
		setLabels();
	}

	private void setLabels() {
		double outcomes = getTotalOutcomes();
		double incomes = getTotalIncomes();
		double balance = incomes - outcomes;
		
		this.expensesLabel.setText("-" + String.valueOf(outcomes));
		this.incomesLabel.setText("+" + String.valueOf(incomes));
		setComputedLabel(this.balanceLabel, balance);
		setComputedLabel(this.currentSavingsLabel, getCurrentSavings());
	}
	
	private void setComputedLabel(Label label, double amount) {
		Color textColor = Color.BLACK;
		String balanceSign = "";
		
		if (amount > 0.0) {
			textColor = Color.GREEN;
			balanceSign = "+";
		} else if (amount < 0.0) {
			textColor = Color.RED;
		}
		
		label.setTextFill(textColor);
		label.setText(balanceSign + String.valueOf(amount));
	}
	
	private double getTotalOutcomes() {
		LocalDate currentDate = LocalDate.now();
		Month currentMonth = currentDate.getMonth();
		Year currentYear = Year.of(currentDate.getYear());
		
		List<PiggyExpenseCategory> expenseCategoriesList = 
				this.piggyExpensesRemote.getExpensesCategoriesByMonthYear(currentMonth, currentYear);
		
		double totalExpenses = 0.0;
		for (PiggyExpenseCategory expenseCategory : expenseCategoriesList) {
			totalExpenses += expenseCategory.getRealAmount();
		}
			
		return totalExpenses;
	}
	
	private double getTotalIncomes() {
		LocalDate currentDate = LocalDate.now();
		Month currentMonth = currentDate.getMonth();
		Year currentYear = Year.of(currentDate.getYear());
		
		List<PiggyIncome> incomesList = 
				this.piggyIncomesRemote.getIncomesByMonthYear(currentMonth, currentYear);
		
		double totalIncomes = 0.0;
		for (PiggyIncome income : incomesList) {
			totalIncomes += income.getAmount();
		}
			
		return totalIncomes;
	}
	
	private double getCurrentSavings() {
		double incomesTillMonth = 0.0;
		List<PiggyIncome> incomes = this.piggyIncomesRemote.getIncomes().stream()
				.filter(income -> 
					0 > YearMonth.of(income.getYear().getValue(), income.getMonth()).compareTo(YearMonth.now()))
				.collect(Collectors.toList());
		for (PiggyIncome income : incomes) {
			incomesTillMonth += income.getAmount();
		}
		
		double expensesTillMonth = 0.0;
		List<PiggyExpenseCategory> expenses = this.piggyExpensesRemote.getExpensesCategories().stream()
				.filter(expense -> 
					0 > YearMonth.of(expense.getYear().getValue(), expense.getMonth()).compareTo(YearMonth.now()))
				.collect(Collectors.toList());
		for (PiggyExpenseCategory expenseCategory: expenses) {
			expensesTillMonth += expenseCategory.getRealAmount();
		}
		
		return incomesTillMonth - expensesTillMonth;
	}
	
	@FXML
	private void initialize() {
		setCurrentMonthName();
	}
}
