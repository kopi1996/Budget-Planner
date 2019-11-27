package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Expense extends BudgetObject {

    private Category category;
    private double amount;


    public Expense(String id, Category category, String title, double amount, String description, Timestamp timestamp) {
        super(id,title,description,timestamp);

        this.category = category;
        this.amount = amount;
        type=BudjetObjectType.EXPENSE;
    }

    public Expense(Category category, String title,String description,Timestamp timestamp, double amount) {
        super("",title,description,timestamp);

        this.category = category;
        this.amount = amount;
        type=BudjetObjectType.EXPENSE;
    }

    public Expense(String id,String title, String description, Category category, double amount) {
        super(id,title, description);
        this.category = category;
        this.amount = amount;
        type=BudjetObjectType.EXPENSE;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = super.toJson();
        map.put("amount",amount);
        map.put("categoryId",category.getId());

        return map;
    }
}
