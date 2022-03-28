package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.utils.CommonUtility;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareLocationActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener{
    private double Lat, Long;
    private GoogleMap mMap;
    private String Location = "";
    private String lat;
    private String CommingType = "";
    private String longs;
    private LatLng mCenterLatLong;
    List<Address> addresses = null;
    @BindView(R.id.tv_location)
    TextView tv_location;
   @BindView(R.id.tv_title)
    TextView tv_title;
 @BindView(R.id.iv_back)
 ImageView iv_back;




    public static Intent getIntent(Context context){
        return new Intent(context,ShareLocationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);
        ButterKnife.bind(this);

        tv_title.setText(getString(R.string.share_live_location));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        GPSTracker gpsTracker = new GPSTracker(this, this);

        Lat = gpsTracker.getLatitude();
        Long = gpsTracker.getLongitude();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = String.valueOf(Lat);
        longs = String.valueOf(Long);
        LatLng latLngg = new LatLng(Lat, Long);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg).icon(BitmapDescriptorFactory.fromResource(R.drawable.you)));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        getAddressOnCameraMove(lat, longs);

    }


    //getAddressfrommovingonMAP
    private void getAddressOnCameraMove(String lat, String longs) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(longs), 1);
            tv_location.setText(addresses.get(0).getAddressLine(0));
            Location = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @OnClick(R.id.btn_share)
    void onClick(){
        Intent intent = new Intent();
        intent.putExtra("ADDRESS", Location);
        intent.putExtra("LAT", lat);
        intent.putExtra("LONG", longs);
        setResult(RESULT_OK, intent);
        finish();

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
