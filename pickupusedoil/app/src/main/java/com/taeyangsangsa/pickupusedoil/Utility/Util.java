package com.taeyangsangsa.pickupusedoil.Utility;

import com.squareup.okhttp.OkHttpClient;
import com.taeyangsangsa.pickupusedoil.Tools.NetworkService;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by juchan on 2015. 12. 5..
 */
public class Util {

    static public RestAdapter restAdapter = null;
    static public NetworkService networkService = null;

    static final String SERVER_ADDR = "http://taeyang3333.cafe24.com";

    static public NetworkService getNetworkService() {

        if(restAdapter == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(SERVER_ADDR)
                    .setClient(new OkClient(new OkHttpClient()))
                    .build();

            networkService = restAdapter.create(NetworkService.class);
        }
        return networkService;
    }
}
