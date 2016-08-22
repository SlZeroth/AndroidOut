package com.pjcstudio.managementpoop.Utility;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by juchanpark on 2015. 7. 26..
 */
public interface NetworkService {

    @FormUrlEncoded
    @POST("/signUp")
    void SignUp(
            @Field("id") String id,
            @Field("passwd") String passwd,
            @Field("name") String name,
            @Field("birth") String birth,
            @Field("sex") String sex,
            @Field("lat") String lat,
            @Field("lng") String lng,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/login")
    void Login(
            @Field("id") String id,
            @Field("passwd") String passwd,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/signFacebook")
    void SignUpFacebook(
            @Field("id") String id,
            @Field("name") String name,
            @Field("birth") String birth,
            @Field("sex") String sex,
            @Field("lat") String lat,
            @Field("lng") String lng,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/registerFood")
    void RegisterFood(
        @Field("session") String session,
        @Field("type") String type,
        @Field("memo") String memo,
        @Field("year") String year,
        @Field("month") String month,
        @Field("day") String day,
        @Field("hour") String hour,
        @Field("minute") String minute,
        Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/registerPoo")
    void RegisterPoo(
            @Field("session") String session,
            @Field("type") String type,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/modifyFood")
    void ModifyFood(
            @Field("session") String session,
            @Field("id") String id,
            @Field("type") String type,
            @Field("memo") String memo,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/modifyPoo")
    void ModifyPoo(
            @Field("session") String session,
            @Field("id") String id,
            @Field("type") String type,
            @Field("year") String year,
            @Field("month") String month,
            @Field("day") String day,
            @Field("hour") String hour,
            @Field("minute") String minute,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/deletePoo")
    void DeletePoo(
            @Field("session") String session,
            @Field("id") String id,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/deleteFood")
    void DeleteFood(
            @Field("session") String session,
            @Field("id") String id,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/register")
    void registerpush(
            @Field("regId") String regId,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/changePasswd")
    void changePassword(
            @Field("session") String session,
            @Field("currP") String currP,
            @Field("changeP") String changeP,
            Callback<Response> callback
    );

    @GET("/getDataToMonth")
    void getDataToMonth(
            @Query("session") String session,
            @Query("year") String year,
            @Query("month") String month,
            Callback<Response> callback
    );

    @GET("/getDataToDay")
    void getDataToDay(
            @Query("session") String session,
            @Query("year") String year,
            @Query("month") String month,
            @Query("day") String day,
            Callback<Response> callback
    );

    @GET("/findToilet")
    void getToilet(
            @Query("session") String session,
            @Query("lat") String lat,
            @Query("lng") String lng,
            Callback<Response> callback
    );

    @GET("/getBoardData")
    void getBoardData(
            @Query("module") String module,
            Callback<Response> callback
    );

    @GET("/getBannerData")
    void getBoardBanner(
            Callback<Response> callback
    );

    @GET("/getLevel")
    void getLevel(
            @Query("session") String session,
            Callback<Response> callback
    );

    @GET("/recommend")
    void getRecommend(
            @Query("session") String session,
            Callback<Response> callback
    );

    @GET("/duplicateID")
    void findId(
            @Query("id") String id,
            Callback<Response> callback
    );
}
