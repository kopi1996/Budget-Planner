package com.planner.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.Category;

import java.util.ArrayList;

public class CategoryView extends BudgetObjectView {

    private CategoryAdapter adapter;
    private ArrayList<Category> list;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        recyclerView = findViewById(R.id.cateViewList);
        homeView=findViewById(R.id.cateViewLayout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Expenditures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        list.add(new Category("1","", 100, 200));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("1","", 100, 200));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("1","", 100, 200));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("1","", 100, 200));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("1","", 100, 200));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("1","", 100, 200));
        list.add(new Category("2","", 15, 35));
        list.add(new Category("2","", 15, 35));

        adapter = new CategoryAdapter(list, new MyItemAdapter.IItemListner() {

            @Override
            public void onClick(View v, int pos) {
                startActivity(new Intent(CategoryView.this, ExpenseView.class));
                Toast.makeText(CategoryView.this, list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.cateViewLayout),recyclerView);

        searchRecyclerView=findViewById(R.id.searhCateViewList);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setNestedScrollingEnabled(false);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.cateViewLayout), searchRecyclerView, new MyItemAdapter.IItemSwipeListner<Category>() {
            @Override
            public void onRemove(Category item, int pos) {
                list.remove(item);
            }

            @Override
            public void onRestore(Category item, int pos) {
                list.add(item);
            }
        });


//        Spinner spinner = findViewById(R.id.catFilter);
//        String[] paths = {"item 1", "item 2", "item 3"};
//        ArrayAdapter<String> spinnerAdapet = new ArrayAdapter<String>(CategoryView.this,
//                android.R.layout.simple_spinner_item, paths);
//        spinnerAdapet.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerAdapet);
    }


    @Override
    protected void onCloseSearchView() {
        super.onCloseSearchView();
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, list, new ISearchListner<Category>() {
            @Override
            public void onResult(ArrayList<Category> result) {
                adapter.initialize(result,new MyItemAdapter.IItemListner() {
                        @Override
                        public void onClick(View v, int pos) {
                            startActivity(new Intent(CategoryView.this, ExpenseView.class));
                            Toast.makeText(CategoryView.this, CategoryView.this.list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
                        }
                    });
                    searchRecyclerView.setAdapter(adapter);
            }
        });

        return true;
    }
}