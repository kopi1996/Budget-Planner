package com.planner.budgetplanner.Model;

public class Category {

    private String title;
    private double spent;
    private double budget;

    public Category(String title, double spent, double budget) {
        this.title = title;
        this.spent = spent;
        this.budget = budget;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getRemaining()
    {
        return budget- spent;
    }
}
