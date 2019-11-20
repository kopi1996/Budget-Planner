package com.planner.budgetplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.planner.budgetplanner.Adapters.ExpenseAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.Expense;

import java.util.ArrayList;

public class ExpenseView extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);

        recyclerView=findViewById(R.id.expViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Expense> list=new ArrayList<>();
        list.add(new Expense(null,"abc",25,"","sdsdd","250"));
        list.add(new Expense(null,"sbc",25,"","sdsdd","250"));
        list.add(new Expense(null,"qbc",25,"","sdsdd","250"));
        list.add(new Expense(null,"1bc",25,"","sdsdd","250"));
        list.add(new Expense(null,"Abc",25,"","sdsdd","250"));
        list.add(new Expense(null,"0bc",25,"","sdsdd","250"));

        list.add(new Expense(null,"abc",25,"","sdsdd","250"));
        list.add(new Expense(null,"sbc",25,"","sdsdd","250"));
        list.add(new Expense(null,"qbc",25,"","sdsdd","250"));
        list.add(new Expense(null,"1bc",25,"","sdsdd","250"));
        list.add(new Expense(null,"Abc",25,"","sdsdd","250"));
        list.add(new Expense(null,"0bc",25,"","sdsdd","250"));


        ExpenseAdapter adapter=new ExpenseAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {

            }
        });
        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.expViewLayout),recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
