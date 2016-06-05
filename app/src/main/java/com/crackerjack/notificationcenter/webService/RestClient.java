package com.crackerjack.notificationcenter.webService;

import com.crackerjack.notificationcenter.BuildConfig;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pratik on 05/6/16.
 */
public class RestClient {


    public static final RestClient instance = new RestClient();

    private ApiService apiService;

    public static String BASE_URL = "";

    public RestClient() {

        Gson gson = new Gson();

        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
        clientBuilder.connectTimeout(45,TimeUnit.SECONDS).readTimeout(45,TimeUnit.SECONDS).writeTimeout(45,TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            BASE_URL = "http://192.168.1.64:80001/api/v1/";

        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        }



        okhttp3.OkHttpClient client = clientBuilder.addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }


}