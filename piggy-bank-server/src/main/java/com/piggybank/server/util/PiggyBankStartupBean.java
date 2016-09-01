package com.piggybank.server.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.piggybank.server.model.PiggyExpenseCategoryName;

@Singleton
@Startup
public class PiggyBankStartupBean {

	@PersistenceContext(unitName="piggy-bank-unit")
	private EntityManager entityManager;
	
	@PostConstruct
	public void init() {
		initCategoryNames();
	}
	
	@SuppressWarnings("unchecked")
	private List<PiggyExpenseCategoryName> getExistingCategoryNames() {
		Query query = this.entityManager.createNamedQuery("categoriesNames");
		return (List<PiggyExpenseCategoryName>) query.getResultList();
	}
	
	private List<String> getExpenseCategoryNames() {
		List<String> categoryNameList = new ArrayList<>();
		categoryNameList.add("Food");
		categoryNameList.add("Entertainment");
		categoryNameList.add("Car");
		categoryNameList.add("Home");
		categoryNameList.add("Transport");
		categoryNameList.add("Bills");
		categoryNameList.add("Clothes");
		categoryNameList.add("Groceries");
		categoryNameList.add("Other");
		return categoryNameList;
	}
	
	private void initCategoryNames() {
		List<PiggyExpenseCategoryName> existingCategoryNames = getExistingCategoryNames();
		List<String> categoryNameList = getExpenseCategoryNames();
		categoryNameList.forEach(categoryName -> {
			PiggyExpenseCategoryName expenseCategoryName = new PiggyExpenseCategoryName(categoryName);
			if (false == existingCategoryNames.contains(expenseCategoryName)) {
				this.entityManager.persist(expenseCategoryName);
			}
		});
	}
}
