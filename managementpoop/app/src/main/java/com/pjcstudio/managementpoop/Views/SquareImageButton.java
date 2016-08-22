package com.pjcstudio.managementpoop.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SquareImageButton extends ImageView {

	public SquareImageButton(Context context) {
        super(context);
    }

	public SquareImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        width = Math.min(width, height);
        height = width;

        setMeasuredDimension(width, height);
	}
}