package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.Date;
import java.util.Map;

public class Category extends BudgetObject {

    private double spent;
    private double budget;

    public Category(String id, String title, String description, double spent, double budget, Timestamp timestamp) {
        super(id, title, description, timestamp);
        this.spent = spent;
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
    }

    public Category(String title, String description, double spent, double budget, Timestamp timestamp) {
        super("", title, description, timestamp);
        this.spent = spent;
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
    }

    public Category(String title, String description, double budget, Timestamp timestamp) {
        super("", title, description, timestamp);
        this.spent = 0;
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
    }

    public Category(String id, String title, String description, double spent, double budget) {
        super(id, title, description);
        this.spent = spent;
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
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

    public double getRemaining() {
        return budget - spent;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = super.toJson();
        map.put("spent", spent);
        map.put("budget", budget);

        return map;
    }

    public static Category jsonToObject(DocumentSnapshot document) {
        //String id = document.get("id").toString();
        String title = document.get("title").toString();
        String description = document.get("description").toString();
        Date tempTimestamp = (Date) document.get("timestamp");
        Timestamp timestamp = MyUtility.convertFromUtcToStamp(tempTimestamp);
        double spent = Double.parseDouble(document.get("spent").toString());
        double budget = Double.parseDouble(document.get("budget").toString());

        return new Category(document.getId(),title, description, spent, budget, timestamp);
    }
}
