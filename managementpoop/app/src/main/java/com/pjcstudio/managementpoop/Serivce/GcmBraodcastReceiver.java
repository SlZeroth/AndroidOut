package com.pjcstudio.managementpoop.Serivce;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.pjcstudio.managementpoop.Activity.LoginActivity;
import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 8. 30..
 */
public class GcmBraodcastReceiver  extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("gcmbroadcast", "|" + "=================" + "|");
        Bundle bundle = intent.getExtras();
        String title="d";
        String msg="d";
        for (String key : bundle.keySet())
        {
            Object value = bundle.get(key);
            Log.i("gcmbroadcast", "|" + String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()) + "|");

            if(key.equals("title")) title = value.toString();
            if(key.equals("msg")) msg = value.toString();
        }
        sendNotification(context, title,msg);
        Log.i("gcmbroadcast", "|" + "================="+"|");


        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GcmService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
    }

    private void sendNotification(Context context,String title, String msg) {
        Intent resultIntent = new Intent(context, LoginActivity.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ico_dung2);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(msg);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(9001, mNotifyBuilder.build());
    }
}