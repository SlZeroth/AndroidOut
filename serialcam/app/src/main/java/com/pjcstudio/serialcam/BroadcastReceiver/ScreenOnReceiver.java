package com.pjcstudio.serialcam.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pjcstudio.serialcam.Activity.MainActivity;

/**
 * Created by juchanpark on 2015. 9. 28..
 */
public class ScreenOnReceiver extends BroadcastReceiver {

    static final String RECEIVER = "SCREEN";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(RECEIVER, "ON");
            /*
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            */
            if(MainActivity.cameraFragment.isCameraOn == false) {
                MainActivity.cameraFragment.CameraOn();
            }
        }
    }
}
