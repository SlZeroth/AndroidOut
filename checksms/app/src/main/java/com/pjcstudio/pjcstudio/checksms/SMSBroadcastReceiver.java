package com.pjcstudio.pjcstudio.checksms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Telephony.Mms.Part;
import android.sax.StartElementListener;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private Boolean downloadstate;
	Intent checkintent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			StringBuilder sms = new StringBuilder();    // SMS���ڸ� ������ ��
            Bundle bundle = intent.getExtras();         // Bundle��ü�� ���ڸ� �޾ƿ´�
            
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i < pdusObj.length; i++) {
                    messages[i] =
                        SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }

                for (SmsMessage smsMessage : messages) {
                    sms.append(smsMessage.getMessageBody());
                }
                
                String smsbody = sms.toString();
                String link = null;
                
                smsbody = smsbody.substring(smsbody.indexOf("http://"), smsbody.length());
        		if(smsbody.indexOf(" ") != -1)
        		{
        			link = smsbody.substring(0, smsbody.indexOf(" "));
        		}
        		else
        		{
        			link = smsbody;
        		}
                Log.i("sms", link);
                
                checkintent = new Intent(context, InspectActivity.class);
                checkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                checkintent.putExtra("includelink", link);
                checkintent.putExtra("downloadstate", "false");
                
                context.startActivity(checkintent);
                
            }
		}
		else if(intent.getAction().equals("DownloadManager.ACTION_DOWNLOAD_COMPLETE"))
		{
			checkintent.putExtra("downloadstate", "true");
		}
	
	}
}