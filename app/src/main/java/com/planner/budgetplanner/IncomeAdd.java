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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;

public class IncomeAdd extends AppCompatActivity {

    private static final String TAG = "IncomeAdd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);

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


    private void displayHintDialog()
    {
        int width = 0;
        int height = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            width = d.getWidth();
            height = d.getHeight();
        }


        int dialogHeight=(int) ((height/100.0)*75);

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.list_view_window);


        final WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.height = dialogHeight;

        dialog.getWindow().setAttributes(lWindowParams);



        String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
        ListView myListView=dialog.findViewById(R.id.listViewDialog);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_view_text_item,R.id.listItemTxt,countryList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position,convertView,parent);

                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = 60;
                params.width=100;

                view.setLayoutParams(params);
                return view;
            }
        };
        myListView.setAdapter(adapter);

        dialog.show();
    }

    private void displayDatePicker()
    {

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.calender_window);



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

        dialog.show();
    }
}
