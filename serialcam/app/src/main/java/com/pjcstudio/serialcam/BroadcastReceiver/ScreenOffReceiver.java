package com.pjcstudio.serialcam.BroadcastReceiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pjcstudio.serialcam.Activity.MainActivity;

/**
 * Created by juchanpark on 2015. 9. 28..
 */
public class ScreenOffReceiver extends BroadcastReceiver {

    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;

    static final String RECEIVER = "SCREEN";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(RECEIVER, "OFF");
            if(km == null) {
                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            }
            if (keyLock == null) {
                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
            }
            disableKeyguard();
            if(MainActivity.cameraFragment.isCameraOn == true) {
                MainActivity.cameraFragment.CameraOff();
            }
        }
    }

    public void disableKeyguard() {
        keyLock.disableKeyguard();
    }
}
