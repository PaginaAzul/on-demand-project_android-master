package com.pagin.azul.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.MessageTrackInner;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;

public class ActiveTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String MY_API_KEY = "AIzaSyBm6eN8n8A3tzhp-RYKwDZhHw3IfJ36BNA";
    private NormalUserPendingOrderInner getInnerData;
    static String address1 = "";
    @BindView(R.id.invoice_date)
    TextView invoice_date;
    @BindView(R.id.invoice_time)
    TextView invoice_time;
    @BindView(R.id.order_id)
    TextView order_id;
    @BindView(R.id.workertopickup)
    TextView droptodelivered;
    @BindView(R.id.pickuptodrop)
    TextView pickuptodrop;
    @BindView(R.id.starttopickup)
    TextView starttopickup;
    @BindView(R.id.tvCreatedInvoice)
    TextView tvInvoiceCreated;
    @BindView(R.id.tvInvoice)
    TextView tvInvoice;
    @BindView(R.id.tvArrived)
    TextView tvArrived;
    @BindView(R.id.tvWorkDone)
    TextView tvWorkDone;
    @BindView(R.id.tv_pickupsatatus)
    TextView tv_pickupsatatus;
    @BindView(R.id.tv_dropoffstatus)
    TextView tv_dropoffstatus;
    @BindView(R.id.webUrl)
    WebView webUrl;
    private boolean mapRefresh;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    private String address = "";
    private GoogleMap mMap;
    private Marker marker;
    ArrayList<LatLng> markerPoints;
    private String commingFrom;
    private MessageTrackInner msgTrackData;

    private Socket mSocket;

    public static Intent getIntent(Context context, NormalUserPendingOrderInner getInnerData, String commingFrom) {
        Intent intent = new Intent(context, ActiveTrackingActivity.class);
        intent.putExtra("kData", (Serializable) getInnerData);
        intent.putExtra("kCome", (Serializable) commingFrom);
        return intent;
    }

    public static Intent getIntent(Context context, NormalUserPendingOrderInner getInnerData, String commingFrom,MessageTrackInner msgTrackData) {
        Intent intent = new Intent(context, ActiveTrackingActivity.class);
        intent.putExtra("kData", (Serializable) getInnerData);
        intent.putExtra("kCome", (Serializable) commingFrom);
        intent.putExtra("kMsgTrackData", (Serializable) msgTrackData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_tracking);
        ButterKnife.bind(this);
        markerPoints = new ArrayList<>();

        if (getIntent() != null) {
            getInnerData = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            commingFrom = (String) getIntent().getStringExtra("kCome");
            //msgTrackData = (MessageTrackInner) getIntent().getSerializableExtra("kMsgTrackData");
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        if (getInnerData != null) {
            order_id.setText("Order ID: " + getInnerData.getOrderNumber());
            starttopickup.setText(getInnerData.getCurrentToPicupLocation() + "km");
            if(!getInnerData.getPickupToDropLocation().equalsIgnoreCase("NaN"))
                pickuptodrop.setText(getInnerData.getPickupToDropLocation() + "km");
            else
                pickuptodrop.setText("0 km");
            droptodelivered.setText("0" + "km");

            String getDate = getInnerData.getCreatedAt();
            String server_format = getDate;    //server comes format ?
            String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
            String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            try {

                Date date = sdf.parse(server_format);
                System.out.println(date);
                String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
                System.out.println(your_format);
                String[] splitted = your_format.split(" ");
                System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
                // Now you can set the TextView here

                invoice_date.setText(String.valueOf(splitted[1]+" "+splitted[2]));
                invoice_time.setText(String.valueOf(splitted[0]));

            } catch (Exception e) {
                System.out.println(e.toString()); //date format error
            }
        }
        /*if (commingFrom.equalsIgnoreCase("ActiveDP")) {
            if (msgTrackData.getGoStatus().equalsIgnoreCase("false")) {
                tvInvoice.setVisibility(View.GONE);
                tvInvoiceCreated.setVisibility(View.GONE);
            } else if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                if(msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")){
                    tvInvoice.setVisibility(View.VISIBLE);
                    tvInvoice.setText("Start");
                    tvWorkDone.setVisibility(View.GONE);
                    tvInvoiceCreated.setVisibility(View.GONE);

                    if(msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {

                        tvArrived.setVisibility(View.VISIBLE);
                    }

                }else if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true"))  {
                    tvWorkDone.setVisibility(View.GONE);
                    tvInvoiceCreated.setVisibility(View.VISIBLE);
                    tvArrived.setVisibility(View.VISIBLE);
                    tvInvoice.setVisibility(View.VISIBLE);
                    tvInvoice.setText(R.string.review_invoice);
                    tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));

                    if(msgTrackData.getArrivedStatus().equalsIgnoreCase("false")){
                        tvInvoice.setText(getResources().getString(R.string.review_invoice));
                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                        tvWorkDone.setVisibility(View.GONE);
                        tvArrived.setVisibility(View.GONE);
                        tvInvoiceCreated.setVisibility(View.VISIBLE);
                    }else if(msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                        tvInvoice.setText(getResources().getString(R.string.review_invoice));
                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                        tvWorkDone.setVisibility(View.GONE);
                        tvArrived.setVisibility(View.VISIBLE);
                        tvInvoiceCreated.setVisibility(View.VISIBLE);

//                        if(getInnerData.getWorkDoneStatus().equalsIgnoreCase("false")){
//                            tvworkdone.setVisibility(View.GONE);
//                        }else {
//                            tvworkdone.setVisibility(View.VISIBLE);
//                        }
                    }

                }
        }

            tvInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvInvoice.getText().toString().trim().equalsIgnoreCase("Edit\nInvoice")) {
                        startActivity(CreateInvoiceActivity.getIntent(ActiveTrackingActivity.this, getInnerData, "ActiveDP"));
                    }
                }
            });


        } else if (commingFrom.equalsIgnoreCase("FromNUActive")) {

//            if (getInnerData.getInvoiceStatus().equalsIgnoreCase("true")) {
//                tvInvoice.setText(R.string.review_invoice);
//                tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
//                tvInvoiceCreated.setVisibility(View.VISIBLE);
//            } else {
//                tvInvoice.setText("Go");
//                tvInvoice.setTextColor(getResources().getColor(R.color.green));
//            }
            tv_pickupsatatus.setText(getString(R.string.dropoff));
            tv_dropoffstatus.setText(getString(R.string.prof_worker));

            if (msgTrackData.getGoStatus().equalsIgnoreCase("false")) {
                tvInvoice.setVisibility(View.GONE);
                tvInvoiceCreated.setVisibility(View.GONE);
            } else if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                if(msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")){
                    tvInvoice.setVisibility(View.VISIBLE);
                    tvInvoice.setText("Start");
                    tvWorkDone.setVisibility(View.GONE);
                    tvInvoiceCreated.setVisibility(View.GONE);
                    tvArrived.setVisibility(View.GONE);
                }else if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true"))  {
                    tvWorkDone.setVisibility(View.GONE);
                    tvInvoiceCreated.setVisibility(View.VISIBLE);
                    tvArrived.setVisibility(View.VISIBLE);
                    tvInvoice.setVisibility(View.VISIBLE);
                    tvInvoice.setText(R.string.review_invoice);
                    tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));

                    if(msgTrackData.getArrivedStatus().equalsIgnoreCase("false")){
                        tvInvoice.setText(getResources().getString(R.string.review_invoice));
                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                        tvWorkDone.setVisibility(View.GONE);
                        tvArrived.setVisibility(View.GONE);
                        tvInvoiceCreated.setVisibility(View.VISIBLE);
                    }else if(msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                        tvInvoice.setText(getResources().getString(R.string.review_invoice));
                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                        tvWorkDone.setVisibility(View.GONE);
                        tvArrived.setVisibility(View.VISIBLE);
                        tvInvoiceCreated.setVisibility(View.VISIBLE);

                        if(getInnerData.getWorkDoneStatus().equalsIgnoreCase("false")){
                            tvWorkDone.setVisibility(View.GONE);
                        }else {
                            tvWorkDone.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }


            tvInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvInvoice.getText().toString().equalsIgnoreCase("Review\nInvoice")) {
                        webUrl.getSettings().setJavaScriptEnabled(true);
                        webUrl.loadUrl("http://docs.google.com/gview?embedded=true&url=" + getInnerData.getInvoicePdf());
                    }
                }
            });
        } else if (commingFrom.equalsIgnoreCase("ActivePW")) {

            if (getInnerData.getGoStatus().equalsIgnoreCase("false")) {
                tvInvoice.setText(R.string.review_invoice);
                tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                tvInvoiceCreated.setVisibility(View.VISIBLE);
            } else {
                tvInvoice.setText("Go");
                tvInvoice.setTextColor(getResources().getColor(R.color.green));
            }


            tvInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvInvoice.getText().toString().equalsIgnoreCase("Review\nInvoice")) {
                        webUrl.getSettings().setJavaScriptEnabled(true);
                        webUrl.loadUrl("http://docs.google.com/gview?embedded=true&url=" + getInnerData.getInvoicePdf());
                    }
                }
            });
        }*/
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        connectSockettest();
    }



    private void connectSockettest() {
        MyDialog.getInstance(this).hideDialog();
        try {
            //mSocket = IO.socket("http://18.189.223.53:3000");
            //mSocket = IO.socket("http://3.128.74.178:3000");
            mSocket = IO.socket("http://3.129.47.202:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        mSocket.on("tracking", onTracking);

        try {
            JSONObject obj = new JSONObject();

            obj.put("roomId", getInnerData.getUserId()+getInnerData.get_id());
            mSocket.emit("room join", obj);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // TODO: 25/1/18 messagechecking
    private Emitter.Listener onTracking = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String latitude;
                    String longitude;

                    try {
                        latitude = data.getString("lattitude");
                        longitude = data.getString("longitude");

                        if (latitude != null && !latitude.equalsIgnoreCase("")) {

                            getRoute(Double.parseDouble(latitude), Double.parseDouble(longitude),Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()), Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()));

                            //getRoute(Double.parseDouble(SharedPreferenceWriter.getInstance(ActiveTrackingActivity.this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(ActiveTrackingActivity.this).getString(kLong)),Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()), Double.parseDouble(latitude), Double.parseDouble(longitude));
                        } else {
                            getRoute(Double.parseDouble(SharedPreferenceWriter.getInstance(ActiveTrackingActivity.this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(ActiveTrackingActivity.this).getString(kLong)),Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()), Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()));

                        }
                        Log.e("lattitude ",latitude);

                }catch (Exception e){
                    e.printStackTrace();}
                }

            });
        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap == null) {
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng current = new LatLng(Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLong)));

        if (getInnerData.getDropOffLat() != null && !getInnerData.getDropOffLat().equalsIgnoreCase("")) {
            getRoute(Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLong)),Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()), Double.parseDouble(getInnerData.getDropOffLat()), Double.parseDouble(getInnerData.getDropOffLong()));
        } else {
            getRoute(Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLong)),Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()), Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()));
        }
    }


    public Bitmap createCustomMarkerDest(Context context, @DrawableRes int resource, String _name, String totalJobs) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.desti_marker_layout, null);

