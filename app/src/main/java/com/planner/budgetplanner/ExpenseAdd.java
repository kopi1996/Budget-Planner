package com.planner.budgetplanner;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.BudgetObjectAdapter;
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
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));
//        elements.add(new BudgetObject("1",""));

        int width = 0;
        int height = 0;
        Point size = new Point();
        WindowManager w = this.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            width = d.getWidth();
            height = d.getHeight();
        }


        int dialogHeight = (int) ((height / 100.0) * 75);

        final Dialog dialog = new Dialog(this,R.style.DialogTitleTheme);
        dialog.setContentView(R.layout.category_pick_dialog);
        dialog.setTitle("Pick Category");
        final WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.height = dialogHeight;

        //dialog.getWindow().setAttributes(lWindowParams);

        final RecyclerView myListView = dialog.findViewById(R.id.catePickDiaViewList);
        myListView.setHasFixedSize(true);
        myListView.setNestedScrollingEnabled(false);
        myListView.setItemAnimator(new DefaultItemAnimator());

        BudgetObjectAdapter adapter=new BudgetObjectAdapter(elements, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                    dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.catPickDiaCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.catPickDiaAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1=new Dialog(ExpenseAdd.this,R.style.DialogTitleTheme);
                dialog1.setContentView(R.layout.category_add_dialog_box);
                dialog1.setTitle("Add Category");
                dialog1.show();
                dialog1.findViewById(R.id.catDialogHelpBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.findViewById(R.id.catDialogHelpBox).setVisibility(View.VISIBLE);
                    }
                });
                dialog.dismiss();
            }
        });


        myListView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = myListView.getLayoutParams();

        int minListViewHeight = (int) ((dialogHeight));
        if(adapter.getItemCount()>9)
            layoutParams.height=minListViewHeight;
        myListView.setLayoutParams(layoutParams);
        dialog.show();

    }
}