package com.pjcstudio.serialcam;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 9. 6..
 */

@ReportsCrashes(
        formUri = "",
        resToastText = R.string.app_name,
        mode = ReportingInteractionMode.DIALOG,
        resDialogIcon = android.R.drawable.ic_dialog_info,
        resDialogTitle = R.string.report_title,
        resDialogText = R.string.report_text,
        resDialogOkToast = R.string.report_complete,
        mailTo = "qkrwncks593@gmail.com"
)

public class ApplicationClass extends Application {

    public String deviceName;
    public String companyName;
    public String ipComputer;
    public String ipAndroid;
    public String password;
    public String photoCounter;
    public File[] arrayExternal;

    public static final int IP_COMPUTER = 0;
    public static final int IP_ANDROID = 1;


    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        SharedPreferences sharedPreferences = getSharedPreferences("SAVE", MODE_PRIVATE);
        if(sharedPreferences != null) {
            companyName = sharedPreferences.getString("COMPNAYNAME", "");
            deviceName = sharedPreferences.getString("DEVICENAME", "");
            ipComputer = sharedPreferences.getString("IPCOMPUTER", "");
            ipAndroid = sharedPreferences.getString("IPANDROID", "");
            password = sharedPreferences.getString("PASSWORD", "");
            photoCounter = sharedPreferences.getString("PHOTOCOUNTER", "");
            Log.d("AND IP", ipAndroid);
        }

        arrayExternal = ContextCompat.getExternalFilesDirs(getApplicationContext(), "");
        for(int idx=0;idx<arrayExternal.length;idx++) {
            //Log.d("External", arrayExternal[idx].getPath());
        }


    }

    public String findSdcardPath() {
        String path = null;
        if(new File("/mnt/external_sd/").isDirectory()) {
            path = "/mnt/external_sd/";
        }
        else if(new File("/mnt/extSdCard/").isDirectory()) {
            path = "/mnt/extSdCard/";
        }
        return path;
    }

    public int checkPhotoData() {
        if(deviceName.equals("") || companyName.equals("")) {
            return 1;
        } else {
            return 0;
        }
    }

    public int checkIpAddress(int type) {
        switch (type)
        {
            case IP_COMPUTER:
                if(ipComputer.equals("")) {
                    return 1;
                }
                break;
            case IP_ANDROID:
                if(ipAndroid.equals("")) {
                    return 1;
                }
                break;
        }
        return 0;
    }
}
