package com.planner.budgetplanner.Utility;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import static com.planner.budgetplanner.FirebaseManager.TAG;

public class DateUtility {
    public static int getYearFromDate(Date date)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    public static int getMaxDateForMonth(int month,int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        Log.i(TAG, "getMaxDateForMonth: "+calendar.getActualMaximum(Calendar.DATE));
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public static int getMonthFromDate(Date date)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH);
    }

    public static String getSortNameForMonth(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Invalid Month input";
        }
    }

    public static int getMonthFromShortName(String month)
    {
        int index=-1;

        if(month.equals("Jan"))
            index=0;
        else if(month.equals("Feb"))
            index=1;
        else if(month.equals("Mar"))
            index=2;
        else if(month.equals("Apr"))
            index=3;
        else if (month.equals("May"))
            index=4;
        else if (month.equals("Jun"))
            index=5;
        else if (month.equals("Jul"))
            index=6;
        else if (month.equals("Aug"))
            index=7;
        else if(month.equals("Sep"))
            index=8;
        else if (month.equals("Oct"))
            index=9;
        else if (month.equals("Nov"))
            index=10;
        else if (month.equals("Dec"))
            index=11;

        return index;
    }

    public static int getDayFromDate(Date date)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DATE);
    }
}
