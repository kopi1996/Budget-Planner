package com.planner.budgetplanner.ViewDirectories;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.IncomeAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.AddActivities.IncomeAdd;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.Model.User;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class IncomeView extends BudgetObjectView<IncomeAdapter,Income> {

    private static final String TAG = "IncomeView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_view);

        list = new ArrayList<>();

        adapter = new IncomeAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(IncomeView.this, IncomeAdd.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(IncomeAdd.INCOME_EDIT, true);
                bundle.putString(IncomeAdd.INCOME_DATA_ID, list.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Log.i(TAG, "onCreate calling: ");
        initialize("Incomes", findViewById(R.id.incomeViewLayout), (RecyclerView) findViewById(R.id.incomeViewList));
    }

    @Override
    protected void updateUI() {
        list = new ArrayList<>();
        list.addAll(Arrays.asList(MyUtility.currentUser.getIncomes()));
        super.updateUI();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, (ArrayList<Income>) list, new ISearchListner<Income>() {
            @Override
            public void onResult(ArrayList<Income> result) {
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
                    @Override
                    public void onClick(View v, int pos) {
                        startActivity(new Intent(IncomeView.this, ExpenseView.class));
                        Toast.makeText(IncomeView.this, IncomeView.this.list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
                    }
                });
                searchRecyclerView.setAdapter(adapter);
            }
        });

        return true;
    }
}
