package com.pagin.azul.onphasesecond.utilty;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationClass extends LocationCallback implements LocationListener {
    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location = null;
    double latitude=0;
    double longitude=0;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute
    protected LocationManager locationManager;
    private Location m_Location;
    Activity mActivity;
    private UpdateLocationListener updateLocationListener;
    public static final int LongLocationInterval=1000;
    public static final int FastestLocationInterval=1000;
    private FusedLocationProviderClient fusedLocationProviderClient;


    public interface UpdateLocationListener{
        void onLocation(LocationResult location);
    }



    public LocationClass(Context context) {
        this.mContext = context;
        this.mActivity = (Activity)context;
        m_Location = getLocation();
//        System.out.println("location Latitude:" + m_Location.getLatitude());
//        System.out.println("location Longitude:" + m_Location.getLongitude());
//        System.out.println("getLocation():" + getLocation());
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //          }

            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);

                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    final LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    locationRequest.setInterval(LongLocationInterval);
                    locationRequest.setFastestInterval(FastestLocationInterval);
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, LocationClass.this, Looper.myLooper());

                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                            else {
                                getLocation();
                                return null;
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
//                            locationManager.requestLocationUpdates(
//                                    LocationManager.GPS_PROVIDER,
//                                    MIN_TIME_BW_UPDATES,
//                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                } else {
                                    getLocation();
                                    return null;
                                }
                            }
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return location;
    }


    public UpdateLocationListener getUpdateLocationListener() {
        return updateLocationListener;
    }

    public void setUpdateLocationListener() {
        this.updateLocationListener = updateLocationListener;
    }

    public void stopUsingGPS() {
//        if (locationManager != null) {
//            locationManager.removeUpdates(GPSTracker.this);
//        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            Log.w("testData","location changed!");
            latitude=location.getLatitude();
            longitude=location.getLongitude();
        }

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        if(updateLocationListener!=null) {
            updateLocationListener.onLocation(locationResult);
            Log.w("testData","location changed!");
        }
        Log.w("testData","locatin send");


    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

}
