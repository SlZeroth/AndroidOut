package com.pjcstudio.serialcam.BroadcastReceiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pjcstudio.serialcam.Activity.MainActivity;
import com.pjcstudio.serialcam.Service.ScreenService;

/**
 * Created by juchanpark on 2015. 9. 27..
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
