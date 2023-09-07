package com.example.get_current_location_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    SupportMapFragment smf;

    LatLng latLngM;

    double latitudeM;

    double longitudeM;

   // double latitudeN;

   // double longitudeN;
    LatLng latLngN;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getmylocation1();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }


    public void getmylocation1() {
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

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if (location != null) {
                            latLngM = new LatLng(location.getLatitude(), location.getLongitude());
                            latitudeM = location.getLatitude();
                            longitudeM = location.getLongitude();
                            MarkerOptions markerOptions = new MarkerOptions().position(latLngM).title("Your Initial Point!!");
                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngM, 20));
                            refresh();
                        } else {
                            Toast.makeText(MainActivity.this, "Please on your phone location app permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void getmylocation2() {
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

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if (location != null) {
                            latLngN = new LatLng(location.getLatitude(), location.getLongitude());
                            double latitudeN = location.getLatitude();
                            double longitudeN = location.getLongitude();

                            double earthRadius = 6371000; //meters
                            double dLat = Math.toRadians(latitudeM-latitudeN);
                            double dLng = Math.toRadians(longitudeN-longitudeM);
                            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                                    Math.cos(Math.toRadians(latitudeM)) * Math.cos(Math.toRadians(latitudeN)) *
                                            Math.sin(dLng/2) * Math.sin(dLng/2);
                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                            float dist = (float) (earthRadius * c);

                                Toast.makeText(MainActivity.this, "Distance Traveled : "+dist, Toast.LENGTH_SHORT).show();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngN, 20));
                        } else {
                            Toast.makeText(MainActivity.this, "Please on your phone location app permission", Toast.LENGTH_SHORT).show();
                        }
                        refresh();
                    }
                });
            }
        });
    }
    private void refresh() {
         Handler handler = new Handler();
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getmylocation2();
            }
        };
        handler.postDelayed(runnable , 3000);
    }

}