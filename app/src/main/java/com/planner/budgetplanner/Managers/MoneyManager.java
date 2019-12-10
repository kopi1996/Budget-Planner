package com.planner.budgetplanner.Managers;

import android.util.Log;

import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.Model.User;

import java.util.List;

import static com.planner.budgetplanner.FirebaseManager.TAG;

public class MoneyManager {

    public static double totalIncome(User user) {
        double incomeAmount = 0;
        for (Income income : user.getIncomes()) {
            incomeAmount += income.getAmount();
        }
        return incomeAmount;
    }

    public static double totalIncome(List<Income> incomes)
    {
        double incomeAmount=0;
        for (Income income : incomes) {
            incomeAmount += income.getAmount();
        }

        return incomeAmount;
    }

    public static double totalExpenditure(List<Expense> expenses) {
        double expenseAmount = 0;
        for (Expense expense : expenses) {
            expenseAmount += expense.getAmount();
        }

        return expenseAmount;
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
            Log.i(TAG, "totalSpentForCategory: " + expense.getId());
            spent += expense.getAmount();
        }

        return spent;
    }
}