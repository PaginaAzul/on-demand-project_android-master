package com.rypropartner.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by mahesh on 27/1/17.
 * SanFranciscoDisplay-Regular font TextView
 */

public class RobotoMediumTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RobotoMediumTextView(Context context) {
        super(context);
        createFont();
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");
        setTypeface(font);
    }

}
