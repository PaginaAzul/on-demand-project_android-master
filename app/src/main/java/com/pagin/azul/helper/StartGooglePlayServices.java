package com.pagin.azul.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pagin.azul.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


/**
 * Created by ravi on 15,June,2019
 * ravi@outlook.com
 */

public class StartGooglePlayServices implements GoogleApiClient.OnConnectionFailedListener {

    private static Context context;

    public StartGooglePlayServices(Context context) {

        this.context = context;
    }

    //  METHOD: TO CHECK IF DEVICE HAS UPDATED GOOGLE PLAY SERVICES
    public static boolean isGPlayServicesOK(Activity activity) {
        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) {
            /* The returned review_dialog displays a localized message about the error and upon
             user confirmation (by tapping on review_dialog) will direct them to the Play Store
              if Google Play services is out of date or missing, */
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, isAvailable, 1001);
            dialog.show();
        } else {
            Toast.makeText(activity, "Can't connect to Google Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

//    //   METHOD: TO  Create an instance of the GoogleAPIClient AND LocationRequest
//    public void   buildGoogleApiClient(Activity activity,
//                                             GoogleApiClient googleApiClient,
//                                             LocationRequest locationRequest
//    , FusedLocationProviderClient mFusedLocationClient,
//                                             AddressResultReceiver mResultReceiver) {
//
//        googleApiClient = new GoogleApiClient.Builder(activity)
//                .addApi(LocationServices.API)
//                .enableAutoManage(activity, 1 /* clientId */, this)
//                .addApi(Places.GEO_DATA_API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        googleApiClient.connect();
//
//        googleApiClient.connect();
//
//
//        createLocationRequest(activity,locationRequest,mFusedLocationClient,mResultReceiver);
//
//    }


    //    Handle "Never Ask Again" when asking permission
    public static void showDenyRationaleDialog(Activity activity, String message, DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(activity)
                        .setTitle("Jokar")
                        .setIcon(ContextCompat.getDrawable(context, R.drawable.down_arrow))
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK", okListener)
                        .setNegativeButton("Cancel", okListener);

        AlertDialog dialog = builder.create();
        //   ANIMATION
        Window window = dialog.getWindow();
        if (window != null) {
//            window.getAttributes().windowAnimations = R.style.dialogAnimation_Enter;
        }

        dialog.show();

        // Alert Buttons
        Button positive_button = dialog.getButton(BUTTON_POSITIVE);
        Button negative_button = dialog.getButton(BUTTON_NEGATIVE);

        positive_button.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        negative_button.setTextColor(ContextCompat.getColor(activity, R.color.black));


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
