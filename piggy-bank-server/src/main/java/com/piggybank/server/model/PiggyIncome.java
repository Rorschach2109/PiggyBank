package com.piggybank.server.model;

import java.time.Month;
import java.time.Year;
import java.util.Objects;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.piggybank.server.model.converter.PiggyMonthConverter;
import com.piggybank.server.model.converter.PiggyYearConverter;
import com.piggybank.server.model.query.PiggyIncomeNamedQuery;

@Entity
@Table(name="Income")
@NamedQueries(value={
		@NamedQuery(name="incomes",
				query=PiggyIncomeNamedQuery.INCOMES),
		@NamedQuery(name="incomesByMonthYear",
				query=PiggyIncomeNamedQuery.INCOMES_BY_MONTH_YEAR)
})
public class PiggyIncome implements java.io.Serializable, Comparable<PiggyIncome> {

	@Transient
	private static final long serialVersionUID = 2762099759818309197L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int incomeId;
	
	private String name;
	private double amount;
	
	@Convert(converter=PiggyMonthConverter.class)
	private Month month;
	
	@Convert(converter=PiggyYearConverter.class)
	private Year year;
	
	public PiggyIncome() {
		this("", 0.0, null, null);
	}
	
	public PiggyIncome(String name, double amount, Month month, Year year) {
		this.name = name;
		this.amount = amount;
		this.month = month;
		this.year = year;
	}
	
	public int getIncomeId() {
		return incomeId;
	}
	
	public void setIncomeId(int incomeId) {
		this.incomeId = incomeId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.month, this.year, this.amount);
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		
		if (this == object) {
			return true;
		}
		
		if (false == object instanceof PiggyIncome) {
			return false;
		}
		
		PiggyIncome income = (PiggyIncome) object;
		return this.name.toLowerCase().equals(income.name.toLowerCase()) && 
				this.month.equals(income.month) &&
				this.year.equals(income.year) &&
				this.amount == income.amount;
	}

	@Override
	public int compareTo(PiggyIncome income) {
		int compareResult = this.year.compareTo(income.year);
		if (0 != compareResult) {
			return compareResult;
		}
		
		compareResult = this.month.compareTo(income.month);
		if (0 != compareResult) {
			return compareResult;
		}
		
		return this.name.toLowerCase().compareTo(income.name.toLowerCase());
	}
}
