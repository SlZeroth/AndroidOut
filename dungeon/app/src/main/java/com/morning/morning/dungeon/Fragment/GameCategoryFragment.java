package com.morning.morning.dungeon.Fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.morning.morning.dungeon.Activity.CategoryActivity;
import com.morning.morning.dungeon.Activity.CategoryGamesActivity;
import com.morning.morning.dungeon.Adapter.CategoryAdapter;
import com.morning.morning.dungeon.Items.CategoryItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchan on 16. 2. 13..
 */
public class GameCategoryFragment extends Fragment {

    public ListView mListView;
    public CategoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gamecategory, container, false);

        mListView = (ListView) rootView.findViewById(R.id.list_category);
        mAdapter = new CategoryAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(0);

        for (ApplicationInfo applicationInfo : list) {
            String name = String.valueOf(applicationInfo.loadLabel(pm));    // 앱 이름
            String pName = applicationInfo.packageName;   // 앱 패키지
            Log.d("log", pName);
            //Drawable iconDrawable = applicationInfo.loadIcon(pm);   // 앱 아이콘
            //applicationInfo.
            //Log.d("name", name);
            //Log.d("pname", pName);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem categoryItem = (CategoryItem) mAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), CategoryGamesActivity.class);
                intent.putExtra("Code", categoryItem.Code);
                startActivity(intent);
            }
        });

        getCategory();

        return rootView;
    }

    void getCategory() {
        Util.getNetworkService().getGameCategorys(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));

                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));

                        for(int i=0; i<jsonArray.length(); i++) {
                            CategoryItem item = new CategoryItem();
                            item.Code = jsonArray.getJSONObject(i).getString("categoryCode");
                            item.Name = jsonArray.getJSONObject(i).getString("categoryName");
                            Log.d("ddd", item.Code);
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
