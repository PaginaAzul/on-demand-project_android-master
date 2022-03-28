package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pagin.azul.R;
import com.pagin.azul.adapter.PlaceAutocompleteAdapter;
import com.pagin.azul.bean.AddressApi;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.bean.UpdateSettingNoti;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

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

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class PickAddressActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, GoogleApiClient.OnConnectionFailedListener {
    private double Lat, Long;
    private GoogleMap mMap;
    private String Location = "";
    private String lat;
    private String CommingType = "";
    private String longs;
    private LatLng mCenterLatLong;
    List<Address> addresses = null;

    private String edtTitle = "";
    private CharSequence primaryText;
    protected GoogleApiClient mGoogleApiClient;
    private boolean isCorrectAddress = false;
    @BindView(R.id.edt_search)
    AutoCompleteTextView autoLocation;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.showadd)
    TextView showadd;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.edt_building_no)
    EditText edt_building_no;

    private AddressList addressList;
    private String commingfrom = "";
    private String currentLoc = "";

    private String comfrm = "";
    private Marker marker;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private String currentAddress = "";
    private String fromActivity = "";
    int i = 0;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private ResultCallback<PlaceBuffer> placeBufferResultCallback = places -> {
        if (!places.getStatus().isSuccess()) {
            places.release();
            return;
        }
        final Place place = places.get(0);
        latLng = place.getLatLng();
        places.release();

        if (latLng != null) {
            try {

                mapMove(latLng);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };


    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            primaryText = item.getPrimaryText(null);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(placeBufferResultCallback);

            showadd.setText(item.getFullText(null));
            Location = item.getFullText(null).toString();
            isCorrectAddress = true;


        }
    };

    private void mapMove(LatLng latLng) {
        //Location location = task.getResult();
        LatLng currentLatLng = new LatLng(latLng.latitude,
                latLng.longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                13);
        mMap.moveCamera(update);
    }


    public static Intent getIntent(Context context, AddressList addressList, String from) {
        Intent intent = new Intent(context, PickAddressActivity.class);
        intent.putExtra("kData", (Serializable) addressList);
        intent.putExtra("kEdit", (Serializable) from);
        return intent;
    }


    public static Intent getIntent(Context context, String from, String currentLoc) {
        Intent intent = new Intent(context, PickAddressActivity.class);
        intent.putExtra("kEdit", (Serializable) from);
        intent.putExtra("Locat", (Serializable) currentLoc);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_address);
        ButterKnife.bind(this);
        tvTitle.setText("Choose your location");
        iv_back.setImageDrawable(getResources().getDrawable(R.drawable.close));


        if (getIntent() != null) {

            commingfrom = (String) getIntent().getStringExtra("kEdit");
            currentLoc = (String) getIntent().getStringExtra("Locat");
            addressList = (AddressList) getIntent().getSerializableExtra("kData");
            autoLocation.setText(currentLoc);

            if (addressList != null) {

                edt_building_no.setText(addressList.getBuildingAndApart());
            }

        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);


        GPSTracker gpsTracker = new GPSTracker(this, this);
        autoLocation.setOnItemClickListener(mAutocompleteClickListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, null);
        autoLocation.setAdapter(placeAutocompleteAdapter);


        Lat = gpsTracker.getLatitude();
        Long = gpsTracker.getLongitude();

        autoLocation.setOnEditorActionListener((v, actionId, event) -> {

            if (latLng != null) {
                pickNewLocation();
            }

            return false;
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;


        lat = String.valueOf(Lat);
        longs = String.valueOf(Long);
        LatLng latLngg = new LatLng(Lat, Long);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg).icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_s)));
        marker.showInfoWindow();


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
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

                showadd.setText(addressList.getAddress());
                autoLocation.setText(addressList.getAddress());
                currentAddress = addressList.getAddress();

            } else if (commingfrom.equalsIgnoreCase("PICKUP")) {

                showadd.setText(currentLoc);
                autoLocation.setText(currentLoc);

            } else if (commingfrom.equalsIgnoreCase("DROPOFF")) {

                showadd.setText(currentLoc);
                autoLocation.setText(currentLoc);
            } else {
                autoLocation.setText(address);
                showadd.setText(address);
            }

            Location = address;
            currentAddress = address;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

    }

    //getAddressfrommovingonMAP
    private void getAddressOnCameraMove(String lat, String longs) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(longs), 1);
            autoLocation.setText(addresses.get(0).getAddressLine(0));
            showadd.setText(addresses.get(0).getAddressLine(0));
            Location = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @OnClick({R.id.btn_done, R.id.iv_back, R.id.iv_clear})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:

                showDialogTitle();

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_clear:
                autoLocation.setText("");
                break;


        }
    }

    private void pickNewLocation() {
        List<Address> addresses = null;
        if (mMap == null) {
            return;
        }
        mMap.clear();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_s)));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));


        //Address pick here
        try {
            lat = String.valueOf(marker.getPosition().latitude);
            longs = String.valueOf(marker.getPosition().longitude);
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //  autoLocation.setText(address);
            Location = autoLocation.getText().toString();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        mMap.clear();

        markerOptions = new MarkerOptions().position(latLng);
        markerOptions.draggable(true);
        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_s)));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        isCorrectAddress = true;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            lat = String.valueOf(marker.getPosition().latitude);
            longs = String.valueOf(marker.getPosition().longitude);
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            autoLocation.setText(address);
            showadd.setText(address);

            Location = address;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(PickAddressActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);
        TextView btn_ok = view.findViewById(R.id.btn_ok);


        textView.setText("Please select the valid address via suggestion address or drop the pin anywhere on map");
        registerNow.setVisibility(View.GONE);
        notNow.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void addressApiHit() {
        try {
            MyDialog.getInstance(this).showDialog(PickAddressActivity.this);
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
                        MyDialog.getInstance(PickAddressActivity.this).hideDialog();
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


                                Toast.makeText(PickAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(PickAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
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


        tv_home.setOnClickListener(view -> edt_option.setText("Home"));

        tv_office.setOnClickListener(view -> edt_option.setText("Office"));


        dialog.findViewById(R.id.tv_save).setOnClickListener(v -> {
            if (edt_option.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter location manually or select location title", Toast.LENGTH_SHORT).show();
            } else if (commingfrom.equalsIgnoreCase("editbtn")) {
                edtTitle = edt_option.getText().toString();
                updateAddressApi();
                dialog.dismiss();
            } else {
                edtTitle = edt_option.getText().toString();
                addressApiHit();
                dialog.dismiss();
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
            MyDialog.getInstance(this).showDialog(PickAddressActivity.this);
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
                        MyDialog.getInstance(PickAddressActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {


                                startActivity(new Intent(PickAddressActivity.this, ManageAddressActivity.class));
//                                Intent intent = new Intent(PickAddressActivity.this, ManageAddressActivity.class);
////                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
////                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
                                finish();


                                // Toast.makeText(PickAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PickAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
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
}
