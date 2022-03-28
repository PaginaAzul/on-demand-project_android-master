package com.pagin.azul.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;

public class OrderDetailsOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    static String address = "";
    private static String addressPickUp = "";
    private static String addressDroup = "";
    ArrayList<LatLng> markerPoints;
    @BindView(R.id.tvpickLoc)
    TextView tvpickLoc;
    @BindView(R.id.tv_pickdis)
    TextView tv_pickdis;
    @BindView(R.id.tv_tym)
    TextView tv_tym;
    @BindView(R.id.tvdropLoc)
    TextView tvdropLoc;
    @BindView(R.id.tv_distance)
    TextView tv_distance;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.btn_offer)
    Button btn_offer;

    @BindView(R.id.tvPicktxt)
    TextView tvPicktxt;
    @BindView(R.id.tvserviceLoc)
    TextView tvserviceLoc;
    @BindView(R.id.tv_serdis)
    TextView tv_serdis;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.llProf)
    LinearLayout llProf;
    @BindView(R.id.llDelevery)
    LinearLayout llDelevery;

    private Marker marker;
    private NormalUserPendingOrderInner offerList;
    private String commingfrom = "";
    private GoogleMap mMap;

    public static Intent getIntent(Context context, NormalUserPendingOrderInner offerList, String from) {
        Intent intent = new Intent(context, OrderDetailsOnMapActivity.class);
        intent.putExtra("kData", (Serializable) offerList);
        intent.putExtra("kFrom", (Serializable) from);
        return intent;
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name, String pickLoc) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.order_marker_layout, null);

/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        TextView txt_name = (TextView) marker.findViewById(R.id.tvMarker);
        ImageView imageView8 = (ImageView) marker.findViewById(R.id.imageView8);
        imageView8.setImageResource(resource);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        txt_name.setText("          " + pickLoc);
        //  Jobs.setText(totalJobs+" Jobs");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }

    public static Bitmap createCustomMarkerDest(Context context, @DrawableRes int resource, String lat, String dropLons, String dropOff, String typeCome) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.desti_marker_layout, null);
/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(dropLons), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            // edtPickUpLocation.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView txt_name = (TextView) marker.findViewById(R.id.tvMarker);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        if (typeCome.equalsIgnoreCase("ActiveDP")) {
            txt_name.setText("            " + dropOff);
        } else {
            txt_name.setText("            " + address);
        }
        //  Jobs.setText(totalJobs+" Jobs");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }

    public static Bitmap createCustomMarkerCurrent(Context context, @DrawableRes int resource, String lat, String dropLons) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.current_marker_layout, null);
/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(dropLons), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            // edtPickUpLocation.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView txt_name = (TextView) marker.findViewById(R.id.tvMarker);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        txt_name.setText("            " + address);
        //  Jobs.setText(totalJobs+" Jobs");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_details);
        ButterKnife.bind(this);
        markerPoints = new ArrayList<>();

        if (getIntent() != null) {
            commingfrom = (String) getIntent().getStringExtra("kFrom");
            offerList = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
        }


        if (commingfrom.equalsIgnoreCase("ActiveDP")) {
            btn_offer.setVisibility(View.GONE);
        }


        if (commingfrom.equalsIgnoreCase("Professional")) {
            llDelevery.setVisibility(View.GONE);
            llProf.setVisibility(View.VISIBLE);
            offerList.setDropOffLat("28.5746");
            offerList.setDropOffLong("77.3561");
            tvTitle.setText("Service Location Details");
        }else if (offerList.getServiceType().equalsIgnoreCase("ProfessionalWorker")){
            llDelevery.setVisibility(View.GONE);
            llProf.setVisibility(View.VISIBLE);
            offerList.setDropOffLat("28.5746");
            offerList.setDropOffLong("77.3561");
            tvTitle.setText("Service Location Details");
        }
        else {
            llDelevery.setVisibility(View.VISIBLE);
            llProf.setVisibility(View.GONE);
            tvTitle.setText("Delivery Details");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        setData();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap == null)
            return;
        mMap = googleMap;

