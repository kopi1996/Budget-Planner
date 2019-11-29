package com.planner.budgetplanner.Utility;

import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Trash {

    private static Map<String, Object> trashObject = new HashMap<>();

    private Trash() {

    }

    public static void addTrash(Object object) {
        if (!trashObject.containsKey(object.hashCode()))
            trashObject.put(String.valueOf(object.hashCode()), object);
    }

    public static boolean removeTrash(Object object) {
        if (trashObject.containsKey(object.hashCode())) {
            trashObject.remove(object.hashCode());
            return true;
        }
        return false;
    }

    public static void clearTrash() {
        Set<String> keySet = trashObject.keySet();
        for (String s : keySet) {
            Object item = trashObject.get(s);
            if (item instanceof Expense)
                FirebaseManager.deleteExpense((Expense) item, null);
            else if (item instanceof Category)
                FirebaseManager.deleteCategory((Category) item);
            else if (item instanceof Income)
                FirebaseManager.deleteIncome((Income) item, null);

            removeTrash(item);
        }
    }

    public static void deleteItems(Object... objects) {
        for (Object s : objects) {
            Object item = trashObject.get(s);
            if (item instanceof Expense)
                FirebaseManager.deleteExpense((Expense) item, null);
            else if (item instanceof Category)
                FirebaseManager.deleteCategory((Category) item);
            else if (item instanceof Income)
                FirebaseManager.deleteIncome((Income) item, null);

            removeTrash(item);
        }
    }
}