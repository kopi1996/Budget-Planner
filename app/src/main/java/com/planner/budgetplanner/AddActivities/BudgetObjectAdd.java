package com.planner.budgetplanner.AddActivities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.planner.budgetplanner.CustomAppBarActivity;
import com.planner.budgetplanner.Model.BudgetObject;

public class BudgetObjectAdd<T extends BudgetObject> extends CustomAppBarActivity {

    @Override
    protected void initialize(String title) {
        super.initialize(title);
        enableBackBtn();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
