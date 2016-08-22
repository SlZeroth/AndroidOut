package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.morning.morning.dungeon.Adapter.BasketAdapter;
import com.morning.morning.dungeon.Items.BasketItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;
import com.morning.morning.dungeon.View.NewCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/*
    장바구니 액티비티
 */

public class BasketActivity extends NewCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.list_basket)
    ListView mListView;
    BasketAdapter mAdapter;

    @InjectView(R.id.basket_buy)
    Button btn_buy;

    @InjectView(R.id.basket_delbtn)
    Button btn_delete;

    public ArrayList<String> cartList = new ArrayList<String>();

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.basket_checkbox);
            if(checkBox.isChecked()) {
                mAdapter.m_List.get(position).isCheck = false;
                checkBox.setChecked(false);
            } else {
                mAdapter.m_List.get(position).isCheck = true;
                checkBox.setChecked(true);
            }

            setPriceResult();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("장바구니");

        init();
    }

    private void init() {
        mAdapter = new BasketAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);

        getItems();
    }

    @OnClick(R.id.basket_buy)
    void onClickBuy() {
        //Util.getNetworkService().buyCart("11", );
    }

    @OnClick(R.id.basket_delbtn)
    void OnDelButton() {

        for(int i=0; i<mAdapter.m_List.size(); i++) {
            if(mAdapter.m_List.get(i).isCheck == true) {

                final int pos = i;
                final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);

                Util.getNetworkService().cancelCart("1001", mAdapter.m_List.get(i).cartNo, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                            if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                                Toast.makeText(BasketActivity.this, "장바구니 삭제성공!", Toast.LENGTH_SHORT).show();
                                mAdapter.m_List.remove(pos);

                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                    }
                });

            }
        }
    }

    private void getItems() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "로그인 중 입니다..", true);

        Util.getNetworkService().getCartList("1001", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));

                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            JSONObject thumbnailObject = new JSONObject(jsonObj.getJSONObject("productInfo").getString("thumbnailFileJSONObject"));

                            BasketItem item = new BasketItem();
                            item.productNum = jsonObj.getString("productNo");
                            item.cartNo = jsonObj.getString("cartNo");
                            Log.d("Name", jsonObj.getJSONObject("productInfo").getString("productName"));
                            item.buyNum = jsonObj.getString("qty");
                            item.textSelect = jsonObj.getJSONObject("productInfo").getString("productName");
                            item.imageLink = thumbnailObject.getString("fileUri");
                            String price = String.valueOf(Integer.parseInt(jsonObj.getJSONObject("saleReceipt").getString("price")) * Integer.parseInt(jsonObj.getString("qty")));
                            item.textPrice = price;
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
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    void setPriceResult() {
        int result = 0;
        for(int i=0;i<mAdapter.m_List.size();i++) {
            if(mAdapter.m_List.get(i).isCheck == true) {
                result += Integer.parseInt(mAdapter.m_List.get(i).textPrice);
            }
        }
        ((TextView) findViewById(R.id.basket_resultprice2)).setText("" + result + "원");
        ((TextView) findViewById(R.id.basket_resultprice)).setText("" + result + "원");
    }
}
