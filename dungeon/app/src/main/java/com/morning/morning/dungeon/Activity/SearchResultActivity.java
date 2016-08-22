package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.morning.morning.dungeon.Adapter.CouponAdapter;
import com.morning.morning.dungeon.Items.CouponItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SearchResultActivity extends AppCompatActivity {

    String searchStr;
    private ListView listView;
    private CouponAdapter couponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        init();
    }

    private void init() {

        listView = (ListView) findViewById(R.id.listview);
        couponAdapter = new CouponAdapter(this);
        listView.setAdapter(couponAdapter);

        Intent myIntent = getIntent();
        searchStr = myIntent.getStringExtra("query");

        getItem(searchStr);
    }

    private void getItem(String str) {

        Log.d("start", "1");
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);

        Util.getNetworkService().getProductInfosBySearchText(str, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {

                        Log.d("start", "2");
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            JSONObject thumbnailObject = new JSONObject(jsonObj.getString("thumbnailFileJSONObject"));

                            CouponItem item = new CouponItem(CouponItem.SINGLE_ITEM);
                            item.couponName = jsonObj.getString("subjectText");
                            item.couponSubName = jsonObj.getString("productName");
                            item.thumbnailLink = thumbnailObject.getString("fileUri");
                            couponAdapter.addItem(item);

                        }
                        couponAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

    }
}
