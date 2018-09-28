package com.superprofan.mycalc;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.util.Arrays;

class Drawer {
    private static final float CURSOR_RELATIVE_HEIGHT = 1.0f;
    private static final int DP_CURSOR_CORNERS_RADIUS = 1;
    private static final int DP_CURSOR_WIDTH = 3;
    private static final int DP_NORMAL_MARK_WIDTH = 1;
    private static final int DP_ZERO_MARK_WIDTH = 2;
    private static final float NORMAL_MARK_RELATIVE_HEIGHT = 0.6f;
    private static final float SCALE_RANGE = 0.1f;
    private static final float SHADE_RANGE = 0.7f;
    private static final float ZERO_MARK_RELATIVE_HEIGHT = 0.8f;
    private int activeColor;
    private int[] colorSwitches = new int[]{-1, -1, -1};
    private int cursorCornersRadius;
    private RectF cursorRect = new RectF();
    private float[] gaps;
    private int marksCount;
    private int maxVisibleMarksCount;
    private int normalColor;
    private int normalMarkHeight;
    private int normalMarkWidth;
    private Paint paint = new Paint(1);
    private float[] scales;
    private float[] shades;
    private boolean showActiveRange;
    private HorizontalWheelView view;
    private int viewportHeight;
    private int zeroMarkHeight;
    private int zeroMarkWidth;

    Drawer(HorizontalWheelView view) {
        this.view = view;
        initDpSizes();
    }
    private void initDpSizes() {
        this.normalMarkWidth = convertToPx(1);
        this.zeroMarkWidth = convertToPx(2);
        this.cursorCornersRadius = convertToPx(1);
    }

    private int convertToPx(int dp) {
        return Utils.convertToPx(dp, this.view.getResources());
    }

    void setMarksCount(int marksCount) {
        this.marksCount = marksCount;
        this.maxVisibleMarksCount = (marksCount / 2) + 1;
        this.gaps = new float[this.maxVisibleMarksCount];
        this.shades = new float[this.maxVisibleMarksCount];
        this.scales = new float[this.maxVisibleMarksCount];
    }
    void setNormalColor(int color) {
        this.normalColor = color;
    }

    void setActiveColor(int color) {
        this.activeColor = color;
    }

    void setShowActiveRange(boolean show) {
        this.showActiveRange = show;
    }
    void onSizeChanged() {
        this.viewportHeight = (this.view.getHeight() - this.view.getPaddingTop()) - this.view.getPaddingBottom();
        this.normalMarkHeight = (int) (((float) this.viewportHeight) * NORMAL_MARK_RELATIVE_HEIGHT);
        this.zeroMarkHeight = (int) (((float) this.viewportHeight) * ZERO_MARK_RELATIVE_HEIGHT);
        setupCursorRect();
    }
    private void setupCursorRect() {
        int cursorHeight = (int) (((float) this.viewportHeight) * CURSOR_RELATIVE_HEIGHT);
        this.cursorRect.top = (float) (this.view.getPaddingTop() + ((this.viewportHeight - cursorHeight) / 2));
        this.cursorRect.bottom = this.cursorRect.top + ((float) cursorHeight);
        int cursorWidth = convertToPx(3);
        this.cursorRect.left = (float) ((this.view.getWidth() - cursorWidth) / 2);
        this.cursorRect.right = this.cursorRect.left + ((float) cursorWidth);
    }
    int getMarksCount() {
        return this.marksCount;
    }
    void onDraw(Canvas canvas) {
        double step = 6.283185307179586d / ((double) this.marksCount);
        double offset = (1.5707963267948966d - this.view.getRadiansAngle()) % step;
        if (offset < 0.0d) {
            offset += step;
        }
        setupGaps(step, offset);
        setupShadesAndScales(step, offset);
        int zeroIndex = calcZeroIndex(step);
        setupColorSwitches(step, offset, zeroIndex);
        drawMarks(canvas, zeroIndex);
        drawCursor(canvas);
    }

    private void setupGaps(double step, double offset) {
        this.gaps[0] = (float) Math.sin(offset / 2.0d);
        float sum = this.gaps[0];
        double angle = offset;
        int n = 1;
        while (angle + step <= 3.141592653589793d) {
            this.gaps[n] = (float) Math.sin((step / 2.0d) + angle);
            sum += this.gaps[n];
            angle += step;
            n++;
        }
        sum += (float) Math.sin((3.141592653589793d + angle) / 2.0d);
        if (n != this.gaps.length) {
            this.gaps[this.gaps.length - 1] = -1.0f;
        }
        float k = ((float) this.view.getWidth()) / sum;
        for (int i = 0; i < this.gaps.length; i++) {
            if (this.gaps[i] != -1.0f) {
                float[] fArr = this.gaps;
                fArr[i] = fArr[i] * k;
            }
        }
    }

