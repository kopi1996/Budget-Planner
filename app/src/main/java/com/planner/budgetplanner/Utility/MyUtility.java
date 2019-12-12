package com.planner.budgetplanner.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.planner.budgetplanner.Adapters.BudgetObjectAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.LoadingActivity;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Model.User;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyUtility {


    private static final String TAG = "MyUtol";
    public static User currentUser;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setDateForDatePicker(DatePicker picker, Date date, DatePicker.OnDateChangedListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    public static Date getPickerDate(DatePicker picker) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return calendar.getTime();//picker.getDayOfMonth() + "/" + (picker.getMonth() + 1) + "/" + picker.getYear();
    }

    public static Dialog displayDatePickerWindow(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.calender_window);
        dialog.show();

        return dialog;
    }

    public static String wrapDecPointDouble(double value) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        return df2.format(value);
    }

    public static Dialog displayHintDialog(final Activity activity, ArrayList<BudgetObject> list, final MyItemAdapter.IItemListner listener, final DialogInterface.OnCancelListener cancelListener, final boolean dismissBox) {
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

        dialog.setOnCancelListener(cancelListener);
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

    public static User conFirUserToMyUser(FirebaseUser firebaseUser) {
        return new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
    }

    public static void startLoading(Activity activity, ILoadingListner listner) {
        activity.findViewById(R.id.loadingHoriBar).setVisibility(View.VISIBLE);
        listner.onLoading(true);
    }

    public static void stopLoading(Activity activity, ILoadingListner listner) {
        activity.findViewById(R.id.loadingHoriBar).setVisibility(View.INVISIBLE);
        listner.onLoading(false);
    }

    public static <T> T getInstanceOfT(Class<T> aClass) throws InstantiationException, IllegalAccessException {
        return aClass.newInstance();
    }

    public static String convertDateToString(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public static String conDateToFullFormat(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
    }

    public static <T extends BudgetObject> ArrayList<T> filterWithName(ArrayList<T> oldList, String newTxt) {
        ArrayList<T> newList = new ArrayList<>();
        for (T c : oldList) {
            if (c.getTitle().contains(newTxt))
                newList.add(c);
        }

        return newList;
    }

    public static void enableLoading(LoadingActivity activity) {
        activity.findViewById(R.id.round_loading_bar).setVisibility(View.VISIBLE);
        activity.getHomeView().setVisibility(View.INVISIBLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void disableLoading(LoadingActivity activity) {
        activity.findViewById(R.id.round_loading_bar).setVisibility(View.INVISIBLE);
        activity.getHomeView().setVisibility(View.VISIBLE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
//
//    public static void enableLoading(Activity activity) {
//        activity.findViewById(R.id.loadingHoriBar).setVisibility(View.VISIBLE);
//        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }

//    public static void disableLoading(Activity activity) {
//        activity.findViewById(R.id.loadingHoriBar).setVisibility(View.INVISIBLE);
//        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }

    public static Timestamp convDateToUtcTimeStamp(Date date) {
//        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
//        calendar.set(2019,10,24);
//
        Timestamp timestamp = new Timestamp(dateToUTC(date));
        return timestamp;
    }

    public static Timestamp convertFromUtcToStamp(Date date) {
        Timestamp timestamp = new Timestamp(dateFromUTC(date));
        return timestamp;
    }

    public static Timestamp convertDateToStamp(Date date) {
        return new Timestamp(date);
    }

    public static Date dateFromUTC(Date date) {
        return new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

    public static Date dateToUTC(Date date) {
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

    public static <T extends BudgetObject> void sortList(List<T> list, SortType type, boolean isAscendant) {
        boolean isNeedSwap = true;
        while (isNeedSwap) {
            isNeedSwap = false;
            for (int i = 1; i < list.size(); i++) {
                boolean compare = false;
                switch (type) {
                    case Name:
                        if (isAscendant)
                            compare = list.get(i - 1).getTitle().compareTo(list.get(i).getTitle()) > 0;
                        else
                            compare = list.get(i - 1).getTitle().compareTo(list.get(i).getTitle()) < 0;
                        break;
                    case Date:
                        if (isAscendant)
                            compare = list.get(i - 1).getTimestamp().compareTo(list.get(i).getTimestamp()) > 0;
                        else
                            compare = list.get(i - 1).getTimestamp().compareTo(list.get(i).getTimestamp()) < 0;
                        break;
                    case Amount:
                        if (isAscendant)
                            compare = list.get(i - 1).getAmount() > list.get(i).getAmount();
                        else
                            compare = list.get(i - 1).getAmount() < list.get(i).getAmount();
                        break;
                }

                if (compare) {
                    T temp = list.get(i);
                    list.set(i, list.get(i - 1));
                    list.set(i - 1, temp);
                    isNeedSwap = true;
                }
            }
        }
    }

    public static <T extends BudgetObject> List<T> filterList(List<T> list,FilterType type)
    {
        if(type==FilterType.LIFETIME) {
            List<T> temp=new ArrayList<>();
            temp.addAll(list);
            return temp;
        }
        if(type==FilterType.CUSTOM)
            return null;

        int day=type==FilterType.SEVENDAY?7:type==FilterType.THIRTYDAY?30:type==FilterType.THREESIXTYDAY?360:180;

        Date currDate = new Date();
        Date fromDate = MyUtility.subtractDate(currDate, day);
        return filterList(list,fromDate,currDate);
    }

    public static <T extends BudgetObject> List<T> filterList(List<T> list,Date from,Date to) {

        List<T> tempList = new ArrayList<>();
        for (T t : list) {
            if (t.getTimestamp().toDate().compareTo(from) >= 0 && t.getTimestamp().toDate().compareTo(to) <= 0)
                tempList.add(t);
        }

        return tempList;
    }

    public static Date subtractDate(Date now, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }
}