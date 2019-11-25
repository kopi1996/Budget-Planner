package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Income extends BudgetObject {

    private double amount;

    public Income(String id,String title, String description, double amount) {
        super(id,title, description);
        this.amount = amount;
    }

    public Income(String id, String title, String description, double amount, Timestamp timestamp) {
        super(id,title,description,timestamp);

        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public Map<String,Object> toJson()
    {
        Map<String, Object> map = super.toJson();
        map.put("amount",amount);
        return map;
    }
}
