package com.planner.budgetplanner;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

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

    public static Dialog displayHintDialog(final Activity activity, String[] arr, final AdapterView.OnItemClickListener listener, final boolean dismissBox) {
        int width = 0;
        int height = 0;
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

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

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.list_view_window);


        final WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.height = dialogHeight;

        dialog.getWindow().setAttributes(lWindowParams);


        final ListView myListView = dialog.findViewById(R.id.listViewDialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.list_view_text_item, R.id.listItemTxt, arr);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(parent,view,position,id);
                if (dismissBox) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

        return dialog;
    }
}
