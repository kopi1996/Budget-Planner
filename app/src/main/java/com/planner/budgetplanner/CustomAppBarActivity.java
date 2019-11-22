package com.planner.budgetplanner;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class CustomAppBarActivity extends AppCompatActivity {

    protected Toolbar toolbar;

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
}
