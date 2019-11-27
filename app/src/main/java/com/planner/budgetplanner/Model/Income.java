package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.planner.budgetplanner.Utility.MyUtility;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Income extends BudgetObject {

    private double amount;

    public Income(String id, String title, String description, double amount) {
        super(id, title, description);
        this.amount = amount;
        type = BudjetObjectType.INCOME;
    }

    public Income(String id, String title, String description, double amount, Timestamp timestamp) {
        super(id, title, description, timestamp);

        this.title = title;
        this.description = description;
        this.amount = amount;
        type = BudjetObjectType.INCOME;
    }

    public Income(String title, String description, Timestamp timestamp, double amount) {
        super("", title, description, timestamp);
        this.amount = amount;
        type = BudjetObjectType.INCOME;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> map = super.toJson();
        map.put("amount", amount);
        return map;
    }

    public static Income jsonToObject(DocumentSnapshot document) {
        // String id = document.get("id").toString();
        String title = document.get("title").toString();
        String description = document.get("description").toString();
        Date tempTimestamp = (Date) document.get("timestamp");
        Timestamp timestamp = MyUtility.convertFromUtcToStamp(tempTimestamp);
        double amount = Double.parseDouble(document.get("amount").toString());

        return new Income(document.getId(),title, description, amount,timestamp);
    }
}