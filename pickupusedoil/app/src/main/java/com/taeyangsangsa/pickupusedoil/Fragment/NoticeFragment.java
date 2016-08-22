package com.taeyangsangsa.pickupusedoil.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.taeyangsangsa.pickupusedoil.Adapter.NoticeAdapter;
import com.taeyangsangsa.pickupusedoil.Items.NoticeChildItem;
import com.taeyangsangsa.pickupusedoil.Items.NoticeGroupItem;
import com.taeyangsangsa.pickupusedoil.MainActivity;
import com.taeyangsangsa.pickupusedoil.R;
import com.taeyangsangsa.pickupusedoil.Tools.NetworkService;
import com.taeyangsangsa.pickupusedoil.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 8. 25..
 */
public class NoticeFragment extends Fragment {

    private ExpandableListView mListView;
    private NoticeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notice, container, false);
        ((MainActivity)getActivity()).setActionbarTitle(1);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        mListView = (ExpandableListView) v.findViewById(R.id.explistivew);
        getNotice();
    }

    private void getNotice() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
        Util.getNetworkService().Json_Notice(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                progressDialog.dismiss();
                ArrayList<NoticeGroupItem> m_GroupList = new ArrayList<NoticeGroupItem>();
                ArrayList<ArrayList<NoticeChildItem>> m_ChildList = new ArrayList<ArrayList<NoticeChildItem>>();
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("text", jsonArray.toString());
                    Log.d("text", "dfdsfsd");
                    for (int idx = 0; idx < jsonArray.length(); idx++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(idx);
                        String year = jsonObject.getString("year");
                        String month = jsonObject.getString("month");
                        String day = jsonObject.getString("day");
                        String title = jsonObject.getString("title");
                        String comment = jsonObject.getString("comment");

                        NoticeGroupItem groupItem = new NoticeGroupItem();
                        groupItem.title = title;
                        groupItem.date = year + "." + month + "." + day;
                        m_GroupList.add(groupItem);

                        NoticeChildItem childSub = new NoticeChildItem();
                        childSub.content = comment;
                        ArrayList<NoticeChildItem> childItem = new ArrayList<NoticeChildItem>();
                        childItem.add(childSub);
                        m_ChildList.add(childItem);

                    }
                    Log.d("str", m_ChildList.toString());
                    mAdapter = new NoticeAdapter(getActivity(), m_GroupList, m_ChildList);
                    mListView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Log.d("fail", "fail");
            }
        });
    }
}
