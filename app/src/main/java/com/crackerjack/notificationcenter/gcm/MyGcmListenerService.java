package com.crackerjack.notificationcenter.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crackerjack.notificationcenter.R;
import com.crackerjack.notificationcenter.main.SplashActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pratik on 05/06/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        String notificationTitle = "Servify";
        String notificationText = "";
        int consumerServiceRequestId = 0;
        String notificationType = "";
        String params = "";

        try {

            JSONObject jsonObject = new JSONObject(message);

            if (jsonObject.has("Notification")) {

                notificationText = jsonObject.getString("Notification");
            }

            if (jsonObject.has("NotificationType")) {

                notificationType = jsonObject.getString("NotificationType");
            }

            if (jsonObject.has("Parameters")) {

                JSONObject parameters = jsonObject.getJSONObject("Parameters");

                if (parameters.has("ConsumerServiceRequestID")) {

                    consumerServiceRequestId = parameters.getInt("ConsumerServiceRequestID");
                }


            }

//            int statusCode = Utilities.instance.getRepairStatus(notificationType);

            int statusCode = 1;

            if (notificationType.equalsIgnoreCase("Marketing")) {

                sendNotification(getString(R.string.app_name), notificationText);

            } else if (notificationType.equalsIgnoreCase("Service cancel")) {

                sendNotification(getString(R.string.app_name), notificationText);

            } else if (notificationType.equalsIgnoreCase("Loyalty")) {

                sendNotification(getString(R.string.app_name), notificationText);

            } else if (statusCode == 12) {

                sendNotification(getString(R.string.app_name), notificationText);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isNotification", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}