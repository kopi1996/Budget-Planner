package com.planner.budgetplanner;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CustomAppBarActivity extends LoadingActivity {

    protected Toolbar toolbar;

    protected void initialize(String title)
    {
        super.initialize();
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
