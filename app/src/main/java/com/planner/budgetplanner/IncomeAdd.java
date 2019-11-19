package com.planner.budgetplanner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
        final String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand","India", "China", "australia", "Portugle", "America", "NewZealand","India", "China", "australia", "Portugle", "America", "NewZealand"};

        Dialog dialog = MyUtility.displayHintDialog(this, countryList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(IncomeAdd.this, countryList[position], Toast.LENGTH_LONG).show();
            }
        },true);

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