    private void setupShadesAndScales(double step, double offset) {
        double angle = offset;
        for (int i = 0; i < this.maxVisibleMarksCount; i++) {
            double sin = Math.sin(angle);
            this.shades[i] = (float) (1.0d - (0.699999988079071d * (1.0d - sin)));
            this.scales[i] = (float) (1.0d - (0.10000000149011612d * (1.0d - sin)));
            angle += step;
        }
    }

    private int calcZeroIndex(double step) {
        double normalizedAngle = ((this.view.getRadiansAngle() + 1.5707963267948966d) + 6.283185307179586d) % 6.283185307179586d;
        if (normalizedAngle > 3.141592653589793d) {
            return -1;
        }
        return (int) ((3.141592653589793d - normalizedAngle) / step);
    }
    private void setupColorSwitches(double step, double offset, int zeroIndex) {
        if (this.showActiveRange) {
            double angle = this.view.getRadiansAngle();
            int afterMiddleIndex = 0;
            if (offset < 1.5707963267948966d) {
                afterMiddleIndex = ((int) ((1.5707963267948966d - offset) / step)) + 1;
            }
            if (angle > 4.71238898038469d) {
                this.colorSwitches[0] = 0;
                this.colorSwitches[1] = afterMiddleIndex;
                this.colorSwitches[2] = zeroIndex;
                return;
            } else if (angle >= 0.0d) {
                this.colorSwitches[0] = Math.max(0, zeroIndex);
                this.colorSwitches[1] = afterMiddleIndex;
                this.colorSwitches[2] = -1;
                return;
            } else if (angle < -4.71238898038469d) {
                this.colorSwitches[0] = 0;
                this.colorSwitches[1] = zeroIndex;
                this.colorSwitches[2] = afterMiddleIndex;
                return;
            } else if (angle < 0.0d) {
                this.colorSwitches[0] = afterMiddleIndex;
                this.colorSwitches[1] = zeroIndex;
                this.colorSwitches[2] = -1;
                return;
            } else {
                return;
            }
        }
        Arrays.fill(this.colorSwitches, -1);
    }

    private void drawMarks(Canvas canvas, int zeroIndex) {
        float x = (float) this.view.getPaddingLeft();
        int color = this.normalColor;
        int colorPointer = 0;
        int i = 0;
        while (i < this.gaps.length && this.gaps[i] != -1.0f) {
            x += this.gaps[i];
            while (colorPointer < 3 && i == this.colorSwitches[colorPointer]) {
                color = color == this.normalColor ? this.activeColor : this.normalColor;
                colorPointer++;
            }
            if (i != zeroIndex) {
                drawNormalMark(canvas, x, this.scales[i], this.shades[i], color);
            } else {
                drawZeroMark(canvas, x, this.scales[i], this.shades[i]);
            }
            i++;
        }
    }

    private void drawNormalMark(Canvas canvas, float x, float scale, float shade, int color) {
        float height = ((float) this.normalMarkHeight) * scale;
        float top = ((float) this.view.getPaddingTop()) + ((((float) this.viewportHeight) - height) / 2.0f);
        float bottom = top + height;
        this.paint.setStrokeWidth((float) this.normalMarkWidth);
        this.paint.setColor(applyShade(color, shade));
        canvas.drawLine(x, top, x, bottom, this.paint);
    }

    private int applyShade(int color, float shade) {
        return Color.rgb((int) (((float) Color.red(color)) * shade), (int) (((float) Color.green(color)) * shade), (int) (((float) Color.blue(color)) * shade));
    }

    private void drawZeroMark(Canvas canvas, float x, float scale, float shade) {
        float height = ((float) this.zeroMarkHeight) * scale;
        float top = ((float) this.view.getPaddingTop()) + ((((float) this.viewportHeight) - height) / 2.0f);
        float bottom = top + height;
        this.paint.setStrokeWidth((float) this.zeroMarkWidth);
        this.paint.setColor(applyShade(this.activeColor, shade));
        canvas.drawLine(x, top, x, bottom, this.paint);
    }

    private void drawCursor(Canvas canvas) {
        this.paint.setStrokeWidth(0.0f);
        this.paint.setColor(this.activeColor);
        canvas.drawRoundRect(this.cursorRect, (float) this.cursorCornersRadius, (float) this.cursorCornersRadius, this.paint);
    }
}

