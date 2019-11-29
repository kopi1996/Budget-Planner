package com.planner.budgetplanner.ViewDirectories;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Adapters.ExpenseAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.AddActivities.CategoryAdd;
import com.planner.budgetplanner.AddActivities.ExpenseAdd;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpenseView extends BudgetObjectView<ExpenseAdapter,Expense> {

    public static final String EXPENSE_VIEW_CAT_ID = "ExpenseViewCatId";
    private static final String TAG = "ExpenseView";
    private Category expenseCate;

    private FloatingActionButton floatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString(EXPENSE_VIEW_CAT_ID);
            if (id != null)
                expenseCate = MyUtility.currentUser.getCategoryForId(id);
        }
        if (expenseCate == null)
            return;
        list = new ArrayList<>();
        Log.i(TAG, "onCreate: "+MyUtility.currentUser.getExpensesForCategory(expenseCate.getId()).length);
        list.addAll(Arrays.asList(MyUtility.currentUser.getExpensesForCategory(expenseCate.getId())));

        adapter = new ExpenseAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(ExpenseView.this, ExpenseAdd.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ExpenseAdd.EXPENSE_EDIT,true);
                bundle.putString(ExpenseAdd.EXPENSE_ID, list.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        initialize(expenseCate.getTitle(), findViewById(R.id.expViewLayout), (RecyclerView) findViewById(R.id.expViewList));
    }

    @Override
    protected void initialize(String title, View homeView, RecyclerView recyclerView) {
        super.initialize(title, homeView, recyclerView);
        floatingBtn = findViewById(R.id.expViewFloatBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseView.this, ExpenseAdd.class);
                Bundle bundle = new Bundle();
                if (expenseCate != null) {
                    bundle.putString(ExpenseAdd.EXPENSE_CAT_ID, expenseCate.getId());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, list, new ISearchListner<Expense>() {
            @Override
            public void onResult(ArrayList<Expense> result) {
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
                    @Override
                    public void onClick(View v, int pos) {
                        Intent intent = new Intent(ExpenseView.this, ExpenseAdd.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(ExpenseAdd.EXPENSE_EDIT, true);
                        bundle.putString(ExpenseAdd.EXPENSE_ID, list.get(pos).getId());
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
    public void onRemove(final Expense item, int pos) {
        super.onRemove(item, pos);
        MyUtility.currentUser.removeExpenses(item);
        FirebaseManager.deleteExpense(item, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    MyUtility.currentUser.addExpenses(item);
            }
        });
    }

    @Override
    public void onRestore(final Expense item, int pos) {
        super.onRestore(item, pos);

        FirebaseManager.addExpenseIntoDB(item, new OnSuccessListener<Expense>() {
            @Override
            public void onSuccess(Expense expense) {
                if (expense != null)
                    MyUtility.currentUser.addExpenses(expense);
            }
        });
    }
}