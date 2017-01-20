package pl.edu.pw.ee.overseer.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import pl.edu.pw.ee.overseer.tasks.LocationTask;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final long INTERVAL = 1000 * 30; //30s

    public static final String ACTION_NEW_LOCATION = "NEW_LOCATION";

    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest = null;

    private SharedPreferencesUtility mSharedPreferencesUtility = null;

    private long lastUpdate;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        createLocationRequest();
        mGoogleApiClient.connect();
        mSharedPreferencesUtility = new SharedPreferencesUtility(this);
        lastUpdate = mSharedPreferencesUtility.getLong(SharedPreferencesUtility.KEY_UPDATE, 0);
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        mGoogleApiClient.disconnect();
        mSharedPreferencesUtility.putLong(SharedPreferencesUtility.KEY_UPDATE, lastUpdate).apply();
        super.onDestroy();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        Log.d("OVERSEER", "Location Updates Started");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        Log.d("OVERSEER", "Location Updates Stopped");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("OVERSEER", "Google Api Client connection failed. ErrorCode = " + connectionResult.getErrorCode() + " ErrorMessage = " + connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        long now = new Date().getTime();
        if (now - lastUpdate > 900000) { //15min
            new LocationTask().execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""), "" + location.getLatitude(), "" + location.getLongitude(), "" + now);
            lastUpdate = now;
        }

        Intent intent = new Intent();
        intent.setAction(ACTION_NEW_LOCATION);
        intent.putExtra("location", location);
        sendBroadcast(intent);
    }
}