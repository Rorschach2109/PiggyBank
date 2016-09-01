package com.piggybank.server.remote.bean;

import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyExpenseCategoryName;
import com.piggybank.server.model.PiggyFutureExpense;
import com.piggybank.server.remote.PiggyBankExpensesRemote;

@Stateless
public class PiggyBankExpensesBean implements PiggyBankExpensesRemote {

	@PersistenceContext(unitName="piggy-bank-unit")
	private EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyExpenseCategoryName> getActiveExpenseCategoryNames() {
		Query query = this.entityManager.createNamedQuery("categoriesNamesActive");
		return (List<PiggyExpenseCategoryName>) query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyExpenseCategoryName> getExpenseCategoryNames() {
		Query query = this.entityManager.createNamedQuery("categoriesNames");
		return (List<PiggyExpenseCategoryName>) query.getResultList();
	}
	
	@Override
	public PiggyExpenseCategoryName getExpenseCategoryNameByName(String categoryName) {
		Query query = this.entityManager.createNamedQuery("categoryNameByName");
		query.setParameter("categoryName", categoryName);
		return (PiggyExpenseCategoryName) query.getSingleResult();
	}
	
	@Override
	public void setPiggyCategoryActiveFlag(PiggyExpenseCategoryName expenseCategoryName, boolean activeFlag) {
		expenseCategoryName.setActive(activeFlag);
		this.entityManager.merge(expenseCategoryName);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyExpenseCategory> getExpensesCategories() {
		Query query = this.entityManager.createNamedQuery("expenseCategories");
		return (List<PiggyExpenseCategory>) query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyExpenseCategory> getExpensesCategoriesByMonthYear(Month month, Year year) {
		Query query = this.entityManager.createNamedQuery("expenseCategoriesByMonthYear");
		query.setParameter("month", month);
		query.setParameter("year", year);
		return (List<PiggyExpenseCategory>) query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyExpenseCategory> getExpensesCategoriesByMonthYearName(Month month, Year year, String name) {
		Query query = this.entityManager.createNamedQuery("expenseCategoriesByMonthYearName");
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("name", name);
		return (List<PiggyExpenseCategory>) query.getResultList();
	}

	@Override
	public PiggyExpenseCategory addExpenseCategory(PiggyExpenseCategory expenseCategory) {
		this.entityManager.persist(expenseCategory);
		this.entityManager.flush();
		return expenseCategory;
	}

	@Override
	public void updateExpenseCategory(PiggyExpenseCategory expenseCategory) {
		PiggyExpenseCategory existingExpenseCategory = this.entityManager.find(PiggyExpenseCategory.class, expenseCategory.getExpenseCategoryId());
		existingExpenseCategory.setPredictedAmount(expenseCategory.getPredictedAmount());
		this.entityManager.merge(existingExpenseCategory);
	}
	
	@Override
	public void removeExpenseCategory(PiggyExpenseCategory expenseCategory) {
		PiggyExpenseCategory existingExpenseCategory = this.entityManager.merge(expenseCategory);
		this.entityManager.remove(existingExpenseCategory);
	}

	@Override
	public List<PiggyExpense> getExpensesByCategory(PiggyExpenseCategory expenseCategory) {
		PiggyExpenseCategory existingExpenseCategory = this.entityManager.merge(expenseCategory);
		
		if (null == existingExpenseCategory) {
			return Collections.emptyList();
		}
		
		return existingExpenseCategory.getExpenses();
	}

	@Override
	public PiggyExpenseCategory addExpense(PiggyExpense expense) {
		PiggyExpenseCategory expenseCategory = expense.getCategory();
		expenseCategory.getExpenses().add(expense);
		expenseCategory.setRealAmount(expenseCategory.getRealAmount() + expense.getAmount());
		return this.entityManager.merge(expenseCategory);
	}
	
	@Override
	public void updateExpense(PiggyExpense expense) {
		PiggyExpense existingExpense = this.entityManager.find(PiggyExpense.class, expense.getExpenseId());
		existingExpense.getCategory().setRealAmount(existingExpense.getCategory().getRealAmount() - existingExpense.getAmount());
		existingExpense.setAmount(expense.getAmount());
		existingExpense.getCategory().setRealAmount(existingExpense.getCategory().getRealAmount() + expense.getAmount());
		existingExpense.setDate(expense.getDate());
		existingExpense.setTitle(expense.getTitle());
		this.entityManager.refresh(existingExpense);
		this.entityManager.merge(existingExpense.getCategory());
	}

	@Override
	public PiggyExpenseCategory removeExpense(PiggyExpense expense) {
		PiggyExpenseCategory expenseCategory = expense.getCategory();
		
		List<PiggyExpense> categoryExpenses = expenseCategory.getExpenses();
		categoryExpenses.remove(expense);
		expenseCategory.setRealAmount(expenseCategory.getRealAmount() - expense.getAmount());
		
		return this.entityManager.merge(expenseCategory);
	}

	@Override
	public PiggyFutureExpense addFutureExpense(PiggyFutureExpense futureExpense) {
		this.entityManager.persist(futureExpense);
		this.entityManager.flush();
		return futureExpense;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyFutureExpense> getFutureExpenses() {
		Query query = this.entityManager.createNamedQuery("futureExpenses");
		return (List<PiggyFutureExpense>) query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyFutureExpense> getFutureExpensesByCategoryName(PiggyExpenseCategoryName expenseCategoryName) {
		Query query = this.entityManager.createNamedQuery("futureExpensesByCategoryName");
		query.setParameter("categoryName", expenseCategoryName.getName());
		return (List<PiggyFutureExpense>) query.getResultList();
	}

	@Override
	public void updateFutureExpense(PiggyFutureExpense futureExpense) {
		PiggyFutureExpense existingFutureExpense = this.entityManager.find(PiggyFutureExpense.class, 
				futureExpense.getFutureExpenseId());
		existingFutureExpense.setAmount(futureExpense.getAmount());
		existingFutureExpense.setCategoryName(futureExpense.getCategoryName());
		existingFutureExpense.setTitle(futureExpense.getTitle());
		this.entityManager.merge(existingFutureExpense);
	}

	@Override
	public void removeFutureExpense(PiggyFutureExpense futureExpense) {
		PiggyFutureExpense existingFutureExpense = this.entityManager.merge(futureExpense);
		this.entityManager.remove(existingFutureExpense);
	}

}
