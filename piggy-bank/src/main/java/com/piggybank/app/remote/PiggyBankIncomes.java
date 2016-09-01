package com.piggybank.app.remote;

import java.time.Month;
import java.time.Year;
import java.util.List;

import com.piggybank.server.model.PiggyIncome;
import com.piggybank.server.model.PiggySaving;
import com.piggybank.server.remote.PiggyBankIncomesRemote;

public final class PiggyBankIncomes {

	public List<PiggyIncome> getIncomes() {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		return piggyIncomes.getIncomes();
	}
	
	public List<PiggyIncome> getIncomesByMonthYear(Month month, Year year) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		return piggyIncomes.getIncomesByMonthYear(month, year);
	}
	
	public void addIncomesList(List<PiggyIncome> incomesList) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		incomesList.forEach(income -> {
			piggyIncomes.addIncome(income);
		});
	}
	
	public PiggyIncome addIncome(PiggyIncome income) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		return piggyIncomes.addIncome(income);
	}
	
	public void removeIncomesList(List<PiggyIncome> incomesList) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		incomesList.forEach(income -> {
			piggyIncomes.removeIncome(income);
		});
	}
	
	public void removeIncome(PiggyIncome income) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		piggyIncomes.removeIncome(income);
	}
	
	public List<PiggySaving> getSavingsByYear(Year year) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		return piggyIncomes.getSavingsByYear(year);
	}
	
	public PiggySaving getSavingByMonthYear(Month month, Year year) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		return piggyIncomes.getSavingByMonthYear(month, year);
	}
	
	public List<PiggySaving> getSavingsTillMonthYear(Month month, Year year) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		return piggyIncomes.getSavingsTillMonthYear(month, year);
	}
	
	public void addSaving(PiggySaving saving) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		piggyIncomes.addSaving(saving);
	}
	
	public void updateSaving(PiggySaving saving) {
		PiggyBankIncomesRemote piggyIncomes = PiggyRemoteProxy.getPiggyIncomes();
		piggyIncomes.updateSaving(saving);
	}
}
