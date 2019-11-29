package com.planner.budgetplanner.Managers;

import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.Model.User;

public class MoneyManager {

    public static double totalIncome(User user) {
        double incomeAmount = 0;
        for (Income income : user.getIncomes()) {
            incomeAmount += income.getAmount();
        }
        return incomeAmount;
    }

    public static double totalExpenditure(User user) {
        double expenseAmount = 0;
        for (Category category : user.getCategories()) {
            expenseAmount += category.getSpent();
        }

        return expenseAmount;
    }

    public static double totalBudgeted(User user) {
        double budgeted = 0;
        for (Category category : user.getCategories()) {
            budgeted += category.getBudget();
        }

        return budgeted;
    }

    public static double totalSpentForCategory(User user,String categoryId)
    {
        double spent=0;
        for (Expense expense : user.getExpensesForCategory(categoryId)) {
            spent=expense.getAmount();
        }

        return spent;
    }
}