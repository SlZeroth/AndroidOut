package com.pjcstudio.pjcstudio.checksms;

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
                    .setEndpoint("54.238.242.12:3000")
                    .setClient(new OkClient(new OkHttpClient()))
                    .build();

            networkService = restAdapter.create(Networking.class);
        }
        return networkService;
    }
}
