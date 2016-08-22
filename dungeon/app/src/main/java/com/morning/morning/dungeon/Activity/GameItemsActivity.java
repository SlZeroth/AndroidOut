package com.morning.morning.dungeon.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.morning.morning.dungeon.Adapter.CouponAdapter;
import com.morning.morning.dungeon.Items.CouponItem;
import com.morning.morning.dungeon.Items.GameItem;
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

public class GameItemsActivity extends AppCompatActivity {

    ListView mListView;
    CouponAdapter mAdapter;
    String Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_items);

        Code = getIntent().getStringExtra("Code");

        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new CouponAdapter(this);
        mListView.setAdapter(mAdapter);
        getItem();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponItem currentItem = (CouponItem) parent.getAdapter().getItem(position);

                Intent intent = new Intent(GameItemsActivity.this, ProductVerActivity.class);
                intent.putExtra("DisplayNo", currentItem.saleDisplayNo);
                startActivity(intent);
            }
        });
    }

    void getItem() {

        Util.getNetworkService().getProductInfosByGameNo(Code, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject saleDisplayObject = jsonArray.getJSONObject(i).getJSONObject("saleDisplayt");
                            JSONObject productInfoObject = jsonArray.getJSONObject(i).getJSONObject("productInfo");
                            JSONObject saleReceiptObject = jsonArray.getJSONObject(i).getJSONObject("saleReceipt");
                            JSONObject thumbnailObject   = new JSONObject(productInfoObject.getString("thumbnailFileJSONObject"));

                            CouponItem item = new CouponItem(CouponItem.SINGLE_ITEM);
                            item.thumbnailLink = thumbnailObject.getString("fileUri");
                            item.couponName = productInfoObject.getString("productName");
                            item.couponSubName = productInfoObject.getString("subjectText");
                            item.couponPrice = saleReceiptObject.getString("price");
                            item.couponSalePrice = saleReceiptObject.getString("salePrice");
                            item.qty = saleReceiptObject.getString("qty");
                            item.saleDisplayNo = saleDisplayObject.getString("saleDisplayNo");
                            mAdapter.addItem(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
