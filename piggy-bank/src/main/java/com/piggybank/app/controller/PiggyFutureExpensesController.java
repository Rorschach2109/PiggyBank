package com.piggybank.app.controller;

import java.lang.ref.WeakReference;
import java.time.Year;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import com.piggybank.app.layout.PiggyFutureExpenseCell;
import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyFutureExpense;
import com.piggybank.server.model.PiggySaving;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class PiggyFutureExpensesController implements IPBController {

	@FXML
	private Label totalSavingsLabel;
	@FXML
	private Label totalFutureExpensesLabel;
	@FXML
	private Pane futureExpensesPane;
	
	private ListView<PiggyFutureExpense> futureExpensesList;
	private WeakReference<PiggyMainWindowController> mainWindowController;
	private final PiggyBankExpenses piggyExpensesRemote;
	private final PiggyBankIncomes piggyIncomesRemote;
	
	{
		this.piggyExpensesRemote = new PiggyBankExpenses();
		this.piggyIncomesRemote = new PiggyBankIncomes();
	}
	
	@Override
	public final void init() {
		createFutureExpensesListView();
		setFutureExpensesHandlers();
		insertFutureExpensesList();
		setTotalSavingsLabel();
		update();
	}
	
	@Override
	public final void setMainWindowController(PiggyMainWindowController mainWindowController) {
		this.mainWindowController = new WeakReference<>(mainWindowController);
	}
	
	private void update() {
		setFutureExpensesContent();
		setHeaderLabels();
	}
	
	private void createFutureExpensesListView() {
		this.futureExpensesList = new ListView<>();
		
		this.futureExpensesList.setCellFactory(new Callback<ListView<PiggyFutureExpense>, ListCell<PiggyFutureExpense>>() {
			@Override
			public ListCell<PiggyFutureExpense> call(ListView<PiggyFutureExpense> param) {
				return new PiggyFutureExpenseCell(futureExpensesPane.getPrefWidth(), futureExpensesPane.getPrefHeight(), 
						futureExpense -> removeFutureExpense(futureExpense), 
						futureExpense -> changeToExpense(futureExpense));
			}
		});
		
		this.futureExpensesList.setMinSize(this.futureExpensesPane.getPrefWidth(), this.futureExpensesPane.getPrefHeight());
		this.futureExpensesList.setMaxSize(this.futureExpensesPane.getPrefWidth(), this.futureExpensesPane.getPrefHeight());
	}
	
	private void setFutureExpensesHandlers() {
		this.futureExpensesList.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MultipleSelectionModel<PiggyFutureExpense> selectionModel = futureExpensesList.getSelectionModel();
				
				int selectedIndex = selectionModel.getSelectedIndex();
				
				if (2 == event.getClickCount() && false == selectionModel.isEmpty()) {
					PiggyFutureExpense selectedFutureExpense = selectionModel.getSelectedItem();
					
					mainWindowController.get().showAddFutureExpense(selectionModel.getSelectedItem(),
							futureExpense -> {
								boolean editResult = editFutureExpense(selectedFutureExpense, futureExpense);
								if (false == editResult) {
									futureExpensesList.getItems().add(selectedIndex, selectedFutureExpense);
								}
								futureExpensesList.getSelectionModel().clearSelection();
								return editResult;
							});
				}
			}
		});
	}

	private void insertFutureExpensesList() {
		this.futureExpensesPane.getChildren().clear();
		this.futureExpensesPane.getChildren().add(this.futureExpensesList);
	}
	
	private void setFutureExpensesContent() {
		List<PiggyFutureExpense> futureExpenses = this.piggyExpensesRemote.getFutureExpenses();
		Collections.sort(futureExpenses);
		
		this.futureExpensesList.setItems(FXCollections.observableArrayList(futureExpenses));
	}
	
	private void setTotalSavingsLabel() {
		double totalSavings = 0.0;
		YearMonth currentYearMonth = YearMonth.now();
		
		List<PiggySaving> savings = this.piggyIncomesRemote.getSavingsTillMonthYear(
				currentYearMonth.getMonth(), Year.of(currentYearMonth.getYear()));
		for (PiggySaving saving : savings) {
			totalSavings += saving.getAmount();
		}
		
		this.totalSavingsLabel.setText(String.valueOf(totalSavings));
	}
	
	private void setHeaderLabels() {
		double totalFutureExpensesAmount = 0.0;
		for (PiggyFutureExpense futureExpense : this.futureExpensesList.getItems()) {
			totalFutureExpensesAmount += futureExpense.getAmount();
		}
		
		this.totalFutureExpensesLabel.setText(String.valueOf(totalFutureExpensesAmount));
	}
	
	private boolean addFutureExpense(PiggyFutureExpense futureExpense) {
		boolean newFutureExpenseFlag = this.futureExpensesList.getItems().stream()
				.noneMatch(existingFutureExpense -> existingFutureExpense.equals(futureExpense));
		
		if (newFutureExpenseFlag) {
			PiggyFutureExpense newFutureExpense = this.piggyExpensesRemote.addFutureExpense(futureExpense);
			
			ObservableList<PiggyFutureExpense> futureExpenses = this.futureExpensesList.getItems();
			futureExpenses.add(newFutureExpense);
			Collections.sort(futureExpenses);
			this.futureExpensesList.setItems(futureExpenses);
		}
		
		setHeaderLabels();
		
		return newFutureExpenseFlag;
	}
	
	private void removeFutureExpense(PiggyFutureExpense futureExpense) {
		this.piggyExpensesRemote.removeFutureExpense(futureExpense);
		this.futureExpensesList.getItems().remove(futureExpense);
		
		setHeaderLabels();
	}
	
	private boolean editFutureExpense(PiggyFutureExpense selectedFutureExpense, PiggyFutureExpense futureExpense) {
		removeFutureExpense(selectedFutureExpense);
		return addFutureExpense(futureExpense);
	}
	
	private boolean addExpense(PiggyExpense expense) {
		PiggyExpenseCategory expenseCategory = expense.getCategory();
		expenseCategory.getExpenses().add(expense);
		
		List<PiggyExpenseCategory> expenseCategoriesList = this.piggyExpensesRemote.getExpensesCategoriesByMonthYearName(
				expenseCategory.getMonth(), expenseCategory.getYear(), expenseCategory.getName().getName());
		
		if (expenseCategoriesList.isEmpty()) {
			this.piggyExpensesRemote.addExpenseCategory(expenseCategory);
			return true;
		}
		
		PiggyExpenseCategory existingExpenseCategory = expenseCategoriesList.get(0);
		boolean uniqueExpense = existingExpenseCategory.getExpenses().stream()
				.noneMatch(existingExpense -> existingExpense.equals(expense));
		if (uniqueExpense) {
			this.piggyExpensesRemote.addExpense(expense);
		}
		return uniqueExpense;
	}
	
	private void changeToExpense(PiggyFutureExpense futureExpense) {
		this.mainWindowController.get().showEvolveFutureExpenseWindow(futureExpense, 
				piggyExpense -> {
					boolean addExpenseResult = addExpense(piggyExpense);
					if (addExpenseResult) {
						removeFutureExpense(futureExpense);
						setHeaderLabels();
					}
					return addExpenseResult;
				});
	}
	
	@FXML
	private void handleAddFutureExpenseButton() {
		this.mainWindowController.get().showAddFutureExpense(futureExpense -> addFutureExpense(futureExpense));
	}
}