//        markerPoints.clear();
//        mMap.clear();


        LatLng current = new LatLng(Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLong)));
        LatLng pickup = new LatLng(Double.parseDouble(offerList.getPickupLat()), Double.parseDouble(offerList.getPickupLong()));
        LatLng desinetion = new LatLng(Double.parseDouble(offerList.getDropOffLat()), Double.parseDouble(offerList.getDropOffLong()));
      /*  mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())));
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapProfessinalWorkerActivity.this,R.drawable.marker, "25k","hello"))));
        marker.showInfoWindow();*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
        // Creating MarkerOptions
        markerPoints.add(current);
        markerPoints.add(pickup);
        markerPoints.add(desinetion);
        MarkerOptions Curernt = new MarkerOptions();
        MarkerOptions Pickup = new MarkerOptions();
        MarkerOptions Dest = new MarkerOptions();
        // Setting the position of the marker
        Curernt.position(current);
        Pickup.position(pickup);
        Dest.position(desinetion);
        if (markerPoints.size() == 3) {
            if (commingfrom.equalsIgnoreCase("ActiveDP")) {

                if(offerList.getServiceType().equalsIgnoreCase("DeliveryPersion")){
                    Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerCurrent(OrderDetailsOnMapActivity.this, R.drawable.you, SharedPreferenceWriter.getInstance(this).getString(kLat), SharedPreferenceWriter.getInstance(this).getString(kLong))));
                    Pickup.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(OrderDetailsOnMapActivity.this, R.drawable.pickup, "25k", offerList.getCurrentToPicupLocation() + "KM")));
                    Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(OrderDetailsOnMapActivity.this, R.drawable.dropoff, offerList.getDropOffLat(), offerList.getDropOffLong(), offerList.getPickupToDropLocation() + "Km", "ActiveDP")));
                    mMap.addMarker(Curernt);
                    mMap.addMarker(Pickup);
                    mMap.addMarker(Dest);
                }else {
                    Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerCurrent(OrderDetailsOnMapActivity.this, R.drawable.you, SharedPreferenceWriter.getInstance(this).getString(kLat), SharedPreferenceWriter.getInstance(this).getString(kLong))));
                    Pickup.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(OrderDetailsOnMapActivity.this, R.drawable.prof_service, "25k", offerList.getCurrentToDrLocation() + "KM")));
                    //Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(OrderDetailsOnMapActivity.this, R.drawable.green_pin, offerList.getDropOffLat(), offerList.getDropOffLong(), offerList.getPickupToDropLocation() + "Km", "ActiveDP")));
                    mMap.addMarker(Curernt);
                    mMap.addMarker(Pickup);
                    //mMap.addMarker(Dest);
                }


            }else if (commingfrom.equalsIgnoreCase("Professional")) {
                Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerCurrent(OrderDetailsOnMapActivity.this, R.drawable.you, SharedPreferenceWriter.getInstance(this).getString(kLat), SharedPreferenceWriter.getInstance(this).getString(kLong))));
                Pickup.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(OrderDetailsOnMapActivity.this, R.drawable.prof_service, "25k", offerList.getCurrentToDrLocation() + "KM")));
                mMap.addMarker(Curernt);
                mMap.addMarker(Pickup);
               // Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(OrderDetailsOnMapActivity.this, R.drawable.green_pin, String.valueOf(25.2744), String.valueOf(133.7751), offerList.getPickupToDropLocation() + "Km", "ActiveDP")));
            } else  {
                Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerCurrent(OrderDetailsOnMapActivity.this, R.drawable.you, SharedPreferenceWriter.getInstance(this).getString(kLat), SharedPreferenceWriter.getInstance(this).getString(kLong))));
                Pickup.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(OrderDetailsOnMapActivity.this, R.drawable.pickup, "25k", offerList.getPickupLocation())));
                Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(OrderDetailsOnMapActivity.this, R.drawable.green_pin, offerList.getDropOffLat(), offerList.getDropOffLong(), "", "test")));
                mMap.addMarker(Curernt);
                mMap.addMarker(Pickup);
                mMap.addMarker(Dest);
            }

        }
    }

    @OnClick({R.id.btn_offer, R.id.iv_back,R.id.tv_distance,
    R.id.tv_pickdis,R.id.tv_serdis})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_offer:
                openNewTab();
                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_distance:{
//                String drAddress = tvdropLoc.getText().toString();
//                String uri = "http://maps.google.co.in/maps?q=" + drAddress;
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                startActivity(intent);
                double lat = Double.parseDouble(offerList.getDropOffLat());
                double lng = Double.parseDouble(offerList.getDropOffLong());
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + tvdropLoc.getText().toString() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
                break;

            case R.id.tv_pickdis:{
                double lat = Double.parseDouble(offerList.getPickupLat());
                double lng = Double.parseDouble(offerList.getPickupLong());
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + tvpickLoc.getText().toString() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
//                String pcAddress = tvpickLoc.getText().toString();
//               // String uripc = "http://maps.google.co.in/maps?q=" + pcAddress;
//                String uripc = "http://maps.google.com/maps?q=loc:" + String.format("%f,%f", offerList.getPickupLat() , offerList.getPickupLong());
//                Intent intentpc = new Intent(Intent.ACTION_VIEW, Uri.parse(uripc));
//                startActivity(intentpc);

                break;
              case R.id.tv_serdis:{
                  double lat = Double.parseDouble(offerList.getPickupLat());
                  double lng = Double.parseDouble(offerList.getPickupLong());
                  //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
                  String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + tvserviceLoc.getText().toString() + ")";
                  //String map = "http://maps.google.co.in/maps?q=" + offerList.getPickupLocation();
                  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                  startActivity(intent);
              }
//                  String srAddress = tvserviceLoc.getText().toString();
//                  String urisr = "http://maps.google.co.in/maps?q=" + srAddress;
//                  Intent intentsr = new Intent(Intent.ACTION_VIEW, Uri.parse(urisr));
//                  startActivity(intentsr);

                break;

        }
    }

    private void setData() {

        String str = offerList.getSeletTime();
        String[] splited = str.split("\\s+");
        String split_one = splited[0];
        String split_second = splited[1];
        String split_three = splited[2];

        tv_tym.setText(split_second + " " + split_three);


        if (commingfrom.equalsIgnoreCase("Professional")) {
            tvserviceLoc.setText(offerList.getPickupLocation());
            tv_serdis.setText(offerList.getCurrentToDrLocation() + " km");
        }else if (commingfrom.equalsIgnoreCase("ActiveDP")) {
            tvserviceLoc.setText(offerList.getPickupLocation());
            tv_serdis.setText(offerList.getCurrentToDrLocation() + " km");
        }else {
            tvpickLoc.setText(offerList.getPickupLocation());
            tv_pickdis.setText(offerList.getCurrentToPicupLocation() + " km");
            tvdropLoc.setText(offerList.getDropOffLocation());
            tv_distance.setText(offerList.getPickupToDropLocation() + " km");
            tv_serdis.setText(offerList.getCurrentToDrLocation() + " km");

        }

    }

    private void openNewTab() {
        startActivity(DeliveryMakeOfferActivity.getIntent(this, offerList, commingfrom));
        finish();
    }
}
