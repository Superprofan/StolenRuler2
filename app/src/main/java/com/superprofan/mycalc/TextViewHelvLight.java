package com.superprofan.mycalc;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextViewHelvLight extends AppCompatTextView {
    public TextViewHelvLight(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewHelvLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewHelvLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/helvlt.otf"));
    }
}
