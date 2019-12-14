package com.planner.budgetplanner.Utility;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {
    public static int getYearFromDate(Date date)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
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

    public static int getDayFromDate(Date date)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DATE);
    }
}
