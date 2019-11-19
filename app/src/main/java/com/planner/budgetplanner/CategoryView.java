package com.planner.budgetplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Model.Category;

import java.util.ArrayList;

public class CategoryView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        RecyclerView listView=findViewById(R.id.cateViewList);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        listView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        listView.setItemAnimator(new DefaultItemAnimator());

        final ArrayList<Category> list=new ArrayList<>();
        list.add(new Category("1",100,200));
        list.add(new Category("2",15,35));
        list.add(new Category("3",40,30));
        list.add(new Category("4",50,50));
        list.add(new Category("5",0,2000000));
        list.add(new Category("6",0,0));
        list.add(new Category("7",0,0));
        list.add(new Category("8",0,0));
        list.add(new Category("9",0,0));
        list.add(new Category("10",0,0));
        list.add(new Category("11",0,0));

        CategoryAdapter adapter=new CategoryAdapter(list, new CategoryAdapter.IItemListner() {
            @Override
            public void onItemClick(View view, int pos) {
                startActivity(new Intent(CategoryView.this,ExpenseView.class));
                Toast.makeText(CategoryView.this,list.get(pos).getTitle(),Toast.LENGTH_LONG).show();
            }
        });

        listView.setAdapter(adapter);
    }
}
