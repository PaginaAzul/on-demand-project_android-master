package com.pagin.azul.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.async.AsyncGetLatLong;
import com.pagin.azul.async.GooglePlacesAutocompleteAdapter;
import com.pagin.azul.async.ModelPlace;
import com.pagin.azul.async.OnLocationSelect;
import com.pagin.azul.bean.AddressApi;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.bean.UpdateSettingNoti;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.AddressResultReceiver;
import com.pagin.azul.helper.FetchAddressService;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.helper.StartGooglePlayServices;
import com.pagin.azul.onphasesecond.activity.FoodAndGroceryActivity;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;


import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class AddressPickerAct extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener, GoogleMap.OnMapClickListener,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleApiClient.ConnectionCallbacks, CommonListener {

    protected GoogleApiClient mGoogleApiClient;
    String latitudes = "";
    String longitudes = "";
    String getAddress = "";
    String spotLocation = "";
    String spotName = "";
    String region = "";
    @BindView(R.id.edt_search)
    AutoCompleteTextView autoLocation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.title)
//    TextView title;
    @BindView(R.id.showadd)
    TextView tvAddress;
    int i = 0;
    List<Address> addresses = null;
    private double Lat, Long;
    private GoogleMap mMap;
    private String Location;
    private String lat;
    private String longs;
    private CharSequence primaryText;
    private boolean isCorrectAddress = false;
    private GooglePlacesAutocompleteAdapter autocompleteAdapter;
    private Marker marker;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private String currentAddress = "";
    private Context context;
    private LatLng mCenterLatLong;
    private AddressList addressList;
    private String commingfrom = "";
    private String currentLoc = "";
    private String edtTitle = "";
    GPSTracker gpsTracker;
    StartGooglePlayServices startGooglePlayServices=new StartGooglePlayServices(this);
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private LocationCallback locationCallback;
    protected final int REQ_CODE_GPS_SETTINGS = 150;
    private final int REQ_CODE_LOCATION = 107;
    private Location mLastLocation;
    private boolean isLocServiceStarted = false;
    GoogleApiClient googleApiClient;
    private boolean isFirstTimeLocation = true;
    private String fullAddress="";
    private boolean isFirstAddress;


    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.showadd)
