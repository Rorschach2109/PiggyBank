package com.piggybank.server.remote;

import java.time.Month;
import java.time.Year;
import java.util.List;

import javax.ejb.Remote;

import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyExpenseCategoryName;
import com.piggybank.server.model.PiggyFutureExpense;

@Remote
public interface PiggyBankExpensesRemote {

	public List<PiggyExpenseCategoryName> getActiveExpenseCategoryNames();
	public List<PiggyExpenseCategoryName> getExpenseCategoryNames();
	public PiggyExpenseCategoryName getExpenseCategoryNameByName(String categoryName);
	public void setPiggyCategoryActiveFlag(PiggyExpenseCategoryName expenseCategoryName, boolean activeFlag);
	
	public List<PiggyExpenseCategory> getExpensesCategories();
	public List<PiggyExpenseCategory> getExpensesCategoriesByMonthYear(Month month, Year year);
	public List<PiggyExpenseCategory> getExpensesCategoriesByMonthYearName(Month month, Year year, String name);
	public PiggyExpenseCategory addExpenseCategory(PiggyExpenseCategory expenseCategory);
	public void updateExpenseCategory(PiggyExpenseCategory expenseCategory);
	public void removeExpenseCategory(PiggyExpenseCategory expenseCategory);
	
	public List<PiggyExpense> getExpensesByCategory(PiggyExpenseCategory expenseCategory);
	public PiggyExpenseCategory addExpense(PiggyExpense expense);
	public void updateExpense(PiggyExpense expense);
	public PiggyExpenseCategory removeExpense(PiggyExpense expense);

	public List<PiggyFutureExpense> getFutureExpenses();
	public List<PiggyFutureExpense> getFutureExpensesByCategoryName(PiggyExpenseCategoryName expenseCategoryName);
	public PiggyFutureExpense addFutureExpense(PiggyFutureExpense futureExpense);
	public void updateFutureExpense(PiggyFutureExpense futureExpense);
	public void removeFutureExpense(PiggyFutureExpense futureExpense);
}
