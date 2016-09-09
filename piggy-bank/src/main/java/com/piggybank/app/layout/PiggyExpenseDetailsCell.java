package com.piggybank.app.layout;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Consumer;

import com.piggybank.server.model.PiggyExpense;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PiggyExpenseDetailsCell extends AbstractPiggyCell<PiggyExpense> {

	private PiggyExpense currentExpense;
	
	private final Label expenseDayLabel;
	private final Label expenseDateLabel;
	private final Label expenseTitleHeaderLabel;
	private final Label expenseTitleLabel;
	private final Label expenseAmountHeaderLabel;
	private final Label expenseAmountLabel;
	private final Button removeExpenseButton;
	
	private final Consumer<PiggyExpense> removeButtonHandler;
	
	{
		this.expenseDayLabel = new Label();
		this.expenseDateLabel = new Label();
		this.expenseTitleHeaderLabel = new Label();
		this.expenseTitleLabel = new Label();
		this.expenseAmountHeaderLabel = new Label();
		this.expenseAmountLabel = new Label();
		this.removeExpenseButton = new Button();
	}
	
	public PiggyExpenseDetailsCell(double width, double height, 
			Consumer<PiggyExpense> removeButtonHandler) {
		super(width, height);
		this.removeButtonHandler = removeButtonHandler;
		
		setColumnConstraints();
		configureComponents();
		configureHeaderLabels();
		configureButtons();
	}
	
	@Override
	protected void addItem(PiggyExpense expense) {
		this.currentExpense = expense;
		this.expenseDayLabel.setText(expense.getDate().getDayOfWeek().name());
		this.expenseDateLabel.setText(expense.getDate().toString());
		this.expenseTitleLabel.setText(expense.getTitle());
		this.expenseAmountLabel.setText(String.format("%.2f", expense.getAmount()));
		setRemoveButtonAvailability();
	}
	
	private final void setRemoveButtonAvailability() {
		LocalDate expenseDate = this.currentExpense.getDate();
		YearMonth expenseYearMonth = YearMonth.of(expenseDate.getYear(), expenseDate.getMonth());
		YearMonth lastYearMonth = YearMonth.now().minusMonths(1);
		
		if (expenseYearMonth.isBefore(lastYearMonth)) {
			this.removeExpenseButton.setDisable(true);
		}
	}
	
	private final void setColumnConstraints() {
		ColumnConstraints columnA = new ColumnConstraints();
		columnA.setPercentWidth(15);

		ColumnConstraints columnB = new ColumnConstraints();
		columnB.setPercentWidth(35);
		
		ColumnConstraints columnC = new ColumnConstraints();
		columnC.setPercentWidth(25);
		
		ColumnConstraints columnD = new ColumnConstraints();
		columnD.setPercentWidth(20);
		
		ColumnConstraints columnE = new ColumnConstraints();
		columnE.setPercentWidth(5);
		
		this.cellGrid.getColumnConstraints().addAll(columnA, columnB, 
				columnC, columnD, columnE);
	}
	
	private final void configureComponents() {
		this.cellGrid.add(this.expenseTitleHeaderLabel, 0, 0);
		this.cellGrid.add(this.expenseTitleLabel, 1, 0);
		this.cellGrid.add(this.expenseAmountHeaderLabel, 0, 1);
		this.cellGrid.add(this.expenseAmountLabel, 1, 1);
		this.cellGrid.add(this.expenseDayLabel, 2, 0, 1, 2);
		this.cellGrid.add(this.expenseDateLabel, 3, 0, 1, 2);
		this.cellGrid.add(this.removeExpenseButton, 4, 0, 1, 2);
		this.cellGrid.setVgap(3);
	}
	
	private final void configureHeaderLabels() {
		this.expenseTitleHeaderLabel.setText("Title");
		this.expenseTitleHeaderLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		this.expenseAmountHeaderLabel.setText("Amount");
		this.expenseAmountHeaderLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
	}
	
	private final void configureButtons() {
		this.removeExpenseButton.setText("-");
		this.removeExpenseButton.setId("list-action-button");
		this.removeExpenseButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				removeButtonHandler.accept(currentExpense);
			}
		});
	}
}
