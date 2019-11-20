package com.planner.budgetplanner;

import android.app.Dialog;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;

import java.util.ArrayList;

public class ExpenseAdd extends AppCompatActivity {

    EditText pickCateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);

        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Button pickerBtn = findViewById(R.id.datePickerBtn);
        pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });


        pickCateBtn = findViewById(R.id.pickCateBtn);

//        EditText amountTxt=findViewById(R.id.amountId);
//        amountTxt.setSelected(false);
//
//        EditText desTxt=findViewById(R.id.descTxt);
//        desTxt.setSelected(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            desTxt.setFocusedByDefault(false);
//        }


        pickCateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHintDialog();
            }
        });

    }

    private void displayDatePicker() {
        final Dialog dialog = MyUtility.displayDatePickerWindow(this);
        final DatePicker picker = dialog.findViewById(R.id.datePicker);
        final View okBtn = dialog.findViewById(R.id.okBtn);
        final View cancelBtn = dialog.findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button pickerBtn = findViewById(R.id.datePickerBtn);
                pickerBtn.setText(MyUtility.getPickerDate(picker));
                dialog.dismiss();
                okBtn.setOnClickListener(null);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelBtn.setOnClickListener(null);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);

        return true;
    }

    private void displayHintDialog() {
        final ArrayList<BudgetObject> elements=new ArrayList<>();
        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));
        Dialog dialog = MyUtility.displayHintDialog(this, elements, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Toast.makeText(ExpenseAdd.this, elements.get(pos).getTitle(), Toast.LENGTH_LONG).show();
            }
        } ,true);
    }
}