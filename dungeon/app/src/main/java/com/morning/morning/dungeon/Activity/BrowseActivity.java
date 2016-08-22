package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.Utility.Util;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
    검색 또는 상세구분 할때 사용하는 액티비티
 */
public class BrowseActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    public Toolbar mToolbar;

    @InjectView(R.id.tablayout)
    public TabLayout mTabLayout;

    @InjectView(R.id.viewpager)
    public ViewPager mViewPager;

    public TabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        // Toolbar 설정
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_36dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // TAB 설정
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        getProduct();
    }

    public void initializationTab() {

    }

    public void getProduct() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);
        Util.getNetworkService();

    }

    class TabAdapter extends FragmentStatePagerAdapter {

        public ArrayList<Fragment> m_FragmentL;
        public ArrayList<String> m_TitleL;

        public TabAdapter(FragmentManager fm) {
            super(fm);


        }

        public void addItem(Fragment fragment, String title) {
            m_FragmentL.add(fragment);
            m_TitleL.add(title);
        }

        @Override
        public int getCount() {
            return m_FragmentL.size();
        }

        @Override
        public Fragment getItem(int position) {
            return m_FragmentL.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return m_TitleL.get(position);
        }
    }
}
