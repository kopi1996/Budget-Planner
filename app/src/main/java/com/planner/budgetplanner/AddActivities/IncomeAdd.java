package com.planner.budgetplanner.AddActivities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.MainActivity;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Date;

public class IncomeAdd extends BudgetObjectAdd<Income> implements OnSuccessListener<Date> {

    private static final String TAG = "IncomeAdd";
    private TextInputLayout titleTxtPar;
    private TextInputLayout amountTxtPar;
    private EditText titleTxt;
    private EditText amountTxt;
    private EditText descriptionTxt;
    private Button datePickerBtn;
    private Date pickedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);

        initialize("Add Income");


    }

    @Override
    protected void initialize(final String title) {
        super.initialize(title);
        titleTxt = findViewById(R.id.incomeAddTitleTxt);
        amountTxt = findViewById(R.id.incomeAddAmountTxt);
        descriptionTxt = findViewById(R.id.incomeAddDesTxt);
        datePickerBtn = findViewById(R.id.incomeAddDatePickBtn);
        titleTxtPar=findViewById(R.id.incomeAddTitleTxtPar);
        amountTxtPar=findViewById(R.id.incomeAddAmountTxtPar);
        pickedDate=new Date();
        datePickerBtn.setText(MyUtility.convertDateToString(pickedDate));

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
            case R.id.saveBtn:
                addIncome();
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
            amountTxtPar.setErrorEnabled(true);
            amountTxtPar.setError("Enter a amount");
            return false;
        }

        return true;
    }

    private void addIncome()
    {
        if(!checkEverythingReady())
            return;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(pickedDate);
        Income income=new Income(titleTxt.getText().toString(),descriptionTxt.getText().toString(),timestamp,Double.parseDouble(amountTxt.getText().toString()));
        FirebaseManager.addIncomeIntoDB(income, new OnSuccessListener<Income>() {
            @Override
            public void onSuccess(Income income) {
                if(income!=null)
                {
                    startActivity(new Intent(IncomeAdd.this, MainActivity.class));
                }
                else
                {

                }
                MyUtility.disableLoading(IncomeAdd.this);
            }
        });
    }

    private void displayHintDialog(final OnSuccessListener<BudgetObject> listener, DialogInterface.OnCancelListener cancelListener) {
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
        elements.add(new BudgetObject("", "1", ""));
        elements.add(new BudgetObject("", "1", ""));


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
}
