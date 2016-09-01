package com.piggybank.server.remote;

import java.time.Month;
import java.time.Year;
import java.util.List;

import javax.ejb.Remote;

import com.piggybank.server.model.PiggyIncome;
import com.piggybank.server.model.PiggySaving;

@Remote
public interface PiggyBankIncomesRemote {

	public List<PiggyIncome> getIncomes();
	public List<PiggyIncome> getIncomesByMonthYear(Month month, Year year);
	public PiggyIncome addIncome(PiggyIncome income);
	public void removeIncome(PiggyIncome income);
	
	public List<PiggySaving> getSavingsByYear(Year year);
	public PiggySaving getSavingByMonthYear(Month month, Year year);
	public List<PiggySaving> getSavingsTillMonthYear(Month month, Year year);
	public void addSaving(PiggySaving saving);
	public void updateSaving(PiggySaving saving);
}
