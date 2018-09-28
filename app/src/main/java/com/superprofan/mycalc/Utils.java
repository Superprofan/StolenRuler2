package com.superprofan.mycalc;

import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {
    Utils() {
    }

    static int convertToPx(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(1, (float) dp, resources.getDisplayMetrics());
    }
}
