package com.superprofan.mycalc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.net.sip.SipAudioCall;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
/**
 * Created by SuperPuper on 25.09.2018.
 */

public class HorizontalWheelView extends View{
    private static final int DEFAULT_ACTIVE_COLOR = -11227920;
    private static final boolean DEFAULT_END_LOCK = false;
    private static final int DEFAULT_MARKS_COUNT = 40;
    private static final int DEFAULT_NORMAL_COLOR = -1;
    private static final boolean DEFAULT_ONLY_POSITIVE_VALUES = false;
    private static final boolean DEFAULT_SHOW_ACTIVE_RANGE = true;
    private static final boolean DEFAULT_SNAP_TO_MARKS = false;
    private static final int DP_DEFAULT_HEIGHT = 32;
    private static final int DP_DEFAULT_WIDTH = 200;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private double angle;
    private Drawer drawer = new Drawer(this);
    private boolean endLock;
    private Listener listener;
    private boolean onlyPositiveValues;
    private TouchHandler touchHandler = new TouchHandler(this);

    public static class Listener {
        public void onRotationChanged(double radians) {
        }

        public void onScrollStateChanged(int state) {
        }
    }

    public HorizontalWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrs(attrs);
    }


    private void readAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, C0478R.styleable.HorizontalWheelView);
        this.drawer.setMarksCount(a.getInt(C0478R.styleable.HorizontalWheelView_marksCount, 40));
        this.drawer.setNormalColor(a.getColor(C0478R.styleable.HorizontalWheelView_normalColor, -1));
        this.drawer.setActiveColor(a.getColor(C0478R.styleable.HorizontalWheelView_activeColor, DEFAULT_ACTIVE_COLOR));
        this.drawer.setShowActiveRange(a.getBoolean(C0478R.styleable.HorizontalWheelView_showActiveRange, DEFAULT_SHOW_ACTIVE_RANGE));
        this.touchHandler.setSnapToMarks(a.getBoolean(C0478R.styleable.HorizontalWheelView_snapToMarks, false));
        this.endLock = a.getBoolean(C0478R.styleable.HorizontalWheelView_endLock, false);
        this.onlyPositiveValues = a.getBoolean(C0478R.styleable.HorizontalWheelView_onlyPositiveValues, false);
        a.recycle();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        this.touchHandler.setListener(listener);
    }

    public void setRadiansAngle(double radians) {
        if (!checkEndLock(radians)) {
            this.angle = radians % 6.283185307179586d;
        }
        if (this.onlyPositiveValues && this.angle < 0.0d) {
            this.angle += 6.283185307179586d;
        }
        invalidate();
        if (this.listener != null) {
            this.listener.onRotationChanged(this.angle);
        }
    }
    private boolean checkEndLock(double radians) {
        if (!this.endLock) {
            return false;
        }
        boolean hit = false;
        if (radians >= 6.283185307179586d) {
            this.angle = Math.nextAfter(6.283185307179586d, Double.NEGATIVE_INFINITY);
            hit = DEFAULT_SHOW_ACTIVE_RANGE;
        } else if (this.onlyPositiveValues && radians < 0.0d) {
            this.angle = 0.0d;
            hit = DEFAULT_SHOW_ACTIVE_RANGE;
        } else if (radians <= -6.283185307179586d) {
            this.angle = Math.nextAfter(-6.283185307179586d, Double.POSITIVE_INFINITY);
            hit = DEFAULT_SHOW_ACTIVE_RANGE;
        }
        if (!hit) {
            return hit;
        }
        this.touchHandler.cancelFling();
        return hit;
    }

    public void setDegreesAngle(double degrees) {
        setRadiansAngle((3.141592653589793d * degrees) / 180.0d);
    }

    public void setCompleteTurnFraction(double fraction) {
        setRadiansAngle((2.0d * fraction) * 3.141592653589793d);
    }

    public double getRadiansAngle() {
        return this.angle;
    }

    public double getDegreesAngle() {
        return (getRadiansAngle() * 180.0d) / 3.141592653589793d;
    }

    public double getCompleteTurnFraction() {
        return getRadiansAngle() / 6.283185307179586d;
    }

    public void setOnlyPositiveValues(boolean onlyPositiveValues) {
        this.onlyPositiveValues = onlyPositiveValues;
    }

    public void setEndLock(boolean lock) {
        this.endLock = lock;
    }

    public void setMarksCount(int marksCount) {
        this.drawer.setMarksCount(marksCount);
        invalidate();
    }

    public void setShowActiveRange(boolean show) {
        this.drawer.setShowActiveRange(show);
        invalidate();
    }

    public void setNormaColor(int color) {
        this.drawer.setNormalColor(color);
        invalidate();
    }

    public void setActiveColor(int color) {
        this.drawer.setActiveColor(color);
        invalidate();
    }

    public void setSnapToMarks(boolean snapToMarks) {
        this.touchHandler.setSnapToMarks(snapToMarks);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.touchHandler.onTouchEvent(event);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.drawer.onSizeChanged();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(resolveMeasureSpec(widthMeasureSpec, 200), resolveMeasureSpec(heightMeasureSpec, 32));
    }

    private int resolveMeasureSpec(int measureSpec, int dpDefault) {
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == 1073741824) {
            return measureSpec;
        }
        int defaultSize = Utils.convertToPx(dpDefault, getResources());
        if (mode == Integer.MIN_VALUE) {
            defaultSize = Math.min(defaultSize, MeasureSpec.getSize(measureSpec));
        }
        return MeasureSpec.makeMeasureSpec(defaultSize, 1073741824);
    }

    protected void onDraw(Canvas canvas) {
        this.drawer.onDraw(canvas);
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.angle = this.angle;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.angle = ss.angle;
        invalidate();
    }

    int getMarksCount() {
        return this.drawer.getMarksCount();
    }



}
