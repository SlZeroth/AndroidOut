package com.pjcstudio.managementpoop.Views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by juchanpark on 2015. 7. 10..
 * 스크롤 차단되는 뷰페이저
 */
public class StoneViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public StoneViewPager(Context context) {
        super(context);
    }

    public StoneViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}