package com.pagin.azul.utils;

/**
 * Created by mobulous06 on 27/9/17.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

public class InstantAutoComplete extends AutoCompleteTextView {
    private Context context;

    public InstantAutoComplete(Context context) {
        super(context);
        this.context=context;

    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.context=arg0;
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        this.context=arg0;
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

   /*@Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
        }
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         super.onTouchEvent(event);
        try {
            performFiltering("", 0);
            showDropDown();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return true;
    }
    /*
      @Override
    public boolean performClick() {
         super.performClick();
        performFiltering(getText(), 0);
          showDropDown();
          requestFocus();
        return true;
    }*/

}