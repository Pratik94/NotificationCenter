package com.crackerjack.notificationcenter.base;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pratik on 05/06/16.
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    private Context context;

    public RetrofitCallback(Context context) {
        this.context = context;
    }

    public RetrofitCallback() {
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        Logger.json(new Gson().toJson(response.body()));

        onSuccess(response.body());

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

//        Utility.getInstance().dismissProgressBar();
        if (t instanceof IOException) {
            Logger.v("Failure error " + t.getLocalizedMessage());
        }

    }

    protected abstract void onSuccess(T response);


}