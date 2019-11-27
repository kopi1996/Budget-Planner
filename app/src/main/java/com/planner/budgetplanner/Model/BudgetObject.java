package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

enum BudjetObjectType {
    CATEGORY, EXPENSE, INCOME
}

public class BudgetObject extends DatabaseObject {

    protected String userId;
    protected String title;
    protected String description;
    protected Timestamp timestamp;
    protected BudjetObjectType type;


    public BudjetObjectType getType() {
        return type;
    }

    public void setType(BudjetObjectType type) {
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public BudgetObject(String id, String title, String description, Timestamp timestamp) {
        super(id);
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    public BudgetObject(String id, String title, String description) {
        super(id);
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("title", title);
        data.put("description", description);
        data.put("timestamp", timestamp);
        data.put("type", type.toString());

        return data;
    }
}
