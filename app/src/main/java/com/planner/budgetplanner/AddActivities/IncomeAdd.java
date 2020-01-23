package com.planner.budgetplanner.AddActivities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Date;

public class IncomeAdd extends BudgetObjectAdd<Income> implements OnSuccessListener<Date>, View.OnFocusChangeListener {

    public static final String INCOME_EDIT="IncomeEdit";
    public static final String INCOME_DATA_ID="IncomeDataId";

    private static final String TAG = "IncomeAdd";
    private TextInputLayout titleTxtPar;
    private TextInputLayout amountTxtPar;
    private TextInputEditText titleTxt;
    private TextInputEditText amountTxt;
    private TextInputEditText descriptionTxt;
    private Button datePickerBtn;
    private Date pickedDate;

    private boolean isSavingProgress;
    private boolean isUpate=false;
    private Income income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isUpate = bundle.getBoolean(INCOME_EDIT, false);
            if (isUpate) {
                String id = bundle.getString(INCOME_DATA_ID);
                if (id != null)
                    income = MyUtility.currentUser.getIncomeForId(id);
            }
        }
        if (income == null)
            isUpate = false;
        String title = isUpate ? "Edit Income" : "Add Income";
        initialize(title, income);
    }

    @Override
    protected void initialize(final String title, Income income) {
        super.initialize(title,income);
        titleTxt = findViewById(R.id.incomeAddTitleTxt);
        amountTxt = findViewById(R.id.incomeAddAmountTxt);
        descriptionTxt = findViewById(R.id.incomeAddDesTxt);
        datePickerBtn = findViewById(R.id.incomeAddDatePickBtn);
        titleTxtPar=findViewById(R.id.incomeAddTitleTxtPar);
        amountTxtPar=findViewById(R.id.incomeAddAmountTxtPar);
        amountTxt.setHint("Amount("+MyUtility.currentUser.getCurrencyType()+")");
        pickedDate=new Date();
        if(isUpate) {
            Log.i(TAG, "initialize: " + income.getTitle());
            titleTxt.setText(income.getTitle());
            amountTxt.setText(Double.toString(income.getAmount()));
            descriptionTxt.setText(income.getDescription());
            if (income.getTimestamp() != null) {
                pickedDate = MyUtility.convertFromUtcToStamp(income.getTimestamp().toDate()).toDate();
                Log.i(TAG, "displayDatePicker time: "+MyUtility.convertDateToString(MyUtility.convertFromUtcToStamp(income.getTimestamp().toDate()).toDate()));
            }
        }

        datePickerBtn.setText(MyUtility.convertDateToString(pickedDate));

        titleTxt.setOnFocusChangeListener(this);
        amountTxt.setOnFocusChangeListener(this);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker(IncomeAdd.this);
            }
        });
        findViewById(R.id.hintBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHintDialog(new OnSuccessListener<BudgetObject>() {
                    @Override
                    public void onSuccess(BudgetObject budgetObject) {
                        titleTxt.setText(budgetObject.getTitle());
                    }
                }, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
            }
        });
    }

    private void updateIncome() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        isSavingProgress = true;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(pickedDate);
        income.setTitle(titleTxt.getText().toString());
        income.setDescription(descriptionTxt.getText().toString());
        income.setAmount(Double.parseDouble(amountTxt.getText().toString()));
        income.setTimestamp(timestamp);
        FirebaseManager.updateIncome(income, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean sucess) {
                MyUtility.disableLoading(IncomeAdd.this);
                isSavingProgress = false;
                if (sucess) {
                    finish();
                } else {

                }
            }
        });
    }

    public void onBackPressed() {
        if(!isSavingProgress)
            super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
            case R.id.saveBtn:
                if(!isUpate)
                    addIncome();
                else
                    updateIncome();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu,menu);

        return true;
    }

    private boolean checkEverythingReady()
    {
        if(titleTxt.getText().toString().isEmpty())
        {
            titleTxtPar.setError("Enter a title");
            return false;
        }
        if(amountTxt.getText().toString().isEmpty())
        {
            amountTxtPar.setError("Enter a amount");
            return false;
        }

        return true;
    }

    private void addIncome() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        isSavingProgress = true;
        MyUtility.enableLoading(this);

        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(pickedDate);
        income = new Income(titleTxt.getText().toString(), descriptionTxt.getText().toString(), timestamp, Double.parseDouble(amountTxt.getText().toString()));
        FirebaseManager.addIncomeIntoDB(income, new OnSuccessListener<Income>() {
            @Override
            public void onSuccess(Income income) {
                MyUtility.disableLoading(IncomeAdd.this);
                isSavingProgress = false;
                if (income != null) {
                    MyUtility.currentUser.addIncomes(income);
                    finish();
                } else {

                }
            }
        });
    }

    private void displayHintDialog(final OnSuccessListener<BudgetObject> listener, DialogInterface.OnCancelListener cancelListener) {
        final ArrayList<BudgetObject> elements = new ArrayList<>();
        elements.add(new BudgetObject("", "Salary/Wages", ""));
        elements.add(new BudgetObject("", "Business", ""));
        elements.add(new BudgetObject("", "Interest/Dividends", ""));
        elements.add(new BudgetObject("", "Miscellaneous", ""));
        elements.add(new BudgetObject("", "Other Income", ""));
        elements.add(new BudgetObject("", "Pension", ""));
        elements.add(new BudgetObject("", "Refunds/Reimbursements", ""));
        elements.add(new BudgetObject("", "Transfer From Savings", ""));
        elements.add(new BudgetObject("", "Wages", ""));


        Dialog dialog = MyUtility.displayHintDialog(this, elements, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                listener.onSuccess(elements.get(pos));
            }
        }, cancelListener,true);
    }

    private void displayDatePicker(final OnSuccessListener<Date> listener)
    {
        final Dialog dialog=MyUtility.displayDatePickerWindow(this);
        final DatePicker picker=dialog.findViewById(R.id.datePicker);
        MyUtility.setDateForDatePicker(picker,pickedDate,null);
        final View okBtn = dialog.findViewById(R.id.okBtn);
        final View cancelBtn = dialog.findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSuccess(MyUtility.getPickerDate(picker));
                dialog.dismiss();
                okBtn.setOnClickListener(null);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSuccess(null);
                dialog.dismiss();
                cancelBtn.setOnClickListener(null);
            }
        });
    }

    @Override
    public void onSuccess(Date date) {
        if (date != null) {
            pickedDate=date;
            datePickerBtn.setText(MyUtility.convertDateToString(date));
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus)
            return;
        switch (v.getId()) {
            case R.id.incomeAddTitleTxt:
                titleTxtPar.setError(null);
                break;
            case R.id.incomeAddAmountTxt:
                amountTxtPar.setError(null);
                break;
            default:
                return;
        }
    }
}