package com.superprofan.mycalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.Timer;


public class MainActivity extends Activity {
    public static int CAL_SWITCH = 5001;
    public static String COUNT_PREF = "Count Pref";
    public static String COUNT_PREFERENCE = "Version 6 Count Pref";
    public static String PPI_PREF = "PPI Pref";
    public static int QUIT_DIALOG = 5003;
    public static int RESET_RULER_CONFIRMATION_CODE = 5002;
    public static String SS_INCH_PREF = "SS inch";
    public static String SS_SMALL_INCH_PREF = "SS small inch";
    private MainActivity activity;
    public int adHeight = 0;
    public int adOffset = 0;
    private Timer adTimer;
    public int adWidth = 0;
    ToggleButton blockselect;
    private int count = 0;
    ToggleButton lengthselect;
    private LinearLayout ll_ads;
    private LinearLayout ll_main;
    private View mainLayout;
    private View modeLayout;
    public boolean modeSelecting = false;
    private SharedPreferences pref;
    private Editor pref_editor;
    TextView reftext1;
    TextView reftext2;
    TextView reftext3;
    TextView reftext4;
    TextView reftext5;
    private RulerSpace rulerSpace;
    ToggleButton typeselect;
    private int ver6_count = 0;
    private WakeLock wakeLock;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        this.list = (ListView) findViewById(C0374R.id.list);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        runruler();
//        hidetextfirststart();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0374R.menu.menu_main_search, menu);
        return true;
    }


    public void runruler() {/*
        this.mainLayout = ((LayoutInflater) getSystemService("layout_inflater"));

                inflate(C0374R.layout.activity_main, null);
        this.typeselect = (ToggleButton) findViewById(C0374R.id.typeselect);
        this.blockselect = (ToggleButton) findViewById(C0374R.id.blockselect);
        this.lengthselect = (ToggleButton) findViewById(C0374R.id.lengthselect);
        //Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/helvneueui.ttf");
        this.pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        this.pref_editor = this.pref.edit();
        this.count = this.pref.getInt(COUNT_PREF, 0);
        if (this.count == 0) {
            Display display = getWindowManager().getDefaultDisplay();
            this.pref_editor.putFloat(PPI_PREF, (float) (Math.sqrt(Math.pow((double) display.getWidth(), 2.0d) + Math.pow((double) display.getHeight(), 2.0d)) / 3.5d));
            this.pref_editor.commit();

        }
        Editor access$14 = this.pref_editor;
        String str = COUNT_PREF;
        int access$13 = this.count + 1;
        this.count = access$13;
        access$14.putInt(str, access$13);
        access$14 = this.pref_editor;
        str = COUNT_PREFERENCE;
        access$13 = this.ver6_count + 1;
        this.ver6_count = access$13;
        access$14.putInt(str, access$13);
        this.pref_editor.commit();
        this.ll_main = (LinearLayout) this.mainLayout.findViewById(C0374R.id.ll_main);
        this.ll_ads = (LinearLayout) this.mainLayout.findViewById(C0374R.id.ll_ads);
        setContentView(this.mainLayout);
        this.pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        this.pref_editor = this.pref.edit();
        this.rulerSpace = new RulerSpace(this.activity);
        this.rulerSpace.setClickable(true);
        this.ll_main.addView(this.rulerSpace);
        */
    }
    public void hidetext() {
        this.reftext1 = (TextView) findViewById(C0374R.id.referencetext1);
        this.reftext2 = (TextView) findViewById(C0374R.id.referencetext2);
        this.reftext3 = (TextView) findViewById(C0374R.id.referencetext3);
        this.reftext4 = (TextView) findViewById(C0374R.id.referencetext4);
        this.reftext5 = (TextView) findViewById(C0374R.id.referencetext5);
        Animation fadeoutcosts = AnimationUtils.loadAnimation(getApplicationContext(), C0374R.anim.fade_out_long);
        this.reftext1.startAnimation(fadeoutcosts);
        this.reftext2.startAnimation(fadeoutcosts);
        this.reftext3.startAnimation(fadeoutcosts);
        this.reftext4.startAnimation(fadeoutcosts);
        this.reftext5.startAnimation(fadeoutcosts);
        this.reftext1.setVisibility(View.INVISIBLE);
        this.reftext2.setVisibility(View.INVISIBLE);
        this.reftext3.setVisibility(View.INVISIBLE);
        this.reftext4.setVisibility(View.INVISIBLE);
        this.reftext5.setVisibility(View.INVISIBLE);
    }

    public void hidetextfirststart() {
        this.reftext1 = (TextView) findViewById(C0374R.id.referencetext1);
        this.reftext2 = (TextView) findViewById(C0374R.id.referencetext2);
        this.reftext3 = (TextView) findViewById(C0374R.id.referencetext3);
        this.reftext4 = (TextView) findViewById(C0374R.id.referencetext4);
        this.reftext5 = (TextView) findViewById(C0374R.id.referencetext5);
/*        Animation fadestart = AnimationUtils.loadAnimation(getApplicationContext(), C0374R.anim.fade_out_verylong);
        this.reftext1.startAnimation(fadestart);
        this.reftext2.startAnimation(fadestart);
        this.reftext3.startAnimation(fadestart);
        this.reftext4.startAnimation(fadestart);
        this.reftext5.startAnimation(fadestart);
        */
        this.reftext1.setVisibility(View.INVISIBLE);
        this.reftext2.setVisibility(View.INVISIBLE);
        this.reftext3.setVisibility(View.INVISIBLE);
        this.reftext4.setVisibility(View.INVISIBLE);
        this.reftext5.setVisibility(View.INVISIBLE);
    }


    public void typeselection(View view) {
        if (((ToggleButton) view).isChecked()) {
            this.rulerSpace.mode = 2;
            this.rulerSpace.invalidate();
            return;
        }
        this.rulerSpace.mode = 1;
        this.rulerSpace.invalidate();
    }

    public void lengthselection(View view) {
        if (((ToggleButton) view).isChecked()) {
            this.rulerSpace.unit = "in";
            this.rulerSpace.changeunit_in();
            return;
        }
        this.rulerSpace.unit = "mm";
        this.rulerSpace.changeunit_mm();
    }

    public void blockselection(View view) {
        if (((ToggleButton) view).isChecked()) {
            this.rulerSpace.locking = true;
        } else {
            this.rulerSpace.locking = false;
        }
    }

    public void reset(View view) {
        this.rulerSpace.resetRuler();
        hidetext();
    }

    public void startModeSelection() {
        this.modeSelecting = true;
    }

    public void stopModeSelection() {
        this.modeSelecting = false;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.modeSelecting) {
                stopModeSelection();
                return true;
            }
            showDialog(QUIT_DIALOG);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void calibrations(View view) {
        startActivity(new Intent(this, RulerCalibration.class));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == CAL_SWITCH && this.rulerSpace != null) {
            this.rulerSpace.renewData();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.rulerSpace != null) {
            this.rulerSpace.renewData();
        }
    }

    public void onPause() {
        super.onPause();
        if (this.wakeLock != null) {
            this.wakeLock.release();
            this.wakeLock = null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.adTimer != null) {
            this.adTimer.cancel();
            this.adTimer = null;
        }
    }
}

