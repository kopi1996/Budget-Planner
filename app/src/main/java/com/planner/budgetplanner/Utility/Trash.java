package com.planner.budgetplanner.Utility;

import android.util.Log;

import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Trash {

    private static final String TAG = "Trash";
    private static Map<String, Object> trashObject = new HashMap<>();

    private Trash() {

    }

    public static void addTrash(Object... object) {
        for (Object o : object) {
            if (!trashObject.containsKey(Integer.toString(o.hashCode())))
                trashObject.put(String.valueOf(o.hashCode()), o);
        }
    }

    public static boolean removeTrash(Object... object) {
        boolean isDelete = true;
        for (Object o : object) {
            if (trashObject.containsKey(Integer.toString(o.hashCode()))) {
                trashObject.remove(Integer.toString(o.hashCode()));
            } else
                isDelete = false;
        }

        return isDelete;
    }

    public static void clearTrash() {
        Set<String> keySet = trashObject.keySet();
        while (!keySet.isEmpty()) {
            String s = keySet.iterator().next();
            Object item = trashObject.get(s);
            if (item instanceof Expense)
                FirebaseManager.deleteExpense((Expense) item, null);
            else if (item instanceof Category)
                FirebaseManager.deleteCategory((Category) item, null);
            else if (item instanceof Income)
                FirebaseManager.deleteIncome((Income) item, null);
            if (item != null)
                removeTrash(item);
            keySet.remove(s);
        }
    }

    public static void deleteItems(Object... objects) {
        for (Object s : objects) {
            Object item = trashObject.get(s);
            if (item instanceof Expense)
                FirebaseManager.deleteExpense((Expense) item, null);
            else if (item instanceof Category)
                FirebaseManager.deleteCategory((Category) item,null);
            else if (item instanceof Income)
                FirebaseManager.deleteIncome((Income) item, null);

            removeTrash(item);
        }
    }
}