/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            if (getInnerData.getDropOffLat() != null && !getInnerData.getDropOffLat().equalsIgnoreCase("")) {
                addresses = geocoder.getFromLocation(Double.parseDouble(getInnerData.getDropOffLat()), Double.parseDouble(getInnerData.getDropOffLong()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }else {
                addresses = geocoder.getFromLocation(Double.parseDouble(getInnerData.getPickupLat()), Double.parseDouble(getInnerData.getPickupLong()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            }
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
        ImageView imageView8 = (ImageView) marker.findViewById(R.id.imageView8);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        txt_name.setText("                " + getInnerData.getDropOffLocation());
        txt_name.setVisibility(View.GONE);
        imageView8.setImageResource(resource);
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


    private void getRoute(double latitudeCurrent, double longitudeCurrent,double latitudePick, double longitudePick, double latitudeDest, double longitudeDest) {
        markerPoints.clear();
        mMap.clear();


        LatLng current = new LatLng(latitudeCurrent, longitudeCurrent);
        LatLng pickUp = new LatLng(latitudePick, longitudePick);
        LatLng desinetion = new LatLng(latitudeDest, longitudeDest);


      /*  mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())));
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapProfessinalWorkerActivity.this,R.drawable.marker, "25k","hello"))));
        marker.showInfoWindow();*/

      /*if(!mapRefresh) {
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
          mapRefresh = true;
      }*/
        // Creating MarkerOptions
        markerPoints.add(current);
        markerPoints.add(pickUp);
        markerPoints.add(desinetion);

        MarkerOptions Curernt = new MarkerOptions();
        MarkerOptions Pickup = new MarkerOptions();
        MarkerOptions Dest = new MarkerOptions();


        // Setting the position of the marker
        Curernt.position(current);
        Pickup.position(pickUp);
        Dest.position(desinetion);

        if (markerPoints.size() == 3) {
            if(getInnerData.getServiceType().equalsIgnoreCase("DeliveryPersion")){
                Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerCurrent(ActiveTrackingActivity.this, R.drawable.you, "25k", "hello")));
                Pickup.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(ActiveTrackingActivity.this, R.drawable.pickup, "25k", "hello")));
                Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(ActiveTrackingActivity.this, R.drawable.prof_service, "25k", "hello")));
                mMap.addMarker(Curernt);
                mMap.addMarker(Dest);
                mMap.addMarker(Pickup);
            }else {
                Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerCurrent(ActiveTrackingActivity.this, R.drawable.prof_icon, "25k", "hello")));
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                    Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(ActiveTrackingActivity.this, R.drawable.dropoff, "25k", "hello")));
                else
                    Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(ActiveTrackingActivity.this, R.drawable.ponto, "25k", "hello")));
                mMap.addMarker(Curernt);
                mMap.addMarker(Dest);
            }

        }
        // Add new marker to the Google Map Android API V2
        /*if(!mapRefresh){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(Curernt.getPosition());
            builder.include(Dest.getPosition());
            LatLngBounds bounds = builder.build();
            int padding = 12; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            *//*int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);*//*
            mMap.moveCamera(cu);
            mapRefresh = true;
        }*/

        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 3) {
            if(getInnerData.getServiceType().equalsIgnoreCase("DeliveryPersion")) {
                String url = getDirectionsUrl(current, pickUp, desinetion);
                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }else {
                String url = getDirectionsUrl(current, desinetion);
                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        }
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }
            // Drawing polyline in the Google Map for the i-th route
            // mMap.addPolyline(lineOptions);
            if ((points) != null) {
                mMap.addPolyline(lineOptions);

                if(!mapRefresh){
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for(int i = 0; i < lineOptions.getPoints().size();i++){
                        builder.include(lineOptions.getPoints().get(i));
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 64; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                    mapRefresh = true;
                }
            }

        }

    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d(" while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed



    public Bitmap createCustomMarkerCurrent(Context context, @DrawableRes int resource, String lat, String dropLons) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.current_marker_layout, null);
/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLat)), Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString(kLong)), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
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
        ImageView imageView8 = (ImageView) marker.findViewById(R.id.imageView8);
        imageView8.setImageResource(resource);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        txt_name.setText("            " + address1);
        txt_name.setVisibility(View.GONE);
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

    public Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name, String totalJobs) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.order_marker_layout, null);

/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        TextView txt_name = (TextView) marker.findViewById(R.id.tvMarker);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        txt_name.setText("              " + getInnerData.getPickupLocation());
        txt_name.setVisibility(View.GONE);
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


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service


        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MY_API_KEY;

        Log.d("getDirectionsUrl: ", url);
        return url;
    }

    private String getDirectionsUrl(LatLng origin, LatLng pickup, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // pickup of route
        String str_pickup = "pickup=" + pickup.latitude + "," + pickup.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_pickup + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service


        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MY_API_KEY;

        Log.d("getDirectionsUrl: ", url);
        return url;
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
