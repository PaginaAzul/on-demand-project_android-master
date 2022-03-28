package com.pagin.azul.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RobotoSemiBoldTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RobotoSemiBoldTextView(Context context) {
        super(context);
        createFont();
    }

    public RobotoSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public RobotoSemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/seguisb_semibold.ttf");
        setTypeface(font);
    }
}
