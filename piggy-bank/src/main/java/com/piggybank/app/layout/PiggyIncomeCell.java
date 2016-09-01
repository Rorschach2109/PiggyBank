package com.piggybank.app.layout;

import com.piggybank.server.model.PiggyIncome;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PiggyIncomeCell extends AbstractPiggyCell<PiggyIncome> {

	private final Label incomeTitleLabel;
	private final Label incomeAmountLabel;
	
	{
		this.incomeTitleLabel = new Label();
		this.incomeAmountLabel = new Label();
	}
	
	public PiggyIncomeCell(double width, double height) {
		super(width, height);
		setColumnConstraints();
		configureComponents();
	}

	@Override
	protected void addItem(PiggyIncome income) {
		this.incomeTitleLabel.setText(income.getName());
		this.incomeAmountLabel.setText(String.valueOf(income.getAmount()));
	}

	private final void setColumnConstraints() {
		ColumnConstraints columnA = new ColumnConstraints();
		columnA.setPercentWidth(70);

		ColumnConstraints columnB = new ColumnConstraints();
		columnB.setPercentWidth(28);
		
		this.cellGrid.getColumnConstraints().addAll(columnA, columnB);
	}
	
	private final void configureComponents() {
		this.cellGrid.add(this.incomeTitleLabel, 0, 0);
		this.cellGrid.add(this.incomeAmountLabel, 1, 0);
		
		this.incomeTitleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
	}
}
