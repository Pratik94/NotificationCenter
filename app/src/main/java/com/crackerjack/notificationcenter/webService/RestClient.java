package com.crackerjack.notificationcenter.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import servify.consumer.android.BuildConfig;

/**
 * Created by yash on 05/11/15.
 */

public class RestClient {

    public static final RestClient instance = new RestClient();

    private ApiService apiService;

    //Staging
//    public static final String API_BASE_URL = "http://staging.servify.in:5000/api/v1";

    //Demo
//    public static final String API_BASE_URL = "http://staging.servify.in:5002/api/v1";

    public static final String API_BASE_URL = "http://172.21.3.185:5000/api/v1";


    //Production
//    public static final String API_BASE_URL = "http://production.servify.in:5000/api/v1";
//    public static final String API_BASE_URL = "http://production.servify.in:8080/api/v3";
//    public static final String API_BASE_URL = "https://node.servify.in/consumer/api/v3";

    public RestClient() {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(API_BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(okHttpClient))
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {

//            request.addHeader("Content-Type", "application/json");
            request.addHeader("DeviceType",  "Android");
            request.addHeader("Version", ""+BuildConfig.VERSION_CODE);
            request.addHeader("App", "Servify");
        }
    };


    public ApiService getApiService() {
        return apiService;
    }
}
