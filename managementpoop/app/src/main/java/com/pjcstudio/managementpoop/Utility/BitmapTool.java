package com.pjcstudio.managementpoop.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by juchanpark on 2015. 8. 14..
 */
public class BitmapTool {

    static public Bitmap resizeBitmap(Context context, int resId, int size) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for(int i=0;i<size;i++) {
            width = width /2;
            height = height /2;
        }
        Log.d("BitmapTool", "width : " + width + " height : " + height);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
