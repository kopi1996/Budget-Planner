package com.planner.budgetplanner.Model;

import java.util.Map;

public abstract class DatabaseObject {

    protected String id;

    public DatabaseObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract Map<String,Object> toJson();
}
