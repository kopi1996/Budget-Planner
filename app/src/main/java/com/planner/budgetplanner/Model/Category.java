package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.planner.budgetplanner.Managers.MoneyManager;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.Date;
import java.util.Map;

public class Category extends BudgetObject {

    private double budget;
    private User user;

    public Category(String id, String title, String description, double budget, Timestamp timestamp) {
        super(id, title, description, timestamp);
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
    }

    public Category(String title, String description, double budget, Timestamp timestamp) {
        super("", title, description, timestamp);
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
    }

    public Category(String id, String title, String description, double budget) {
        super(id, title, description);
        this.budget = budget;
        type = BudjetObjectType.CATEGORY;
    }

    public double getSpent() {
        return MoneyManager.totalSpentForCategory(user,id);
    }

    public void setUser(User user)
    {
        this.user=user;
    }


    public double getRemaining()
    {
        return budget-getSpent();
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }


    @Override
    public double getAmount() {
        return getBudget();
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = super.toJson();
        map.put("budget", budget);

        return map;
    }

    public static Category jsonToObject(DocumentSnapshot document) {
        //String id = document.get("id").toString();
        String title = document.get("title").toString();
        String description = document.get("description").toString();
        Date tempTimestamp = (Date) document.get("timestamp");
        Timestamp timestamp = MyUtility.convertFromUtcToStamp(tempTimestamp);
        double budget = Double.parseDouble(document.get("budget").toString());

        return new Category(document.getId(),title, description, budget, timestamp);
    }
}
