package com.pagin.azul.helper;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.pagin.azul.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rajeev Kr. Sharma [rajeevrocker7@gmail.com] on 27/6/18.
 */

public class FetchAddressService extends IntentService {

    private static final String TAG = FetchAddressService.class.getSimpleName();
    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    protected ResultReceiver resultReceiver;

    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;

    public static final int FIND_BY_NAME = 10;
    public static final int FIND_BY_LOCATION = 11;
    public static final int FIND_BY_LOCATION_LATLNG = 13;

    public static final String FIND_BY = "find_by";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String LOCATION_NAME = PACKAGE_NAME + ".LOCATION_NAME";
    public static final String LOCATION = PACKAGE_NAME + ".LOCATION";
    public static final String LOCATION_LAT = PACKAGE_NAME + ".LOCATION_LAT";
    public static final String LOCATION_LNG = PACKAGE_NAME + ".LOCATION_LNG";

    private  double location_lat = 0.0;
    private  double location_lng = 0.0;

//  RESULT DATA CONSTANTS
    public static final String RESULT_MSG = PACKAGE_NAME + ".RESULT_MSG";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String CITY = PACKAGE_NAME + ".FETCH_CITY";
    public static final String COUNTRY_NAME = PACKAGE_NAME + ".FETCH_COUNTRY_NAME";
    public static final String AREA = PACKAGE_NAME + ".FETCH_AREA";
    public static final String PIN_CODE = PACKAGE_NAME + ".FETCH_PIN_CODE";

    public static final String ADDRESS_ARRAY_LIST = PACKAGE_NAME + ".ADDRESS_ARRAY_LIST";
    public static final String ADDRESS_ARRAY = PACKAGE_NAME + ".ADDRESS_ARRAY";

    public static final String LOC_LAT = PACKAGE_NAME + ".LOC_LAT";
    public static final String LOC_LNG = PACKAGE_NAME + ".LOC_LNG";

    public static final String FULL_LOCATION_VIA_JSON = PACKAGE_NAME + ".FULL_LOCATION_VIA_JSON";


//CONSTRUCTOR
    public FetchAddressService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null)
        {
            String errorMessage = "", area ="",city,pinCode="" , country_name ;


            resultReceiver = intent.getParcelableExtra(FetchAddressService.RECEIVER);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addressList = null;

            int findBy = intent.getIntExtra(FIND_BY,0);

            if(findBy == FIND_BY_NAME)
            {
                String locationName = intent.getStringExtra(FetchAddressService.LOCATION_NAME);
                try {
                    addressList = geocoder.getFromLocationName(locationName, 1);
                } catch (IOException e) {
                    errorMessage = "Service Not Available";
                    Log.e(TAG, errorMessage, e);
                }
            }
            else if(findBy == FIND_BY_LOCATION)
            {

                // Get the location passed to this service through an extra.
                Location location = intent.getParcelableExtra(FetchAddressService.LOCATION);
                Log.i(TAG, "LOCATION: LAT,LNG:  "+ location.getLatitude() +", "+location.getLongitude());

                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException ioException) {

                    errorMessage = "Service Not Available";
                    Log.e(TAG, errorMessage, ioException);


/*TO SOLVE IT: https://stackoverflow.com/questions/20238197/get-current-location-using-json/20238474#20238474*/
//                    //NOW
//                    String currentLocation = getCurrentLocationViaJSON(lat, lng);
//
//                    deliverResultToReceiver(FetchAddressService.SUCCESS_RESULT, "Address Found via JSON.",
//                            null, "",currentLocation,"",null,null,location_lat,location_lng);


                } catch (IllegalArgumentException illegalArgumentException) {

                    errorMessage = "Invalid Lat,Long used: ";
                    Log.e(TAG, errorMessage +
                                    " Latitude = " + location.getLatitude() +
                                    ", Longitude = " + location.getLongitude()
                            , illegalArgumentException);
                }


            }
            else if(findBy == FIND_BY_LOCATION_LATLNG)
            {

                // Get the location passed to this service through an extra.

                double lat = intent.getDoubleExtra(FetchAddressService.LOCATION_LAT,0.0);
                double lng = intent.getDoubleExtra(FetchAddressService.LOCATION_LNG,0.0);

                Log.i(TAG, "LOCATION: LAT,LNG:  "+lat +", "+lng);

                try {
                    addressList = geocoder.getFromLocation(lat,lng, 1);
                } catch (IOException ioException) {


                    errorMessage = "Service Not Available";
                    Log.e(TAG, errorMessage, ioException);

/*TO SOLVE IT: https://stackoverflow.com/questions/20238197/get-current-location-using-json/20238474#20238474*/
//                    //NOW
//                    String currentLocation = getCurrentLocationViaJSON(lat, lng);
//
//                    deliverResultToReceiver(FetchAddressService.SUCCESS_RESULT, "Address Found via JSON.",
//                            null, "",currentLocation,"",null,null,location_lat,location_lng);


                } catch (IllegalArgumentException illegalArgumentException) {

                    errorMessage = "Invalid Lat,Long used: ";
                    Log.e(TAG, errorMessage +
                                    " Latitude = " + lat +
                                    ", Longitude = " + lng
                            , illegalArgumentException);
                }


            }
            else {
                Log.e(TAG,"PLEASE MENTION 'FIND_BY' ATTRIBUTE Correctly");
            }



            /*RETRIEVE ADDRESS FROM ADDRESS LIST :---*/

            if (addressList == null || addressList.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "No Address Found \n";
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(FetchAddressService.FAILURE_RESULT, errorMessage,
                        null,null,null,null,null,null,null,location_lat,location_lng);
            }
            else
            {
                //ADDRESS STRING ARRAY AND Address ArrayList

                Address address = addressList.get(0);

                ArrayList<String> addressArrList = new ArrayList<>();

                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressArrList.add(address.getAddressLine(i));
                }

                String[] addressArray = new String[addressArrList.size()];
                addressArray = addressArrList.toArray(addressArray);

                Log.w(TAG, " Address ArrayList is : \n"+addressArrList+"\n");
                Log.w(TAG, " Address String[] Array is : \n"+ Arrays.toString(addressArray) +"\n");

