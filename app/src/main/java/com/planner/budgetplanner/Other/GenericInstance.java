package com.planner.budgetplanner.Other;

import android.os.Build;

import java.util.function.Supplier;

public class GenericInstance<T> {

    private Supplier<T> supplier;

    GenericInstance(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    T createContents() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return supplier.get();
        }
        return null;
    }
}
