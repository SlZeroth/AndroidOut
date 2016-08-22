package com.pjcstudio.managementpoop.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.SimpleMonthAdapter;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pjcstudio.managementpoop.BaseActivity;
import com.pjcstudio.managementpoop.Fragment.CalendarDayFragment;
import com.pjcstudio.managementpoop.Fragment.CalendarFragment;
import com.pjcstudio.managementpoop.Fragment.CalendarMonthFragment;
import com.pjcstudio.managementpoop.Fragment.MyPageFragment;
import com.pjcstudio.managementpoop.Fragment.NearMapFragment;
import com.pjcstudio.managementpoop.Fragment.TipsFragment;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.BitmapTool;

import java.util.Calendar;


public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;
    private RelativeLayout relActionbar;
    private FrameLayout fraActionbar;
    public String session;
    public Typeface typeface;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    static final int TAB_CALENDAR = 0;
    static final int TAB_PLACE = 1;
    static final int TAB_TIPS = 2;
    static final int TAB_MYINFO = 3;
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String tut = pref.getString("tutorial", "");
        if(tut.length() != 0) {
        } else {
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
        }

        setSession();
        setLayout();
        initTab();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int keyCodes = event.getKeyCode();
        if (keyCodes == KeyEvent.KEYCODE_MENU){
            return true;
        }

        return super.onKeyDown(keyCodes, event);
    }

    // 세션값 가져오기
    public void setSession() {
        session = getIntent().getStringExtra("session");
    }

    public void setLayout() {
        typeface = Typeface.createFromAsset(getAssets(), "font.ttf.mp3");
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
    }

    // 메인화면의 탭 4개를 등록
    public void initTab() {
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getTabView(TAB_CALENDAR)),
                CalendarFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(getTabView(TAB_PLACE)),
                NearMapFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(getTabView(TAB_TIPS)),
                TipsFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(getTabView(TAB_MYINFO)),
                MyPageFragment.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
            }
        });

    }

    // 탭 이미지 로딩
    private View getTabView(int Tabname) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.imagetab, null);
        ImageView imagetab = (ImageView) view.findViewById(R.id.tabimage);
        switch (Tabname)
        {
            case TAB_CALENDAR:
                imagetab.setBackgroundResource(R.drawable.calendar_tab);
                break;
            case TAB_PLACE:
                imagetab.setBackgroundResource(R.drawable.place_tab);
                break;
            case TAB_TIPS:
                imagetab.setBackgroundResource(R.drawable.tips_tab);
                break;
            case TAB_MYINFO:
                imagetab.setBackgroundResource(R.drawable.myinfo_tab);
                break;
        }
        return view;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"'뒤로'버튼을한번더누르시면종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }


}