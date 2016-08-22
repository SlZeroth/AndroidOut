package com.pjcstudio.managementpoop.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by juchanpark on 2015. 8. 5..
 */
public class NetworkImage {

    static public Drawable urltoDrawable(String url, Context context) {
        Drawable drawable = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent(), null, options);
            drawable = new BitmapDrawable(context.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
