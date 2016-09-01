package com.piggybank.app.controller;

import java.time.LocalDate;
import java.util.function.Predicate;

import com.piggybank.app.util.LayoutManager;
import com.piggybank.app.util.ResourcePathFinder;
import com.piggybank.server.model.PiggyExpense;
import com.piggybank.server.model.PiggyExpenseCategory;
import com.piggybank.server.model.PiggyFutureExpense;
import com.piggybank.server.model.PiggyIncome;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PiggyMainWindowController implements IPBController {
	
	@FXML
	private Pane contentPane;
	
	private IPBController currentController;
	private Stage topStage;
	private Stage mainStage;
	
	private final LayoutManager layoutManager;
	
	public PiggyMainWindowController() {
		this.layoutManager = new LayoutManager();
	}
	
	public void setMainStage(Stage mainStage) {
		this.mainStage = mainStage;
	}
	
	public void showAddExpenseCategoryWindow(PiggyExpenseCategory expenseCategory, 
			Predicate<PiggyExpenseCategory> confirmHandler, LocalDate expenseDate) {
		showAddExpenseCategoryWindow(confirmHandler, expenseDate);
		IPBTopStageController controller = (IPBTopStageController) this.layoutManager.getCurrentController();
		controller.setContent(expenseCategory);
	}
	
	public void showAddExpenseCategoryWindow(Predicate<PiggyExpenseCategory> confirmHandler, 
			LocalDate expenseDate) {
		showNewStage(ResourcePathFinder.PB_PLAN_EXPENSE_VIEW, confirmHandler, expenseDate, "Expense Category");
	}
	
	public void showAddIncomeWindow(PiggyIncome income, Predicate<PiggyIncome> confirmHandler, 
			LocalDate expenseDate) {
		showAddIncomeWindow(confirmHandler, expenseDate);
		IPBTopStageController controller = (IPBTopStageController) this.layoutManager.getCurrentController();
		controller.setContent(income);
	}
	
	public void showAddIncomeWindow(Predicate<PiggyIncome> confirmHandler, 
			LocalDate expenseDate) {
		showNewStage(ResourcePathFinder.PB_PLAN_INCOME_VIEW, confirmHandler, expenseDate, "Income");
	}
	
	public void showAddExpense(PiggyExpense expense, Predicate<PiggyExpense> confirmHandler) {
		showAddExpense(expense.getCategory(), confirmHandler, expense.getDate());
		IPBTopStageController controller = (IPBTopStageController) this.layoutManager.getCurrentController();
		controller.setContent(expense);
	}
	
	public void showAddExpense(PiggyExpenseCategory expenseCategory, 
			Predicate<PiggyExpense> confirmHandler, LocalDate expenseDate) {
		showNewStage(ResourcePathFinder.PB_ADD_EXPENSE, confirmHandler, expenseDate, "Expense");
		
		PiggyAddExpenseController controller = (PiggyAddExpenseController) this.layoutManager.getCurrentController();
		controller.setExpenseCategory(expenseCategory);
	}
	
	public void showAddFutureExpense(PiggyFutureExpense futureExpense, Predicate<PiggyFutureExpense> confirmHandler) {
		showAddFutureExpense(confirmHandler);
		IPBTopStageController controller = (IPBTopStageController) this.layoutManager.getCurrentController();
		controller.setContent(futureExpense);
	}
	
	public void showAddFutureExpense(Predicate<PiggyFutureExpense> confirmHandler) {
		showNewStage(ResourcePathFinder.PB_ADD_FUTURE_EXPENSE, confirmHandler, null, "Future Expense");
	}
	
	public void showEvolveFutureExpenseWindow(PiggyFutureExpense futureExpense, Predicate<PiggyExpense> confirmHandler) {
		showNewStage(ResourcePathFinder.PB_EVOLVE_FUTURE_EXPENSE, confirmHandler, null, "Evolve Future Expense");
		IPBTopStageController controller = (IPBTopStageController) this.layoutManager.getCurrentController();
		controller.setContent(futureExpense);
	}
	
	public void closeTopStage() {
		topStage.close();
	}
	
	private void changeContentView(String viewResourcePath) {
		Scene scene = layoutManager.loadScene(viewResourcePath);
		this.currentController = (IPBController) layoutManager.getCurrentController();
		this.contentPane.getChildren().clear();
		this.contentPane.getChildren().add(scene.getRoot());
	}
	
	@SuppressWarnings("unchecked")
	private <T> void showNewStage(String viewResourcePath, Predicate<T> confirmHandler, 
			LocalDate expenseDate, String stageTitle) {
		Scene scene = layoutManager.loadScene(viewResourcePath);
		
		IPBTopStageController controller = (IPBTopStageController) layoutManager.getCurrentController();
		
		controller.init();
		controller.setDiscardHandler(() -> closeTopStage());
		controller.setConfirmHandler(item -> {
			if (confirmHandler.test((T) item)) {
				closeTopStage();
				return true;
			} else {
				return false;
			}
		});
		controller.setDate(expenseDate);
		
		configureNewStage(scene, stageTitle);
		this.topStage.show();
	}
	
	private void update() {
		this.currentController.init(contentPane.getWidth(), contentPane.getHeight());
		this.currentController.setMainWindowController(this);
	}
	
	private void configureNewStage(Scene scene, String stageTitle) {
		this.topStage = new Stage();
		this.topStage.initModality(Modality.WINDOW_MODAL);
		this.topStage.initOwner(mainStage);
		this.topStage.setResizable(false);
		this.topStage.setTitle(stageTitle);
		this.topStage.setScene(scene);
	}
	
	@FXML
	private void initialize() {
		handleBalanceButtonReleased();
	}
	
	
	@FXML
	private void handleBalanceButtonReleased() {
		changeContentView(ResourcePathFinder.PB_BALANCE_VIEW);
		update();
	}
	
	@FXML
	private void handleMonthPlannerButtonReleased() {
		changeContentView(ResourcePathFinder.PB_MONTH_PLANNER_VIEW);
		update();
	}
	
	@FXML
	private void handleExpensesButtonReleased() {
		changeContentView(ResourcePathFinder.PB_EXPENSES_CATEGORY_VIEW);
		update();
	}
	
	@FXML
	private void handleFutureExpensesButtonReleased() {
		changeContentView(ResourcePathFinder.PB_FUTURE_EXPENSES_VIEW);
		update();
	}
	
	@FXML
	private void handleIncomesButtonReleased() {
		changeContentView(ResourcePathFinder.PB_INCOMES_VIEW);
		update();
	}
	
	@FXML
	private void handleReportsButtonReleased() {
		changeContentView(ResourcePathFinder.PB_REPORTS_VIEW);
		update();
	}
	
	@FXML
	private void handleSavingsButtonReleased() {
		changeContentView(ResourcePathFinder.PB_SAVINGS_VIEW);
		update();
	}
}
