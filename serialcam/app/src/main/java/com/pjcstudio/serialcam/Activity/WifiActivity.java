package com.pjcstudio.serialcam.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pjcstudio.serialcam.Adapter.WifiAdapter;
import com.pjcstudio.serialcam.R;

import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private ListView wifiListView;
    private WifiAdapter wifiAdapter;
    private WifiManager wifiManager;

    private ProgressDialog progressDialog;

    private int finishFlag = 0;

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if(finishFlag == 0) {
                    getWifiResult();
                    finishFlag = 1;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiListView = (ListView) findViewById(R.id.wifilist);
        wifiAdapter = (WifiAdapter) new WifiAdapter(this);

        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String item = (String) adapterView.getAdapter().getItem(pos);
            }
        });

        progressDialog = ProgressDialog.show(WifiActivity.this, "", "잠시만 기다려주세요.", true);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, intentFilter);

        wifiManager.startScan();
    }

    private void getWifiResult() {
        List<ScanResult> result = wifiManager.getScanResults();
        if(result != null) {
            for(int i=0;i<result.size();i++) {
                wifiAdapter.addItem(result.get(i).SSID);
                Log.d("SSID", result.get(i).SSID + "   :  " + result.get(i).capabilities);
            }
            wifiListView.setAdapter(wifiAdapter);
            progressDialog.dismiss();
        }
    }

    private void connect(String ssid, String pass, String type) {

        String networkSSID = "test";
        String networkPass = "pass";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"".concat(ssid).concat("\"");
        conf.status = WifiConfiguration.Status.DISABLED;
        conf.priority = 40;

        if(type.contains("WEP")) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.clear();
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        } else if(type.contains("[WPA2]")) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        }
    }
}
