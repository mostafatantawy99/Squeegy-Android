package com.squeegy.android.ui.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squeegy.android.R;
import com.squeegy.android.base.BaseFragment;
import com.squeegy.android.controller.CoordinateServiceController;
import com.squeegy.android.listener.OnNearByCoordsResponseListner;
import com.squeegy.android.model.NearByCoords;
import com.squeegy.android.util.AppLog;

import java.util.List;


public class HomeFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        PlaceSelectionListener,OnNearByCoordsResponseListner {

    private static final String TAG = "HomeFragment";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private SupportMapFragment mapFragment;
    private SupportPlaceAutocompleteFragment mAutocompleteFragment;

    private List<NearByCoords> mNearByCoordses;

    private double mCurrentLatitude;
    private double mCurrentLongitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buildGoogleApiClient();

        loadNearByCoordsData();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                try {
                    mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
                }
                googleMap.setOnMapLoadedCallback(HomeFragment.this);
            }
        });

        // Retrieve the PlaceAutocompleteFragment.
        mAutocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        mAutocompleteFragment.setOnPlaceSelectedListener(this);

        return view;
    }


    @Override
    public void onConnected(Bundle bundle) {

        // Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }
        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                //   Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onConnectionSuspended(int i) {
        AppLog.v(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        AppLog.v(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMapLoadedCallback(HomeFragment.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapLoaded() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }

        if(mLastLocation!=null){
            mCurrentLatitude = mLastLocation.getLatitude();
            mCurrentLongitude = mLastLocation.getLongitude();

            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)
                    .bearing(90)
                    .tilt(40)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return true;
                }
            });
        }

    }

    @Override
    public void onPlaceSelected(Place place) {
        AppLog.v(TAG, "Place Selected: " + place.getName());
        LatLng latLng = place.getLatLng();
        Toast.makeText(getActivity(), place.getName(), Toast.LENGTH_SHORT).show();
        if (latLng != null) {
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to location user
                    .zoom(15)
                    .bearing(90)
                    .tilt(40)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(latLng).title("" + place.getName()));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return true;
                }
            });

            updateNearByCoords();
        }

    }

    private void updateNearByCoords() {

        if(mNearByCoordses!=null){

            for (int i = 0; i < mNearByCoordses.size(); i++) {
                NearByCoords nearByCoords=mNearByCoordses.get(i);
                double distance = distFrom(nearByCoords.getLatitude(),nearByCoords.getLongitude(),mCurrentLatitude,mCurrentLongitude);
               if(distance<=10) {
                   mMap.addMarker(new MarkerOptions().position(new LatLng(nearByCoords.getLatitude(), nearByCoords.getLongitude())).title(""));

                   mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                       @Override
                       public boolean onMarkerClick(Marker marker) {
                           return true;
                       }
                   });
               }
            }
        }
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        AppLog.e(TAG, "onError: Status = " + status.toString());
        Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAutocompleteFragment.onActivityResult(requestCode, resultCode, data);
    }

    private void loadNearByCoordsData() {

        CoordinateServiceController.getNearbyCoOrdinates(getActivity(), this);
    }

    @Override
    public void onSuccessResponse(List<NearByCoords> nearByCoordses) {

        mNearByCoordses = nearByCoordses;

        updateNearByCoords();
    }

    @Override
    public void onError() {

    }

    public static double distFrom(double lat1, double lng1, double lat2,double lng2) {

        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)+ Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)* Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }

}
