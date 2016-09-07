package com.piggybank.app.controller;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;

import com.piggybank.app.layout.PiggyExpenseCell;
import com.piggybank.app.layout.PiggyIncomeCell;
import com.piggybank.app.remote.PiggyBankExpenses;
import com.piggybank.app.remote.PiggyBankIncomes;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyExpenseCategoryName;
import com.piggybank.server.model.PiggyIncome;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class PiggyMonthPlannerController implements IPBController {

	@FXML
	private ChoiceBox<String> monthYearChoiceBox;
	@FXML
	private Label predictedExpensesLabel;
	@FXML
	private Label predictedIncomesLabel;
	@FXML
	private Label predictedBalanceLabel;
	@FXML
	private Button addIncomeButton;
	@FXML
	private Button removeIncomeButton;
	@FXML
	private Button addExpenseButton;
	@FXML
	private Button removeExpenseButton;
	@FXML
	private Pane incomesPane;
	@FXML
	private Pane expensesPane;
	
	private WeakReference<PiggyMainWindowController> mainWindowController;
	
	private ListView<PiggyIncome> incomesList;
	private ListView<PiggyExpenseCategory> expensesList;
	
	private Month selectedMonth;
	private Year selectedYear;
	
	private double currentPredictedExpensesAmount;
	private double currentPredictedIncomesAmount;
	
	private final PiggyBankExpenses piggyExpensesRemote;
	private final PiggyBankIncomes piggyIncomesRemote;
	
	{
		this.piggyExpensesRemote = new PiggyBankExpenses();
		this.piggyIncomesRemote = new PiggyBankIncomes();
		this.currentPredictedExpensesAmount = 0.0;
		this.currentPredictedIncomesAmount = 0.0;
	}
	
	@Override
	public void init() {
		configureContentLists();
		setButtonsAvailability(false);
		configureMonthYearChoiceBox();
		setHeaderLabels();
	}
	
	@Override
	public void setMainWindowController(PiggyMainWindowController mainWindowController) {
		this.mainWindowController = new WeakReference<>(mainWindowController);
	}

	private void configureContentLists() {
		configureIncomesList();
		configureExpensesList();
		insertContentLists();
	}
	
	private void configureIncomesList() {
		double width = incomesPane.getPrefWidth();
		double height = incomesPane.getPrefHeight();
		
		this.incomesList = new ListView<>();
		this.incomesList.setCellFactory(new Callback<ListView<PiggyIncome>, ListCell<PiggyIncome>>() {
			@Override
			public ListCell<PiggyIncome> call(ListView<PiggyIncome> param) {
				return new PiggyIncomeCell(width, height);
			}
		});
		
		this.incomesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				expensesList.getSelectionModel().clearSelection();
				MultipleSelectionModel<PiggyIncome> selectionModel = incomesList.getSelectionModel();
				int selectedIndex = selectionModel.getSelectedIndex();
				
				if (2 == event.getClickCount() && false == selectionModel.isEmpty()) {
					PiggyIncome selectedIncome = selectionModel.getSelectedItem();
					
					mainWindowController.get().showAddIncomeWindow(selectedIncome, 
							income -> {
								boolean editResult = editIncome(selectedIncome, income);
								if (false == editResult) {
									incomesList.getItems().add(selectedIndex, selectedIncome);
								}
								incomesList.getSelectionModel().clearSelection();
								return editResult;
							},
							LocalDate.of(selectedYear.getValue(), selectedMonth, 1));
				}
			}
		});
		
		this.incomesList.setMinSize(width, height);
		this.incomesList.setPrefSize(width, height);
		this.incomesList.setMaxSize(width, height);
	}
	
	private void configureExpensesList() {
		double width = expensesPane.getPrefWidth();
		double height = expensesPane.getPrefHeight();
				
		this.expensesList = new ListView<>();
		this.expensesList.setCellFactory(new Callback<ListView<PiggyExpenseCategory>, ListCell<PiggyExpenseCategory>>() {
			@Override
			public ListCell<PiggyExpenseCategory> call(ListView<PiggyExpenseCategory> param) {
				return new PiggyExpenseCell(width, height);
			}
		});
		
		this.expensesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				incomesList.getSelectionModel().clearSelection();
				MultipleSelectionModel<PiggyExpenseCategory> selectionModel = expensesList.getSelectionModel();
				
				if (2 == event.getClickCount() && false == selectionModel.isEmpty()) {
					PiggyExpenseCategory selectedExpenseCategory = selectionModel.getSelectedItem();
					int selectedItemIndex = selectionModel.getSelectedIndex();
					
					mainWindowController.get().showAddExpenseCategoryWindow(selectedExpenseCategory, 
							expense -> {
								boolean editResult = editExpense(selectedExpenseCategory, expense, 
										selectedItemIndex);
								expensesList.getSelectionModel().clearSelection();
								return editResult;
							},
							LocalDate.of(selectedYear.getValue(), selectedMonth, 1));
				}
			}
		});
		
		this.expensesList.setMinSize(width, height);
		this.expensesList.setPrefSize(width, height);
		this.expensesList.setMaxSize(width, height);
	}
	
	private void insertContentLists() {
		this.incomesPane.getChildren().add(this.incomesList);
		this.expensesPane.getChildren().add(this.expensesList);
	}
		
	private void configureMonthYearChoiceBox() {
		setChoiceBoxHandler();
		configureMonthYearItems();
	}
	
	private void configureMonthYearItems() {
		ObservableList<String> monthYearList = FXCollections.observableArrayList();
		LocalDate currentDate = LocalDate.now();
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int monthOffset = 0; monthOffset < 3; ++monthOffset) {
			String monthYearString = stringBuilder.append(currentDate.getMonth().toString())
					.append(" ").append(String.valueOf(currentDate.getYear())).toString();
			monthYearList.add(monthYearString);
			
			stringBuilder.delete(0, stringBuilder.length());
			currentDate = currentDate.plusMonths(1);
		}
		
		this.monthYearChoiceBox.setItems(monthYearList);
		this.monthYearChoiceBox.setValue(monthYearList.get(0));
	}
	
	private void setChoiceBoxHandler() {
		this.monthYearChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldMonthYear,
					String newMonthYear) {
				if (null != newMonthYear) {
					setContentButtonsAvailability(true);
					setSelectedDate(newMonthYear);
					refreshContentLists();
					refreshHeaderLabels();
				}
			}
		});
	}
	
	private void setSelectedDate(String monthYear) {
		String[] monthYearTable = monthYear.split(" ");
		
		this.selectedMonth = Month.valueOf(monthYearTable[0]);
		this.selectedYear = Year.of(Integer.parseInt(monthYearTable[1]));
	}
	
	private void refreshHeaderLabels() {
		this.currentPredictedIncomesAmount = 0.0;
		this.currentPredictedExpensesAmount = 0.0;
		
		this.incomesList.getItems()
			.forEach(income -> currentPredictedIncomesAmount += income.getAmount());
		this.expensesList.getItems()
			.forEach(expenseCategory -> currentPredictedExpensesAmount += expenseCategory.getPredictedAmount());

		setHeaderLabels();
	}
	
	private void setHeaderLabels() {
		this.predictedExpensesLabel.setText(String.valueOf("-" + this.currentPredictedExpensesAmount));
		this.predictedIncomesLabel.setText(String.valueOf("+" + this.currentPredictedIncomesAmount));
		setPredictedBalanceLabel();
	}
	
	private void setPredictedBalanceLabel() {
		double predictedBalance = this.currentPredictedIncomesAmount - this.currentPredictedExpensesAmount;

		String predictedBalanceSign = "";
		if (predictedBalance > 0.0) {
			predictedBalanceSign = "+";
		}
		
		this.predictedBalanceLabel.setText(predictedBalanceSign + String.valueOf(predictedBalance));
	}
	
	private void refreshContentLists() {
		clearLists();
		insertIncomesList();
		insertExpensesList();
	}
	
	private void insertIncomesList() {
		List<PiggyIncome> incomesList = this.piggyIncomesRemote.getIncomesByMonthYear(
				this.selectedMonth, this.selectedYear);
		Collections.sort(incomesList);
		this.incomesList.setItems(FXCollections.observableArrayList(incomesList));
	}
	
	private void insertExpensesList() {
		List<PiggyExpenseCategory> expensesList = this.piggyExpensesRemote.getExpensesCategoriesByMonthYear(
				this.selectedMonth, this.selectedYear);
		Collections.sort(expensesList);
		this.expensesList.setItems(FXCollections.observableArrayList(expensesList));
		expensesList.forEach(expenseCategory -> {
			this.piggyExpensesRemote.setPiggyCategoryActiveFlag(expenseCategory.getName(), false);
		});
	}
	
	private void clearLists() {
		this.expensesList.getItems().forEach(expenseCategory -> {
			this.piggyExpensesRemote.setPiggyCategoryActiveFlag(expenseCategory.getName(), true);
		});
		
		this.incomesList.getItems().clear();
		this.expensesList.getItems().clear();
	}
	
	private void setButtonsAvailability(boolean available) {
		setContentButtonsAvailability(available);
	}
	
	private void setContentButtonsAvailability(boolean available) {
		this.addIncomeButton.setDisable(!available);
		this.removeIncomeButton.setDisable(!available);
		this.addExpenseButton.setDisable(!available);
		this.removeExpenseButton.setDisable(!available);
	}
	
	private void addToIncomesList(PiggyIncome income) {
		List<PiggyIncome> currentIncomes = this.incomesList.getItems();
		if (false == currentIncomes.contains(income)) {
			currentIncomes.add(income);
			Collections.sort(this.incomesList.getItems());
		}
	}
	
	private void addToExpenseCategoriesList(PiggyExpenseCategory expenseCategory) {
		this.expensesList.getItems().add(expenseCategory);
		Collections.sort(this.expensesList.getItems());
	}
	
	private boolean addIncome(PiggyIncome income) {
		boolean newIncomeFlag = this.incomesList.getItems().stream()
				.noneMatch(listIncome -> listIncome.getName().equals(income.getName()));
		
		if (newIncomeFlag) {
			PiggyIncome newIncome = this.piggyIncomesRemote.addIncome(income);
			
			addToIncomesList(newIncome);
			currentPredictedIncomesAmount += newIncome.getAmount();
			setHeaderLabels();
		}
		
		return newIncomeFlag;
	}
	
	private boolean editIncome(PiggyIncome oldIncome, PiggyIncome newIncome) {
		currentPredictedIncomesAmount -= oldIncome.getAmount();
		incomesList.getItems().remove(oldIncome);
		
		boolean addResult = addIncome(newIncome);
		if (addResult) {
			this.piggyIncomesRemote.removeIncome(oldIncome);
		}
		return addResult;
	}
	
	private boolean addExpense(PiggyExpenseCategory expenseCategory) {
		PiggyExpenseCategory newExpenseCategory = this.piggyExpensesRemote.addExpenseCategory(expenseCategory);
		addToExpenseCategoriesList(newExpenseCategory);
		currentPredictedExpensesAmount += newExpenseCategory.getPredictedAmount();
		setHeaderLabels();
		
		return true;
	}
	
	private boolean editExpense(PiggyExpenseCategory oldExpenseCategory, 
			PiggyExpenseCategory newExpenseCategory, int selectedItemIndex) {
		newExpenseCategory.setExpenseCategoryId(oldExpenseCategory.getExpenseCategoryId());
		newExpenseCategory.setRealAmount(oldExpenseCategory.getRealAmount());
		
		currentPredictedExpensesAmount -= oldExpenseCategory.getPredictedAmount();
		currentPredictedExpensesAmount += newExpenseCategory.getPredictedAmount();
		
		this.piggyExpensesRemote.updateExpenseCategory(newExpenseCategory);
		
		expensesList.getItems().remove(oldExpenseCategory);
		expensesList.getItems().add(selectedItemIndex, newExpenseCategory);
		
		setHeaderLabels();
		
		return true;
	}
	
	@FXML
	private void handleAddIncomeButtonReleased() {
		this.mainWindowController.get().showAddIncomeWindow(
				income -> addIncome(income),
				LocalDate.of(this.selectedYear.getValue(), this.selectedMonth, 1));
	}
	
	@FXML
	private void handleRemoveIncomeButtonReleased() {
		MultipleSelectionModel<PiggyIncome> selectionModel = this.incomesList.getSelectionModel();
		
		if (false == selectionModel.isEmpty()) {
			PiggyIncome selectedIncome = selectionModel.getSelectedItem();
			currentPredictedIncomesAmount -= selectedIncome.getAmount();
			
			this.incomesList.getItems().remove(selectionModel.getSelectedIndex());
			setHeaderLabels();
			
			this.piggyIncomesRemote.removeIncome(selectedIncome);
		}
	}
	
	@FXML
	private void handleAddExpenseButtonReleased() {
		this.mainWindowController.get().showAddExpenseCategoryWindow(
				expenseCategory -> addExpense(expenseCategory), 
				LocalDate.of(this.selectedYear.getValue(), this.selectedMonth, 1));
	}
	
	@FXML
	private void handleRemoveExpenseButtonReleased() {
		MultipleSelectionModel<PiggyExpenseCategory> selectionModel = this.expensesList.getSelectionModel();
		
		if (false == selectionModel.isEmpty()) {
			PiggyExpenseCategory selectedExpenseCategory = selectionModel.getSelectedItem();
			PiggyExpenseCategoryName expenseCategoryName = selectedExpenseCategory.getName();
			currentPredictedExpensesAmount -= selectedExpenseCategory.getPredictedAmount();
			
			this.piggyExpensesRemote.setPiggyCategoryActiveFlag(expenseCategoryName, true);
			this.expensesList.getItems().remove(selectionModel.getSelectedIndex());
			setHeaderLabels();
			
			this.piggyExpensesRemote.removeExpenseCategory(selectedExpenseCategory);
		}
	}
}
