package com.crackerjack.notificationcenter.base;

/**
 * Created by Ashish on 01/03/16.
 */
public class Servify {

    private static Servify sharedInstance;

    public static Servify getSharedInstance() {

        if (sharedInstance == null){
            sharedInstance = new Servify();


        }
        return sharedInstance;
    }

}
