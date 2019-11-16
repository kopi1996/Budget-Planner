package com.planner.budgetplanner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class IncomeAdd extends AppCompatActivity {

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
    }

    private void displayDatePicker()
    {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);

        int width=(int)((point.x/100.0)*90);
        int height=(int)((point.y/100.0)*70);


        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.calender_window);

        dialog.show();

//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = inflater.inflate(R.layout.calender_window, (ViewGroup) findViewById(R.id.calenderLayout));
//
//        final PopupWindow pw = new PopupWindow(layout,point.x,point.y,true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            pw.setElevation(120f);
//        }
//        pw.showAtLocation(findViewById(R.id.incomeLayout), Gravity.CENTER,0,0);
    }
}
