package com.e.yatra;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback ,  GoogleMap.OnMapLongClickListener  {

    private GoogleMap mMap;
    public LocationManager locationManager ;
    public LocationListener locationListener;
    public void centerMapOnLocation(Location location, String title){
      if (location != null){
       LatLng latLng = new LatLng(location.getLongitude(),location.getLatitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,1));

    }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       //
    }
        @Override
        public void onMapLongClick(LatLng latLng) {
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            String address = "";
            try {

                List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                if (addresses.size()>0&& addresses != null){
                    if (addresses.get(0).getCountryName() != null){
                            address +=addresses.get(0).getCountryName()+"";
                    }    if (addresses.get(0).getLocality() != null){
                        address +=addresses.get(0).getLocality()+"";
                    }

                }
            }
catch (Exception e){
                e.printStackTrace();
}           mMap.addMarker(new MarkerOptions().position(latLng).title( address));
            Favourite.location.add(latLng);
            Favourite.places.add(address);
            Favourite.arrayAdapter.notifyDataSetChanged();

            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            centerMapOnLocation(last, "this is you last location ");
        }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        Toast.makeText(getBaseContext(),Integer.toString(intent.getIntExtra(" place number ",0)),Toast.LENGTH_SHORT).show();
        mMap.setOnMapLongClickListener(this);
        // Add a marker in Sydney and move the camera
        if(intent.getIntExtra(" place number ",0)== 0 ){
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(location," your location  ");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location locationLast = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(locationLast, "your location ");

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }

        }else {
                Location favLocation  =  new Location(LocationManager.GPS_PROVIDER);
                favLocation.setLatitude(Favourite.location.get(intent.getIntExtra(" place number ",0)).latitude);
                favLocation.setLongitude(Favourite.location.get(intent.getIntExtra(" place number ",0)).longitude);
                centerMapOnLocation(favLocation,Favourite.places.get( intent.getIntExtra(" place number " ,  0)));


        }




    }



}