//              LOCATION's LAT AND LNG:
                location_lat = address.getLatitude();
                location_lng = address.getLongitude();

//              COUNTRY_NAME
                country_name = address.getCountryName();
                if(country_name==null)
                {
                    country_name=address.getAdminArea();
                }


//              CITY
                city=address.getLocality();
                if(city==null)
                {
                    city=address.getFeatureName();
                }
//              AREA
                if(address.getAdminArea() !=null)
                {
                     area=address.getAdminArea();
                }
//              PIN_CODE
                if(address.getPostalCode() !=null)
                {
                     pinCode=address.getPostalCode();
                }

                Log.w("Address", ": "+address+"\n");
                Log.w("City", ": "+city+"\n");
                Log.w("Area", ": "+area+"\n");
                Log.w("COUNTRY_NAME", ": "+country_name+"\n");
                Log.w("PIN_CODE", ": "+pinCode+"\n");

                deliverResultToReceiver(FetchAddressService.SUCCESS_RESULT, "Address Found.",
                        address, city,area,pinCode,country_name,addressArrList,addressArray,location_lat,location_lng);

            }
        }
    }

//    METHOD: TO GIVE DATA RESULTS BACK TO A ResultReceiver CLASS
    private void deliverResultToReceiver(int resultCode, String message, Address address,
                                         String city, String area, String pinCode, String country_name,
                                         ArrayList<String> addressArrayList, String[] addressArray,
                                         double lat, double lng)
    {
        Bundle bundle = new Bundle();

        if("Address Found via JSON.".equalsIgnoreCase(message))
        {
            bundle.putString(FetchAddressService.FULL_LOCATION_VIA_JSON, area);


            bundle.putString(FetchAddressService.RESULT_MSG, message);
            bundle.putParcelable(FetchAddressService.RESULT_ADDRESS, address);
            bundle.putString(FetchAddressService.CITY, city);
            bundle.putString(FetchAddressService.AREA, area);
            bundle.putString(FetchAddressService.COUNTRY_NAME, country_name);
            bundle.putString(FetchAddressService.PIN_CODE, pinCode);

            bundle.putStringArrayList(FetchAddressService.ADDRESS_ARRAY_LIST, addressArrayList);
            bundle.putStringArray(FetchAddressService.ADDRESS_ARRAY, addressArray);

            bundle.putDouble(FetchAddressService.LOC_LAT, lat);
            bundle.putDouble(FetchAddressService.LOC_LNG, lng);

            resultReceiver.send(resultCode, bundle);
        }
        else {
            bundle.putString(FetchAddressService.RESULT_MSG, message);
            bundle.putParcelable(FetchAddressService.RESULT_ADDRESS, address);
            bundle.putString(FetchAddressService.CITY, city);
            bundle.putString(FetchAddressService.AREA, area);
            bundle.putString(FetchAddressService.COUNTRY_NAME, country_name);
            bundle.putString(FetchAddressService.PIN_CODE, pinCode);

            bundle.putStringArrayList(FetchAddressService.ADDRESS_ARRAY_LIST, addressArrayList);
            bundle.putStringArray(FetchAddressService.ADDRESS_ARRAY, addressArray);

            bundle.putDouble(FetchAddressService.LOC_LAT, lat);
            bundle.putDouble(FetchAddressService.LOC_LNG, lng);

            resultReceiver.send(resultCode, bundle);
        }



    }


    /*
    * SAMPLE JSON  GOT BY GOOGLE ADDRESS URL*/
