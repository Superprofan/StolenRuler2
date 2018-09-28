package com.superprofan.mycalc;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;



public class RulerCalibration extends Activity {
    private double diagonalLength = 0.0d;
    double dopsize;
    private HorizontalWheelView horizontalWheelView;
    private int inch;
    private float ppi = 0.0f;
    private SharedPreferences pref;
    private Editor pref_info_editor;
    private RulerSmall rulerSmall;
    double size;
    private int small_inch;

    /* renamed from: calculate.willmaze.ru.build_calculate.Instruments.Ruler.RulerCalibration$1 */
    class C06161 extends HorizontalWheelView.Listener {
        C06161() {
        }

        public void onRotationChanged(double radians) {
            RulerCalibration.this.dopsize = RulerCalibration.this.horizontalWheelView.getDegreesAngle() / 72.0d;
            RulerCalibration.this.updateSizeToPpi();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.pref_info_editor = this.pref.edit();
        this.inch = this.pref.getInt(MainActivity.SS_INCH_PREF, 5);
        this.small_inch = this.pref.getInt(MainActivity.SS_SMALL_INCH_PREF, 50);
        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        this.diagonalLength = Math.sqrt(Math.pow((double) screenWidth, 2.0d) + Math.pow((double) screenHeight, 2.0d));
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int densityDpi = metrics.densityDpi;
        setContentView(C0374R.layout.ruler_calibration);
        initViews();
        setupListeners();
        getWindow().setLayout((int) (((double) screenWidth) * 1.0d), (int) (((double) screenHeight) * 1.0d));
        this.size = 5.0d;
        this.rulerSmall = (RulerSmall) findViewById(C0374R.id.Ruler_Small);
        updateSizeToPpi();
        setResult(-1);
    }

    private void setupListeners() {
        this.horizontalWheelView.setListener(new C06161());
    }

    private void initViews() {
        this.horizontalWheelView = (HorizontalWheelView) findViewById(C0374R.id.horizontalWheelView);
    }

    public void updateSizeToPpi() {
        this.size = 8.0d + this.dopsize;
        this.ppi = (float) (this.diagonalLength / ((double) Float.parseFloat(String.valueOf(this.size))));
        if (this.rulerSmall != null) {
            this.rulerSmall.redraw(this.ppi);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onClick(View view) {
        this.pref_info_editor.putInt(MainActivity.SS_INCH_PREF, this.inch);
        this.pref_info_editor.putInt(MainActivity.SS_SMALL_INCH_PREF, this.small_inch);
        this.pref_info_editor.putFloat(MainActivity.PPI_PREF, this.ppi);
        this.pref_info_editor.commit();
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}
