package com.superprofan.mycalc;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;


public class TouchHandler extends SimpleOnGestureListener{
    private static final float FLING_ANGLE_MULTIPLIER = 2.0E-4f;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator(2.5f);
    private static final float SCROLL_ANGLE_MULTIPLIER = 0.002f;
    private static final int SETTLING_DURATION_MULTIPLIER = 1000;
    private AnimatorListener animatorListener = new C04812();
    private AnimatorUpdateListener flingAnimatorListener = new C04801();
    private GestureDetector gestureDetector;
    private HorizontalWheelView.Listener listener;
    private int scrollState = 0;
    private ValueAnimator settlingAnimator;
    private boolean snapToMarks;
    private HorizontalWheelView view;

    class C04801 implements AnimatorUpdateListener {
        C04801() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            TouchHandler.this.view.setRadiansAngle((double) ((Float) animation.getAnimatedValue()).floatValue());
        }
    }

    class C04812 extends AnimatorListenerAdapter {
        C04812() {
        }

        public void onAnimationEnd(Animator animation) {
            TouchHandler.this.updateScrollStateIfRequired(0);
        }
    }
    TouchHandler(HorizontalWheelView view) {
        this.view = view;
        this.gestureDetector = new GestureDetector(view.getContext(), this);
    }

    void setListener(HorizontalWheelView.Listener listener) {
        this.listener = listener;
    }

    void setSnapToMarks(boolean snapToMarks) {
        this.snapToMarks = snapToMarks;
    }

    boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        int action = event.getActionMasked();
        if (this.scrollState != 2 && (action == 1 || action == 3)) {
            if (this.snapToMarks) {
                playSettlingAnimation(findNearestMarkAngle(this.view.getRadiansAngle()));
            } else {
                updateScrollStateIfRequired(0);
            }
        }
        return true;
    }

    public boolean onDown(MotionEvent e) {
        cancelFling();
        return true;
    }

    void cancelFling() {
        if (this.scrollState == 2) {
            this.settlingAnimator.cancel();
        }
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        this.view.setRadiansAngle(this.view.getRadiansAngle() + ((double) (SCROLL_ANGLE_MULTIPLIER * distanceX)));
        updateScrollStateIfRequired(1);
        return true;
    }

    private void updateScrollStateIfRequired(int newState) {
        if (this.listener != null && this.scrollState != newState) {
            this.scrollState = newState;
            this.listener.onScrollStateChanged(newState);
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        double endAngle = this.view.getRadiansAngle() - ((double) (FLING_ANGLE_MULTIPLIER * velocityX));
        if (this.snapToMarks) {
            endAngle = (double) ((float) findNearestMarkAngle(endAngle));
        }
        playSettlingAnimation(endAngle);
        return true;
    }

    private double findNearestMarkAngle(double angle) {
        double step = 6.283185307179586d / ((double) this.view.getMarksCount());
        return ((double) Math.round(angle / step)) * step;
    }

    private void playSettlingAnimation(double endAngle) {
        updateScrollStateIfRequired(2);
        float[] fArr = new float[]{(float) this.view.getRadiansAngle(), (float) endAngle};
        this.settlingAnimator = ValueAnimator.ofFloat(fArr).setDuration((long) ((int) (Math.abs(this.view.getRadiansAngle() - endAngle) * 1000.0d)));
        this.settlingAnimator.setInterpolator(INTERPOLATOR);
        this.settlingAnimator.addUpdateListener(this.flingAnimatorListener);
        this.settlingAnimator.addListener(this.animatorListener);
        this.settlingAnimator.start();
    }



















}
