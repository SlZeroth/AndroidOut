package com.pjcstudio.managementpoop.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Fragment.LoginSelectBirthFragment;
import com.pjcstudio.managementpoop.Fragment.LoginSelectFragment;
import com.pjcstudio.managementpoop.Fragment.LoginSelectLocationFragment;
import com.pjcstudio.managementpoop.Fragment.LoginSelectSexFragment;
import com.pjcstudio.managementpoop.Fragment.LoginSignupFragment;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.pjcstudio.managementpoop.Views.NonSwipeableViewPager;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SignupActivity extends FragmentActivity {

    private NonSwipeableViewPager viewPager;
    private LoginSignupFragment fragment1;
    private LoginSelectSexFragment fragment2;
    private LoginSelectBirthFragment fragment3;
    private LoginSelectLocationFragment fragment4;
    private View[] radiodot;
    private signUpPager SignupPager;
    private FragmentManager fm;
    private int beforePos =0 ;
    private Bundle infoBundle;

    static final int MAX_PAGE = 4;
    static final int PAGE_SIGN = 0;
    static final int PAGE_SEX = 1;
    static final int PAGE_BIRTH = 2;
    static final int PAGE_LOCATION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        setFragment();
        setLayout();
        setListnerToRootView();
    }

    public void SignProcess() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.LOGINADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.SignUp(infoBundle.getString("id"), infoBundle.getString("pw"), infoBundle.getString("email"), infoBundle.getString("birth"), infoBundle.getString("sex"), "1111", "1111", new Callback<Response>() {
            @Override
            public void success(Response o, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("SIGN", jsonObject.toString());
                    if (jsonObject.getString("code").equals("1")) {
                        Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        String session = new JSONObject(jsonObject.getString("info")).getString("session");
                        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("logintype", "1"); // 일반 LOGIN
                        editor.putString("session", session);
                        editor.putString("autologin", "true");
                        editor.commit();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.e("LoginActivity", "error : " + error.getMessage());
            }
        });
    }

    public void setPage(int pageNum) {
        viewPager.setCurrentItem(pageNum);
    }

    private void setFragment() {
        fragment1 = new LoginSignupFragment();
        fragment2 = new LoginSelectSexFragment();
        fragment3 = new LoginSelectBirthFragment();
        fragment4 = new LoginSelectLocationFragment();
    }

    private void setLayout() {
        fm = getSupportFragmentManager();
        infoBundle = new Bundle();
        radiodot = new View[4];
        radiodot[0] = findViewById(R.id.sign_dot);
        radiodot[1] = findViewById(R.id.sign_dot2);
        radiodot[2] = findViewById(R.id.sign_dot3);
        radiodot[3] = findViewById(R.id.sign_dot4);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.signup_pager);
        SignupPager = new signUpPager(getSupportFragmentManager());
        viewPager.setAdapter(SignupPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radiodot[beforePos].setBackground(getResources().getDrawable(R.drawable.radio_on));
                radiodot[position].setBackground(getResources().getDrawable(R.drawable.radio_off));
                beforePos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setInfoBundle(String key, String date) {
        infoBundle.putString(key, date);
    }

    boolean isOpened = false;

    public void setListnerToRootView(){
        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100 ) { // 99% of the time the height diff will be due to a keyboard.

                    ((LinearLayout)findViewById(R.id.lv_signup_selector)).setVisibility(View.GONE);

                    if(isOpened == false){
                        //Do two things, make the view top visible and the editText smaller
                    }
                    isOpened = true;
                }else if(isOpened == true){
                    ((LinearLayout)findViewById(R.id.lv_signup_selector)).setVisibility(View.VISIBLE);
                    isOpened = false;
                }
            }
        });
    }
    public class signUpPager extends FragmentStatePagerAdapter {

        public signUpPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position)
            {
                case 0:
                    fragment = fragment1;
                    break;
                case 1:
                    fragment = fragment2;
                    break;
                case 2:
                    fragment = fragment3;
                    break;
                case 3:
                    fragment = fragment4;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}
