package com.superprofan.mycalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

public class RulerSmall extends View {
    private Context context;
    private Paint linePaint;
    private Paint numberPaint;
    private float ppcm;
    private float ppi;
    private SharedPreferences pref;
    private int screenHeight = 0;
    private int screenWidth = 0;
    private Paint textPaint;

    public RulerSmall(Context c) {
        super(c);
        this.context = c;
    }

    public RulerSmall(Context c, AttributeSet attrs) {
        super(c, attrs);
        this.context = c;
        this.pref = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.ppi = this.pref.getFloat(MainActivity.PPI_PREF, 0.0f);
        this.ppcm = this.ppi / 2.54f;
        Display display = ((RulerCalibration) this.context).getWindowManager().getDefaultDisplay();
        this.screenWidth = display.getWidth();
        this.screenHeight = display.getHeight();
        this.linePaint = new Paint();
        this.linePaint.setStrokeWidth(1.0f);
        this.linePaint.setColor(Color.parseColor("#ffffff"));
        this.numberPaint = new Paint(1);
        this.numberPaint.setTextSize((float) (this.screenHeight / 30));
        this.numberPaint.setColor(Color.parseColor("#ffffff"));
        this.textPaint = new Paint(1);
        this.textPaint.setTextSize((float) (this.screenHeight / 30));
        this.textPaint.setColor(Color.parseColor("#ffffff"));
    }
    public RulerSmall(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        this.context = c;
    }
    public void onDraw(Canvas canvas) {
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        int lineWidth = (int) (((double) viewWidth) * 0.95d);
        int leftMargin = (int) (((double) viewWidth) * 0.05d);
        float scale = this.ppcm;
        for (int i = 0; ((float) i) < ((float) lineWidth) / scale; i++) {
            for (int j = 1; j < 10; j++) {
                if (j == 5) {
                    canvas.drawLine(((((float) j) * scale) / 10.0f) + (((float) leftMargin) + (((float) i) * scale)), (float) (viewHeight / 2), ((((float) j) * scale) / 10.0f) + (((float) leftMargin) + (((float) i) * scale)), (float) ((viewHeight / 2) - (this.screenHeight / 24)), this.linePaint);
                } else {
                    canvas.drawLine(((((float) j) * scale) / 10.0f) + (((float) leftMargin) + (((float) i) * scale)), (float) (viewHeight / 2), ((((float) j) * scale) / 10.0f) + (((float) leftMargin) + (((float) i) * scale)), (float) ((viewHeight / 2) - (this.screenHeight / 48)), this.linePaint);
                }
            }
            canvas.drawLine((((float) i) * scale) + ((float) leftMargin), (float) (viewHeight / 2), (((float) i) * scale) + ((float) leftMargin), (float) ((viewHeight / 2) - (this.screenHeight / 16)), this.linePaint);
            canvas.drawText(String.valueOf(i), (((float) leftMargin) + (((float) i) * scale)) + 4.0f, (float) ((viewHeight / 2) - (this.screenHeight / 18)), this.numberPaint);
        }
        canvas.drawLine((float) leftMargin, (float) (viewHeight / 2), (float) viewWidth, (float) (viewHeight / 2), this.linePaint);
        super.onDraw(canvas);
    }
    public void redraw(float newPpi) {
        this.ppi = newPpi;
        this.ppcm = this.ppi / 2.54f;
        invalidate();
    }
}
