package com.piggybank.server.model;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.piggybank.server.model.converter.PiggyMonthConverter;
import com.piggybank.server.model.converter.PiggyYearConverter;
import com.piggybank.server.model.query.PiggyExpenseCategoryNamedQuery;

@Entity
@Table(name="ExpenseCategory")
@NamedQueries(value={
		@NamedQuery(name="expenseCategories", query=PiggyExpenseCategoryNamedQuery.EXPENSE_CATEGORIES),
		@NamedQuery(name="expenseCategoriesByMonthYear", query=PiggyExpenseCategoryNamedQuery.EXPENSE_CATEGORIES_BY_MONTH_YEAR),
		@NamedQuery(name="expenseCategoriesByMonthYearName", query=PiggyExpenseCategoryNamedQuery.EXPENSE_CATEGORIES_BY_MONTH_YEAR_NAME)
})
public class PiggyExpenseCategory implements java.io.Serializable, Comparable<PiggyExpenseCategory> {

	@Transient
	private static final long serialVersionUID = -1759628413568840752L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int expenseCategoryId;

	@OneToOne
	private PiggyExpenseCategoryName name;
	
	private double predictedAmount;
	private double realAmount;
	
	@Convert(converter=PiggyMonthConverter.class)
	private Month month;
	
	@Convert(converter=PiggyYearConverter.class)
	private Year year;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="category", orphanRemoval=true)
	private List<PiggyExpense> expenses; 
	
	public PiggyExpenseCategory() {
		this(null, 0.0, null, null);
	}
	
	public PiggyExpenseCategory(PiggyExpenseCategoryName name, double predictedAmount, Month month, Year year) {
		this.name = name;
		this.predictedAmount = predictedAmount;
		this.realAmount = 0.0;
		this.month = month;
		this.year = year;
		this.expenses = new ArrayList<>();
	}

	public int getExpenseCategoryId() {
		return expenseCategoryId;
	}

	public void setExpenseCategoryId(int expenseCategoryId) {
		this.expenseCategoryId = expenseCategoryId;
	}

	public PiggyExpenseCategoryName getName() {
		return name;
	}

	public void setName(PiggyExpenseCategoryName name) {
		this.name = name;
	}

	public double getPredictedAmount() {
		return predictedAmount;
	}

	public void setPredictedAmount(double predictedAmount) {
		this.predictedAmount = predictedAmount;
	}

	public double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
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

	public List<PiggyExpense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<PiggyExpense> expenses) {
		this.expenses = expenses;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.month, this.year);
	}
	
	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		
		if (this == object) {
			return true;
		}
		
		if (false == object instanceof PiggyExpenseCategory) {
			return false;
		}
		
		PiggyExpenseCategory expenseCategory = (PiggyExpenseCategory) object;
		return this.name.equals(expenseCategory.name) &&
				this.month.equals(expenseCategory.month) &&
				this.year.equals(expenseCategory.year);
	}

	@Override
	public int compareTo(PiggyExpenseCategory expense) {
		int compareResult = this.year.compareTo(expense.year);
		if (0 != compareResult) {
			return compareResult;
		}
		
		compareResult = this.month.compareTo(expense.month);
		if (0 != compareResult) {
			return compareResult;
		}
		
		return this.name.compareTo(expense.name);
	}
}
