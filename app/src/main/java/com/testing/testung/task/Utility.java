package com.testing.testung.task;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utility {
    public static int calculateNoOfColumns(Context context,int dpwi) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / dpwi);
        return noOfColumns;
    }
}
