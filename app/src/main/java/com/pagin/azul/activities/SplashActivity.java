package com.pagin.azul.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.pagin.azul.Constant.Constants.kAppLaunchMode;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener{

    private final int SPLASH_DISPLAY_TIMER = 2000;
    private String device_token ="";
    private static final int PERMISSION_REQUEST_CODE = 12;
    List<String> listPermissionsNeeded;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    GoogleApiClient googleApiClient;
    Location mylocation;
    private static final int REQUEST_CHECK_SETTINGS_GPS = 27;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_splash);
        getDeviceToken();

        setUpGClient();
//        if(checkPermissions()){
//
//            onLocation();
//
//        }

    }

    private void onLocation() {
        Handler handler = new Handler();
        int TIMEDALEYSLASHSCREEN = 3000;
        handler.postDelayed(() -> {
            if(!SharedPreferenceWriter.getInstance(this).getString(kAppLaunchMode).equalsIgnoreCase("true")) {
                Intent mainIntent = new Intent(SplashActivity.this, TakeTourActivity.class);
                startActivity(mainIntent);
                finish();
            }else {
                startActivity(HomeMainActivity.getIntent(this, ""));
                finish();
            }
        }, TIMEDALEYSLASHSCREEN);

    }

    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.P)
    private boolean checkPermissions1() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(SplashActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            switch (requestCode) {
//                case PERMISSION_REQUEST_CODE:
//                    try {
//                        if(!SharedPreferenceWriter.getInstance(this).getString(kAppLaunchMode).equalsIgnoreCase("true")) {
//                            startActivity(new Intent(SplashActivity.this, TakeTourActivity.class));
//                            finish();
//                        }else {
//                            startActivity(HomeMainActivity.getIntent(this, ""));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        } else {
//            Toast.makeText(this, "Please accept permissions due to security purpose", Toast.LENGTH_SHORT).show();
//            //checkPermissions();
//        }
//    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                Log.e("denied", permission);
                showSettingLocation(this);


            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);
                    getMyLocation();
                } else {
                    //set to never ask again
                    Log.e("set to never ask again", permission);
                    //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    Toast.makeText(SplashActivity.this, "Grant the permission to use this app.", Toast.LENGTH_SHORT).show();
                    // User selected the Never Ask Again Option
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 0);


                }
            }
        }
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }



    private void getDeviceToken1() {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                Log.e(">>>>>>>>>>>>>>", "thred IS  running");
                SharedPreferenceWriter mPreference = SharedPreferenceWriter.getInstance(getApplicationContext());
                try {
                    String token1 = FirebaseInstanceId.getInstance().getToken();
                    if (mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN).isEmpty()) {
                        String token = FirebaseInstanceId.getInstance().getToken();
//                        String token = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),
//                                android.provider.Settings.Secure.ANDROID_ID);
                        //String token="";
                        Log.e("Generated Device Token", "-->" + token);
                        if (token == null) {
                            getDeviceToken();
                        } else {
                            mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, token);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();

    }




    //    METHOD: TO GET DEVICE_TOKEN FROM FCM
    /*private void getDeviceToken() {

        final String TAG = SplashActivity.class.getSimpleName();
        Runnable runnableObj = new Runnable() {
            @Override
            public void run() {
                SharedPreferenceWriter mPreference = SharedPreferenceWriter.getInstance(getApplicationContext());
                Log.w(TAG, "Previous Device Token : " + mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN));

                try {
                    if (mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN).isEmpty()) {
                        String device_token = FirebaseInstanceId.getInstance().getToken();
                        Log.e(TAG, "Generated Device Token : " + device_token);
                        if (device_token == null) {
                            getDeviceToken();
                        } else {
                            mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, device_token);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnableObj);
        thread.start();

    }*/
    private void getDeviceToken() {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                Log.e(">>>>>>>>>>>>>>", "thred IS  running");
                try {
                    if (SharedPreferenceWriter.getInstance(SplashActivity.this).getString(SharedPreferenceKey.DEVICE_TOKEN).isEmpty()) {

                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                            if(!task.isSuccessful()){
                                return;
                            }
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            Log.e("Generated Device Token", "-->" + token);
                            if (token == null) {
                                getDeviceToken();
                            } else {
                                SharedPreferenceWriter.getInstance(SplashActivity.this).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN,token);
                                //mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, token);
                            }
                        });

                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
    }





    //fourth
    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(SplashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    //Toast.makeText(SplashActivity.this, "permission allow", Toast.LENGTH_SHORT).show();
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(SplashActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);

                                        getTime();
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        //  Toast.makeText(SplashActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                                        status.startResolutionForResult(SplashActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    //finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    //fifth
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        break;
                }
                break;
        }
    }

    private void getTime() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    if(!SharedPreferenceWriter.getInstance(SplashActivity.this).getString(kAppLaunchMode).equalsIgnoreCase("true")) {
                        startActivity(new Intent(SplashActivity.this, TakeTourActivity.class));
                        finish();
                    }else {

                        //startActivity(HomeMainActivity.getIntent(SplashActivity.this, ""));
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_DISPLAY_TIMER);
    }





    public void showSettingLocation(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Jokar requires access to location. Turn on your GPS and give Jokar access to your location.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
               /* Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                dialog.dismiss();*/


                checkPermissions();
                dialog.dismiss();

            }
        });


        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        if (!(activity).isFinishing()) {
            alert.show();
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void setLanguage() {
        // check language from data base
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                LocaleHelper.setLocale(this,"en");
            }else {
                LocaleHelper.setLocale(this, "pt");
            }
        }else {
            LocaleHelper.setLocale(this,"pt");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");

            //SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kEnglish);
        }
    }
}
