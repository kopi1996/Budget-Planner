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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.Adapters.ExpenseAdapter;
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

public class ExpenseView extends BudgetObjectView<ExpenseAdapter,Expense> {

    public static final String EXPENSE_VIEW_CAT_ID = "ExpenseViewCatId";
    private static final String TAG = "ExpenseView";

    private TextView budgetTxt;
    private TextView remainTxt;
    private TextView spentTxt;
    private TextView cateEditBtn;
    private ProgressBar progressBar;


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
        orginList = new ArrayList<>();
        orginList.addAll(Arrays.asList(MyUtility.currentUser.getExpensesForCategory(expenseCate.getId())));

        tempList=new ArrayList<>();
        tempList.addAll(orginList);

        adapter = new ExpenseAdapter(tempList, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(ExpenseView.this, ExpenseAdd.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(ExpenseAdd.EXPENSE_EDIT, true);
                bundle.putString(ExpenseAdd.EXPENSE_ID, tempList.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        initialize(expenseCate.getTitle(), findViewById(R.id.expViewLayout), (RecyclerView) findViewById(R.id.expViewList));
    }

    @Override
    protected void initialize(String title, View homeView, RecyclerView recyclerView) {
        super.initialize(title, homeView, recyclerView);
        budgetTxt = findViewById(R.id.expFullViewBudget);
        remainTxt = findViewById(R.id.expFullViewRemaining);
        spentTxt = findViewById(R.id.expFullViewSpent);
        progressBar = findViewById(R.id.expFullViewProBar);
        cateEditBtn = findViewById(R.id.categoryEditBtn);


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
        cateEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseView.this, CategoryAdd.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(CategoryAdd.CATEGORY_EDIT, true);
                if (expenseCate != null)
                    bundle.putString(CategoryAdd.CATEGORY_ID, expenseCate.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateUI() {
        orginList.clear();
        orginList.addAll(Arrays.asList(MyUtility.currentUser.getExpensesForCategory(expenseCate.getId())));
        tempList.clear();
        filterList(type);
        super.updateUI();
        updateOverView();
    }

    protected void updateOverView() {
        if (expenseCate == null)
            return;
        double totalSpent = MoneyManager.totalExpenditure(tempList);
        double totalBudget = expenseCate.getBudget();
        double totalRemin = totalBudget - totalSpent;
        double percentage = totalBudget == 0 ? 0 : (totalSpent / totalBudget) * 100;

        budgetTxt.setText(totalBudget + MyUtility.currentUser.getCurrencyType());
        spentTxt.setText(totalSpent + MyUtility.currentUser.getCurrencyType());
        remainTxt.setText(totalRemin + MyUtility.currentUser.getCurrencyType());
        progressBar.setProgress((int) percentage);
        Drawable budgetedProgDrawable = progressBar.getProgressDrawable();
        if (percentage > 100) {
            budgetedProgDrawable.setColorFilter(getResources().getColor(R.color.dangerColor), PorterDuff.Mode.SRC_IN);
        } else {
            budgetedProgDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
        progressBar.setProgressDrawable(budgetedProgDrawable);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSearchView(adapter, new ISearchListner<Expense>() {
            @Override
            public void onResult(final ArrayList<Expense> result) {
                adapter.initialize(result, new MyItemAdapter.IItemListner() {
                    @Override
                    public void onClick(View v, int pos) {
                        Intent intent = new Intent(ExpenseView.this, ExpenseAdd.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(ExpenseAdd.EXPENSE_EDIT, true);
                        bundle.putString(ExpenseAdd.EXPENSE_ID, result.get(pos).getId());
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
        Trash.addTrash(item);
        updateOverView();
    }

    @Override
    public void onRestore(final Expense item, int pos) {
        super.onRestore(item, pos);
        MyUtility.currentUser.addExpenses(item);
        Trash.removeTrash(item);
        updateOverView();
    }
}