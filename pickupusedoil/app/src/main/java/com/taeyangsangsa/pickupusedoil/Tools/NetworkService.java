package com.taeyangsangsa.pickupusedoil.Tools;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by juchanpark on 2015. 8. 25..
 */
public interface NetworkService {

    @FormUrlEncoded
    @POST("/servlet/json_sugo.php")
    void Json_Sugo(
            @Field("paidflag") String paidFlag,
            @Field("paidphone") String paidPhone,
            Callback<Response> callback
    );

    @GET("/servlet/json_notice.php")
    void Json_Notice(
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/servlet/json_checkpaid.php")
    void Json_CheckPaid(
            @Field("phonenumber") String phoneNum,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/servlet/update_sugo.php")
    void Json_UpdateSugo(
            @Field("seq") String seq,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/servlet/update_sugodata.php")
    void Update_SugoData(
            @Field("client") String client,
            @Field("client_hp") String client_hp,
            @Field("client_addr") String client_addr,
            @Field("client_req_date") String client_req_date,
            @Field("client_amount") String client_amount,
            @Field("client_memo") String client_memo,
            Callback<Response> callback
    );

    @GET("/servlet/json_sugomap.php")
    void Json_Sugomap(
            Callback<Response> callback
    );
}
