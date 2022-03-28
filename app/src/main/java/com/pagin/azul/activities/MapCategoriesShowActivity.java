package com.pagin.azul.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.pagin.azul.R;
import com.pagin.azul.adapter.AutoTextComplete;
import com.pagin.azul.adapter.MapCategoriesAdapter;
import com.pagin.azul.bean.CateResponse;
import com.pagin.azul.bean.CategoriesResultInner;
import com.pagin.azul.bean.CategoriesResultResponse;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.utils.InstantAutoComplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pagin.azul.Constant.Constants.kToken;

public class MapCategoriesShowActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
//    private String Api_keya = "AIzaSyBL4ngANDBnJLusG09x2t7mkGwi_mX1SWo";
    private String Api_key = "AIzaSyDbeg8Dh2fnyHpmMcuL2PtUPN9kqvQFDdY";
    private GoogleMap mMap;
    private ArrayList<CateResponse> categoryList;
    private MapCategoriesAdapter categoriesAdapter;
    private CategoriesResultResponse resultResponse;
    private ArrayList<CategoriesResultInner> categoryListResponse;
    private ArrayList<CategoriesResultInner> categoryListFilter;
    @BindView(R.id.rv_option)
    RecyclerView rv_option;
    @BindView(R.id.edt_search)
    InstantAutoComplete edt_search;
    @BindView(R.id.btn_list_view)
    ImageView btn_list_view;
    @BindView(R.id.btnBackBottom)
    ImageView btnBackBottom;

    private AutoTextComplete autoTextCompleteAdapter;
    ArrayList<LatLng> markerPoints;
    private GPSTracker gpsTracke;
    private double cLat, cLong;
    private String selectCategory;


    public static Intent getIntent(Context context, String selectCategory) {
        Intent intent = new Intent(context, MapCategoriesShowActivity.class);
        intent.putExtra("kCategory", (Serializable) selectCategory);
        return intent;
    }

