package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
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

public class OrderListActivity extends AppCompatActivity {

    public ListView mListView;
    public CouponAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        init();
    }

    private void init() {

        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new CouponAdapter(this);
        mListView.setAdapter(mAdapter);

        getItem();

    }

    private void getItem() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);

        Util.getNetworkService().getSaleReceiptInfosByReceiptAccountNo("1001", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));
                        String baseGroupNum = "single";
                        Boolean isGroupFirst = false;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject productObject = jsonArray.getJSONObject(i).getJSONObject("productInfo");
                            JSONObject thumbnailObject = new JSONObject(productObject.getString("thumbnailFileJSONObject"));

                            CouponItem item = new CouponItem(CouponItem.SINGLE_ITEM);
                            item.thumbnailLink = thumbnailObject.getString("fileUri");
                            item.couponName = productObject.getString("productName");
                            mAdapter.addItem(item);

                        }
                        mAdapter.notifyDataSetChanged();
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
