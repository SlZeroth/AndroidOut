package com.pjcstudio.managementpoop.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.pjcstudio.managementpoop.Fragment.TutorialFragment;
import com.pjcstudio.managementpoop.Fragment.nullfragment;
import com.pjcstudio.managementpoop.R;

public class TutorialActivity extends FragmentActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.tutpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 7) {
                    SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("tutorial", "true");
                    editor.commit();
                    finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        static final int ITEM_NUM = 8;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int imageId = 0;
            switch (position)
            {
                case 0:
                    imageId = R.drawable.tuto1;
                    break;
                case 1:
                    imageId = R.drawable.tuto2;
                    break;
                case 2:
                    imageId = R.drawable.tuto3;
                    break;
                case 3:
                    imageId = R.drawable.tuto4;
                    break;
                case 4:
                    imageId = R.drawable.tuto5;
                    break;
                case 5:
                    imageId = R.drawable.tuto6;
                    break;
                case 6:
                    imageId = R.drawable.tuto7;
                    break;
                case 7:
                    return new nullfragment();
                default:
            }
            Bundle bundle = new Bundle();
            bundle.putString("imageid", String.valueOf(imageId));
            TutorialFragment tutorialFragment = new TutorialFragment();
            tutorialFragment.setArguments(bundle);
            return tutorialFragment;
        }

        @Override
        public int getCount() {
            return ITEM_NUM;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
