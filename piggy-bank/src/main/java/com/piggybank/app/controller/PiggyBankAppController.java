package com.piggybank.app.controller;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.app.util.LayoutManager;
import com.piggybank.app.util.ResourcePathFinder;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyIncome;
import com.piggybank.server.model.PiggySaving;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PiggyBankAppController {

	private final Stage mainStage;
	private final LayoutManager layoutManager;
	private final PiggyBankExpenses piggyExpensesRemote;
	private final PiggyBankIncomes piggyIncomesRemote;
	
	{
		this.piggyExpensesRemote = new PiggyBankExpenses();
		this.piggyIncomesRemote = new PiggyBankIncomes();
	}
	
	public PiggyBankAppController(Stage mainStage) {
		this.mainStage = mainStage;
		this.layoutManager = new LayoutManager();
	}
	
	public void start() {
		configureSavings();
		setMainWindow();
		this.mainStage.show();
	}
	
	private void setMainWindow() {
		Scene mainScene = layoutManager.loadScene(ResourcePathFinder.PB_MAIN_WINDOW);
		PiggyMainWindowController mainController = (PiggyMainWindowController) layoutManager.getCurrentController();
		mainController.setMainStage(mainStage);
		
		this.mainStage.setResizable(false);
		this.mainStage.setTitle("PiggyBank");
		this.mainStage.setScene(mainScene);
	}
	
	private void configureSavings() {
		YearMonth lastYearMonth = YearMonth.now().minusMonths(1);
		PiggySaving saving = this.piggyIncomesRemote.getSavingByMonthYear(
				lastYearMonth.getMonth(), Year.of(lastYearMonth.getYear()));
		
		if (null == saving) {
			PiggySaving newSaving = new PiggySaving(0.0, lastYearMonth);
			newSaving.setAmount(getMonthlyIncomesAmount(lastYearMonth) - getMonthlyExpensesAmount(lastYearMonth));
			this.piggyIncomesRemote.addSaving(newSaving);
		} else {
			saving.setAmount(getMonthlyIncomesAmount(lastYearMonth) - getMonthlyExpensesAmount(lastYearMonth));
			this.piggyIncomesRemote.updateSaving(saving);
		}
	}
	
	private double getMonthlyExpensesAmount(YearMonth lastYearMonth) {
		double totalMonthlyExpenses = 0.0;
		
		List<PiggyExpenseCategory> expenseCategoriesList = 
				this.piggyExpensesRemote.getExpensesCategoriesByMonthYear(lastYearMonth.getMonth(), Year.of(lastYearMonth.getYear()));
		for (PiggyExpenseCategory expenseCategory: expenseCategoriesList) {
			totalMonthlyExpenses += expenseCategory.getRealAmount();
		}
		
		return totalMonthlyExpenses;
	}
	
	private double getMonthlyIncomesAmount(YearMonth lastYearMonth) {
		double totalMonthlyIncomes = 0.0;
		
		List<PiggyIncome> incomesList = 
				this.piggyIncomesRemote.getIncomesByMonthYear(lastYearMonth.getMonth(), Year.of(lastYearMonth.getYear()));
		for (PiggyIncome income : incomesList) {
			totalMonthlyIncomes += income.getAmount();
		}
		return totalMonthlyIncomes;
	}
}
