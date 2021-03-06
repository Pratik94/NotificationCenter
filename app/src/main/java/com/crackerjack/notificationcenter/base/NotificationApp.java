package com.crackerjack.notificationcenter.base;

import android.app.Application;
import android.content.Context;

import com.crackerjack.notificationcenter.BuildConfig;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
/**
 * Created by pratik on 02/06/16.
 */
public class NotificationApp extends Application {


    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        NotificationApp.context = getApplicationContext();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/GothamRoundedBook.ttf")
//                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Hawk.init(this)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(com.orhanobut.hawk.LogLevel.NONE)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .build();

        if (BuildConfig.DEBUG) {
            Logger.init("SERVIFY XPERT").logLevel(LogLevel.FULL);

        } else {
            Logger.init("SERVIFY XPERT")
                    .logLevel(LogLevel.NONE);
        }


    }


    public static Context getAppContext() {
        return NotificationApp.context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
