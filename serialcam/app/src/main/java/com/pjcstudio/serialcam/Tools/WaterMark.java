package com.pjcstudio.serialcam.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Calendar;

/**
 * Created by juchanpark on 2015. 9. 6..
 */
public class WaterMark {

    static public Bitmap setWatermark(Context context, Bitmap bitmap, String companyName, String DeviceName, String fileLen) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        SharedPreferences pref = context.getSharedPreferences("SAVE", Context.MODE_PRIVATE);

        String strDate = String.format("%04d.%02d.%02d %02d:%02d", year, month, day, hour, minute);
        strDate = companyName + " / " + DeviceName + " / " + strDate + " / " + fileLen;

        if(!pref.getString("YEAR", "").equals("") || !pref.getString("MONTH", "").equals("") || !pref.getString("DAY", "").equals("")) {
            year = Integer.parseInt(pref.getString("YEAR", ""));
            month = Integer.parseInt(pref.getString("MONTH", ""));
            day = Integer.parseInt(pref.getString("DAY", ""));
            strDate = String.format("%04d.%02d.%02d %02d.%02d", year, month, day, hour, minute);
            strDate = companyName + " / " + DeviceName + " / " + strDate + " / " + fileLen;
        }
        Bitmap bitcopy = bitmap;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(70);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setTextAlign(Paint.Align.LEFT);
        Canvas canvas = new Canvas(bitcopy);
        canvas.drawText(strDate, 15, 15+paint.getTextSize(), paint);
        return bitcopy;
    }
}