//    TextView showadd;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.edt_building_no)
    EditText edt_building_no;


    public static Intent getIntent(Context context, AddressList addressList, String from) {
        Intent intent = new Intent(context, AddressPickerAct.class);
        intent.putExtra("kData", (Serializable) addressList);
        intent.putExtra("kEdit", (Serializable) from);
        return intent;
    }

    public static Intent getIntent(Context context, String from, String currentLoc) {
        Intent intent = new Intent(context, AddressPickerAct.class);
        intent.putExtra("kEdit", (Serializable) from);
        intent.putExtra("Locat", (Serializable) currentLoc);
        return intent;
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, AddressPickerAct.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_address);
        ButterKnife.bind(this);
        context = this;
        tvTitle.setText(R.string.choose_your_location);
        iv_back.setImageDrawable(getResources().getDrawable(R.drawable.close));
        startLocationFunctioning();
        if (getIntent() != null) {
            commingfrom = (String) getIntent().getStringExtra("kEdit");
            currentLoc = (String) getIntent().getStringExtra("Locat");
            addressList = (AddressList) getIntent().getSerializableExtra("kData");
            //autoLocation.setText(currentLoc);
            if (addressList != null) {
                edt_building_no.setText(addressList.getBuildingAndApart());
            }
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
        gpsTracker = new GPSTracker(this, this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        getAddress();

        Lat = gpsTracker.getLatitude();
        Long = gpsTracker.getLongitude();
//setToolbar();

        if (gpsTracker != null) {
            if (gpsTracker.canGetLocation()) {
                Location location = gpsTracker.getLocation();
                if (location != null) {
                    SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
                    SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));
                } else {
                    takeTime();
                }
            } else {
                takeTime();
            }
        } else {
            Toast.makeText(this, "Can't Fetch Location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Address> addresses = null;
        lat = String.valueOf(Lat);
        longs = String.valueOf(Long);
        LatLng latLngg = new LatLng(Lat, Long);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
        marker.showInfoWindow();

//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (addresses != null && addresses.size() > 0) {
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            tvAddress.setText(address);
//            Location = address;
//            currentAddress = address;
//        }

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera position change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


                mMap.clear();

                try {
                    android.location.Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    lat = String.valueOf(mCenterLatLong.latitude);
                    longs = String.valueOf(mCenterLatLong.longitude);

                    getAddressOnCameraMove(lat, longs);
                    // Toast.makeText(PickAddressActivity.this, "11", Toast.LENGTH_SHORT).show();
                    // startIntentService(mLocation);
                    // mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                } catch (Exception e) {
                    e.printStackTrace();
                    autoLocation.setText("");
                    tvAddress.setText("");
                }
            }
        });

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            if (commingfrom.equalsIgnoreCase("editbtn")) {

                /*tvAddress.setText(addressList.getAddress());
                autoLocation.setText(addressList.getAddress());
                currentAddress = addressList.getAddress();*/
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocationName(addressList.getAddress(), 1);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(list.get(0).getLatitude(),list.get(0).getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (commingfrom.equalsIgnoreCase("PICKUP")) {

                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocationName(currentLoc, 1);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(list.get(0).getLatitude(),list.get(0).getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*tvAddress.setText(currentLoc);
                autoLocation.setText(currentLoc);*/
                //autoLocation.setText(address);
                //tvAddress.setText(address);

            }else if (commingfrom.equalsIgnoreCase(FoodAndGroceryActivity.class.getSimpleName())) {

                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocationName(currentLoc, 1);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(list.get(0).getLatitude(),list.get(0).getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (commingfrom.equalsIgnoreCase("DROPOFF")) {

//                tvAddress.setText(currentLoc);
//                autoLocation.setText(currentLoc);
                //tvAddress.setText(addresses.get(0).getAddressLine(0));
                //autoLocation.setText(addresses.get(0).getAddressLine(0));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                //autoLocation.setText(address);
                //tvAddress.setText(address);
            }

            Location = address;
            currentAddress = address;
        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setOnMapClickListener(this);
//        mMap.setOnMapLongClickListener(this);
//        mMap.setOnMarkerDragListener(this);

    }

    //getAddressfrommovingonMAP
    private void getAddressOnCameraMove(String lat, String longs) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(longs), 1);
            /*autoLocation.setText(addresses.get(0).getAddressLine(0));
            tvAddress.setText(addresses.get(0).getAddressLine(0));
            Location = addresses.get(0).getAddressLine(0);*/
            if(isFirstAddress){
                autoLocation.setText(addresses.get(0).getAddressLine(0));
                tvAddress.setText(addresses.get(0).getAddressLine(0));
                Location = addresses.get(0).getAddressLine(0);
            }else if (commingfrom.equalsIgnoreCase("editbtn")){
                String address = addressList.getAddress();
                autoLocation.setText(address);
                tvAddress.setText(address);
                Location = currentLoc;
                List<Address> list = geocoder.getFromLocationName(address,1);
                this.lat = String.valueOf(list.get(0).getLatitude());
                this.longs = String.valueOf(list.get(0).getLongitude());
                isFirstAddress = true;
            }else{
                autoLocation.setText(currentLoc);
                tvAddress.setText(currentLoc);
                Location = currentLoc;
                List<Address> list = geocoder.getFromLocationName(currentLoc,1);
                this.lat = String.valueOf(list.get(0).getLatitude());
                this.longs = String.valueOf(list.get(0).getLongitude());
                isFirstAddress = true;
            }
            /*if(isFirstAddress && fullAddress.equals("")){
                autoLocation.setText(addresses.get(0).getAddressLine(0));
                tvAddress.setText(addresses.get(0).getAddressLine(0));
                Location = addresses.get(0).getAddressLine(0);
            }else {
                autoLocation.setText(fullAddress);
                tvAddress.setText(fullAddress);
                Location = fullAddress;
                fullAddress = "";
                isFirstAddress = true;
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onMarkerDragStart(Marker marker) {
//    }
//
//    @Override
//    public void onMarkerDrag(Marker marker) {
//// marker.setAlpha(0.5f);
//    }
//
//    @Override
//    public void onMarkerDragEnd(Marker marker) {
//        isCorrectAddress = true;
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            lat = String.valueOf(marker.getPosition().latitude);
//            longs = String.valueOf(marker.getPosition().longitude);
//            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (addresses != null && addresses.size() > 0) {
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            tvAddress.setText(address);
//
//            Location = address;
//        }


    //   }

    @OnClick({R.id.btn_done, R.id.iv_back, R.id.iv_clear})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                if(commingfrom.equalsIgnoreCase(FoodAndGroceryActivity.class.getSimpleName())){
                    Intent intent = new Intent();
                    intent.putExtra("ADDRESS", Location);
                    intent.putExtra("LAT", lat);
                    intent.putExtra("LONG", longs);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    showDialogTitle();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_clear:
                autoLocation.setText("");
                break;


        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        GPSTracker gpsTracker = new GPSTracker(this, this);
        if (gpsTracker != null) {
            if (gpsTracker.canGetLocation()) {
                Location location = gpsTracker.getLocation();
                if (location != null) {
                    Lat = gpsTracker.getLatitude();
                    Long = gpsTracker.getLongitude();
                    initilizeMap();

                    getAddress();

                } else {
                    //takeTime();
                }
            } else {
                //takeTime();
            }
        } else {
            Toast.makeText(this, "Can't Fetch Location", Toast.LENGTH_SHORT).show();
        }
        *//*if (googleApiClient != null) {
            startLocationUpdates();
            googleApiClient.connect();
        }*//*

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient != null) {
            startLocationUpdates();
            googleApiClient.connect();
        }
    }

    private void initilizeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
    }

    private void pickNewLocation() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoLocation.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        List<Address> addresses = null;
        if (mMap == null) {
            return;
        }
        mMap.clear();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        //Address pick here
        try {
            lat = String.valueOf(marker.getPosition().latitude);
            longs = String.valueOf(marker.getPosition().longitude);
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            tvAddress.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
// autoLocation.setText(address);
            Location = autoLocation.getText().toString();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        mMap.clear();
//
//        markerOptions = new MarkerOptions().position(latLng);
//        markerOptions.draggable(true);
//        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
//
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        pickNewLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    public void setToolbar() {

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);

        //title.setText("Location");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void getAddress() {

        autocompleteAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.activity_pick_address);
        autoLocation.setAdapter(autocompleteAdapter);
        autoLocation.dismissDropDown();

        autoLocation.setOnItemClickListener(AddressPickerAct.this);

        autoLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ModelPlace str = (ModelPlace) parent.getItemAtPosition(position);
        getAddress = str.getPlaceName();
        spotName = str.getSpotName();
        region = str.getRegion();

        //set "address" before set "city"
        autoLocation.setText(getAddress);

        if (true) {
            new AsyncGetLatLong(this, str.getPlaceId(), new OnLocationSelect() {
                @Override
                public void onLocationSelect(String lat, String longi, String city, String state, String country, String postal) {
                    try {


                        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));

                        if (latLng != null) {
                            AddressPickerAct.this.pickNewLocation();
                        }

                        latitudes = lat;
                        longitudes = longi;

                        isCorrectAddress = true;

                        if (latitudes != null && !latitudes.equals("") && longitudes != null && !longitudes.equals("")) {
                            spotLocation = latitudes + "," + longitudes;

                            // getCurrentLocation(Double.parseDouble(latitudes),Double.parseDouble(longitudes));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).execute();
        } else {
            //new DialogInternetConnection(this).show();
        }
    }

    //pickLocation dialog
    private void showDialogTitle() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_location);
        TextView tv_showloc = dialog.findViewById(R.id.tv_showloc);
        EditText edt_option = dialog.findViewById(R.id.edt_option);
        TextView tv_home = dialog.findViewById(R.id.tv_home);
        TextView tv_office = dialog.findViewById(R.id.tv_office);
        TextView tv_save = dialog.findViewById(R.id.tv_save);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);

        if (!edt_building_no.getText().toString().equalsIgnoreCase("")) {
            tv_showloc.setText(edt_building_no.getText().toString() + ", " + autoLocation.getText().toString());
        } else {
            tv_showloc.setText(autoLocation.getText().toString());
        }


        tv_home.setOnClickListener(view -> edt_option.setText(R.string.home));

        tv_office.setOnClickListener(view -> edt_option.setText(R.string.office));


        dialog.findViewById(R.id.tv_save).setOnClickListener(v -> {
            if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                if (edt_option.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.please_enter_location_manualyy), Toast.LENGTH_SHORT).show();
                } else if (commingfrom.equalsIgnoreCase("editbtn")) {
                    edtTitle = edt_option.getText().toString();
                    updateAddressApi();
                    dialog.dismiss();
                } else {
                    edtTitle = edt_option.getText().toString();
                    addressApiHit();
                    dialog.dismiss();
                }
            }else {
                showDialogLogin();
            }

        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.putExtra("ADDRESS", Location);
            intent.putExtra("LAT", lat);
            intent.putExtra("LONG", longs);
            setResult(RESULT_OK, intent);
            finish();

            dialog.dismiss();
        });

        dialog.show();

    }


    private void updateAddressApi() {
        try {
            MyDialog.getInstance(this).showDialog(AddressPickerAct.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("address", autoLocation.getText().toString());
                jsonObject.put("buildingAndApart", edt_building_no.getText().toString());
                jsonObject.put("landmark", edtTitle);
                jsonObject.put("addressId", addressList.get_id());
                jsonObject.put("lat", lat);
                jsonObject.put("long", longs);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UpdateSettingNoti> beanCall = apiInterface.updateAddress(token, body);

                beanCall.enqueue(new Callback<UpdateSettingNoti>() {
                    @Override
                    public void onResponse(Call<UpdateSettingNoti> call, Response<UpdateSettingNoti> response) {
                        MyDialog.getInstance(AddressPickerAct.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                                startActivity(new Intent(AddressPickerAct.this, ManageAddressActivity.class));
//                                Intent intent = new Intent(PickAddressActivity.this, ManageAddressActivity.class);
////                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
////                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
                                finish();


                                // Toast.makeText(PickAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddressPickerAct.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateSettingNoti> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }

    }



    private void addressApiHit() {
        try {
            MyDialog.getInstance(this).showDialog(AddressPickerAct.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("address", autoLocation.getText().toString().trim());
                jsonObject.put("buildingAndApart", edt_building_no.getText().toString().trim());
                jsonObject.put("landmark", edtTitle);
                jsonObject.put("lat", lat);
                jsonObject.put("long", longs);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<AddressApi> beanCall = apiInterface.addAddress(token, body);

                beanCall.enqueue(new Callback<AddressApi>() {
                    @Override
                    public void onResponse(Call<AddressApi> call, Response<AddressApi> response) {
                        MyDialog.getInstance(AddressPickerAct.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {


                          /*      Intent intent = new Intent(PickAddressActivity.this, ManageAddressActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);*/


                                // if (isCorrectAddress == true || autoLocation.getText().toString().equalsIgnoreCase(currentAddress)) {

                                Intent intent = new Intent();
                                intent.putExtra("ADDRESS", Location);
                                intent.putExtra("LAT", lat);
                                intent.putExtra("LONG", longs);
                                setResult(RESULT_OK, intent);
                                finish();

//                                } else {
//                                    showDialog();
//                                }


                                Toast.makeText(AddressPickerAct.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(AddressPickerAct.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AddressApi> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }


    }


    private void showDialogLogin() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);
        textView.setText(R.string.you_are_not_logged_in_please_login_sign_up_further_proced);
        registerNow.setText(R.string.ok_upper_case);
        notNow.setText(R.string.cancel);

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressPickerAct.this, SignUpOptions.class));
                finish();
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    //for popup open after some time
    public void takeTime() {
// take time
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//Do something after 100ms
                showSettingsAlert(1124, AddressPickerAct.this);
            }
        }, 1000);
    }


    //for location alert turn on gps
    public void showSettingsAlert(final int requestCode, final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressPickerAct.this);
        alertDialog.setCancelable(false);

        alertDialog.setTitle("Location Disable");

