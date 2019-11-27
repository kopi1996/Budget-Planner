package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.Map;

public class Expense extends BudgetObject {

    private Category category;
    private double amount;
    private String categoryId;

    public Expense(String id, Category category, String title, double amount, String description, Timestamp timestamp) {
        super(id, title, description, timestamp);

        this.category = category;
        this.amount = amount;
        type = BudjetObjectType.EXPENSE;
    }

    public Expense(String id, String title, double amount, String description, Timestamp timestamp) {
        super(id, title, description, timestamp);

        this.amount = amount;
        type = BudjetObjectType.EXPENSE;
    }

    public Expense(Category category, String title, String description, Timestamp timestamp, double amount) {
        super("", title, description, timestamp);

        this.category = category;
        this.amount = amount;
        type = BudjetObjectType.EXPENSE;
    }

    public Expense(String id, String title, String description, Category category, double amount) {
        super(id, title, description);
        this.category = category;
        this.amount = amount;
        type = BudjetObjectType.EXPENSE;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> map = super.toJson();
        map.put("amount", amount);
        map.put("categoryId", category.getId());

        return map;
    }

    public static Expense jsonToObject(DocumentSnapshot document) {
        String id = document.get("id").toString();
        String categoryId=document.get("categoryId").toString();
        String title = document.get("title").toString();
        String description = document.get("description").toString();
        Timestamp tempTimestamp = (Timestamp) document.get("timestamp");
        Timestamp timestamp = MyUtility.convertFromUtcToStamp(tempTimestamp.toDate());
        double amount = Double.parseDouble(document.get("amount").toString());

        Expense expense = new Expense(id, title, amount, description, timestamp);
        expense.setCategoryId(categoryId);

        return expense;
    }
}