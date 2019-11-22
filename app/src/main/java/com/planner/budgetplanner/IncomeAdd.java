package com.planner.budgetplanner;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;

public class IncomeAdd extends AppCompatActivity {

    private static final String TAG = "IncomeAdd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);
        getSupportActionBar().setTitle("Add Income");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.datePickerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });
        findViewById(R.id.hintBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHintDialog();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu,menu);

        return true;
    }

    private void displayHintDialog()
    {
        final ArrayList<BudgetObject> elements=new ArrayList<>();
        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));
        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

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
                Toast.makeText(IncomeAdd.this, elements.get(pos).getTitle(), Toast.LENGTH_LONG).show();
            }
        } ,true);
    }

    private void displayDatePicker()
    {
        final Dialog dialog=MyUtility.displayDatePickerWindow(this);
        final DatePicker picker=dialog.findViewById(R.id.datePicker);
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
}
