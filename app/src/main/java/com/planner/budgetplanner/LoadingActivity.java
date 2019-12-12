package com.planner.budgetplanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.planner.budgetplanner.Interfaces.IInitialize;

public class LoadingActivity extends AppCompatActivity implements IInitialize {

    protected View homeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View getHomeView() {
        return homeView;
    }

    @Override
    public void initialize() {
        if (homeView == null)
            homeView = findViewById(R.id.homeView);
    }
}