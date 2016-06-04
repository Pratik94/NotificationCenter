package com.crackerjack.notificationcenter.base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import servify.associate.android.R;
import servify.associate.android.engineer.model.ApiExecutionModel;
import servify.associate.android.utils.Constants;
import servify.associate.android.utils.ServifyDialogClick;
import servify.associate.android.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ashish on 01/03/16.
 */
public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String RUPEES = "\u20B9 ";
    final static int REQUEST_LOCATION = 199;
    protected Context context;
    protected Gson gson;
    private GoogleApiClient googleApiClient;
    protected ApiExecutionModel apiExecutionModel;
    protected CoordinatorLayout Clayout;
    protected RelativeLayout baseLayout;
    protected Toolbar baseToolbar;

    //  public Servify servify = Servify.getSharedInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        gson = new Gson();
        apiExecutionModel = new ApiExecutionModel(context);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        baseLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.base_layout, null);
        setContentView(baseLayout);
        baseToolbar = (Toolbar) baseLayout.findViewById(R.id.toolbar);
        setSupportActionBar(baseToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Clayout = (CoordinatorLayout) findViewById(R.id.snackbarLocation);

        ViewStub stub = (ViewStub) baseLayout.findViewById(R.id.container);
        stub.setLayoutResource(layoutResID);
        stub.inflate();

        ButterKnife.bind(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //  showNetworkDialog();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //   Utility.getInstance().dismissProgress();
    }

    public boolean isNetworkOnline() {
        boolean status = false;

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }

        try {
            NetworkInfo netInfo = connManager.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = connManager.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public void showNetworkDialog(){
        if (!isNetworkOnline()) {
            Utility.getInstance().showServifyDialog(context, android.R.drawable.ic_menu_report_image, false,new ServifyDialogClick() {

                @Override
                public void btnClickYes() {
                    Utility.getInstance().dismissServifyDialog();
                    showNetworkDialog();
                }

                @Override
                public void btnClickNo() {

                }

            }, "OH NO!", "We couldn't detect a network connection. :(", "Ok");
        }
    }

    public Boolean checkGps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();

            enableLoc();
            return false;
        }
        return true;

    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Logger.v("Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) context, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }


    public void shortSnack(String message) {

        Snackbar snackbar = Snackbar.make(baseLayout, message, Snackbar.LENGTH_SHORT);
        View snackView = snackbar.getView();
        snackbar.show();

        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public String getIMEI() {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getDeviceId();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Logger.json(gson.toJson(bundle));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}