// alertDialog.setMessage("Please enable your Location");
        alertDialog.setMessage("Jokar requires access to location. To enjoy all that Jokar has to offer, turn on your GPS and give Jokar access to your location.");


        alertDialog.setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, requestCode);

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        if (!(activity).isFinishing()) {
            alert.show();
        }

    }

    //    METHOD: TO START FULL PROCESS FOR FIRST TIME..
    private void startLocationFunctioning() {
        if (!CommonUtility.isNetworkAvailable(this)) {
            Toast.makeText(this.getApplicationContext(), "Internet not available.", Toast.LENGTH_SHORT).show();
        } else {
            if (startGooglePlayServices.isGPlayServicesOK(this)) {
                buildGoogleApiClient();
            }
        }
    }

    //   METHOD: TO  Create an instance of the GoogleAPIClient AND LocationRequest
    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 1  /*clientId*/ , this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        createLocationRequest();
    }

    //Method: To create location request and set its priorities
    public  void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // CREATE A FUSED LOCATION CLIENT PROVIDER OBJECT
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //      Initialize AddressResultReceiver class object
        mResultReceiver = new AddressResultReceiver(this,this,null,isFirstTimeLocation);

    }

    // LOCATION SETTINGS SET UP
    public  void setUpLocationSettingsTaskStuff() {
         //Initialize the pending result and LocationSettingsRequest
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.

            // GPS is already enabled no need of review_dialog
            //Log.w(TAG, "onResult: Success 1");

            loadCurrentLoc(); //GET CURRENT LOCATION
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a review_dialog.
                // Log.w(TAG, "REQ_CODE_GPS_SETTINGS: REQ " + 0);

                try {
                    // Show the review_dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    //Log.w(TAG, "REQ_CODE_GPS_SETTINGS: REQ " + 1);
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(AddressPickerAct.this, REQ_CODE_GPS_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                    sendEx.printStackTrace();
                }
            }
        });
    }

    //    METHOD TO GET CURRENT LOCATION OF DEVICE
    public void loadCurrentLoc() {

        //      Marshmallow +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    startGooglePlayServices.showDenyRationaleDialog(this,"You need to allow access to Device Location", (dialog, which) -> {

                        switch (which) {
                            case BUTTON_POSITIVE:
                                dialog.dismiss();
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQ_CODE_LOCATION);

                                break;
                            case BUTTON_NEGATIVE:
                                dialog.dismiss();
                                finish();
                                break;
                        }

                    });

                    return;
                }

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_CODE_LOCATION);

                return;
            }


            //DO THE LOCATION STUFF

            try {


                mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                        location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                //Log.w(TAG, "addOnSuccessListener: location: " + location);

                                locationCallBack(location);


                            } else {
//                                    Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                isLocServiceStarted = false;
                                //Log.w(TAG, "addOnSuccessListener: location: " + null);
//                                    MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
                            }
                        });


                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            if (location != null) {
                                // Log.w(TAG, "LocationCallback:" + location);

                                if (!isLocServiceStarted) {


                                    locationCallBack(location);

                                }
                            }

                        }
                    }

                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else    //    PRE-Marshmallow
        {
            //DO THE LOCATION STUFF

            try {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                        location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                //Log.w(TAG, "addOnSuccessListener: location: " + location);

                                locationCallBack(location);

                            } else {
//                                    Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                isLocServiceStarted = false;
                                //Log.w(TAG, "addOnSuccessListener: location: " + null);
//                                    MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
                            }
                        });


                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            if (location != null) {
                                //Log.w(TAG, "LocationCallback:" + location);
                                if (!isLocServiceStarted) {
                                    locationCallBack(location);
                                }
                            }
                        }
                    }

                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        boolean b = mFusedLocationClient != null && locationRequest != null;
        if (b && locationCallback != null)
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void locationCallBack(Location location) {

        mLastLocation = location;

        /*if(isFirstTimeLocation){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }*/

        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(FetchAddressService.FIND_BY, FetchAddressService.FIND_BY_LOCATION);
        intent.putExtra(FetchAddressService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressService.LOCATION, mLastLocation);
        this.startService(intent);

        isLocServiceStarted = true;

        //currentLocationLatLngs = new LatLng (lat, lng);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_GPS_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        loadCurrentLoc();
                        break;
                    case Activity.RESULT_CANCELED:
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpLocationSettingsTaskStuff();   // On Connected
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstTimeLocation = false;
    }

    @Override
    public void onFetchLocation(String fullAddress,double the_lat,double the_long) {
        /*try {
            this.fullAddress = fullAddress;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(the_lat,the_long)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                }else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            }else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        }
        else {
            super.attachBaseContext(newBase);
        }
    }
}