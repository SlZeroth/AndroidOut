package com.morning.morning.dungeon.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.morning.morning.dungeon.Fragment.TodayBestFragment;
import com.morning.morning.dungeon.R;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 10. 17..
 */
public class PurchaseInfomationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_tab);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);

    }

    private class PurchaseAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> titleArray = new ArrayList<String>();

        public PurchaseAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        public void addTitle(String str) {
            titleArray.add(str);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArray.get(position);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment returnFragment = null;
            switch (position)
            {
                case 0:
                    returnFragment = new TodayBestFragment();
                    break;
                case 1:
                    returnFragment = new TodayBestFragment();
                    break;
                case 2:
                    returnFragment = new TodayBestFragment();
                    break;
            }
            return returnFragment;
        }
    }
}
