package com.planner.budgetplanner.Model;

import java.sql.Time;
import java.util.Date;

public class Income {

    private String title;
    private String description;
    private double amount;
    private String date;
    private String time;


    public Income(String title, String description, double amount, String date, String time) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.time = time;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
