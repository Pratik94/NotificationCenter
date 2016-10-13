package com.crackerjack.notificationcenter.fcm;

import com.crackerjack.notificationcenter.utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

/**
 * Created by pratik on 08/10/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService ";

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.v("\n"+"\n"+"\n"+"<<<<< FCM Token >>>>>>>>"+"\n"+"\n"+"\n");
        Logger.d(TAG+ "Refreshed token: " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
        Hawk.put(Constants.DEVICE_TOKEN,refreshedToken);

    }

}
