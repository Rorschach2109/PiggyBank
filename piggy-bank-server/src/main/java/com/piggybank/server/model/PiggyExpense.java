package com.piggybank.server.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.piggybank.server.model.converter.PiggyLocalDateConverter;

@Entity
@Table(name="Expense")
public class PiggyExpense implements java.io.Serializable, Comparable<PiggyExpense> {

	@Transient
	private static final long serialVersionUID = -210724152518544575L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int expenseId;
	
	private String title;
	private double amount;
	
	@Convert(converter=PiggyLocalDateConverter.class)
	private LocalDate date;
	
	@ManyToOne
	private PiggyExpenseCategory category;
	
	public PiggyExpense() {
		this("", 0.0, null, null);
	}
	
	public PiggyExpense(String title, double amount, 
			LocalDate date, PiggyExpenseCategory category) {
		this.title = title;
		this.amount = amount;
		this.date = date;
		this.category = category;
	}

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public PiggyExpenseCategory getCategory() {
		return category;
	}

	public void setCategory(PiggyExpenseCategory category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.title, this.date, this.category);
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		
		if (this == object) {
			return true;
		}
		
		if (false == object instanceof PiggyExpense) {
			return false;
		}
		
		PiggyExpense expense = (PiggyExpense) object;
		return this.title.equals(expense.title) &&
				this.date.equals(expense.date) && 
				this.category.equals(expense.category);
	}

	@Override
	public int compareTo(PiggyExpense expense) {
		int compareResult = this.date.compareTo(expense.date);
		if (0 != compareResult) {
			return compareResult;
		}
		
		return this.title.compareTo(expense.title);
	}
}
