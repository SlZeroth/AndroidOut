package com.pjcstudio.managementpoop.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class SquareHeightButton extends Button {

	public SquareHeightButton(Context context) {
        super(context);
	}

	public SquareHeightButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareHeightButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		height = Math.min(width, height);
		width = height;

		setMeasuredDimension(width, height);
	}
}