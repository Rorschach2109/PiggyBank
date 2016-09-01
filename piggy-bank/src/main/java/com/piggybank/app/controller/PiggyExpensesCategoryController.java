package com.piggybank.app.controller;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;

import com.piggybank.app.layout.PiggyExpenseCell;
import com.piggybank.app.layout.PiggyExpenseDetailsCell;
import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.util.LocalDateFormatter;
import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class PiggyExpensesCategoryController implements IPBController {

	@FXML
	private Label headerLabel;
	@FXML
	private Label monthYearLabel;
	@FXML
	private Label firstHeaderTextLabel;
	@FXML
	private Label firstHeaderLabel;
	@FXML
	private Label secondHeaderTextLabel;
	@FXML
	private Label secondHeaderLabel;
	@FXML
	private Pane expensesPane;
	@FXML
	private HBox headerBox;
	@FXML
	private GridPane headerGrid;
	@FXML
	private Button nextMonthButton;
	@FXML
	private Button previousMonthButton;
	
	private Button addExpenseButton;
	private Button returnButton;
	
	private double contentListWidth;
	private double contentListHeight;
	
	private WeakReference<PiggyMainWindowController> mainWindowController;
	private LocalDate currentDate;
	private ListView<PiggyExpenseCategory> expenseCategoriesListView;
	private ListView<PiggyExpense> expensesListView;
	private PiggyExpenseCategory selectedExpenseCategory;
	
	private final PiggyBankExpenses piggyExpensesRemote;
	
	{
		this.currentDate = LocalDate.now();
		this.piggyExpensesRemote = new PiggyBankExpenses();
	}
	
	@Override
	public void setMainWindowController(PiggyMainWindowController mainWindowController) {
		this.mainWindowController = new WeakReference<>(mainWindowController);
	}
	
	@Override
	public final void init(double width, double height) {
		this.contentListWidth = width;
		this.contentListHeight = height;
		
		createExpenseCategoryListView();
		setExpenseCategoryListHandlers();
		createExpenseListView();
		setExpenseListHandlers();
		configureButtons();
		insertExpenseCategoriesList();
		update();
	}
	
	private void update() {
		setExpensesContent();
		setCurrentMonthYear();
		setExpenseCategoriesHeaderLabels();
		setButtonsAvailability(false);
		setHeaders(false);
	}
	
	private void setCurrentMonthYear() {
		this.monthYearLabel.setText(
				LocalDateFormatter.getMonthYearStringFromDate(this.currentDate));
	}
	
	private void setExpenseCategoriesSummaryLabels() {
		double predictedExpenseAmount = 0.0;
		double realExpenseAmount = 0.0;
		
		for (PiggyExpenseCategory expenseCategory : this.expenseCategoriesListView.getItems()) {
			predictedExpenseAmount += expenseCategory.getPredictedAmount();
			realExpenseAmount += expenseCategory.getRealAmount();
		}
		
		this.secondHeaderLabel.setText(String.valueOf(predictedExpenseAmount));
		this.firstHeaderLabel.setText(String.valueOf(realExpenseAmount));
		setSummaryLabelsColor();
	}
	
	private void setExpensesSummaryLabels() {
		double categoryExpensesAmount = 0.0;
		for (PiggyExpense expense : this.expensesListView.getItems()) {
			categoryExpensesAmount += expense.getAmount();
		}
		
		this.firstHeaderLabel.setText(String.valueOf(categoryExpensesAmount));
		this.secondHeaderLabel.setText(String.valueOf(this.selectedExpenseCategory.getPredictedAmount()));
		setSummaryLabelsColor();
	}
	
	private void setSummaryLabelsColor() {
		double firstHeaderValue = Double.parseDouble(this.firstHeaderLabel.getText());
		double secondHeaderValue = Double.parseDouble(this.secondHeaderLabel.getText());
		double ratio = firstHeaderValue / secondHeaderValue;
		
		Color firstHeaderColor = Color.GREEN;
		if (ratio > 0.5 && ratio <= 0.8) {
			firstHeaderColor = Color.ORANGE;
		} else if (ratio > 0.8) {
			firstHeaderColor = Color.RED;
		}
		
		this.firstHeaderLabel.setTextFill(firstHeaderColor);
	}
	
	private void setExpenseCategoriesHeaderLabels() {
		this.firstHeaderTextLabel.setText("Total expenses:");
		this.secondHeaderTextLabel.setText("Predicted expenses:");
		setExpenseCategoriesSummaryLabels();
	}

	private void setExpensesDetailsHeaderLabels() {
		String expenseCategoryName = this.selectedExpenseCategory.getName().getName();
		this.firstHeaderTextLabel.setText(expenseCategoryName +  " expenses:");
		this.secondHeaderTextLabel.setText(expenseCategoryName + " predicted expenses:");
		setExpensesSummaryLabels();
	}
	
	private void configureButtons() {
		createAddExpenseButton();
		setAddExpenseButtonHandler();
		createReturnButton();
		setReturnButtonHandler();
	}
	
	private void createAddExpenseButton() {
		this.addExpenseButton = new Button("Add Expense");
		this.headerGrid.add(this.addExpenseButton, 0, 0);
	}
	
	private void setAddExpenseButtonHandler() {
		this.addExpenseButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleAddExpense(selectedExpenseCategory);
			}
		});
	}
	
	private void createReturnButton() {
		this.returnButton = new Button("Return to summary");
		this.headerGrid.add(this.returnButton, 0, 1);
	}
	
	private void setReturnButtonHandler() {
		this.returnButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				createExpenseCategoryListView();
				setExpenseCategoryListHandlers();
				insertExpenseCategoriesList();
				update();
			}
		});
	}
	
	private void setButtonsAvailability(boolean available) {
		this.addExpenseButton.setDisable(!available);
		this.addExpenseButton.setVisible(available);
		this.returnButton.setDisable(!available);
		this.returnButton.setVisible(available);
		
		this.nextMonthButton.setDisable(available);
		this.nextMonthButton.setVisible(!available);
		this.previousMonthButton.setDisable(available);
		this.previousMonthButton.setVisible(!available);
	}
	
	private void setHeaders(boolean expensesDetailsViewFlag) {
		if (expensesDetailsViewFlag) {
			this.headerLabel.setText(selectedExpenseCategory.getName().getName() + " expenses");
		} else {
			this.headerLabel.setText("Expenses in");
		}
	}
	
	private void createExpenseCategoryListView() {
		this.expenseCategoriesListView = new ListView<>();
		
		this.expenseCategoriesListView.setCellFactory(new Callback<ListView<PiggyExpenseCategory>, ListCell<PiggyExpenseCategory>>() {
			@Override
			public ListCell<PiggyExpenseCategory> call(ListView<PiggyExpenseCategory> param) {
				return new PiggyExpenseCell(expensesPane.getWidth(), expensesPane.getHeight());
			}
		});
		
		double listHeight = this.contentListHeight - (headerBox.getPrefHeight() + headerGrid.getPrefHeight());
		this.expenseCategoriesListView.setMinSize(this.contentListWidth, listHeight);
		this.expenseCategoriesListView.setMaxSize(this.contentListWidth, listHeight);
	}
	
	private void createExpenseListView() {
		this.expensesListView = new ListView<>();
		
		this.expensesListView.setCellFactory(new Callback<ListView<PiggyExpense>, ListCell<PiggyExpense>>() {
			@Override
			public ListCell<PiggyExpense> call(ListView<PiggyExpense> param) {
				return new PiggyExpenseDetailsCell(expensesPane.getWidth(), expensesPane.getHeight(),
						expense -> handleRemoveExpense(expense));
			}
		});
		
		double listHeight = this.contentListHeight - (headerBox.getPrefHeight() + headerGrid.getPrefHeight());
		this.expensesListView.setMinSize(this.contentListWidth, listHeight);
		this.expensesListView.setMaxSize(this.contentListWidth, listHeight);
	}
	
	private void setExpenseCategoryListHandlers() {
		this.expenseCategoriesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MultipleSelectionModel<PiggyExpenseCategory> selectionModel = expenseCategoriesListView.getSelectionModel();
				if (false == selectionModel.isEmpty() && 
						2 == event.getClickCount()) {
					selectedExpenseCategory = selectionModel.getSelectedItem();
					handleExpenseCategorySelection(selectedExpenseCategory);
					setExpensesDetailsHeaderLabels();
					setButtonsAvailability(true);
					setHeaders(true);
				}
			}
		});
	}
	
	private void setExpenseListHandlers() {
		this.expensesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MultipleSelectionModel<PiggyExpense> selectionModel = expensesListView.getSelectionModel();
				if (false == selectionModel.isEmpty() && 
						2 == event.getClickCount()) {
					PiggyExpense selectedExpense = selectionModel.getSelectedItem();
					handleEditExpense(selectedExpense, 
							() -> expensesListView.getSelectionModel().clearSelection());
				}
			}
		});
	}
	
	private void insertExpenseCategoriesList() {
		this.expensesPane.getChildren().clear();
		this.expensesPane.getChildren().add(this.expenseCategoriesListView);
	}
	
	private void insertExpensesList() {
		this.expensesPane.getChildren().clear();
		this.expensesPane.getChildren().add(this.expensesListView);
	}
	
	private void setExpensesContent() {
		List<PiggyExpenseCategory> expenseCategoriesList = this.piggyExpensesRemote.getExpensesCategoriesByMonthYear(
				this.currentDate.getMonth(), Year.of(this.currentDate.getYear()));
		Collections.sort(expenseCategoriesList);
		
		this.expenseCategoriesListView.setItems(FXCollections.observableArrayList(expenseCategoriesList));
	}
	
	private void handleExpenseCategorySelection(PiggyExpenseCategory selectedExpenseCategory) {
		List<PiggyExpense> expensesList = selectedExpenseCategory.getExpenses();
		Collections.sort(expensesList);
		
		this.expensesListView.setItems(
				FXCollections.observableArrayList(expensesList));
		insertExpensesList();
	}
	
	private void handleAddExpense(PiggyExpenseCategory expenseCategory) {
		this.mainWindowController.get().showAddExpense(expenseCategory, 
				expense -> {
					expense.setCategory(expenseCategory);
					boolean uniqueFlag = isExpenseUnique(expense);
					if (uniqueFlag) {
						persistExpense(expense);
					}
					
					return uniqueFlag;
				}, this.currentDate);
	}
	
	private void handleEditExpense(PiggyExpense selectedExpense, Runnable onCloseAction) {
		this.mainWindowController.get().showAddExpense(selectedExpense, 
				expense -> {
					expense.setCategory(selectedExpense.getCategory());
					boolean uniqueFlag = isExpenseUnique(expense);
					if (uniqueFlag) {
						handleRemoveExpense(selectedExpense);
						expense.setCategory(selectedExpenseCategory);
						persistExpense(expense);
						onCloseAction.run();
					}
					
					return uniqueFlag;
				});
	}
	
	private void handleRemoveExpense(PiggyExpense expense) {
		expense.setCategory(this.selectedExpenseCategory);
		this.expensesListView.getItems().remove(expense);
		
		this.selectedExpenseCategory = this.piggyExpensesRemote.removeExpense(expense);
		this.setExpensesDetailsHeaderLabels();
	}
	
	private void persistExpense(PiggyExpense expense) {
		ObservableList<PiggyExpense> expensesList =  expensesListView.getItems();
		
		expensesList.add(expense);
		Collections.sort(expensesList);
		expensesListView.setItems(expensesList);
		
		this.selectedExpenseCategory = this.piggyExpensesRemote.addExpense(expense);
		this.setExpensesDetailsHeaderLabels();
	}
	
	private boolean isExpenseUnique(PiggyExpense expense) {
		return this.expensesListView.getItems().stream()
				.noneMatch(existingExpense -> existingExpense.equals(expense));
	}
	
	@FXML
	private void handlePreviousButtonReleased() {
		this.currentDate = this.currentDate.minusMonths(1);
		update();
	}
	
	@FXML
	private void handleNextButtonReleased() {
		this.currentDate = this.currentDate.plusMonths(1);
		update();
	}
}
