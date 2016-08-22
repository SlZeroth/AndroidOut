package com.pjcstudio.serialcam.Service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.pjcstudio.serialcam.BroadcastReceiver.ScreenOffReceiver;
import com.pjcstudio.serialcam.BroadcastReceiver.ScreenOnReceiver;
import com.pjcstudio.serialcam.BroadcastReceiver.ScreenReceiver;

/**
 * Created by juchanpark on 2015. 9. 2..
 */
public class ScreenService extends Service {

    private ScreenOnReceiver mScreenOnReceiver = null;
    private ScreenOffReceiver mScreenOffReceiver = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mScreenOnReceiver = new ScreenOnReceiver();
        IntentFilter filterOn = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenOnReceiver, filterOn);

        mScreenOffReceiver = new ScreenOffReceiver();
        IntentFilter filterOff = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOffReceiver, filterOff);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(intent != null) {
            if(intent.getAction() == null) {
                if(mScreenOnReceiver==null){
                    mScreenOnReceiver = new ScreenOnReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                    registerReceiver(mScreenOnReceiver, filter);
                }
            }
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d("onDestory", "ScreenService");
        super.onDestroy();

        if(mScreenOnReceiver != null){
            unregisterReceiver(mScreenOnReceiver);
        }
        if(mScreenOffReceiver != null){
            unregisterReceiver(mScreenOffReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
