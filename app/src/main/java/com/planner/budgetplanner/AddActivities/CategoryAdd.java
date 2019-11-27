package com.planner.budgetplanner.AddActivities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Date;

public class CategoryAdd extends BudgetObjectAdd<Category> implements View.OnFocusChangeListener {

    private static final String TAG = "CategoryAdd";
    private TextInputEditText titleTxt;
    private TextInputEditText amountTxt;
    private TextInputEditText descriptionTxt;

    private TextInputLayout titleTxtPar;
    private TextInputLayout amountTxtPar;
    private TextInputLayout descriptionTxtPar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        initialize("Add Category");

        findViewById(R.id.cateHintBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHintCateTitle();
            }
        });
    }

    @Override
    protected void initialize(String title) {
        super.initialize(title);
        titleTxt = findViewById(R.id.catAddTitleTxt);
        amountTxt = findViewById(R.id.catAddAmountTxt);
        descriptionTxt = findViewById(R.id.catAddDesTxt);

        titleTxtPar = findViewById(R.id.catAddTitleTxtPar);
        amountTxtPar = findViewById(R.id.catAddAmountTxtPar);
        descriptionTxtPar = findViewById(R.id.catAddDesTxtPar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
            case R.id.saveBtn:
                addCategory();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
            return;
        switch (v.getId()) {
            case R.id.catAddTitleTxt:
                titleTxtPar.setError(null);
                break;
            case R.id.catAddAmountTxt:
                amountTxtPar.setError(null);
                break;
            default:
                return;
        }
    }

    private void addCategory() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(new Date());
        Category category = new Category(titleTxt.getText().toString(), descriptionTxt.getText().toString(), Double.parseDouble(amountTxt.getText().toString()), timestamp);
        // Expense expense=new Expense(pickedCategory,titleTxt.getText().toString(),descriptionTxt.getText().toString(),timestamp,Double.parseDouble(amountTxt.getText().toString()));
        FirebaseManager.addCategoryIntoDB(category, new OnSuccessListener<Category>() {
            @Override
            public void onSuccess(Category category1) {
                MyUtility.disableLoading(CategoryAdd.this);
                if (category1 != null) {
                    Log.i(TAG, "onSuccess category add: ");
                    finish();
                } else {

                }
            }
        });
    }

    private boolean checkEverythingReady() {
        if (titleTxt.getText().toString().isEmpty()) {
            titleTxtPar.setError("Enter a title");
            return false;
        }
        if (amountTxt.getText().toString().isEmpty()) {
            amountTxtPar.setError("Enter a amount");
            return false;
        }

        return true;
    }

    private void displayHintCateTitle() {
        final ArrayList<BudgetObject> elements = new ArrayList<>();
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));


        Dialog dialog = MyUtility.displayHintDialog(this, elements, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                titleTxt.setText(elements.get(pos).getTitle());
            }
        }, null, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }
}