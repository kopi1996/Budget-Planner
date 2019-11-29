package com.planner.budgetplanner.ViewDirectories;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.AddActivities.CategoryAdd;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryView extends BudgetObjectView<CategoryAdapter,Category> {

    private FloatingActionButton floatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        list = new ArrayList<>();
        list.addAll(Arrays.asList(MyUtility.currentUser.getCategories()));

        adapter = new CategoryAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(CategoryView.this, ExpenseView.class);
                Bundle bundle = new Bundle();
                bundle.putString(ExpenseView.EXPENSE_VIEW_CAT_ID, list.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        initialize("Expenditure", findViewById(R.id.cateViewLayout), (RecyclerView) findViewById(R.id.cateViewList));
    }

    @Override
    protected void initialize(String title, View homeView, RecyclerView recyclerView) {
        super.initialize(title, homeView, recyclerView);
        floatingBtn = findViewById(R.id.cateViewFloatBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryView.this, CategoryAdd.class));
                finish();
            }
        });
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
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
                    @Override
                    public void onClick(View v, int pos) {
                        Intent intent = new Intent(CategoryView.this, ExpenseView.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ExpenseView.EXPENSE_VIEW_CAT_ID, list.get(pos).getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                searchRecyclerView.setAdapter(adapter);
            }
        });

        return true;
    }

    @Override
    public void onRemove(final Category item, int pos) {
        super.onRemove(item, pos);
        MyUtility.currentUser.removeCategories(item);
        FirebaseManager.deleteCategory(MyUtility.currentUser, item, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    MyUtility.currentUser.addCategories(item);
            }
        });
    }

    @Override
    public void onRestore(final Category item, int pos) {
        super.onRestore(item, pos);
        FirebaseManager.addCategoryIntoDB(item, new OnSuccessListener<Category>() {
            @Override
            public void onSuccess(Category category) {
                if (category != null)
                    MyUtility.currentUser.addCategories(category);
            }
        });
    }
}