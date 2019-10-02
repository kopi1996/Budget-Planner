package com.planner.budgetplanner;

import android.text.TextUtils;
import android.util.Patterns;

public class MyUtility {


    public static User currentUser;
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
