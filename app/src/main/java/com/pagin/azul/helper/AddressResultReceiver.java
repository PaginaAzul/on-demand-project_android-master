package com.pagin.azul.helper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.pagin.azul.adapter.AutoTextComplete;
import com.pagin.azul.callback.CommonListener;

import java.util.ArrayList;

public class AddressResultReceiver extends ResultReceiver {

    public static final String TAG = AddressResultReceiver.class.getSimpleName();

    Activity activity;
    TextView editText;
    AutoCompleteTextView autoLocation;
    private boolean isFirstTimeLocation;
    private CommonListener commonListener;

    public AddressResultReceiver(Activity activity, TextView editText, Handler handler, boolean isFirstTimeLocation) {
            super(handler);
            this.activity=activity;
            this.editText=editText;
            this.isFirstTimeLocation = isFirstTimeLocation;
        }

        public AddressResultReceiver(Activity activity, CommonListener commonListener, Handler handler, boolean isFirstTimeLocation) {
            super(handler);
            this.activity=activity;
            this.commonListener=commonListener;
            this.isFirstTimeLocation = isFirstTimeLocation;
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {

            switch (resultCode) {
                case FetchAddressService.SUCCESS_RESULT:

                    if (activity != null) {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                android.location.Address address = resultData.getParcelable(FetchAddressService.RESULT_ADDRESS);

                                String msg = resultData.getString(FetchAddressService.RESULT_MSG);
                                String city = resultData.getString(FetchAddressService.CITY);
                                String area = resultData.getString(FetchAddressService.AREA);
                                String country_name = resultData.getString(FetchAddressService.COUNTRY_NAME);
                                String full_loc_via_json = resultData.getString(FetchAddressService.FULL_LOCATION_VIA_JSON);

                                String pin = resultData.getString(FetchAddressService.PIN_CODE);
                                double the_lat = resultData.getDouble(FetchAddressService.LOC_LAT);
                                double the_lng = resultData.getDouble(FetchAddressService.LOC_LNG);


                                ArrayList<String> addressArrayList = resultData.getStringArrayList(FetchAddressService.ADDRESS_ARRAY_LIST);


                                String fullAddress = "";
                                String locality;
                                int max;
                                if (address != null) {
                                    max = address.getMaxAddressLineIndex();

                                    for (int i = 0; i <= max - 1; i++) {
                                        fullAddress = fullAddress + address.getAddressLine(i) + ", ";
                                    }

                                    fullAddress = fullAddress + address.getAddressLine(address.getMaxAddressLineIndex());

                                    if (address.getLocality() != null && !address.getLocality().isEmpty()) {
                                        locality = address.getLocality();
                                    } else {
                                        locality = address.getSubLocality();
                                    }

                                    Log.w(TAG, "Address +++++: " + fullAddress);

                                    Log.w(TAG, "+++++++++++++++++ AddressResultReceiver +++++++++++++++++++");
                                    Log.w("Results ARE: \n",

                                            "MESSAGE: \n" + msg
                                                    + "\nADDRESS: \n" + address
                                                    + "\nLOCALITY: \n" + locality
                                                    + "\nCITY: \n" + city
                                                    + "\nAREA: \n" + area
                                                    + "\nCOUNTRY_NAME: \n" + country_name
                                                    + "\nPIN: \n" + pin
                                                    + "\nADDRESS_ARRAY_LIST: \n" + addressArrayList
                                                    + "\nLOC_LAT: \n" + the_lat
                                                    + "\nLOC_LNG: \n" + the_lng);


                                    if(isFirstTimeLocation){

                                        if(editText!=null)
                                            editText.setText(fullAddress);

                                        if(commonListener!=null){
                                            commonListener.onFetchLocation(fullAddress,the_lat,the_lng);
                                        }
                                            //autoLocation.setText(fullAddress);

                                        isFirstTimeLocation = false;

                                    }

                                } else {
                                    Log.e(TAG, "---- AddressResultReceiver : Address is NULL ----");

                                    Log.w(TAG, "---- full_loc_via_json : ----" + full_loc_via_json);

                                    if(isFirstTimeLocation){
                                    editText.setText(fullAddress);
                                    isFirstTimeLocation = false;

                                    }

                                }


                            }
                        });
                    }

                    break;

                case FetchAddressService.FAILURE_RESULT:

                    Log.w("Results ARE:", "FAILED"); //Timed out waiting for response from server

                    break;
            }

        }

    }
