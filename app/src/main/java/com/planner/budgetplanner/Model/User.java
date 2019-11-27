package com.planner.budgetplanner.Model;

import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.DatabaseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class User extends DatabaseObject {

    private String name;
    private String email;
    private FirebaseManager.LoginType type;
    private ArrayList<Income> incomes = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Expense> expenses = new ArrayList<>();

    private Map<String, Income> incomeVsId = new HashMap<>();
    private Map<String, Expense> expenseVsId = new HashMap<>();
    private Map<String, Category> categoryVsId = new HashMap<>();

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
            if(!incomeVsId.containsKey(income1.id))
                incomeVsId.put(income1.id,income1);
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

    public Income[] getIncomes() {
        return (Income[]) incomes.toArray();
    }

    public Category[] getCategories() {
        return (Category[]) categories.toArray();
    }

    public Expense[] getExpenses() {
        return (Expense[]) expenses.toArray();
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

    public boolean removeCategory(Category category) {
        return categories.remove(category);
    }

    public boolean removeExpense(Expense expense) {
        return expenses.remove(expense);
    }

    public boolean removeIncome(Income income) {
        return incomes.remove(income);
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("type", type.toString());

        return map;
    }
}