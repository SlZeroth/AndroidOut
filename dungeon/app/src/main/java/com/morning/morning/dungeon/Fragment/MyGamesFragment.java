package com.morning.morning.dungeon.Fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.morning.morning.dungeon.Activity.GameItemsActivity;
import com.morning.morning.dungeon.Adapter.GameAdapter;
import com.morning.morning.dungeon.Items.GameItem;
import com.morning.morning.dungeon.Items.PackageItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchan on 16. 2. 27..
 */
public class MyGamesFragment extends Fragment {

    ListView mListView;
    GameAdapter mAdapter;
    String gameListStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mygames, container, false);

        mListView   = (ListView) rootView.findViewById(R.id.listview);
        mAdapter    = new GameAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameItem item = (GameItem) mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), GameItemsActivity.class);
                intent.putExtra("Code", item.gameIdx);
                startActivity(intent);
            }
        });

        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(0);
        List<PackageItem.InGame> gameLists = new ArrayList<>();

        for (ApplicationInfo applicationInfo : list) {
            String name = String.valueOf(applicationInfo.loadLabel(pm));    // 앱 이름
            String pName = applicationInfo.packageName;   // 앱 패키지
            PackageItem.InGame item = new PackageItem.InGame();
            item.setAppName(name);
            item.setPakeageName(pName);
            gameLists.add(item);
            Log.d("pname", pName);
            Log.d("appname", name);
        }

        PackageItem item = new PackageItem();
        item.setInGames(gameLists);

        Gson gson = new Gson();
        gameListStr = gson.toJson(item.getInGames());
        Log.d("gamelist", gameListStr);

        setGames();
        getItem();

        return rootView;
    }

    private void getItem() {

        Util.getNetworkService().getMyGameInfosByAccountNo("1001", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));

                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));

                        for(int i=0; i<jsonArray.length(); i++) {

                            JSONObject thumbnailObject = new JSONObject(jsonArray.getJSONObject(i).getString("thumbnailFileJSONObject"));

                            GameItem item = new GameItem();
                            item.gameName = jsonArray.getJSONObject(i).getString("gameName");
                            item.gameIdx  = jsonArray.getJSONObject(i).getString("gameNo");
                            item.gameLink = thumbnailObject.getString("fileUri");
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

    private void setGames() {

        Util.getNetworkService().regMyGames("1001", "android", gameListStr, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    Log.d("get", jsonObject.toString());
                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                        Log.d("good", "good");
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
