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
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Other.SwipeUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryView extends AppCompatActivity {

    CategoryAdapter adapter;
    ArrayList<Category> list;
    RecyclerView recyclerView;
    RecyclerView searcRecylerView;
    SearchView searchView;
    private boolean isSearchEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        recyclerView = findViewById(R.id.cateViewList);
        searcRecylerView = findViewById(R.id.searhCateViewList);
        getSupportActionBar().setTitle("Expenditures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searcRecylerView.setLayoutManager(new LinearLayoutManager(this));
        searcRecylerView.setHasFixedSize(true);
        searcRecylerView.setNestedScrollingEnabled(false);
        searcRecylerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searcRecylerView.setItemAnimator(new DefaultItemAnimator());

        list = new ArrayList<>();
        list.add(new Category("1", 100, 200));
        list.add(new Category("2", 15, 35));
        list.add(new Category("3", 40, 30));
        list.add(new Category("4", 50, 50));
        list.add(new Category("5", 0, 2000000));
        list.add(new Category("6", 0, 0));
        list.add(new Category("7", 0, 0));
        list.add(new Category("8", 0, 0));
        list.add(new Category("9", 0, 0));
        list.add(new Category("10", 0, 0));
        list.add(new Category("11", 0, 0));

        adapter = new CategoryAdapter(list, new MyItemAdapter.IItemListner() {

            @Override
            public void onClick(View v, int pos) {
                startActivity(new Intent(CategoryView.this, ExpenseView.class));
                Toast.makeText(CategoryView.this, list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.cateViewLayout),recyclerView);
        //enableSwipeToDeleteAndUndo();
        //setSwipeForRecyclerView();

        Spinner spinner = findViewById(R.id.catFilter);
        String[] paths = {"item 1", "item 2", "item 3"};
        ArrayAdapter<String> spinnerAdapet = new ArrayAdapter<String>(CategoryView.this,
                android.R.layout.simple_spinner_item, paths);
        spinnerAdapet.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapet);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isSearchEnabled) {
            searchView.onActionViewCollapsed();
            searchView.setIconified(true);
            isSearchEnabled = false;
        } else {
            super.onBackPressed();
        }
    }

    private void setSwipeForRecyclerView() {

        SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                adapter = (CategoryAdapter)recyclerView.getAdapter();
                //myAdapter.pendingRemoval(swipedPosition);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
//                myAdapter = (MyAdapter) mRecyclerView.getAdapter();
//                if (myAdapter.isPendingRemoval(position)) {
//                    return 0;
//                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(swipeHelper);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        //set swipe label
        swipeHelper.setLeftSwipeLable("Archive");
        //set swipe background-Color
        swipeHelper.setLeftcolorCode(ContextCompat.getColor(this, R.color.swipebackground));
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Category item = adapter.getData().get(position);

                adapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.cateViewLayout), "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchBtn);
        searchView = (SearchView) item.getActionView();
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        final ImageView searchClose = searchView.findViewById(searchCloseButtonId);
        searchClose.setColorFilter(Color.argb(150, 255, 255, 255));


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchEnabled = true;
                searcRecylerView.setAdapter(adapter);
                findViewById(R.id.cateViewLayout).setVisibility(View.INVISIBLE);
                findViewById(R.id.searchViewLayout).setVisibility(View.VISIBLE);
                searcRecylerView.setVisibility(View.VISIBLE);
                findViewById(R.id.catEmptyMsgTxt).setVisibility(View.INVISIBLE);
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isSearchEnabled = false;
                findViewById(R.id.cateViewLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.searchViewLayout).setVisibility(View.INVISIBLE);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(CategoryView.this, "Search Fiished: " + query, Toast.LENGTH_LONG).show();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    searchClose.setAlpha(150);
                } else {
                    searchClose.setAlpha(255);
                }
                ArrayList<Category> newList = categoryFilter(CategoryView.this.list, newText);
                if (newList.isEmpty()) {
                    findViewById(R.id.catEmptyMsgTxt).setVisibility(View.VISIBLE);
                    searcRecylerView.setVisibility(View.INVISIBLE);
                } else {
                    adapter = new CategoryAdapter(newList, new MyItemAdapter.IItemListner() {

                        @Override
                        public void onClick(View v, int pos) {
                            startActivity(new Intent(CategoryView.this, ExpenseView.class));
                            Toast.makeText(CategoryView.this, CategoryView.this.list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
                        }
                    });
                    searcRecylerView.setAdapter(adapter);
                }
                return false;
            }
        });

        return true;
    }

    private ArrayList<Category> categoryFilter(ArrayList<Category> oldList, String title) {
        ArrayList<Category> newList = new ArrayList<>();
        for (Category c : oldList) {
            if (c.getTitle().contains(title))
                newList.add(c);
        }

        return newList;
    }
}