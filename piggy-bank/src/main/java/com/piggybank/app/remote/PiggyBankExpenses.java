package com.piggybank.app.remote;

import java.time.Month;
import java.time.Year;
import java.util.List;

import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyExpenseCategoryName;
import com.piggybank.server.model.PiggyFutureExpense;
import com.piggybank.server.remote.PiggyBankExpensesRemote;

public class PiggyBankExpenses {

	public void addExpensesCategoryList(List<PiggyExpenseCategory> expenseCategoriesList) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		expenseCategoriesList.forEach(expenseCategory -> {
			piggyExpenses.addExpenseCategory(expenseCategory);
		});
	}
	
	public PiggyExpenseCategory addExpenseCategory(PiggyExpenseCategory expenseCategory) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.addExpenseCategory(expenseCategory);
	}
	
	public List<PiggyExpenseCategoryName> getActiveExpenseCategoryNames() {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getActiveExpenseCategoryNames();
	}
	
	public List<PiggyExpenseCategoryName> getExpenseCategoryNames() {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getExpenseCategoryNames();
	}
	
	public PiggyExpenseCategoryName getCategoryNameByName(String categoryName) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getExpenseCategoryNameByName(categoryName);
	}
	
	public void setPiggyCategoryActiveFlag(PiggyExpenseCategoryName expenseCategory, boolean activeFlag) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		piggyExpenses.setPiggyCategoryActiveFlag(expenseCategory, activeFlag);
	}
	
	public List<PiggyExpenseCategory> getExpensesCategories() {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getExpensesCategories();
	}
	
	public List<PiggyExpenseCategory> getExpensesCategoriesByMonthYear(Month month, Year year) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getExpensesCategoriesByMonthYear(month, year);
	}
	
	public List<PiggyExpenseCategory> getExpensesCategoriesByMonthYearName(Month month, Year year, String name) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getExpensesCategoriesByMonthYearName(month, year, name);
	}
	
	public void updateExpenseCategory(PiggyExpenseCategory expenseCategory) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		piggyExpenses.updateExpenseCategory(expenseCategory);
	}
	
	public void removeExpenseCategoriesList(List<PiggyExpenseCategory> expenseCategoriesList) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		expenseCategoriesList.forEach(expenseCategory -> {
			piggyExpenses.removeExpenseCategory(expenseCategory);
		});
	}
	
	public void removeExpenseCategory(PiggyExpenseCategory expenseCategory) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		piggyExpenses.removeExpenseCategory(expenseCategory);
	}
	
	public PiggyExpenseCategory addExpense(PiggyExpense expense) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.addExpense(expense);
	}
	
	public void updateExpense(PiggyExpense expense) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		piggyExpenses.updateExpense(expense);
	}
	
	public PiggyExpenseCategory removeExpense(PiggyExpense expense) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.removeExpense(expense);
	}
	
	public PiggyFutureExpense addFutureExpense(PiggyFutureExpense futureExpense) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.addFutureExpense(futureExpense);
	}
	
	public List<PiggyFutureExpense> getFutureExpenses() {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getFutureExpenses();
	}
	
	public List<PiggyFutureExpense> getFutureExpensesByCategoryName(PiggyExpenseCategoryName expenseCategoryName) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		return piggyExpenses.getFutureExpensesByCategoryName(expenseCategoryName);
	}
	
	public void removeFutureExpense(PiggyFutureExpense futureExpense) {
		PiggyBankExpensesRemote piggyExpenses = PiggyRemoteProxy.getPiggyExpenses();
		piggyExpenses.removeFutureExpense(futureExpense);
	}
}
