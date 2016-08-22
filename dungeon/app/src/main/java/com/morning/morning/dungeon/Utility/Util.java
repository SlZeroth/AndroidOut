package com.morning.morning.dungeon.Utility;

import android.util.Log;

import com.morning.morning.dungeon.String.ServerInfo;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by juchan on 2015. 12. 5..
 */
public class Util {

    static public RestAdapter restAdapter = null;
    static public Networking networkService = null;

    static public Networking getNetworkService() {

        if(restAdapter == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ServerInfo.SERVER_ADDR)
                    .setClient(new OkClient(new OkHttpClient()))
                    .build();

            networkService = restAdapter.create(Networking.class);
        }
        return networkService;
    }
}
