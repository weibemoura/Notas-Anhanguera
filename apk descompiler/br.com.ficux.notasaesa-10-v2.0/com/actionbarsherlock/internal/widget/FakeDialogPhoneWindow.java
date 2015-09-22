package com.actionbarsherlock.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import com.actionbarsherlock.C0029R;

public class FakeDialogPhoneWindow extends LinearLayout {
    final TypedValue mMinWidthMajor;
    final TypedValue mMinWidthMinor;

    public FakeDialogPhoneWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMinWidthMajor = new TypedValue();
        this.mMinWidthMinor = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(attrs, C0029R.styleable.SherlockTheme);
        a.getValue(34, this.mMinWidthMajor);
        a.getValue(35, this.mMinWidthMinor);
        a.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        boolean isPortrait = metrics.widthPixels < metrics.heightPixels;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        boolean measure = false;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, 1073741824);
        TypedValue tv = isPortrait ? this.mMinWidthMinor : this.mMinWidthMajor;
        if (tv.type != 0) {
            int min;
            if (tv.type == 5) {
                min = (int) tv.getDimension(metrics);
            } else if (tv.type == 6) {
                min = (int) tv.getFraction((float) metrics.widthPixels, (float) metrics.widthPixels);
            } else {
                min = 0;
            }
            if (width < min) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(min, 1073741824);
                measure = true;
            }
        }
        if (measure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
