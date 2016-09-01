package com.piggybank.server.model;

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
import javax.persistence.UniqueConstraint;

import com.piggybank.server.model.converter.PiggyBooleanConverter;
import com.piggybank.server.model.query.PiggyExpenseCategoryNameNamedQuery;

@Entity
@Table(name = "CategoryName", 
	uniqueConstraints=@UniqueConstraint(columnNames={"name"})
)
@NamedQueries(value={
		@NamedQuery(name="categoriesNames",
				query=PiggyExpenseCategoryNameNamedQuery.CATEGORY_NAMES),
		@NamedQuery(name="categoryNameByName",
			query=PiggyExpenseCategoryNameNamedQuery.CATEGORY_NAME_BY_NAME),
		@NamedQuery(name="categoriesNamesActive",
			query=PiggyExpenseCategoryNameNamedQuery.CATEGORY_NAMES_ACTIVE)
})
public class PiggyExpenseCategoryName implements java.io.Serializable, Comparable<PiggyExpenseCategoryName> {

	@Transient
	private static final long serialVersionUID = -5874306113546135349L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int categoryNameId;
	
	private String name;
	
	@Convert(converter=PiggyBooleanConverter.class)
	private boolean active;
	
	public PiggyExpenseCategoryName() {
		this("");
	}
	
	public PiggyExpenseCategoryName(String name) {
		this.name = name;
		this.active = true;
	}

	public int getCategoryNameId() {
		return categoryNameId;
	}

	public void setCategoryNameId(int categoryNameId) {
		this.categoryNameId = categoryNameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	@Override
	public boolean equals(Object object) {
		if (null == object) {
			return false;
		}
		
		if (this == object) {
			return true;
		}
		
		if (false == object instanceof PiggyExpenseCategoryName) {
			return false;
		}
		
		PiggyExpenseCategoryName categoryName = (PiggyExpenseCategoryName) object; 
		return this.name.equals(categoryName.name);
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(PiggyExpenseCategoryName categoryName) {
		return this.name.compareTo(categoryName.name);
	}
	
}
