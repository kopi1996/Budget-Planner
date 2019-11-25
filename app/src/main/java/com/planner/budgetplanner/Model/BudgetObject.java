package com.planner.budgetplanner.Model;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class BudgetObject extends DatabaseObject {

    protected String title;
    protected String description;
    protected Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public BudgetObject(String id, String title, String description,Timestamp timestamp) {
        super(id);
        this.title = title;
        this.description = description;
        this.timestamp=timestamp;
    }

    public BudgetObject(String id,String title, String description) {
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

    public Map<String,Object> toJson()
    {
        Map<String,Object> data=new HashMap<>();
        data.put("id",id);
        data.put("title",title);
        data.put("description",description);
        data.put("timestamp",timestamp);

        return data;
    }
}
