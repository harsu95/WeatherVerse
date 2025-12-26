package com.weather.app.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class LocationService {

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    public interface LocationListener {
        void onLocationReceived(double latitude, double longitude);
        void onLocationError(String error);
    }

    @Inject
    public LocationService(@ApplicationContext Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }

    public void getCurrentLocation(LocationListener listener) {
        if (!hasLocationPermission()) {
            listener.onLocationError("Location permission not granted");
            return;
        }

        try {
            // First try to get last known location
            fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        listener.onLocationReceived(location.getLatitude(), location.getLongitude());
                    } else {
                        // Request fresh location
                        requestFreshLocation(listener);
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onLocationError(e.getMessage());
                });
        } catch (SecurityException e) {
            listener.onLocationError("Location permission denied");
        }
    }

    private void requestFreshLocation(LocationListener listener) {
        if (!hasLocationPermission()) {
            listener.onLocationError("Location permission not granted");
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    listener.onLocationReceived(location.getLatitude(), location.getLongitude());
                }
                stopLocationUpdates();
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        } catch (SecurityException e) {
            listener.onLocationError("Location permission denied");
        }
    }

    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
