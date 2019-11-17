package com.planner.budgetplanner;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.DatePicker;

public class MyUtility {


    public static User currentUser;
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static String getPickerDate(DatePicker picker)
    {
        return picker.getDayOfMonth()+"/"+(picker.getMonth()+1)+"/"+picker.getYear();
    }

    public static Dialog displayDatePickerWindow(Activity activity)
    {
        final Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.calender_window);
        dialog.show();

        return dialog;
    }
}
