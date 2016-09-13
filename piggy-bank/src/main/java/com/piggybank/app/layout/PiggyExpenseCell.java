package com.piggybank.app.layout;

import java.util.ArrayList;
import java.util.List;

import com.piggybank.server.model.PiggyExpenseCategory;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PiggyExpenseCell extends AbstractPiggyCell<PiggyExpenseCategory> {

	private final Label expenseCategoryTitleLabel;
	private final ProgressBar progressBar;
	private final Label realCostLabel;
	private final Label predictedCostLabel;
	
	{
		this.expenseCategoryTitleLabel = new Label();
		this.progressBar = new ProgressBar();
		this.realCostLabel = new Label();
		this.predictedCostLabel = new Label();
	}
	
	public PiggyExpenseCell(double width, double height) {
		super(width, height);
		
		setColumnConstraints();
		configureComponents();
	}
	
	@Override
	protected void addItem(PiggyExpenseCategory expenseCategory) {
		this.expenseCategoryTitleLabel.setText(expenseCategory.getName().getName());
		this.realCostLabel.setText(String.format("%.2f", expenseCategory.getRealAmount()));
		this.predictedCostLabel.setText(String.format("%.2f", expenseCategory.getPredictedAmount()));
		
		double expenseRatio = expenseCategory.getRealAmount() / expenseCategory.getPredictedAmount();
		setProgressBar(expenseRatio);
	}
	
	private void setProgressBar(double expenseRatio) {
		this.progressBar.setProgress(expenseRatio);
		
		String progressBarStyleString = "-fx-accent: ";
		if (expenseRatio < 0.5) {
			progressBarStyleString += "green;";
		} else if (expenseRatio < 0.9) {
			progressBarStyleString += "orange;";
		} else {
			progressBarStyleString += "red;";
		}

		this.progressBar.setStyle(progressBarStyleString);		
	}
	
	private final void setColumnConstraints() {
		List<ColumnConstraints> columnConstraintsList = new ArrayList<>();
		
		ColumnConstraints columnA = new ColumnConstraints();
		columnA.setPercentWidth(68);
		columnConstraintsList.add(columnA);

		ColumnConstraints columnB = new ColumnConstraints();
		columnB.setPercentWidth(28);
		columnConstraintsList.add(columnB);
		
		this.cellGrid.getColumnConstraints().addAll(columnConstraintsList);
	}
	
	private final void configureComponents() {
		this.cellGrid.add(this.expenseCategoryTitleLabel, 0, 0);
		this.cellGrid.add(this.progressBar, 0, 1);
		this.cellGrid.add(this.realCostLabel, 1, 0);
		this.cellGrid.add(this.predictedCostLabel, 1, 1);
		
		this.expenseCategoryTitleLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		this.predictedCostLabel.setFont(Font.font(null, FontWeight.BOLD, 10));
		
		double firstColumnWidthPercentage = this.cellGrid.getColumnConstraints()
				.get(0).percentWidthProperty().doubleValue() / 100; 
		
		double progressBarWidth = firstColumnWidthPercentage * this.cellGrid.getMaxWidth() * 0.8;
		this.progressBar.setMinWidth(progressBarWidth);
	}
}
