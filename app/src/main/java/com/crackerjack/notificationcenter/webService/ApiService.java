package com.crackerjack.notificationcenter.webService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by yash on 05/11/15.
 */
public interface ApiService {

//    @Headers({"Content-Type:application/json", "DeviceType: Android",  "Version: "+ AppConstant.AppVersion, "App: "+ AppConstant.AppName})

    @POST("Consumer/setDeviceToken")
    Call<MyResponse> updatePushNotifcationToken(@Header("Authorization") String token, @Body HashMap<String, Object> param);


  /*  @GET("/Consumer/appConfig")
    void getConfig(Callback<ServifyResponse<Config>> configCallback);

    *//*
        Display Schedule
     *//*
    @POST("/Consumer/getProductList")
    void getProducts(@Header("Authorization") String token, @Body Product product, Callback<ServifyResponse<ArrayList<Product>>> productCallback);


    @POST("/ExclusiveBrands/getCategory")
    void getProductSubCategory(@Header("Authorization") String token, @Body HashMap<String, Object> params, @Body Callback<ServifyResponse<ArrayList<ProductSubCategory>>> callback);
*/

}

