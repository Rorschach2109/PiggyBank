package com.piggybank.app.util;

public final class ResourcePathFinder {
	
	public static final String PB_MAIN_WINDOW;
	public static final String PB_BALANCE_VIEW;
	public static final String PB_MONTH_PLANNER_VIEW;
	public static final String PB_PLAN_EXPENSE_VIEW;
	public static final String PB_PLAN_INCOME_VIEW;
	public static final String PB_EXPENSES_CATEGORY_VIEW;
	public static final String PB_EXPENSES_DETAILS_VIEW;
	public static final String PB_FUTURE_EXPENSES_VIEW;
	public static final String PB_ADD_EXPENSE;
	public static final String PB_ADD_FUTURE_EXPENSE;
	public static final String PB_EVOLVE_FUTURE_EXPENSE;
	public static final String PB_INCOMES_VIEW;
	public static final String PB_REPORTS_VIEW;
	public static final String PB_SAVINGS_VIEW;
	
	static {
		PB_MAIN_WINDOW = "view/PBMainWindow.fxml";
		PB_BALANCE_VIEW = "view/PBBalanceView.fxml";
		PB_MONTH_PLANNER_VIEW = "view/PBMonthPlannerView.fxml";
		PB_PLAN_EXPENSE_VIEW = "view/PBPlanExpenseView.fxml";
		PB_PLAN_INCOME_VIEW = "view/PBPlanIncomeView.fxml";
		PB_EXPENSES_CATEGORY_VIEW = "view/PBExpensesCategoryView.fxml";
		PB_EXPENSES_DETAILS_VIEW = "view/PBExpensesDetailsView.fxml";
		PB_FUTURE_EXPENSES_VIEW = "view/PBFutureExpensesView.fxml";
		PB_ADD_EXPENSE = "view/PBAddExpense.fxml";
		PB_ADD_FUTURE_EXPENSE = "view/PBAddFutureExpense.fxml";
		PB_EVOLVE_FUTURE_EXPENSE = "view/PBEvolveFutureExpenseView.fxml";
		PB_INCOMES_VIEW = "view/PBIncomesView.fxml";
		PB_REPORTS_VIEW = "view/PBReportsView.fxml";
		PB_SAVINGS_VIEW = "view/PBSavingsView.fxml";
	}
	
	private ResourcePathFinder() {
		
	}
}
