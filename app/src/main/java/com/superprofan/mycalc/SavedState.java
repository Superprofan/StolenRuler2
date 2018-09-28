package com.superprofan.mycalc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View.BaseSavedState;


public class SavedState extends BaseSavedState {
    public static final Creator<SavedState> CREATOR = new C04791();
    double angle;


    static class C04791 implements Creator<SavedState> {
        C04791() {
        }

        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    }

    SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in) {
        super(in);
        this.angle = ((Double) in.readValue(null)).doubleValue();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeValue(Double.valueOf(this.angle));
    }

    public String toString() {
        return "HorizontalWheelView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " angle=" + this.angle + "}";
    }
}
