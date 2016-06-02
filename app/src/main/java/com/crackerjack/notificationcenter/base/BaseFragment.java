package com.crackerjack.notificationcenter.base;


import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import servify.associate.android.engineer.main.HomeFragment;
import servify.associate.android.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    public static final String RUPEES = "\u20B9 ";
    protected Context context;
    protected FragmentTransacListener fragmentTransacListener;
    protected FragmentAttachListener fragmentAttachListener;
    public Gson gson;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
    //    Utility.getInstance().dismissProgress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =onCreateKnifeView(inflater,container,savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    public abstract View onCreateKnifeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
     //   service = RestClient.instance.getApiService();
        gson = new Gson();

        setFragmentTransition();
    }


    public void setFragmentTransition() {
        //Fragment transitions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Inflate transitions to apply
            HomeFragment homeFragment = new HomeFragment();
            Transition enterTransform = TransitionInflater.from(context).
                    inflateTransition(android.R.transition.fade);

            Transition exitTransform = TransitionInflater.from(context).
                    inflateTransition(android.R.transition.fade);

            // Setup transition on  fragment
            homeFragment.setSharedElementReturnTransition(enterTransform);
            homeFragment.setReenterTransition(exitTransform);
            homeFragment.setExitTransition(exitTransform);
        }

    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        if (activity instanceof FragmentTransacListener ){
            fragmentTransacListener = (FragmentTransacListener) activity;
        }

        if (activity instanceof FragmentAttachListener){
            fragmentAttachListener = (FragmentAttachListener) activity;
            fragmentAttachListener.onAttached(this);

        }
    }

    protected void replaceFragment(BaseFragment baseFragment){
        if (fragmentTransacListener!=null){
            fragmentTransacListener.requestTransaction(baseFragment, true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public abstract String getTitle();



    public void animate(BaseFragment second){

    }

    public boolean onBackPressed(){
        return false;
    }

    public interface FragmentTransacListener{
        void requestTransaction(BaseFragment baseFragment, boolean shouldReplace);
    }

    public interface FragmentAttachListener{
        void onAttached(BaseFragment fragment);
    }

    public boolean isNetworkOnline() {
        boolean status = false;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public int getEngineerID(){
        return Hawk.get(Constants.ENGINEER_ID);
    }


    public String getType(){
        return Hawk.get(Constants.TYPE);
    }

    public Boolean checkGps() {
        final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();

            enableLoc();
            return false;
        }
        return true;

    }
    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
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

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
