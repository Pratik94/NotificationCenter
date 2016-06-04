package com.crackerjack.notificationcenter.webService;

/**
 * Created by yash on 05/11/15.
 */
public interface ApiService {

//    @Headers({"Content-Type:application/json", "DeviceType: Android",  "Version: "+ AppConstant.AppVersion, "App: "+ AppConstant.AppName})

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

