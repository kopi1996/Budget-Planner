package com.planner.budgetplanner;

import com.planner.budgetplanner.Model.DatabaseObject;

import java.util.ArrayList;

public class User extends DatabaseObject {

    private String name;
    private String email;
    private FirebaseManager.LoginType type;
    private ArrayList<String> incomeIds=new ArrayList<>();

    public User(String id, String name, String email, FirebaseManager.LoginType type) {
        super(id);
        this.name = name;
        this.email = email;
        this.type=type;
    }

    public User(String id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
    }

    public FirebaseManager.LoginType getType() {
        return type;
    }

    public void setType(FirebaseManager.LoginType type) {
        this.type = type;
    }

    public User() {
        super("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addIncomeId(String id)
    {
        incomeIds.add(id);
    }

    public ArrayList<String> getIncomeIds() {
        return incomeIds;
    }

    public void setIncomeIds(ArrayList<String> incomeIds) {
        this.incomeIds = incomeIds;
    }
}
