package com.planner.budgetplanner.Model;

import android.os.Build;
import android.util.Log;

import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.DatabaseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class User extends DatabaseObject {

    private String name;
    private String email;
    private String currencyType="";


    private FirebaseManager.LoginType type;
    private ArrayList<Income> incomes = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Expense> expenses = new ArrayList<>();

    private Map<String, Income> incomeVsId = new HashMap<>();
    private Map<String, Expense> expenseVsId = new HashMap<>();
    private Map<String, Category> categoryVsId = new HashMap<>();
    private static final String TAG="User";

    public User(String id, String name, String email, FirebaseManager.LoginType type) {
        super(id);
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public User(String id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
    }

    public FirebaseManager.LoginType getType() {
        return type;
    }

    public void setType(FirebaseManager.LoginType type) {
        this.type = type;
    }

    public User() {
        super("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addIncomes(Income... income) {
        for (Income income1 : income) {
            if (!incomeVsId.containsKey(income1.id))
                incomeVsId.put(income1.id, income1);
        }
        incomes.addAll(Arrays.asList(income));
    }

    public void addCategories(Category... obj) {
        for (Category cat : obj) {
            if(!categoryVsId.containsKey(cat.id))
                categoryVsId.put(cat.id,cat);
        }
        categories.addAll(Arrays.asList(obj));
    }

    public void addExpenses(Expense... obj) {
        for (Expense expense : obj) {
            if (!expenseVsId.containsKey(expense.id))
                expenseVsId.put(expense.id, expense);
        }
        expenses.addAll(Arrays.asList(obj));
    }

    public Expense[] getExpensesForCategory(String categoryId) {
        ArrayList<Expense> tempList = new ArrayList<>();
        Log.i(TAG, "getExpensesForCategoryCo: "+expenses.size());

        for (Expense expense : expenses) {
            Log.i(TAG, "getExpensesForCategory: "+expense.getCategoryId());
            if (expense.getCategoryId().equals(categoryId)) {
                tempList.add(expense);
            }
        }

        return tempList.toArray(new Expense[tempList.size()]);
    }

    public Income[] getIncomes() {
        return incomes.toArray(new Income[incomes.size()]);
    }

    public Category[] getCategories() {
        return categories.toArray(new Category[categories.size()]);
    }

    public Expense[] getExpenses() {
        return expenses.toArray(new Expense[expenses.size()]);
    }

    public Category getCategoryForId(String id)
    {
        return categoryVsId.get(id);
    }

    public Income getIncomeForId(String id)
    {
        return incomeVsId.get(id);
    }

    public Expense getExpenseForId(String id)
    {
        return expenseVsId  .get(id);
    }

    public boolean removeCategories(Category... category) {
        for (Category category1 : category) {
            categoryVsId.remove(category1.getId());
        }
        return categories.removeAll(Arrays.asList(category));
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public boolean removeExpenses(Expense... expense) {
        for (Expense expense1 : expense) {
            expenseVsId.remove(expense1.getId());
        }
        for (int i = 0; i < expense.length; i++) {
            expenses.remove(expense[i]);
        }
        return true;
    }

    public boolean removeIncomes(Income... income) {
        for (Income income1 : income) {
            incomeVsId.remove(income1.getId());
        }
        return incomes.removeAll(Arrays.asList(income));
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("type", type.toString());
        map.put("currencyType",currencyType);

        return map;
    }
}