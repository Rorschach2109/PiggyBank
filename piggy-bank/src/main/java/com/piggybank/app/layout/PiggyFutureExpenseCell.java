package com.piggybank.app.layout;

import java.util.function.Consumer;

import com.piggybank.server.model.PiggyFutureExpense;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PiggyFutureExpenseCell extends AbstractPiggyCell<PiggyFutureExpense> {

	private PiggyFutureExpense currentFutureExpense;
	
	private final Label futureExpenseCategoryName;
	private final Label futureExpenseTitle;
	private final Label futureExpenseAmount;
	private final Button removeFutureExpenseButton;
	private final Button setFutureDateButton;
	
	private final Consumer<PiggyFutureExpense> removeButtonHandler;
	private final Consumer<PiggyFutureExpense> setDateButtonHandler;
	
	{
		futureExpenseCategoryName = new Label();
		futureExpenseTitle = new Label();
		futureExpenseAmount = new Label();
		removeFutureExpenseButton = new Button();
		setFutureDateButton = new Button();
	}
	
	public PiggyFutureExpenseCell(double width, double height, 
			Consumer<PiggyFutureExpense> removeButtonHandler,
			Consumer<PiggyFutureExpense> setDateButtonHandler) {
		super(width, height);
		this.removeButtonHandler = removeButtonHandler;
		this.setDateButtonHandler = setDateButtonHandler;
		
		setColumnsConstraints();
		configureComponents();
		configureButtons();
	}

	@Override
	protected void addItem(PiggyFutureExpense futureExpense) {
		this.currentFutureExpense = futureExpense;
		this.futureExpenseCategoryName.setText(futureExpense.getCategoryName().getName());
		this.futureExpenseTitle.setText(futureExpense.getTitle());
		this.futureExpenseAmount.setText(String.valueOf(futureExpense.getAmount()));
	}
	
	private final void setColumnsConstraints() {
		ColumnConstraints columnA = new ColumnConstraints();
		columnA.setPercentWidth(25);
		
		ColumnConstraints columnB = new ColumnConstraints();
		columnB.setPercentWidth(35);
		
		ColumnConstraints columnC = new ColumnConstraints();
		columnC.setPercentWidth(15);
		
		ColumnConstraints columnD = new ColumnConstraints();
		columnD.setPercentWidth(10);
		
		ColumnConstraints columnE = new ColumnConstraints();
		columnE.setPercentWidth(15);
		
		this.cellGrid.getColumnConstraints().addAll(columnA, columnB, 
				columnC, columnD, columnE);
	}

	private final void configureComponents() {
		this.cellGrid.add(this.futureExpenseCategoryName, 0, 0);
		this.cellGrid.add(this.futureExpenseTitle, 1, 0);
		this.cellGrid.add(this.futureExpenseAmount, 2, 0);
		this.cellGrid.add(this.removeFutureExpenseButton, 3, 0);
		this.cellGrid.add(this.setFutureDateButton, 4, 0);
		
		this.futureExpenseCategoryName.setFont(Font.font(null, FontWeight.BOLD, -1));
		this.removeFutureExpenseButton.setId("list-action-button");
		this.setFutureDateButton.setId("list-action-button");
	}
	
	private void configureButtons() {
		this.removeFutureExpenseButton.setText("-");
		this.removeFutureExpenseButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				removeButtonHandler.accept(currentFutureExpense);
			}
		});
		
		this.setFutureDateButton.setText("Set Date");
		this.setFutureDateButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				setDateButtonHandler.accept(currentFutureExpense);
			}
		});
	}
}
