package com.planner.budgetplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.ExpenseAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;

public class ExpenseView extends BudgetObjectView<ExpenseAdapter,Expense> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);

        list = new ArrayList<>();
        list.add(new Expense("",null, "abc", 25, "", "sdsdd", "250"));
        list.add(new Expense("",null, "sbc", 25, "", "sdsdd", "250"));
        list.add(new Expense("",null, "qbc", 25, "", "sdsdd", "250"));
        list.add(new Expense("",null, "1bc", 25, "", "sdsdd", "250"));
        list.add(new Expense("",null, "Abc", 25, "", "sdsdd", "250"));
        list.add(new Expense("",null, "0bc", 25, "", "sdsdd", "250"));

        adapter = new ExpenseAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {

            }
        });

        initialize("Something",findViewById(R.id.expViewLayout), (RecyclerView) findViewById(R.id.expViewList));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, list, new ISearchListner<Expense>() {
            @Override
            public void onResult(ArrayList<Expense> result) {
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
                    @Override
                    public void onClick(View v, int pos) {
                        startActivity(new Intent(ExpenseView.this, ExpenseView.class));
                        Toast.makeText(ExpenseView.this, ExpenseView.this.list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
                    }
                });
                searchRecyclerView.setAdapter(adapter);
            }
        });

        return true;
    }
}