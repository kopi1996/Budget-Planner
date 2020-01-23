package com.planner.budgetplanner.AddActivities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Date;

public class CategoryAdd extends BudgetObjectAdd<Category> implements View.OnFocusChangeListener {

    private static final String TAG = "CategoryAdd";
    public static final String CATEGORY_EDIT="CategoryEdit";
    public static final String CATEGORY_ID="CategoryId";

    private TextInputEditText titleTxt;
    private TextInputEditText amountTxt;
    private TextInputEditText descriptionTxt;

    private TextInputLayout titleTxtPar;
    private TextInputLayout amountTxtPar;
    private TextInputLayout descriptionTxtPar;

    private Category category;
    private boolean isUpdate;

    private boolean isSavingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isUpdate = bundle.getBoolean(CATEGORY_EDIT);
            String id = bundle.getString(CATEGORY_ID);
            if (id != null)
                category = MyUtility.currentUser.getCategoryForId(id);
        }

        if (category == null)
            isUpdate = false;

        initialize(isUpdate ? "Edit Category" : "Add Category", category);
    }

    @Override
    protected void initialize(String title,Category category) {
        super.initialize(title);
        titleTxt = findViewById(R.id.catAddTitleTxt);
        amountTxt = findViewById(R.id.catAddAmountTxt);
        descriptionTxt = findViewById(R.id.catAddDesTxt);

        titleTxtPar = findViewById(R.id.catAddTitleTxtPar);
        amountTxtPar = findViewById(R.id.catAddAmountTxtPar);
        descriptionTxtPar = findViewById(R.id.catAddDesTxtPar);

        amountTxt.setHint("Estimated Budget("+MyUtility.currentUser.getCurrencyType()+")");

        if(category!=null) {
            titleTxt.setText(category.getTitle());
            amountTxt.setText(Double.toString(category.getBudget()));
            descriptionTxt.setText(category.getDescription());
        }

        titleTxt.setOnFocusChangeListener(this);
        amountTxt.setOnFocusChangeListener(this);
        descriptionTxt.setOnFocusChangeListener(this);
        findViewById(R.id.cateHintBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHintCateTitle();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isSavingProgress)
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveBtn:
                if(isUpdate)
                    updateCategory();
                else
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

    private void updateCategory() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        isSavingProgress = true;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(new Date());
        category.setTitle(titleTxt.getText().toString());
        category.setBudget(Double.parseDouble(amountTxt.getText().toString()));
        category.setTimestamp(timestamp);
        category.setDescription(descriptionTxt.getText().toString());
        FirebaseManager.updateCategories(category, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                MyUtility.disableLoading(CategoryAdd.this);
                isSavingProgress = false;
                if (success) {
                    finish();
                } else {

                }
            }
        });
    }

    private void addCategory() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        isSavingProgress = true;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(new Date());
        category = new Category(titleTxt.getText().toString(), descriptionTxt.getText().toString(), Double.parseDouble(amountTxt.getText().toString()), timestamp);
        FirebaseManager.addCategoryIntoDB(category, new OnSuccessListener<Category>() {
            @Override
            public void onSuccess(Category category1) {
                MyUtility.disableLoading(CategoryAdd.this);
                isSavingProgress = false;
                if (category1 != null) {
                    MyUtility.currentUser.addCategories(category1);
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
            Category[] categories = MyUtility.currentUser.getCategories();
            for (Category category1 : categories) {
                if(isUpdate&&category!=null&&category.getTitle().equals(category1.getTitle()))
                    continue;
                if (category1.getTitle().equals(titleTxt.getText().toString())) {
                    titleTxtPar.setError("This title already exist");
                    return false;
                }
            }

        if (amountTxt.getText().toString().isEmpty()) {
            amountTxtPar.setError("Enter a amount");
            return false;
        }

        return true;
    }

    private void displayHintCateTitle() {
        final ArrayList<BudgetObject> elements = new ArrayList<>();
        elements.add(new BudgetObject("", "Business", ""));
        elements.add(new BudgetObject("", "Health", ""));
        elements.add(new BudgetObject("", "Health Expenses", ""));
        elements.add(new BudgetObject("", "Holidays", ""));
        elements.add(new BudgetObject("", "Children", ""));
        elements.add(new BudgetObject("", "Daily Living", ""));
        elements.add(new BudgetObject("", "Dues/Subscriptions", ""));
        elements.add(new BudgetObject("", "Education", ""));
        elements.add(new BudgetObject("", "Entertainment", ""));
        elements.add(new BudgetObject("", "Family Care", ""));
        elements.add(new BudgetObject("", "Financil Obligations", ""));
        elements.add(new BudgetObject("", "Gifts and Donations", ""));
        elements.add(new BudgetObject("", "Home/Rent", ""));
        elements.add(new BudgetObject("", "Housing", ""));
        elements.add(new BudgetObject("", "Insurance", ""));
        elements.add(new BudgetObject("", "Legal", ""));
        elements.add(new BudgetObject("", "Personals", ""));


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