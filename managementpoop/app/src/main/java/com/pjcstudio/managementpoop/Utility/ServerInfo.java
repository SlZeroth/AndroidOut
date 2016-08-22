package com.pjcstudio.managementpoop.Utility;

import retrofit.RestAdapter;

/**
 * Created by juchanpark on 2015. 7. 26..
 */
public class ServerInfo {
    public static final String LOGINADDR = "http://175.126.166.158:50001";
    public static final String APIADDR = "http://175.126.166.158:50002";
    public static final String APIADDR2 = "http://175.126.166.158:50003";
    public static final String APIBOARD = "http://175.126.166.158/xe/";
    public static final String WEBURL = "http://175.126.166.158/xe/index.php?";

    public static NetworkService getService(String url){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        NetworkService service = restAdapter.create(NetworkService.class);

        return service;
    }
}
