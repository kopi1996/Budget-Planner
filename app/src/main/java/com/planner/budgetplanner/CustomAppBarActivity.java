package com.planner.budgetplanner;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CustomAppBarActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected View homeView;

    protected void initialize(String title)
    {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
    }

    public void enableBackBtn()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    protected void updateUI()
    {

    }

    public void enableHomeView()
    {
        homeView.setVisibility(View.VISIBLE);
    }
    public void disableHomeView()
    {
        homeView.setVisibility(View.INVISIBLE);
    }
}
