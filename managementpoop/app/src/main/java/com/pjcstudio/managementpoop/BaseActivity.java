package com.pjcstudio.managementpoop;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by juchanpark on 2015. 8. 27..
 */
public class BaseActivity extends FragmentActivity {

    private static Typeface mTypeface;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if (BaseActivity.mTypeface == null)
            BaseActivity.mTypeface = Typeface.createFromAsset(getAssets(), "font.ttf.mp3");

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
    }

    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView) child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup) child);
        }
    }
}