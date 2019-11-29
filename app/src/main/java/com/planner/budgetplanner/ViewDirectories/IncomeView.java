package com.planner.budgetplanner.ViewDirectories;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Adapters.IncomeAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.AddActivities.IncomeAdd;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Managers.MoneyManager;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class IncomeView extends BudgetObjectView<IncomeAdapter,Income> {

    private static final String TAG = "IncomeView";
    private FloatingActionButton floatingBtn;
    private TextView incomeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_view);


        list = new ArrayList<>();
        list.addAll(Arrays.asList(MyUtility.currentUser.getIncomes()));

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
    protected void initialize(String title, View homeView, RecyclerView recyclerView) {
        super.initialize(title, homeView, recyclerView);
        floatingBtn = findViewById(R.id.incViewFloatBtn);
        incomeTxt = findViewById(R.id.incomeViewAmount);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeView.this, IncomeAdd.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateUI() {
        list.clear();
        list.addAll(Arrays.asList(MyUtility.currentUser.getIncomes()));
        adapter.setList(list);
        super.updateUI();
        updateOverView();
    }

    private void updateOverView() {
        incomeTxt.setText(MoneyManager.totalIncome(MyUtility.currentUser) + MyUtility.currentUser.getCurrencyType());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, (ArrayList<Income>) list, new ISearchListner<Income>() {
            @Override
            public void onResult(ArrayList<Income> result) {
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
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
                searchRecyclerView.setAdapter(adapter);
            }
        });

        return true;
    }

    @Override
    public void onRemove(final Income item, int pos) {
        super.onRemove(item, pos);
        MyUtility.currentUser.removeIncomes(item);
        updateOverView();
        FirebaseManager.deleteIncome(item, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    MyUtility.currentUser.addIncomes(item);
            }
        });
    }

    @Override
    public void onRestore(final Income item, int pos) {
        super.onRestore(item, pos);
        MyUtility.currentUser.addIncomes(item);
        updateOverView();
        FirebaseManager.addIncomeIntoDB(item, new OnSuccessListener<Income>() {
            @Override
            public void onSuccess(Income income) {
                if (income != null) {
                    MyUtility.currentUser.removeIncomes(item);
                    MyUtility.currentUser.addIncomes(income);
                    updateOverView();
                }
            }
        });
    }
}