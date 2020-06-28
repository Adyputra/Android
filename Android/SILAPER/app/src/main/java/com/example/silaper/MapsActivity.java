package com.example.silaper;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    final private int REQUEST_COURSE_ACCESS = 123;
    boolean permissionGranted = false;
    private GoogleMap mMap;
    LocationManager lm;
    LocationListener locationListener;
    GoogleMap mg_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onPause(){
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=  PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{  Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COURSE_ACCESS);
            return;
        }else{
            permissionGranted = true;
        }
        if(permissionGranted) {
            lm.removeUpdates(locationListener);
        }
    }

    //public void onResume(){
    //super.onResume();
    //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    //---request for location updates--
    //lm.requestLocationUpdates(
    //      LocationManager.NETWORK_PROVIDER,
    //    0,
    //  0,
    //locationListener);
    //}


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mg_map = googleMap;
        mg_map.setMyLocationEnabled(true);
        mg_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mg_map.getUiSettings().setZoomControlsEnabled(true);
        mg_map.getUiSettings().setZoomGesturesEnabled(true);
        mg_map.getUiSettings().setCompassEnabled(true);


        mMap = googleMap;
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_COURSE_ACCESS);
            return;
        }else{
            permissionGranted = true;
        }
        if(permissionGranted) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  locationListener);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(Intent.ACTION_VIEW, Uri. parse("http://www.amazon.com")),  0);
        lm.addProximityAlert(-8.171772, 113.685428, 5, -1, pendingIntent);
    }

    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COURSE_ACCESS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                } else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,  grantResults);
        }
    }

    private class MyLocationListener implements LocationListener{
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                Toast.makeText(getBaseContext(),
                        "Location changed : Lat: " + loc.getLatitude() + "Lng: " + loc.getLongitude(),
                        Toast.LENGTH_SHORT).show();
                LatLng p = new LatLng(
                        (int) (loc.getLatitude()),
                        (int) (loc.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(7));
            }
        }
        public void onProviderDisabled(String provider){
            Toast.makeText(getBaseContext(),
                    provider + "disabled",
                    Toast.LENGTH_SHORT).show();
        }
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(),
                    provider + " enabled",
                    Toast.LENGTH_SHORT).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String statusString = "";
            switch (status) {
                case android.location.LocationProvider.AVAILABLE:
                    statusString = "available";
                case android.location.LocationProvider.OUT_OF_SERVICE:
                    statusString = "out of service";
                case android.location.LocationProvider.TEMPORARILY_UNAVAILABLE:
                    statusString = "temporarily unavailable";
            }
            Toast.makeText(getBaseContext(),
                    provider + " " + statusString,
                    Toast.LENGTH_SHORT).show();
        }
    }

}