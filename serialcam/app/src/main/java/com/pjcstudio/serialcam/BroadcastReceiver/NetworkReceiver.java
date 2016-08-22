package com.pjcstudio.serialcam.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by juchan on 16. 3. 26..
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {

            Log.d("wifi", "aaa");
            Intent mirrorIntent = new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG");
            mirrorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mirrorIntent);

        }
    }
}
