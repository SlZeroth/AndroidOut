package com.morning.morning.dungeon.Utility;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by juchan on 2015. 10. 17..
 */
public interface Networking {

    @FormUrlEncoded
    @POST("/member/sign/userLogIn")
    void login(
        @Field("userId") String id,
        @Field("password") String password,
        @Field("certifiedCode") String logintype,
        Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/member/user/createUser")
    void createUser(
            @Field("userId") String id,
            @Field("password") String password,
            @Field("userName") String username,
            @Field("nickName") String nickname,
            @Field("telNo") String tel,
            @Field("sex") String sex,
            @Field("birthday") String birth,
            @Field("certifiedCode") String type,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/buy/purchase/createPurchase")
    void buyItem(
            @Field("purchaseDttm") String purchaseDttm,
            @Field("purchaseManagerNo") String purchaseManagerNo,
            @Field("groupProductNo") String groupProductNo,
            @Field("productNo") String productNo,
            @Field("qty") String numItem,
            @Field("price") String price,
            @Field("salePrice") String salePrice,
            @Field("commissionPrice") String commissionPrice,
            Callback<Response> callback
    );

    @GET("/store/items/getStoreInfosByPopularity")
    void getBestSales(Callback<Response> callback);

    @GET("/store/search/getStoreInfosByDeadline")
    void getStoreDeadline(Callback<Response> callback);

    @GET("/store/items/getStoreInfosByFree")
    void getStoreInfosByFree(Callback<Response> callback);

    @FormUrlEncoded
    @POST("/sale/cart/createCart")
    void addCart(
            @Field("cartAccountNo") String cartUserNo,
            @Field("saleReceiptNo") String saleReceiptNo,
            @Field("qty") String qty,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/sale/sales/createCartSales")
    void buyCart(
            @Field("saleAccountNo") String saleAccountNo,
            @Field("cartNo") String cartNo,
            @Field("qty") String qty,
            @Field("price") String price,
            Callback<Response> callback
    );

    @GET("/sale/cart/getCartInfosByCartAccountNo/cartAccountNo={usernum}")
    void getCartList(
            @Path("usernum") String usernum,
            Callback<Response> callback
    );

    @GET("/product/game/getGameCategorys")
    void getGameCategorys(Callback<Response> callback);

    @GET("/product/game/getGameInfosByCategoryCode/categoryCode={category}")
    void getGameInfosByCategoryCode(
            @Path("category") String categoryCode,
            Callback<Response> callback
    );

    @GET("/product/product/getProductInfosBySearchText/searchText={itemname}")
    void getProductInfosBySearchText(
            @Path("itemname") String itemname,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/sale/cart/cancelCart")
    void cancelCart(
            @Field("cancelAccountNo") String cancelAccountNo,
            @Field("cartNo") String cartNo,
            Callback<Response> callback
    );

    @GET("/sale/saleReceipt/getSaleReceiptInfosByReceiptAccountNo/receiptAccountNo={usernum}")
    void getSaleReceiptInfosByReceiptAccountNo(
            @Path("usernum") String usernum,
            Callback<Response> callback
    );

    @GET("/product/game/getMyGameInfosByAccountNo/accountNo={usernum}")
    void getMyGameInfosByAccountNo(
            @Path("usernum") String userNum,
            Callback<Response> callback
    );

    @GET("/sale/saleReceipt/getSaleReceiptInfosBySaleDisplayNo/saleDisplayNo={itemnum}")
    void getSaleReceiptInfosBySaleDisplayNo(
            @Path("itemnum") String itemNum,
            Callback<Response> callback
    );

    @GET("/product/product/getProductInfosByGameNo/gameNo={gamenum}")
    void getProductInfosByGameNo(
            @Path("gamenum") String gameNum,
            Callback<Response> callback
    );

    @FormUrlEncoded
    @POST("/product/mygame/regMyGames")
    void regMyGames(
            @Field("createAccountNo") String userNum,
            @Field("platformCode") String platCode,
            @Field("inGames") String gameList,
            Callback<Response> callback
    );


}
