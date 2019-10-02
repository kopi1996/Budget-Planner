package com.planner.budgetplanner;

public class User {


    private String id;
    private String f_name;
    private String l_name;
    private String email;

    public User(String id, String f_name, String l_name, String email) {
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.email = email;
    }



    public User() {
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getName() {
        return f_name+" "+l_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
