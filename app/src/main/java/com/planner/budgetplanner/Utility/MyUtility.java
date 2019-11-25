package com.planner.budgetplanner.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.planner.budgetplanner.Adapters.BudgetObjectAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.User;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyUtility {


    public static User currentUser;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String getPickerDate(DatePicker picker) {
        return picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear();
    }

    public static Dialog displayDatePickerWindow(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.calender_window);
        dialog.show();

        return dialog;
    }

    public static Dialog displayHintDialog(final Activity activity, ArrayList<BudgetObject> list, final MyItemAdapter.IItemListner listener, final boolean dismissBox) {
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

        final Dialog dialog = new Dialog(activity, R.style.MyDialogTheme);
        dialog.setContentView(R.layout.list_view_window);

        final WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.height = dialogHeight;

        dialog.getWindow().setAttributes(lWindowParams);


        final RecyclerView myListView = dialog.findViewById(R.id.listViewDialog);
        myListView.setHasFixedSize(true);
        myListView.setNestedScrollingEnabled(false);
        myListView.setItemAnimator(new DefaultItemAnimator());
        BudgetObjectAdapter adapter = new BudgetObjectAdapter(list, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                if (dismissBox)
                    dialog.dismiss();
                listener.onClick(v, pos);
            }
        });
        myListView.setAdapter(adapter);

        dialog.show();

        return dialog;
    }

    public static User conFirUserToMyUser(FirebaseUser firebaseUser)
    {
        return new User(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getEmail());
    }

    public static void startLoading(Activity activity,ILoadingListner listner)
    {
        activity.findViewById(R.id.loadingHoriBar).setVisibility(View.VISIBLE);
        listner.onLoading(true);
    }

    public static void stopLoading(Activity activity,ILoadingListner listner)
    {
        activity.findViewById(R.id.loadingHoriBar).setVisibility(View.INVISIBLE);
        listner.onLoading(false);
    }

    public static <T> T getInstanceOfT(Class<T> aClass) throws InstantiationException, IllegalAccessException {
        return aClass.newInstance();
    }

    public static<T extends BudgetObject> ArrayList<T> filterWithName(ArrayList<T> oldList,String newTxt)
    {
        ArrayList<T> newList = new ArrayList<>();
        for (T c : oldList) {
            if (c.getTitle().contains(newTxt))
                newList.add(c);
        }

        return newList;
    }


    public static Timestamp convDateToUtcTimeStamp()
    {
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.set(2019,10,24);

        Timestamp timestamp=new Timestamp(dateToUTC(calendar.getTime()));

        return timestamp;
    }

    public static Date dateFromUTC(Date date){
        return new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

    public static Date dateToUTC(Date date){
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }
}
