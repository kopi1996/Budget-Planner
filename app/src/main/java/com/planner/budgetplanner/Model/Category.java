package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Category extends BudgetObject {

    private double spent;
    private double budget;

    public Category(String id, String title, String description, double spent, double budget, Timestamp timestamp) {
        super(id,title, description, timestamp);
        this.spent = spent;
        this.budget = budget;
        type=BudjetObjectType.CATEGORY;
    }

    public Category(String title, String description, double spent, double budget, Timestamp timestamp) {
        super("",title, description, timestamp);
        this.spent = spent;
        this.budget = budget;
        type=BudjetObjectType.CATEGORY;
    }

    public Category(String title, String description, double budget, Timestamp timestamp) {
        super("",title, description, timestamp);
        this.spent =0;
        this.budget = budget;
        type=BudjetObjectType.CATEGORY;
    }

    public Category(String id,String title, String description, double spent, double budget) {
        super(id,title, description);
        this.spent = spent;
        this.budget = budget;
        type=BudjetObjectType.CATEGORY;
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

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = super.toJson();
        map.put("spent",spent);
        map.put("budget",budget);

        return map;
    }
}
