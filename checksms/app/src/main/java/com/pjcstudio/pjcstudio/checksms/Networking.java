package com.pjcstudio.pjcstudio.checksms;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by juchan on 2015. 10. 17..
 */
public interface Networking {

    @FormUrlEncoded
    @POST("/reghash")
    void regHash(
            @Field("hash") String hash,
            @Field("deviceid") String deviceid,
            Callback<Response> callback
    );
}
