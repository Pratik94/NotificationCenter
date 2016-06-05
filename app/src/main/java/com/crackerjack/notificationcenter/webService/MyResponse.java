package com.crackerjack.notificationcenter.webService;

import android.support.annotation.Nullable;

/**
 * Created by pratik on 05/06/16.
 */
public class MyResponse<T> {

    private boolean success = true;
    private String msg="";
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Nullable
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

