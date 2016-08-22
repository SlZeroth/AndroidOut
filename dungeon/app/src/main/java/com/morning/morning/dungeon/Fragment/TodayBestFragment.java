package com.morning.morning.dungeon.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.morning.morning.dungeon.Activity.ProductActivity;
import com.morning.morning.dungeon.Activity.ProductVerActivity;
import com.morning.morning.dungeon.Adapter.CouponAdapter;
import com.morning.morning.dungeon.Items.CouponItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.ServerInfo;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Networking;
import com.morning.morning.dungeon.Utility.Util;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchan on 2015. 10. 17..
 */
public class TodayBestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private CouponAdapter couponAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todaybest, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View v) {
        listView = (ListView) v.findViewById(R.id.listtodaybest);
        couponAdapter = new CouponAdapter(getActivity());

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swype_layout);
        refreshLayout.setOnRefreshListener(this);

        listView.setAdapter(couponAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponItem currentItem = (CouponItem) parent.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity(), ProductVerActivity.class);
                intent.putExtra("DisplayNo", currentItem.saleDisplayNo);
                startActivity(intent);
            }
        });

        getItem();
    }

    private void getItem() {

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);

        Util.getNetworkService().getBestSales(new Callback<Response>() {
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

    @Override
    public void onRefresh() {
        couponAdapter.resetItem();
        getItem();
        refreshLayout.setRefreshing(false);
    }
}
