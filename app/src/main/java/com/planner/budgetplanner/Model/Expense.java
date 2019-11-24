package com.planner.budgetplanner.Model;

public class Expense extends BudgetObject {

    private Category category;
    private double amount;


    public Expense(String id,Category category, String title, double amount, String description, String date, String time) {
        super(id,title,description,date,time);

        this.category = category;
        this.amount = amount;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
