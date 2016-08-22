package com.morning.morning.dungeon.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.morning.morning.dungeon.R;

/*
    동적으로 탭 갯수와 이름을 받아서 등록하는 액티비티
 */
public class DynamicTabActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    static final String BTN_BUYLIST = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_tab);
        initTab(getIntent().getStringExtra("mode"));
    }

    private void initTab(String mode) {
        tabLayout = (TabLayout) findViewById(R.id.dynamictab);
        viewPager = (ViewPager) findViewById(R.id.dynamicpager);

        if(mode.equals(BTN_BUYLIST)) {
            //tabLayout.
        }
    }
}