//    public static Intent getIntent(Context context, String from, String currentLoc) {
//        Intent intent = new Intent(context, MapCategoriesShowActivity.class);
//        intent.putExtra("kCategory", (Serializable) from);
//        intent.putExtra("Locat", (Serializable) currentLoc);
//        return intent;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_categories_show);
        ButterKnife.bind(this);

        edt_search.setText("");
        markerPoints = new ArrayList<>();
        if (getIntent() != null) {
            selectCategory = (String) getIntent().getStringExtra("kCategory");
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        GPSTracker gpsTracker = new GPSTracker(this, this);
        cLat = gpsTracker.getLatitude();
        cLong = gpsTracker.getLongitude();

        //https://maps.googleapis.com/maps/api/place/textsearch/json?query=type=bar&location=28.6196272,77.3634781&radius=100&key=AIzaSyBL4ngANDBnJLusG09x2t7mkGwi_mX1SWo

////CategoryAdapterSetup...//
        categoryList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapCategoriesShowActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_option.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new MapCategoriesAdapter(this, categoryList);
        rv_option.setAdapter(categoriesAdapter);


        preParedData();
        makeLocationUrl(selectCategory);
        findCategory();


    }


    private void autoSugestdata() {

        autoTextCompleteAdapter = new AutoTextComplete(MapCategoriesShowActivity.this, categoryListResponse, 0);
        edt_search.setAdapter(autoTextCompleteAdapter);
        edt_search.setThreshold(1);
        autoTextCompleteAdapter.notifyDataSetChanged();
        edt_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                filterList(autoTextCompleteAdapter.getIDList().get(position));

            }
        });

    }

    private void filterList(String name) {
        categoryListFilter = new ArrayList<>();

        for (int i = 0; i < categoryListResponse.size(); i++) {
            if (categoryListResponse.get(i).getName().equalsIgnoreCase(name)) {
                categoryListFilter.add(categoryListResponse.get(i));

            }
        }
        categoryListResponse = categoryListFilter;
        prepareMap(categoryListResponse);

    }

    private void prepareMap(ArrayList<CategoriesResultInner> categoryListResponse) {
        mMap.clear();
        if (categoryListResponse != null) {
            for (int i = 0; i < categoryListResponse.size(); i++) {
                createMarker(categoryListResponse.get(i).getGeometryData().getLocation().getLat(), categoryListResponse.get(i).getGeometryData().getLocation().getLng(), categoryListResponse.get(i).getName(), categoryListResponse.get(i).getFormatted_address(), i);
                LatLng current = new LatLng(categoryListResponse.get(i).getGeometryData().getLocation().getLat(), categoryListResponse.get(i).getGeometryData().getLocation().getLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12));
                autoSugestdata();
            }
        }
    }


    private void findCategory() {
        categoriesAdapter.setListener(new MapCategoriesAdapter.SelectCategoryInterface() {
            @Override
            public void onCategoryClick(String name) {
                if (name.equalsIgnoreCase("Shopping Centers")) {
                    name = "ShoppingCenters";
                    makeLocationUrl(name);
                    selectCategory = name;
                } else {
                    makeLocationUrl(name);
                    selectCategory = name;
                }
            }
        });
    }

    private void preParedData() {
        categoryList.add(new CateResponse("All", R.drawable._all));
        categoryList.add(new CateResponse("Restaurants", R.drawable._restaurant));
        categoryList.add(new CateResponse("Supermarket", R.drawable._supermarket));
        categoryList.add(new CateResponse("Pharmacy", R.drawable._pharmacy));
        categoryList.add(new CateResponse("Coffee", R.drawable._coffee));
        categoryList.add(new CateResponse("Sweets", R.drawable._sweets));
        categoryList.add(new CateResponse("Gas", R.drawable._gas));
        categoryList.add(new CateResponse("Shopping Centers", R.drawable._shopping_centers));
        categoriesAdapter.notifyDataSetChanged();
    }

    // https://maps.googleapis.com/maps/api/place/textsearch/json?query=type=bar&location=28.6196272,77.3634781&radius=100&key=AIzaSyBL4ngANDBnJLusG09x2t7mkGwi_mX1SWo

    private String makeLocationUrl(String name) {
        String location = "&location=" + cLat + "," + cLong;
        String type = name;
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=type=" + type + location + "&radius=100&key=" + Api_key;

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        return url;


    }

    @OnClick({R.id.btnBackBottom, R.id.btn_list_view, R.id.iv_back, R.id.iv_noti})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackBottom:
                onBackPressed();
                break;
            case R.id.btn_list_view:
                startActivityForResult(MapCategoryListActivity.getIntent(this, selectCategory), 111);
                break;

            case R.id.iv_back:
                Intent intent = new Intent(MapCategoriesShowActivity.this, NavDrawerActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_noti:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    //startActivity(NotificationFragment.getIntent(this));
                } else {
                    showDialogLogin();
                }
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (mMap == null) {
            return;
        }

        GPSTracker gpsTracker = new GPSTracker(this, this);
        cLat = gpsTracker.getLatitude();
        cLong = gpsTracker.getLongitude();
        LatLng current = new LatLng(cLat, cLong);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12));


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

            mMap.clear();
            resultResponse = new Gson().fromJson(result, CategoriesResultResponse.class);
            if (resultResponse != null) {
                for (int i = 0; i < resultResponse.getResults().size(); i++) {
                    categoryListResponse = new ArrayList<>();
                    categoryListResponse = resultResponse.getResults();

                    createMarker(resultResponse.getResults().get(i).getGeometryData().getLocation().getLat(), resultResponse.getResults().get(i).getGeometryData().getLocation().getLng(), resultResponse
                            .getResults().get(i).getName(), resultResponse.getResults().get(i).getFormatted_address(), i);


                    LatLng current = new LatLng(resultResponse.getResults().get(i).getGeometryData().getLocation().getLat(), resultResponse.getResults().get(i).getGeometryData().getLocation().getLng());

//                MarkerOptions Curernt = new MarkerOptions();
//                Curernt.icon(BitmapDescriptorFactory.fromResource((R.drawable.drop_s)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12));
                    autoSugestdata();

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            int position = (int) marker.getRotation();

                            String lat = String.valueOf(resultResponse.getResults().get(position).getGeometryData().getLocation().getLat());
                            String lng = String.valueOf(resultResponse.getResults().get(position).getGeometryData().getLocation().getLng());
                            Intent intent = new Intent();
                            intent.putExtra("ADDRESS", resultResponse.getResults().get(position).getName()+", "+resultResponse.getResults().get(position).getFormatted_address());
                            intent.putExtra("LAT", lat);
                            intent.putExtra("LONG", lng);
                            setResult(RESULT_OK, intent);
                            finish();

                        }
                    });

                }
            }
        }
    }


    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int position) {


        float pos = position;


        return mMap.addMarker(new MarkerOptions()

                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_s)).rotation(pos));

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra("ADDRESS", data.getStringExtra("ADDRESS"));
                intent.putExtra("LAT", data.getStringExtra("LAT"));
                intent.putExtra("LONG", data.getStringExtra("LONG"));
                setResult(RESULT_OK, intent);
                finish();

            }
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
        textView.setText("You are not logged in. Please login/signup before proceeding further.");
        registerNow.setText("OK");
        notNow.setText("Cancel");

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapCategoriesShowActivity.this, SignUpOptions.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
