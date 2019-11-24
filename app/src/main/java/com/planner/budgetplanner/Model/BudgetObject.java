package com.planner.budgetplanner.Model;

public class BudgetObject extends DatabaseObject {

    protected String title;
    protected String description;
    protected String date;
    protected String time;

    public BudgetObject(String id, String title, String description, String date, String time) {
        super(id);
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public BudgetObject(String id,String title, String description) {
        super(id);
        this.title = title;
        this.description = description;
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
}
