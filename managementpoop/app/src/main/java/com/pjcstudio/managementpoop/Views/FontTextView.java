package com.pjcstudio.managementpoop.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by juchanpark on 2015. 8. 30..
 */
public class FontTextView extends TextView {

    private static Typeface mTypeface;

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "font.ttf.mp3");
        }
        setTypeface(mTypeface);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "font.ttf.mp3");
        }
        setTypeface(mTypeface);
    }
}
