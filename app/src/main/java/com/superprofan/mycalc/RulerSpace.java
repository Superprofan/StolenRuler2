package com.superprofan.mycalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import java.text.DecimalFormat;
import java.text.NumberFormat;

    public class RulerSpace extends View {
        private Paint areaFillPaint;
        private Paint areaTextPaint;
        private float areaTextSize = 35.0f;
        private int bigLine = 0;
        private boolean bottomLineMoving = false;
        private int bottomLinePos = 0;
        private int boundOffset = 5;
        private int btnHeight = 0;
        private int btnUpperOffset = 0;
        private int btnWidth = 0;
        private Context context;
        private int decimalPoint = 2;
        private Paint gridThickLinePaint;
        private boolean horizontalLineMoving = false;
        private int horizontalLinePos = 0;
        private boolean leftLineMoving = false;
        private int leftLinePos = 0;
        private int lineColorCode = SupportMenu.CATEGORY_MASK;
        private String lineColorString = "Red";
        private Rect lockRect;
        private Paint lockedTextPaint;
        public boolean locking = false;
        private Rect menuRect;
        public int mode = 1;
        private Rect modeRect;
        private Paint movingLinePaint;
        private int normalLine = 0;
        private float ppcm = 0.0f;
        private float ppi = 0.0f;
        private SharedPreferences pref;
        private Editor pref_editor;
        private Rect resetRect;
        private Paint resultTextPaint;
        private float resultTextSize = 30.0f;
        private boolean rightLineMoving = false;
        private int rightLinePos = 0;
        private Paint scaleLinePaint;
        private Paint scaleTextPaint;
        private float scaleTextSize = 20.0f;
        private int screenHeight = 0;
        private int screenWidth = 0;
        private Paint selectedLinePaint;
        private boolean showArea = true;
        private boolean showGrid = true;
        private int smallLine = 0;
        private int textColorCode = ViewCompat.MEASURED_STATE_MASK;
        private String textColorString = "Black";
        private String textSizeString = "Normal";
        private int themeColorCode = -1;
        private String themeString = "Gray";
        private boolean topLineMoving = false;
        private int topLinePos = 0;
        public String unit = "mm";
        private Rect unitRect;
        private boolean verticalLineMoving = false;
        private int verticalLinePos = 0;

        public RulerSpace(Context c) {
            super(c);
            this.context = c;
            this.pref = PreferenceManager.getDefaultSharedPreferences(this.context);
            this.pref_editor = this.pref.edit();
            this.unit = this.pref.getString("LengthUnitPref", "mm");
            this.decimalPoint = Integer.parseInt(this.pref.getString("DecimalPointPref", "2"));
            this.themeString = this.pref.getString("ThemePref", "Gray");
            this.lineColorString = this.pref.getString("LineColorPref", "Red");
            this.textColorString = this.pref.getString("TextColorPref", "Black");
            this.textSizeString = this.pref.getString("TextSizePref", "Normal");
            this.showArea = this.pref.getBoolean("ShowAreaPref", true);
            this.showGrid = this.pref.getBoolean("ShowGridPref", true);
            this.ppi = this.pref.getFloat(MainActivity.PPI_PREF, 0.0f);
            this.ppcm = this.ppi / 2.54f;
            Display display = ((MainActivity) this.context).getWindowManager().getDefaultDisplay();
            this.screenWidth = display.getWidth();
            this.screenHeight = display.getHeight();
            setTextSize();
            if (this.screenHeight <= 480) {
                this.smallLine = this.screenHeight / 15;
                this.normalLine = this.screenHeight / 10;
                this.bigLine = (this.screenHeight * 2) / 15;
            } else {
                this.smallLine = 32;
                this.normalLine = 48;
                this.bigLine = 64;
            }
            Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/helvneueui.ttf");
            Typeface customFont2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/robothin.ttf");
            this.btnUpperOffset = this.bigLine + 8;
            this.btnHeight = this.screenHeight / 5;
            this.btnWidth = this.btnHeight;
            this.verticalLinePos = (this.screenWidth * 13) / 24;
            this.horizontalLinePos = this.screenHeight / 2;
            this.leftLinePos = (this.screenWidth * 1) / 4;
            this.topLinePos = (this.screenHeight * 1) / 4;
            this.rightLinePos = (this.screenWidth * 3) / 4;
            this.bottomLinePos = (this.screenHeight * 3) / 4;
            this.scaleTextPaint = new Paint(1);
            this.scaleTextPaint.setStyle(Style.STROKE);
            this.scaleTextPaint.setTextSize(this.scaleTextSize);
            this.scaleTextPaint.setTypeface(customFont2);
            this.scaleTextPaint.setColor(Color.parseColor("#ffffff"));
            this.resultTextPaint = new Paint(1);
            this.resultTextPaint.setTextSize(this.resultTextSize);
            this.resultTextPaint.setTypeface(customFont2);
            this.resultTextPaint.setColor(Color.parseColor("#ffffff"));
            this.lockedTextPaint = new Paint(1);
            this.lockedTextPaint.setTextSize(this.resultTextSize);
            this.lockedTextPaint.setTypeface(Typeface.create("null", Typeface.NORMAL));
            this.lockedTextPaint.setTextSkewX(-0.25f);
            this.lockedTextPaint.setColor(Color.parseColor("#ffffff"));
            this.areaTextPaint = new Paint(1);
            this.areaTextPaint.setTextSize(this.areaTextSize);
            this.areaTextPaint.setTypeface(customFont);
            this.areaTextPaint.setColor(Color.parseColor("#ffffff"));
            this.scaleLinePaint = new Paint();
            this.scaleLinePaint.setStyle(Style.STROKE);
            this.scaleLinePaint.setStrokeWidth(1.0f);
            this.scaleLinePaint.setColor(Color.parseColor("#ffffff"));
            this.movingLinePaint = new Paint();
            this.movingLinePaint.setStyle(Style.STROKE);
            this.movingLinePaint.setStrokeWidth(1.0f);
            this.movingLinePaint.setColor(Color.parseColor("#586478"));
            this.selectedLinePaint = new Paint();
            this.selectedLinePaint.setStyle(Style.STROKE);
            this.selectedLinePaint.setStrokeWidth(2.0f);
            this.selectedLinePaint.setColor(Color.parseColor("#fd3b39"));
            this.gridThickLinePaint = new Paint();
            this.gridThickLinePaint.setStyle(Style.STROKE);
            this.gridThickLinePaint.setStrokeWidth(2.0f);
            this.gridThickLinePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.areaFillPaint = new Paint(1);
            this.areaFillPaint.setStyle(Style.FILL);
            this.areaFillPaint.setColor(Color.parseColor("#2E4F6E"));
            Resources res = ((MainActivity) this.context).getResources();
        }

        public RulerSpace(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.context = c;
        }

        public RulerSpace(Context c, AttributeSet attrs, int defStyle) {
            super(c, attrs, defStyle);
            this.context = c;
        }

        public void onDraw(Canvas canvas) {
            int i, j;
            super.onDraw(canvas);
            float scale = 0.0f;
            if (this.unit.equals("mm")) {
                scale = this.ppcm;
            } else if (this.unit.equals("in")) {
                scale = this.ppi;
            }
            NumberFormat numberFormat = DecimalFormat.getInstance();
            numberFormat.setMaximumFractionDigits(this.decimalPoint);
            if (this.mode == 1) {
                canvas.drawRect(0.0f, 0.0f, (float) this.verticalLinePos, (float) this.horizontalLinePos, this.areaFillPaint);
            } else if (this.mode == 2) {
                canvas.drawRect((float) this.leftLinePos, (float) this.topLinePos, (float) this.rightLinePos, (float) this.bottomLinePos, this.areaFillPaint);
            }
            for (i = 0; ((float) i) < ((float) this.screenWidth) / scale; i++) {
               // int j;

                for (j = 1; j < 10; j++) {
                    if (j == 5) {
                        canvas.drawLine(((((float) j) * scale) / 10.0f) + (((float) i) * scale), 0.0f, ((((float) j) * scale) / 10.0f) + (((float) i) * scale), (float) this.normalLine, this.scaleLinePaint);
                    } else {
                        canvas.drawLine(((((float) j) * scale) / 10.0f) + (((float) i) * scale), 0.0f, ((((float) j) * scale) / 10.0f) + (((float) i) * scale), (float) this.smallLine, this.scaleLinePaint);
                    }
                }
                canvas.drawLine(((float) i) * scale, 0.0f, ((float) i) * scale, (float) this.bigLine, this.scaleLinePaint);
                if (i > 0) {
                    canvas.drawText(String.valueOf(i), (((float) i) * scale) + 4.0f, (float) (this.bigLine + 2), this.scaleTextPaint);
                }
            }
            for (i = 0; ((float) i) < ((float) this.screenHeight) / scale; i++) {
                for (j = 1; j < 10; j++) {
                    if (((double) ((((float) i) * scale) + ((((float) j) * scale) / 10.0f))) < ((double) this.screenHeight) - (((double) ((MainActivity) this.context).adOffset) * 1.9d)) {
                        if (j == 5) {
                            canvas.drawLine(0.0f, ((((float) j) * scale) / 10.0f) + (((float) i) * scale), (float) this.normalLine, (((float) i) * scale) + ((((float) j) * scale) / 10.0f), this.scaleLinePaint);
                        } else {
                            canvas.drawLine(0.0f, ((((float) j) * scale) / 10.0f) + (((float) i) * scale), (float) this.smallLine, (((float) i) * scale) + ((((float) j) * scale) / 10.0f), this.scaleLinePaint);
                        }
                    }
                }
                if (((double) (((float) i) * scale)) < ((double) this.screenHeight) - (((double) ((MainActivity) this.context).adOffset) * 1.9d)) {
                    canvas.drawLine(0.0f, ((float) i) * scale, (float) this.bigLine, ((float) i) * scale, this.scaleLinePaint);
                    if (i > 0) {
                        canvas.drawText(String.valueOf(i), (float) (this.bigLine - 10), ((((float) i) * scale) + this.scaleTextPaint.getTextSize()) + 2.0f, this.scaleTextPaint);
                    }
                }
            }
            float heightResult;
            float widthResult;
            String heightResultText;
            String widthResultText;
            Rect heightTextBounds;
            int heightTextWidth;
            int heightTextHeight;
            Rect widthTextBounds;
            int widthTextWidth;
            int widthTextHeight;
            if (this.mode == 1) {
                float verticalLineEnd_Y = (float) this.screenHeight;
                if (((MainActivity) this.context).adWidth > 0 && ((double) this.verticalLinePos) < ((double) ((MainActivity) this.context).adWidth) * 1.06d) {
                    verticalLineEnd_Y = ((float) this.screenHeight) - (((float) ((MainActivity) this.context).adOffset) * 1.25f);
                }
                if (this.verticalLineMoving) {
                    canvas.drawLine((float) this.verticalLinePos, 0.0f, (float) this.verticalLinePos, verticalLineEnd_Y, this.selectedLinePaint);
                } else {
                    canvas.drawLine((float) this.verticalLinePos, 0.0f, (float) this.verticalLinePos, verticalLineEnd_Y, this.movingLinePaint);
                }
                if (this.horizontalLineMoving) {
                    canvas.drawLine(0.0f, (float) this.horizontalLinePos, (float) this.screenWidth, (float) this.horizontalLinePos, this.selectedLinePaint);
                } else {
                    canvas.drawLine(0.0f, (float) this.horizontalLinePos, (float) this.screenWidth, (float) this.horizontalLinePos, this.movingLinePaint);
                }
                if (this.unit.equals("mm")) {
                    heightResult = Math.abs(((float) (this.horizontalLinePos * 10)) / scale);
                    widthResult = Math.abs(((float) (this.verticalLinePos * 10)) / scale);
                } else {
                    heightResult = Math.abs(((float) this.horizontalLinePos) / scale);
                    widthResult = Math.abs(((float) this.verticalLinePos) / scale);
                }
                heightResultText = numberFormat.format((double) heightResult) + this.unit;
                widthResultText = numberFormat.format((double) widthResult) + this.unit;
                heightTextBounds = new Rect();
                this.resultTextPaint.getTextBounds(heightResultText, 0, heightResultText.length(), heightTextBounds);
                heightTextWidth = heightTextBounds.width();
                heightTextHeight = heightTextBounds.height();
                widthTextBounds = new Rect();
                this.resultTextPaint.getTextBounds(widthResultText, 0, widthResultText.length(), widthTextBounds);
                widthTextWidth = widthTextBounds.width();
                widthTextHeight = widthTextBounds.height();
                if (((double) this.horizontalLinePos) < ((double) (this.screenHeight - ((widthTextHeight + 30) + 10))) - (((double) ((MainActivity) this.context).adOffset) * 1.25d)) {
                    if (this.verticalLinePos - 20 < widthTextWidth) {
                        canvas.drawText(widthResultText, 10.0f, (float) ((this.horizontalLinePos + 10) + widthTextHeight), this.resultTextPaint);
                    } else {
                        canvas.drawText(widthResultText, (float) ((this.verticalLinePos / 2) - (widthTextWidth / 2)), (float) ((this.horizontalLinePos + 10) + widthTextHeight), this.resultTextPaint);
                    }
                } else if (this.verticalLinePos - 20 < widthTextWidth) {
                    canvas.drawText(widthResultText, 10.0f, (float) (this.horizontalLinePos - 10), this.resultTextPaint);
                } else {
                    canvas.drawText(widthResultText, (float) ((this.verticalLinePos / 2) - (widthTextWidth / 2)), (float) (this.horizontalLinePos - 10), this.resultTextPaint);
                }
                if (this.verticalLinePos < (((this.screenWidth - this.btnWidth) - 10) - heightTextWidth) - 10) {
                    canvas.drawText(heightResultText, (float) (this.verticalLinePos + 10), (float) ((this.horizontalLinePos / 2) + heightTextHeight), this.resultTextPaint);
                    return;
                }
                canvas.drawText(heightResultText, (float) ((this.verticalLinePos - 10) - heightTextWidth), (float) ((this.horizontalLinePos / 2) + heightTextHeight), this.resultTextPaint);
            } else if (this.mode == 2) {
                float leftLineEnd_Y = (float) this.screenHeight;
                if (((MainActivity) this.context).adWidth > 0 && ((double) this.leftLinePos) < ((double) ((MainActivity) this.context).adWidth) * 1.06d) {
                    leftLineEnd_Y = ((float) this.screenHeight) - (((float) ((MainActivity) this.context).adOffset) * 1.25f);
                }
                if (this.leftLineMoving) {
                    canvas.drawLine((float) this.leftLinePos, 0.0f, (float) this.leftLinePos, leftLineEnd_Y, this.selectedLinePaint);
                } else {
                    canvas.drawLine((float) this.leftLinePos, 0.0f, (float) this.leftLinePos, leftLineEnd_Y, this.movingLinePaint);
                }
                if (this.topLineMoving) {
                    canvas.drawLine(0.0f, (float) this.topLinePos, (float) this.screenWidth, (float) this.topLinePos, this.selectedLinePaint);
                } else {
                    canvas.drawLine(0.0f, (float) this.topLinePos, (float) this.screenWidth, (float) this.topLinePos, this.movingLinePaint);
                }
                float rightLineEnd_Y = (float) this.screenHeight;
                if (((MainActivity) this.context).adWidth > 0 && ((double) this.rightLinePos) < ((double) ((MainActivity) this.context).adWidth) * 1.1d) {
                    rightLineEnd_Y = ((float) this.screenHeight) - (((float) ((MainActivity) this.context).adOffset) * 1.25f);
                }
                if (this.rightLineMoving) {
                    canvas.drawLine((float) this.rightLinePos, 0.0f, (float) this.rightLinePos, rightLineEnd_Y, this.selectedLinePaint);
                } else {
                    canvas.drawLine((float) this.rightLinePos, 0.0f, (float) this.rightLinePos, rightLineEnd_Y, this.movingLinePaint);
                }
                if (this.bottomLineMoving) {
                    canvas.drawLine(0.0f, (float) this.bottomLinePos, (float) this.screenWidth, (float) this.bottomLinePos, this.selectedLinePaint);
                } else {
                    canvas.drawLine(0.0f, (float) this.bottomLinePos, (float) this.screenWidth, (float) this.bottomLinePos, this.movingLinePaint);
                }
                if (this.unit.equals("mm")) {
                    heightResult = Math.abs(((float) ((this.topLinePos - this.bottomLinePos) * 10)) / scale);
                    widthResult = Math.abs(((float) ((this.leftLinePos - this.rightLinePos) * 10)) / scale);
                } else {
                    heightResult = Math.abs(((float) (this.topLinePos - this.bottomLinePos)) / scale);
                    widthResult = Math.abs(((float) (this.leftLinePos - this.rightLinePos)) / scale);
                }
                heightResultText = numberFormat.format((double) heightResult) + this.unit;
                widthResultText = numberFormat.format((double) widthResult) + this.unit;
                heightTextBounds = new Rect();
                this.resultTextPaint.getTextBounds(heightResultText, 0, heightResultText.length(), heightTextBounds);
                heightTextWidth = heightTextBounds.width();
                heightTextHeight = heightTextBounds.height();
                widthTextBounds = new Rect();
                this.resultTextPaint.getTextBounds(widthResultText, 0, widthResultText.length(), widthTextBounds);
                widthTextWidth = widthTextBounds.width();
                widthTextHeight = widthTextBounds.height();
                if (((double) this.bottomLinePos) < ((double) (this.screenHeight - ((widthTextHeight + 30) + 10))) - (((double) ((MainActivity) this.context).adOffset) * 1.25d)) {
                    if (((this.rightLinePos + this.leftLinePos) / 2) - 10 < widthTextWidth / 2) {
                        canvas.drawText(widthResultText, 10.0f, (float) ((this.bottomLinePos + 10) + widthTextHeight), this.resultTextPaint);
                    } else {
                        canvas.drawText(widthResultText, (float) (((this.rightLinePos + this.leftLinePos) / 2) - (widthTextWidth / 2)), (float) ((this.bottomLinePos + 10) + widthTextHeight), this.resultTextPaint);
                    }
                } else if (((this.rightLinePos + this.leftLinePos) / 2) - 10 < widthTextWidth / 2) {
                    canvas.drawText(widthResultText, 10.0f, (float) (this.bottomLinePos - 10), this.resultTextPaint);
                } else {
                    canvas.drawText(widthResultText, (float) (((this.rightLinePos + this.leftLinePos) / 2) - (widthTextWidth / 2)), (float) (this.bottomLinePos - 10), this.resultTextPaint);
                }
                if (this.rightLinePos < (((this.screenWidth - this.btnWidth) - 10) - heightTextWidth) - 10) {
                    canvas.drawText(heightResultText, (float) (this.rightLinePos + 10), (float) (((this.topLinePos + this.bottomLinePos) / 2) + heightTextHeight), this.resultTextPaint);
                    return;
                }
                canvas.drawText(heightResultText, (float) ((this.rightLinePos - 10) - heightTextWidth), (float) (((this.topLinePos + this.bottomLinePos) / 2) + heightTextHeight), this.resultTextPaint);
            }
        }

        public boolean changeunit_mm() {
            this.unit = "mm";
            this.pref_editor.putString("LengthUnitPref", this.unit);
            this.pref_editor.commit();
            invalidate();
            return true;
        }

        public boolean changeunit_in() {
            this.unit = "in";
            this.pref_editor.putString("LengthUnitPref", this.unit);
            this.pref_editor.commit();
            invalidate();
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            int action = event.getAction();
            int actionCode = action & 255;
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (action == 0) {
                String keyString = getPressKey(x, y);
                if (keyString.equals("Cross Point")) {
                    this.verticalLineMoving = true;
                    this.horizontalLineMoving = true;
                } else if (keyString.equals("Vertical Line")) {
                    this.verticalLineMoving = true;
                } else if (keyString.equals("Horizontal Line")) {
                    this.horizontalLineMoving = true;
                } else if (keyString.equals("Left Top Cross Point")) {
                    this.leftLineMoving = true;
                    this.topLineMoving = true;
                } else if (keyString.equals("Right Bottom Cross Point")) {
                    this.rightLineMoving = true;
                    this.bottomLineMoving = true;
                } else if (keyString.equals("Left Line")) {
                    this.leftLineMoving = true;
                } else if (keyString.equals("Top Line")) {
                    this.topLineMoving = true;
                } else if (keyString.equals("Right Line")) {
                    this.rightLineMoving = true;
                } else if (keyString.equals("Bottom Line")) {
                    this.bottomLineMoving = true;
                }
            }
            if (action == 2) {
                if (this.verticalLineMoving) {
                    this.verticalLinePos = x;
                }
                if (this.horizontalLineMoving && ((double) y) < ((double) this.screenHeight) - (((double) ((MainActivity) this.context).adOffset) * 1.2d)) {
                    this.horizontalLinePos = y;
                }
                if (this.leftLineMoving) {
                    if (x >= this.rightLinePos) {
                        this.leftLineMoving = false;
                        this.rightLineMoving = true;
                        this.rightLinePos = x;
                    } else {
                        this.leftLinePos = x;
                    }
                }
                if (this.topLineMoving && ((double) y) < ((double) this.screenHeight) - (((double) ((MainActivity) this.context).adOffset) * 1.2d)) {
                    if (y >= this.bottomLinePos) {
                        this.topLineMoving = false;
                        this.bottomLineMoving = true;
                        this.bottomLinePos = y;
                    } else {
                        this.topLinePos = y;
                    }
                }
                if (this.rightLineMoving) {
                    if (x <= this.leftLinePos) {
                        this.rightLineMoving = false;
                        this.leftLineMoving = true;
                        this.leftLinePos = x;
                    } else {
                        this.rightLinePos = x;
                    }
                }
                if (this.bottomLineMoving && ((double) y) < ((double) this.screenHeight) - (((double) ((MainActivity) this.context).adOffset) * 1.2d)) {
                    if (y <= this.topLinePos) {
                        this.bottomLineMoving = false;
                        this.topLineMoving = true;
                        this.topLinePos = y;
                    } else {
                        this.bottomLinePos = y;
                    }
                }
            }
            if (action == 1) {
                this.verticalLineMoving = false;
                this.horizontalLineMoving = false;
                this.leftLineMoving = false;
                this.topLineMoving = false;
                this.rightLineMoving = false;
                this.bottomLineMoving = false;
            }
            invalidate();
            return true;
        }

        public String getPressKey(int x, int y) {
            if (!this.locking) {
                if (this.mode == 1) {
                    if (Math.abs(x - this.verticalLinePos) < 25 && Math.abs(y - this.horizontalLinePos) < 25) {
                        return "Cross Point";
                    }
                    if (Math.abs(x - this.verticalLinePos) < 35) {
                        return "Vertical Line";
                    }
                    if (Math.abs(y - this.horizontalLinePos) < 35) {
                        return "Horizontal Line";
                    }
                }
                if (this.mode == 2) {
                    if (Math.abs(x - this.leftLinePos) < 25 && Math.abs(y - this.topLinePos) < 25) {
                        return "Left Top Cross Point";
                    }
                    if (Math.abs(x - this.rightLinePos) < 25 && Math.abs(y - this.bottomLinePos) < 25) {
                        return "Right Bottom Cross Point";
                    }
                    if (Math.abs(x - this.leftLinePos) < 35) {
                        return "Left Line";
                    }
                    if (Math.abs(y - this.topLinePos) < 35) {
                        return "Top Line";
                    }
                    if (Math.abs(x - this.rightLinePos) < 35) {
                        return "Right Line";
                    }
                    if (Math.abs(y - this.bottomLinePos) < 35) {
                        return "Bottom Line";
                    }
                }
            }
            return "";
        }

        public void setTextSize() {
            if (this.textSizeString.equals("Small")) {
                this.scaleTextSize = ((float) this.screenHeight) / 30.0f;
                this.resultTextSize = ((float) this.screenHeight) / 20.0f;
                this.areaTextSize = ((float) this.screenHeight) / 22.0f;
            } else if (this.textSizeString.equals("Normal")) {
                this.scaleTextSize = ((float) this.screenHeight) / 24.0f;
                this.resultTextSize = ((float) this.screenHeight) / 16.0f;
                this.areaTextSize = ((float) this.screenHeight) / 18.0f;
            } else if (this.textSizeString.equals("Large")) {
                this.scaleTextSize = ((float) this.screenHeight) / 21.0f;
                this.resultTextSize = ((float) this.screenHeight) / 13.5f;
                this.areaTextSize = ((float) this.screenHeight) / 16.5f;
            } else if (this.textSizeString.equals("Huge")) {
                this.scaleTextSize = ((float) this.screenHeight) / 18.0f;
                this.resultTextSize = ((float) this.screenHeight) / 11.5f;
                this.areaTextSize = ((float) this.screenHeight) / 13.5f;
            }
            if (this.scaleTextPaint != null) {
                this.scaleTextPaint.setTextSize(this.scaleTextSize);
            }
            if (this.resultTextPaint != null) {
                this.resultTextPaint.setTextSize(this.resultTextSize);
            }
            if (this.lockedTextPaint != null) {
                this.lockedTextPaint.setTextSize(this.resultTextSize);
            }
            if (this.areaTextPaint != null) {
                this.areaTextPaint.setTextSize(this.areaTextSize);
            }
        }

        public void resetRuler() {
            if (this.mode == 1) {
                this.verticalLinePos = (this.screenWidth * 10) / 20;
                this.horizontalLinePos = (this.screenHeight * 2) / 3;
            } else if (this.mode == 2) {
                this.leftLinePos = (((this.screenWidth * 1) / 5) * 2) / 3;
                this.topLinePos = (((this.screenHeight * 1) / 5) * 2) / 3;
                this.rightLinePos = (((this.screenWidth * 3) / 5) * 2) / 3;
                this.bottomLinePos = (((this.screenHeight * 3) / 5) * 2) / 3;
            }
            invalidate();
        }

        public void renewData() {
            if (this.pref != null) {
                this.unit = this.pref.getString("LengthUnitPref", "mm");
                this.decimalPoint = Integer.parseInt(this.pref.getString("DecimalPointPref", "2"));
                this.themeString = this.pref.getString("ThemePref", "White");
                this.lineColorString = this.pref.getString("LineColorPref", "Red");
                this.textColorString = this.pref.getString("TextColorPref", "Black");
                this.textSizeString = this.pref.getString("TextSizePref", "Normal");
                this.showArea = this.pref.getBoolean("ShowAreaPref", true);
                this.showGrid = this.pref.getBoolean("ShowGridPref", true);
                this.ppi = this.pref.getFloat(MainActivity.PPI_PREF, 0.0f);
                this.ppcm = this.ppi / 2.54f;
                setTextSize();
                setBackgroundColor(Color.parseColor("#202026"));
                invalidate();
            }
        }
    }

