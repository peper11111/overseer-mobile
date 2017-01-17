package pl.edu.pw.ee.overseer.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.services.LocationService;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private Activity mContext;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private LocationUpdateReceiver mLocationUpdateReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        mContext = getActivity();
        mLocationUpdateReceiver = new LocationUpdateReceiver();

        mMapView = (MapView) v.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationService.ACTION_NEW_LOCATION);
        mContext.registerReceiver(mLocationUpdateReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mContext.unregisterReceiver(mLocationUpdateReceiver);
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
    }

    private class LocationUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMapPosition((Location) intent.getParcelableExtra("location"));
        }
    }

    private void updateMapPosition(Location location) {
        if (mGoogleMap != null)
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        ToastUtility.makeText(mContext, "New Location obtained");
    }
}