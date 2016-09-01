package com.piggybank.server.model;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
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
import com.piggybank.server.model.query.PiggySavingNamedQuery;

@Entity
@Table(name="Saving")
@NamedQueries(value={
		@NamedQuery(name="savingsByYear",
				query=PiggySavingNamedQuery.SAVINGS_BY_YEAR),
		@NamedQuery(name="savingByMonthYear",
				query=PiggySavingNamedQuery.SAVING_BY_MONTH_YEAR),
		@NamedQuery(name="savingsTillMonthYear",
			query=PiggySavingNamedQuery.SAVINGS_TILL_MONTH_YEAR)
})
public class PiggySaving implements java.io.Serializable, Comparable<PiggySaving> {

	@Transient
	private static final long serialVersionUID = 753394024065468153L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int savingId;
	
	private double amount;
	
	@Convert(converter=PiggyMonthConverter.class)
	private Month month;
	
	@Convert(converter=PiggyYearConverter.class)
	private Year year;
	
	public PiggySaving() {
		this(0.0, null, null);
	}
	
	public PiggySaving(double amount, YearMonth yearMonth) {
		this(amount, yearMonth.getMonth(), Year.of(yearMonth.getYear()));
	}
	
	public PiggySaving(double amount, Month month, Year year) {
		this.amount = amount;
		this.month = month;
		this.year = year;
	}

	public int getSavingId() {
		return savingId;
	}

	public void setSavingId(int savingId) {
		this.savingId = savingId;
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
		return Objects.hash(this.month, this.year);
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		
		if (this == object) {
			return true;
		}
		
		if (false == object instanceof PiggySaving) {
			return false;
		}
		
		PiggySaving saving = (PiggySaving) object;
		return this.month.equals(saving.month) &&
				this.year.equals(saving.year);
	}

	@Override
	public int compareTo(PiggySaving saving) {
		YearMonth thisYearMonth = YearMonth.of(this.year.getValue(), this.month);
		YearMonth savingYearMonth = YearMonth.of(saving.year.getValue(), saving.month);
		return thisYearMonth.compareTo(savingYearMonth);
	}
}
