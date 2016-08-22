package com.morning.morning.dungeon.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.morning.morning.dungeon.Fragment.GameCategoryFragment;
import com.morning.morning.dungeon.Fragment.MyGamesFragment;
import com.morning.morning.dungeon.Fragment.StoreDeadlineFragment;
import com.morning.morning.dungeon.Fragment.StoreFreeFragment;
import com.morning.morning.dungeon.Fragment.TodayBestFragment;
import com.morning.morning.dungeon.Items.CouponItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.tablayout)
    TabLayout tabLayout;

    @InjectView(R.id.mainpager)
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @InjectView(R.id.category_btn)
    ImageView btnCategory;
    @InjectView(R.id.mypage_btn)
    ImageView btnMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addItem(new MyGamesFragment(), "게임리스트");
        adapter.addItem(new StoreDeadlineFragment(), "마감임박");
        adapter.addItem(new StoreFreeFragment(), "무료아이템");
        adapter.addItem(new TodayBestFragment(), "오늘의 세일");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.category_btn)
    void onCategoryClick() {
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @OnClick(R.id.mypage_btn)
    void onMyPageClick() {
        Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("게임 이름을 입력하세요.");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setIconifiedByDefault(true);
        return true;
    }

    public void searchItem(String itemname) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("query", itemname);
        startActivity(intent);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> m_FragmentL;
        private ArrayList<String> m_TitleL;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            m_FragmentL = new ArrayList<Fragment>();
            m_TitleL = new ArrayList<String>();
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
        public CharSequence getPageTitle(int position) {
            return m_TitleL.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return m_FragmentL.get(position);
        }
    }
}
