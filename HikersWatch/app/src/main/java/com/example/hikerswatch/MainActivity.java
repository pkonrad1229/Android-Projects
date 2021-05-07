package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitudeView;
    TextView longitudeView;
    TextView accuracyView;
    TextView altitudeView;
    TextView streetView;
    TextView postView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeView = findViewById(R.id.latitudeView);
        longitudeView = findViewById(R.id.longitudeView);
        accuracyView = findViewById(R.id.accuracyView);
        altitudeView = findViewById(R.id.altitudeView);
        streetView = findViewById(R.id.streetView);
        postView = findViewById(R.id.postView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitudeView.setText("Latitude: " + Double.toString(location.getLatitude()));
                longitudeView.setText("Longnitude: " + Double.toString(location.getLongitude()));
                accuracyView.setText("Accuracy: " + Double.toString(location.getAccuracy()));
                altitudeView.setText("Altitude: " + Double.toString(location.getAltitude()));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (listAddresses != null && listAddresses.size()>0) {
                        String address = "";
                        if (listAddresses.get(0).getThoroughfare() != null) {
                            address += listAddresses.get(0).getThoroughfare() + " ";
                        }

                        if (listAddresses.get(0).getFeatureName() != null) {
                            address += listAddresses.get(0).getFeatureName();
                        }
                        streetView.setText(address);
                        address = "";

                        if (listAddresses.get(0).getPostalCode() != null) {
                            address += listAddresses.get(0).getPostalCode() + " ";
                        }
                        if (listAddresses.get(0).getLocality() != null) {
                            address += listAddresses.get(0).getLocality();
                        }
                        postView.setText(address);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 50, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 50, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitudeView.setText("Latitude: " + Double.toString(lastKnownLocation.getLatitude()));
                longitudeView.setText("Longnitude: " + Double.toString(lastKnownLocation.getLongitude()));
                accuracyView.setText("Accuracy: " + Double.toString(lastKnownLocation.getAccuracy()));
                altitudeView.setText("Latitude: " + Double.toString(lastKnownLocation.getAltitude()));

            }
        }
    }
}