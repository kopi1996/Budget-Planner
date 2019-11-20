package com.planner.budgetplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.planner.budgetplanner.Adapters.IncomeAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;

import java.util.ArrayList;

public class IncomeView extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_view);

        recyclerView=findViewById(R.id.incomeViewList);
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ArrayList<Income> list=new ArrayList<>();
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("aas","abc",25,"","sdsdd"));
        list.add(new Income("gfdg","abc",25,"","sdsdd"));
        list.add(new Income("erew","abc",25,"","sdsdd"));
        list.add(new Income("adss","abc",25,"","sdsdd"));
        list.add(new Income("zxzx","abc",25,"","sdsdd"));
        list.add(new Income("xzcx","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));
        list.add(new Income("soem","abc",25,"","sdsdd"));

        IncomeAdapter adapter=new IncomeAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {

            }
        });

        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.incomeViewLayout),recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
