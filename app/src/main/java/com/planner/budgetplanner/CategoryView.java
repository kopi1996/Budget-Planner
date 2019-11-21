package com.planner.budgetplanner;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Other.SwipeUtil;

import java.util.ArrayList;
import java.util.List;

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

//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isSearchEnabled = true;
//                searchRecyclerView.setAdapter(adapter);
//                findViewById(R.id.cateViewLayout).setVisibility(View.INVISIBLE);
//                findViewById(R.id.searchViewLayout).setVisibility(View.VISIBLE);
//                searchRecyclerView.setVisibility(View.VISIBLE);
//                findViewById(R.id.catEmptyMsgTxt).setVisibility(View.INVISIBLE);
//            }
//        });
//
//
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                isSearchEnabled = false;
//                findViewById(R.id.cateViewLayout).setVisibility(View.VISIBLE);
//                findViewById(R.id.searchViewLayout).setVisibility(View.INVISIBLE);
//                return false;
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(CategoryView.this, "Search Fiished: " + query, Toast.LENGTH_LONG).show();
//                searchView.clearFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                if (newText.isEmpty()) {
//                    searchClose.setAlpha(150);
//                } else {
//                    searchClose.setAlpha(255);
//                }
//                ArrayList<BudgetObject> tempList=new ArrayList<>();
//                ArrayList<Category> newList = MyUtility.filterWithName(CategoryView.this.list, newText);
//                if (newList.isEmpty()) {
//                    findViewById(R.id.catEmptyMsgTxt).setVisibility(View.VISIBLE);
//                    searchRecyclerView.setVisibility(View.INVISIBLE);
//                } else {
//                    findViewById(R.id.catEmptyMsgTxt).setVisibility(View.INVISIBLE);
//                    searchRecyclerView.setVisibility(View.VISIBLE);
//                    adapter = new CategoryAdapter(newList, new MyItemAdapter.IItemListner() {
//
//                        @Override
//                        public void onClick(View v, int pos) {
//                            startActivity(new Intent(CategoryView.this, ExpenseView.class));
//                            Toast.makeText(CategoryView.this, CategoryView.this.list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    adapter.enableSwipeToDeleteAndUndo(homeView,searchRecyclerView);
//                    searchRecyclerView.setAdapter(adapter);
//                }
//                return false;
//            }
//        });

        return true;
    }
}