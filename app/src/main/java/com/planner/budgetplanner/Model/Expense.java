package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

public class Expense extends BudgetObject {

    private Category category;
    private double amount;


    public Expense(String id, Category category, String title, double amount, String description, Timestamp timestamp) {
        super(id,title,description,timestamp);

        this.category = category;
        this.amount = amount;
    }

    public Expense(String id,String title, String description, Category category, double amount) {
        super(id,title, description);
        this.category = category;
        this.amount = amount;
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

}
