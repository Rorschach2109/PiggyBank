package com.piggybank.server.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.piggybank.server.model.query.PiggyFutureExpenseNamedQuery;

@Entity
@Table(name="FutureExpense")
@NamedQueries(value={
		@NamedQuery(name="futureExpenses",
				query=PiggyFutureExpenseNamedQuery.FUTURE_EXPENSES),
		@NamedQuery(name="futureExpensesByCategoryName",
				query=PiggyFutureExpenseNamedQuery.FUTURE_EXPENSES_BY_CATEGORY_NAME)
})
public class PiggyFutureExpense implements java.io.Serializable, Comparable<PiggyFutureExpense> {

	@Transient
	private static final long serialVersionUID = -3122862706520570999L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int futureExpenseId;
	
	private String title;
	private double amount;
	
	@ManyToOne
	private PiggyExpenseCategoryName categoryName;

	public PiggyFutureExpense() {
		this("", 0.0, null);
	}
	
	public PiggyFutureExpense(String title, double amount, 
			PiggyExpenseCategoryName categoryName) {
		this.title = title;
		this.amount = amount;
		this.categoryName = categoryName;
	}
	
	public int getFutureExpenseId() {
		return futureExpenseId;
	}

	public void setFutureExpenseId(int futureExpenseId) {
		this.futureExpenseId = futureExpenseId;
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

	public PiggyExpenseCategoryName getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(PiggyExpenseCategoryName categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.title, this.categoryName);
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		
		if (this == object) {
			return true;
		}
		
		if (false == object instanceof PiggyFutureExpense) {
			return false;
		}
		
		PiggyFutureExpense futureExpense = (PiggyFutureExpense) object;
		return this.title.equals(futureExpense.title) && 
				this.categoryName.equals(futureExpense.categoryName);
	}

	@Override
	public int compareTo(PiggyFutureExpense futureExpense) {
		int compareResult = this.categoryName.compareTo(futureExpense.categoryName);
		if (0 != compareResult) {
			return compareResult;
		}
		
		return this.title.compareTo(futureExpense.title);
	}
}
