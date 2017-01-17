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

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final long INTERVAL = 1000 * 5; //5s

    public static final String ACTION_NEW_LOCATION = "NEW_LOCATION";

    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest = null;

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
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        mGoogleApiClient.disconnect();
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
        Log.d("OVERSEER","Location Updates Started");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        Log.d("OVERSEER","Location Updates Stopped");
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
        //TODO Logika po otrzymaniu lokalizacji
        Intent intent = new Intent();
        intent.setAction(ACTION_NEW_LOCATION);
        intent.putExtra("location", location);
        sendBroadcast(intent);
    }
}