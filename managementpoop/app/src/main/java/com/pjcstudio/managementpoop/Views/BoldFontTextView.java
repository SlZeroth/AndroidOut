package com.pjcstudio.managementpoop.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by juchanpark on 2015. 9. 17..
 */
public class BoldFontTextView extends TextView {

    private static Typeface mTypeface;

    public BoldFontTextView(Context context) {
        super(context);
    }

    public BoldFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "bold.ttf.mp3");
        }
        setTypeface(mTypeface);
    }

    public BoldFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "bold.ttf.mp3");
        }
        setTypeface(mTypeface);
    }

}
