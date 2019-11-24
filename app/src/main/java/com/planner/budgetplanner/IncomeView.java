package com.planner.budgetplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.IncomeAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;

import java.util.ArrayList;

public class IncomeView extends BudgetObjectView<IncomeAdapter,Income> {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_view);

        list=new ArrayList<>();
        list.add(new Income("","soem","abc",25,"","sdsdd"));
        list.add(new Income("","aas","abc",25,"","sdsdd"));
        list.add(new Income("","gfdg","abc",25,"","sdsdd"));
        list.add(new Income("","erew","abc",25,"","sdsdd"));
        list.add(new Income("","adss","abc",25,"","sdsdd"));
        list.add(new Income("","zxzx","abc",25,"","sdsdd"));
        list.add(new Income("","xzcx","abc",25,"","sdsdd"));
        list.add(new Income("","soem","abc",25,"","sdsdd"));
        list.add(new Income("","aas","abc",25,"","sdsdd"));
        list.add(new Income("","gfdg","abc",25,"","sdsdd"));
        list.add(new Income("","erew","abc",25,"","sdsdd"));
        list.add(new Income("","adss","abc",25,"","sdsdd"));
        list.add(new Income("","zxzx","abc",25,"","sdsdd"));
        list.add(new Income("","xzcx","abc",25,"","sdsdd"));

        adapter=new IncomeAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {

            }
        });


        initialize("Income",findViewById(R.id.incomeViewLayout), (RecyclerView) findViewById(R.id.incomeViewList));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, list, new ISearchListner<Income>() {
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
