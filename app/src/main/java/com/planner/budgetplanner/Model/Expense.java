package com.planner.budgetplanner.Model;

public class Expense {

    private Category category;
    private String title;
    private double amount;
    private String description;
    private String date;
    private String time;


    public Expense(Category category, String title, double amount, String description, String date, String time) {
        this.category = category;
        this.title = title;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
