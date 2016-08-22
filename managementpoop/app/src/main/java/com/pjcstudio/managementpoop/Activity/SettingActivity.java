package com.pjcstudio.managementpoop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.pjcstudio.managementpoop.R;

public class SettingActivity extends FragmentActivity {

    private ToggleButton tgAutoLogin, tgPush, tgSaveid;
    private Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setLayout();
    }

    private void setLayout() {
        tgAutoLogin = (ToggleButton) findViewById(R.id.switchautologin);
        tgPush = (ToggleButton) findViewById(R.id.switchpush);
        btnChangePass = (Button) findViewById(R.id.setting_chagnepass);
        tgSaveid = (ToggleButton) findViewById(R.id.switchsaveid);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String strAutoLogin = pref.getString("autologin", "");
        String strPush = pref.getString("push", "");
        if(strAutoLogin.length() != 0) {
            tgAutoLogin.setChecked(true);
        }
        if(strPush.length() != 0) {
            tgPush.setChecked(true);
        }

        tgSaveid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(tgAutoLogin.isChecked()) {
                    editor.putString("saveid", "true");

                } else {
                    editor.putString("saveid", "");
                }
                editor.commit();
            }
        });

        tgAutoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(tgAutoLogin.isChecked()) {
                    editor.putString("autologin", "true");

                } else {
                    editor.putString("autologin", "");
                }
                editor.commit();
            }
        });
        tgPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(tgPush.isChecked()) {
                    editor.putString("push", "true");
                } else {
                    editor.putString("push", "");
                }
                editor.commit();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePassActivity.class);
                startActivity(intent);
            }
        });
    }
}
