package com.planner.budgetplanner.ViewDirectories;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Adapters.CategoryAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.AddActivities.CategoryAdd;
import com.planner.budgetplanner.AddActivities.ExpenseAdd;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Managers.MoneyManager;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;
import com.planner.budgetplanner.Utility.Trash;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryView extends BudgetObjectView<CategoryAdapter,Category> {

    private FloatingActionButton floatingBtn;
    private TextView remainingTxt;
    private TextView budgetTxt;
    private TextView spentTxt;
    private ProgressBar progressBar;
    private Expense[] tempExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        orginList = new ArrayList<>();
        tempList=new ArrayList<>();
        adapter = new CategoryAdapter(tempList, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(CategoryView.this, ExpenseView.class);
                Bundle bundle = new Bundle();
                bundle.putString(ExpenseView.EXPENSE_VIEW_CAT_ID, tempList.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(CategoryView.this, ExpenseAdd.class);
                Bundle bundle = new Bundle();
                bundle.putString(ExpenseAdd.EXPENSE_CAT_ID, tempList.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        initialize("Expenditure", findViewById(R.id.cateViewLayout), (RecyclerView) findViewById(R.id.cateViewList));
    }

    @Override
    protected void initialize(String title, View homeView, RecyclerView recyclerView) {
        super.initialize(title, homeView, recyclerView);
        budgetTxt = findViewById(R.id.cateFullViewBudget);
        spentTxt = findViewById(R.id.cateFullViewSpent);
        remainingTxt = findViewById(R.id.cateFullViewRemaining);
        progressBar = findViewById(R.id.catFullViewProBar);

        floatingBtn = findViewById(R.id.cateViewFloatBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryView.this, CategoryAdd.class));
            }
        });
    }

    @Override
    protected void initFilterView() {

    }

    @Override
    protected void updateUI() {
        orginList.clear();
        orginList.addAll(Arrays.asList(MyUtility.currentUser.getCategories()));
        tempList.clear();
        tempList.addAll(orginList);
        adapter.setList(tempList);
        super.updateUI();

        updateOverview();
    }

    private void updateOverview() {
        double totalSpent = MoneyManager.totalExpenditure(MyUtility.currentUser);
        double totalBudget = MoneyManager.totalBudgeted(MyUtility.currentUser);
        double totalRemin = totalBudget - totalSpent;
        double percentage = totalBudget == 0 ? 0 : (totalSpent / totalBudget) * 100;

        budgetTxt.setText(totalBudget + MyUtility.currentUser.getCurrencyType());
        spentTxt.setText(totalSpent + MyUtility.currentUser.getCurrencyType());
        remainingTxt.setText(totalRemin + MyUtility.currentUser.getCurrencyType());
        progressBar.setProgress((int) percentage);
        Drawable budgetedProgDrawable = progressBar.getProgressDrawable();
        if (percentage > 100) {
            budgetedProgDrawable.setColorFilter(getResources().getColor(R.color.dangerColor), PorterDuff.Mode.SRC_IN);
        } else {
            budgetedProgDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
        progressBar.setProgressDrawable(budgetedProgDrawable);
    }

    @Override
    protected void onCloseSearchView() {
        super.onCloseSearchView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, new ISearchListner<Category>() {
            @Override
            public void onResult(final ArrayList<Category> result) {
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
                    @Override
                    public void onClick(View v, int pos) {
                        Intent intent = new Intent(CategoryView.this, ExpenseView.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ExpenseView.EXPENSE_VIEW_CAT_ID, result.get(pos).getId());
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
        Log.i(TAG, "onMyRemove: " + item.hashCode());
        tempExpenses = MyUtility.currentUser.getExpensesForCategory(item.getId());
        Trash.addTrash(item);
        Trash.addTrash((Object[]) tempExpenses);
        MyUtility.currentUser.removeCategories(item);
        MyUtility.currentUser.removeExpenses(tempExpenses);
        updateOverview();
    }

    @Override
    public void onRestore(final Category item, int pos) {
        super.onRestore(item, pos);
        Log.i(TAG, "onMyRestore: " + item.hashCode());
        MyUtility.currentUser.addCategories(item);
        Trash.removeTrash(item);
        Trash.removeTrash((Object[]) tempExpenses);
        MyUtility.currentUser.addExpenses(tempExpenses);
        updateOverview();
    }

}