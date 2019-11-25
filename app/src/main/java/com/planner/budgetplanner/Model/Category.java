package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

public class Category extends BudgetObject {

    private double spent;
    private double budget;

    public Category(String id, String title, String description, double spent, double budget, Timestamp timestamp) {
        super(id,title, description, timestamp);
        this.spent = spent;
        this.budget = budget;
    }

    public Category(String id,String title, String description, double spent, double budget) {
        super(id,title, description);
        this.spent = spent;
        this.budget = budget;
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
