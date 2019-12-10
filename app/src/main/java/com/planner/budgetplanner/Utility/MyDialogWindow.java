package com.planner.budgetplanner.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.planner.budgetplanner.Adapters.BudgetObjectAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.R;

import java.util.ArrayList;

public class MyDialogWindow {

    public static Dialog defaultSortDialog(final Activity activity, final DialogInterface.OnCancelListener cancelListener, final boolean dismissBox) {
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
        dialog.setContentView(R.layout.sorting_dialog_box);
        dialog.setTitle("Display preferences");
//        final WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
//        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
//        lWindowParams.height = dialogHeight;
//
//        dialog.getWindow().setAttributes(lWindowParams);

        dialog.setOnCancelListener(cancelListener);
        dialog.show();

        return dialog;
    }

}
