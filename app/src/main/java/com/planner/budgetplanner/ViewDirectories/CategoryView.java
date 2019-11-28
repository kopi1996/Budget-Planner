package com.planner.budgetplanner.ViewDirectories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;

import java.util.ArrayList;

public class CategoryView extends BudgetObjectView<CategoryAdapter,Category> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        list = new ArrayList<>();
        list.add(new Category("","1","", 100, 200));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","1","", 100, 200));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","1","", 100, 200));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","1","", 100, 200));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","1","", 100, 200));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","1","", 100, 200));
        list.add(new Category("","2","", 15, 35));
        list.add(new Category("","2","", 15, 35));

        adapter = new CategoryAdapter((ArrayList<Category>) list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                startActivity(new Intent(CategoryView.this, ExpenseView.class));
                Toast.makeText(CategoryView.this, list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        initialize("Expenditure",findViewById(R.id.cateViewLayout),(RecyclerView) findViewById(R.id.cateViewList));
    }


    @Override
    protected void onCloseSearchView() {
        super.onCloseSearchView();

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