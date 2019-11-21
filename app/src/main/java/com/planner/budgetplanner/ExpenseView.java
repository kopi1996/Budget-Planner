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

import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Adapters.ExpenseAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;

import java.util.ArrayList;

public class ExpenseView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView searcRecylerView;
    private boolean isSearchEnabled;
    private ExpenseAdapter adapter;
    private ArrayList<Expense> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);

        getSupportActionBar().setTitle("Expenditures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.expViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searcRecylerView = findViewById(R.id.searhCateViewList);
        searcRecylerView.setLayoutManager(new LinearLayoutManager(this));
        searcRecylerView.setHasFixedSize(true);
        searcRecylerView.setNestedScrollingEnabled(false);
        searcRecylerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searcRecylerView.setItemAnimator(new DefaultItemAnimator());

        list=new ArrayList<>();
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


        adapter=new ExpenseAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {

            }
        });
        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.expViewLayout),recyclerView);
        adapter.enableSwipeToDeleteAndUndo(findViewById(R.id.expViewLayout),searcRecylerView);
        recyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchBtn);
        final SearchView searchView = (SearchView) item.getActionView();
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

                ArrayList<Expense> newList = MyUtility.filterWithName(ExpenseView.this.list, newText);
                if (newList.isEmpty()) {
                    findViewById(R.id.catEmptyMsgTxt).setVisibility(View.VISIBLE);
                    searcRecylerView.setVisibility(View.INVISIBLE);
                } else {
                    adapter = new ExpenseAdapter(newList, new MyItemAdapter.IItemListner() {
                        @Override
                        public void onClick(View v, int pos) {
                            startActivity(new Intent(ExpenseView.this, ExpenseAdd.class));
                            Toast.makeText(ExpenseView.this, ExpenseView.this.list.get(pos).getTitle(), Toast.LENGTH_LONG).show();
                        }
                    });
                    searcRecylerView.setAdapter(adapter);
                }
                return false;
            }
        });

        return true;
    }
}