//    {
//        "results": [
//        {
//            "address_components": [
//            {
//                "long_name": "C-1414",
//                    "short_name": "C-1414",
//                    "types": [
//                "premise"
//          ]
//            },
//            {
//                "long_name": "Sector 5",
//                    "short_name": "Sector 5",
//                    "types": [
//                "neighborhood",
//                        "political"
//          ]
//            },
//            {
//                "long_name": "Block D",
//                    "short_name": "Block D",
//                    "types": [
//                "political",
//                        "sublocality",
//                        "sublocality_level_2"
//          ]
//            },
//            {
//                "long_name": "Indira Nagar",
//                    "short_name": "Indira Nagar",
//                    "types": [
//                "political",
//                        "sublocality",
//                        "sublocality_level_1"
//          ]
//            },
//            {
//                "long_name": "Lucknow",
//                    "short_name": "Lucknow",
//                    "types": [
//                "locality",
//                        "political"
//          ]
//            },
//            {
//                "long_name": "Lucknow",
//                    "short_name": "Lucknow",
//                    "types": [
//                "administrative_area_level_2",
//                        "political"
//          ]
//            },
//            {
//                "long_name": "Uttar Pradesh",
//                    "short_name": "UP",
//                    "types": [
//                "administrative_area_level_1",
//                        "political"
//          ]
//            },
//            {
//                "long_name": "India",
//                    "short_name": "IN",
//                    "types": [
//                "country",
//                        "political"
//          ]
//            },
//            {
//                "long_name": "226016",
//                    "short_name": "226016",
//                    "types": [
//                "postal_code"
//          ]
//            }
//      ],
//            "formatted_address": "C-1414, Sector 5, Block D, Indira Nagar, Lucknow, Uttar Pradesh 226016, India",
//                "geometry": {
//            "location": {
//                "lat": 26.879746,
//                        "lng": 80.992723
//            },
//            "location_type": "ROOFTOP",
//                    "viewport": {
//                "northeast": {
//                    "lat": 26.8810949802915,
//                            "lng": 80.99407198029151
//                },
//                "southwest": {
//                    "lat": 26.8783970197085,
//                            "lng": 80.9913740197085
//                }
//            }
//        },
//            "place_id": "ChIJLTnzqq7imzkRDbvzXe4fwhE",
//                "types": [
//            "street_address"
//      ]
//        }
//  ],
//        "status": "OK"
//    }
/**/
//    public JSONObject getLocationInfo(double lat, double lng) {
//
//        HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true");
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response;
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream stream = entity.getContent();
//            int b;
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject = new JSONObject(stringBuilder.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return jsonObject;
//    }
//
//    public String getCurrentLocationViaJSON(double lat, double lng) {
//
//        JSONObject jsonObj = getLocationInfo(lat, lng);
//        Log.i("JSON string =>", jsonObj.toString());
//
//        String currentLocation = "";
//        String street_address = null;
//        String postal_code = null;
//
//        try {
//            String status = jsonObj.getString("status").toString();
//            Log.i("status", status);
//
//
//            if (status.equalsIgnoreCase("OK")) {
//                JSONArray results = jsonObj.getJSONArray("results");
//
//                int i = 0;
//                Log.i("i", i + "," + results.length());
//                do {
//                    Log.w(TAG,"******************** ADDRESS VIA JSON ******************" );
//
//                    ///////////Rajeev code starts here
//                    try {
//                        JSONObject r = results.getJSONObject(i);
//
//                        JSONArray address_componentsArr = results.getJSONObject(0).getJSONArray("address_components");
//
//                        String full_address_format  = results.getJSONObject(0).getString("formatted_address");
//                        SharedPreferenceWriter.getInstance(FetchAddressService.this).writeStringValue(SPreferenceKey.USER_PERSONAL_LOCATION, full_address_format);
//
//                        if (address_componentsArr != null) {
//                            for (int j = 0; j < address_componentsArr.length(); j++) {
//
//                                JSONObject object = address_componentsArr.getJSONObject(j);
//                                JSONArray typesArr = object.getJSONArray("types");
//
//                                if("locality".equalsIgnoreCase(typesArr.getString(0))) {
//                                    SharedPreferenceWriter.getInstance(FetchAddressService.this)
//                                            .writeStringValue(SPreferenceKey.USER_LOCATION_LOCALITY, typesArr.getString(0));
//
//                                    Log.w(TAG,"locality: "+typesArr.getString(0));
//                                }
//                                else if("administrative_area_level_2".equalsIgnoreCase(typesArr.getString(0))) {
//                                    SharedPreferenceWriter.getInstance(FetchAddressService.this)
//                                            .writeStringValue(SPreferenceKey.USER_CITY, typesArr.getString(0));
//
//                                    Log.w(TAG,"administrative_area_level_2 (city): "+typesArr.getString(0));
//                                }
//                                else if("administrative_area_level_1".equalsIgnoreCase(typesArr.getString(0))) {
//                                    SharedPreferenceWriter.getInstance(FetchAddressService.this)
//                                            .writeStringValue(SPreferenceKey.USER_STATE, typesArr.getString(0));
//
//                                    Log.w(TAG,"administrative_area_level_1 (state): "+typesArr.getString(0));
//                                }
//                                else if("country".equalsIgnoreCase(typesArr.getString(0))) {
//
//                                    Log.w(TAG,"country: "+typesArr.getString(0));
//                                }
//                                else if("postal_code".equalsIgnoreCase(typesArr.getString(0))){
//                                    SharedPreferenceWriter.getInstance(FetchAddressService.this)
//                                            .writeStringValue(SPreferenceKey.USER_PIN_CODE, typesArr.getString(0));
//
//                                    Log.w(TAG,"postal_code: "+typesArr.getString(0));
//                                }
//                            }
//
//                        }
//                        ///////////Rajeev code ends here
//
//
//                        JSONArray typesArray = r.getJSONArray("types");
//                        String types = typesArray.getString(0);
//
//                        if (types.equalsIgnoreCase("street_address")) {
//                            street_address = r.getString("formatted_address").split(",")[0];
//                            Log.i("street_address", street_address);
//                        } else if (types.equalsIgnoreCase("postal_code")) {
//                            postal_code = r.getString("formatted_address");
//                            Log.i("postal_code", postal_code);
//                        }
//
//                        if (street_address != null && postal_code != null) {
//                            currentLocation = street_address + "," + postal_code;
//                            Log.i("Current Location =>", currentLocation); //Delete this
//                            i = results.length();
//                        }
//
//                        i++;
//                    }catch (Exception e)
//                    {e.printStackTrace();}
//
//
//                } while (i < results.length());
//
//                Log.i("JSON Geo Location =>", currentLocation);
//                return currentLocation;
//            }
//
//        } catch (JSONException e) {
//            Log.e("testing", "Failed to load JSON");
//            e.printStackTrace();
//        }
//        return null;
//    }


}
