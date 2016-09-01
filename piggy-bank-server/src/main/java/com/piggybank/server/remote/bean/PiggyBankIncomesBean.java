package com.piggybank.server.remote.bean;

import java.time.Month;
import java.time.Year;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.piggybank.server.model.PiggyIncome;
import com.piggybank.server.model.PiggySaving;
import com.piggybank.server.remote.PiggyBankIncomesRemote;

@Stateless
public class PiggyBankIncomesBean implements PiggyBankIncomesRemote {

	@PersistenceContext(unitName="piggy-bank-unit")
	private EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyIncome> getIncomes() {
		Query query = this.entityManager.createNamedQuery("incomes");
		return (List<PiggyIncome>) query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggyIncome> getIncomesByMonthYear(Month month, Year year) {
		Query query = this.entityManager.createNamedQuery("incomesByMonthYear");
		query.setParameter("month", month);
		query.setParameter("year", year);
		return (List<PiggyIncome>) query.getResultList();
	}

	@Override
	public PiggyIncome addIncome(PiggyIncome income) {
		this.entityManager.persist(income);
		this.entityManager.flush();
		return income;
	}

	@Override
	public void removeIncome(PiggyIncome income) {
		PiggyIncome existingIncome = this.entityManager.merge(income);
		this.entityManager.remove(existingIncome);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PiggySaving> getSavingsByYear(Year year) {
		Query query = this.entityManager.createNamedQuery("savingsByYear");
		query.setParameter("year", year);
		return (List<PiggySaving>) query.getResultList();
	}
	
	@Override
	public PiggySaving getSavingByMonthYear(Month month, Year year) {
		Query query = this.entityManager.createNamedQuery("savingByMonthYear");
		query.setParameter("month", month);
		query.setParameter("year", year);
		try {
			return (PiggySaving) query.getSingleResult();
		} catch (NoResultException exception) {
			return null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PiggySaving> getSavingsTillMonthYear(Month month, Year year) {
		Query query = this.entityManager.createNamedQuery("savingsTillMonthYear");
		query.setParameter("month", month);
		query.setParameter("year", year);
		return (List<PiggySaving>) query.getResultList();
	}

	@Override
	public void addSaving(PiggySaving saving) {
		this.entityManager.persist(saving);		
	}

	@Override
	public void updateSaving(PiggySaving saving) {
		PiggySaving existingSaving = this.entityManager.find(PiggySaving.class, saving.getSavingId());
		existingSaving.setAmount(saving.getAmount());
		this.entityManager.merge(existingSaving);
	}
}
