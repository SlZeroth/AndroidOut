package com.taeyangsangsa.pickupusedoil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.taeyangsangsa.pickupusedoil.Adapter.DrawerAdapter;
import com.taeyangsangsa.pickupusedoil.Fragment.BoardFragment;
import com.taeyangsangsa.pickupusedoil.Fragment.CustomerFragment;
import com.taeyangsangsa.pickupusedoil.Fragment.NoticeFragment;
import com.taeyangsangsa.pickupusedoil.Items.BoardItem;
import com.taeyangsangsa.pickupusedoil.Tools.NetworkService;
import com.taeyangsangsa.pickupusedoil.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class MainActivity extends ActionBarActivity {

    public String isUser; // 유료회원이면 Y 아니면 N
    static public String phoneNum = null; // 나의 핸드폰번호

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    DrawerAdapter drawerAdapter;
    ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
        CheckPaid();
        setListView();
        setActionbarTitle(0);
    }

    private void setLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawerlist);
        drawerAdapter = new DrawerAdapter(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneNum = telManager.getLine1Number();
        //phoneNum = "01021289446";
    }

    public void setActionbarTitle(int id) {
        if(id == 0) {
            getSupportActionBar().setTitle("태양상사(폐유수거)");
        } else if(id == 0) {
            getSupportActionBar().setTitle("공지사항");
        } else if(id == 1) {
            getSupportActionBar().setTitle("고객센터");
        }

    }

    public void CheckPaid() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);

           // phoneNum = "01021289446";
        Util.getNetworkService().Json_CheckPaid(phoneNum, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                progressDialog.dismiss();
                try {
                    Log.d("dd", new String(((TypedByteArray) response.getBody()).getBytes()));
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("Paid", jsonArray.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.getString("return").equals("Y")) {
                        isUser = "Y";
                    } else if (jsonObject.getString("return").equals("N")) {
                        isUser = "N";
                    } else if (jsonObject.getString("return").equals("C")) {
                        isUser = "C";
                    }

                    Log.d("return", isUser);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new BoardFragment()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "인증정보 로딩에 실패했습니다.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void movePage(int id) {
        if(id == 0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, new BoardFragment()).commit();
        } else if(id == 1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, new NoticeFragment()).commit();
        } else if(id == 2) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, new CustomerFragment()).commit();
        }
    }

    public void setBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListView() {
        drawerAdapter.addItem("페유수거 리스트");
        drawerAdapter.addItem("공지사항");
        drawerAdapter.addItem("고객센터");
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position)
                {
                    case 0:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new BoardFragment()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new NoticeFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new CustomerFragment()).commit();
                        break;
                }
                dlDrawer.closeDrawer(drawerList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        } else if(item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Click HOME", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.write_sugobtn) {
            if(isUser.equals("C")) {
                Intent intent = new Intent(MainActivity.this, WriteSugoActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "수거등록 고객이 아닙니다", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }
}
