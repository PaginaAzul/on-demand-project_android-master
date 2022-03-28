package com.pagin.azul.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pagin.azul.R;
import com.pagin.azul.adapter.AutoTextComplete;
import com.pagin.azul.adapter.MapCategoriesAdapter;
import com.pagin.azul.adapter.MapCategoryListAdapter;
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

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;

public class MapCategoryListActivity extends AppCompatActivity {
    private String FApi_keyT = "AIzaSyBL4ngANDBnJLusG09x2t7mkGwi_mX1SWo";
    private String Api_key = "AIzaSyDbeg8Dh2fnyHpmMcuL2PtUPN9kqvQFDdY";

    @BindView(R.id.rv_option)
    RecyclerView rv_option;

    @BindView(R.id.edt_search)
    InstantAutoComplete edt_search;

    @BindView(R.id.rv_option_show)
    RecyclerView rv_option_show;
    private AutoTextComplete autoTextComplete;

    private ArrayList<CateResponse> categoryList;
    private MapCategoriesAdapter categoriesAdapter;
    private CategoriesResultResponse resultResponse;
    private MapCategoryListAdapter categoryListAdapter;
    private ArrayList<CategoriesResultInner> categoryListResponse;
    private ArrayList<CategoriesResultInner> categoryListFilter;
    private AutoTextComplete autoTextCompleteAdapter;
    private String selectedCategory;
    private double cLat, cLong;

    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, MapCategoryListActivity.class);
        intent.putExtra("kType", (Serializable) type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_category_list);
        ButterKnife.bind(this);

        edt_search.setText("");
        if (getIntent() != null) {
            selectedCategory = (String) getIntent().getStringExtra("kType");
        }

        GPSTracker gpsTracker = new GPSTracker(this, this);
        cLat = gpsTracker.getLatitude();
        cLong = gpsTracker.getLongitude();

        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));


        categoryList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapCategoryListActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_option.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new MapCategoriesAdapter(this, categoryList);
        rv_option.setAdapter(categoriesAdapter);

        preParedData();
        makeLocationUrl(selectedCategory);
        findCategory();


    }

    private void autoSugestdata() {

        autoTextCompleteAdapter = new AutoTextComplete(MapCategoryListActivity.this, categoryListResponse, 0);
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

        if (categoryListResponse != null) {
            for (int i = 0; i < categoryListResponse.size(); i++) {

                rv_option_show.setLayoutManager(new LinearLayoutManager(MapCategoryListActivity.this));
                categoryListAdapter = new MapCategoryListAdapter(MapCategoryListActivity.this, categoryListResponse);
                rv_option_show.setAdapter(categoryListAdapter);
                categoryListAdapter.notifyDataSetChanged();

                autoSugestdata();
                getitemClickData();

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
                    selectedCategory = name;
                } else {
                    makeLocationUrl(name);
                    selectedCategory = name;
                }

            }

        });
    }

    private void getitemClickData() {

        categoryListAdapter.setListener(new MapCategoryListAdapter.SelectCategoryInterface() {
            @Override
            public void onItemClick(CategoriesResultInner getList) {
                String lat = String.valueOf(getList.getGeometryData().getLocation().getLat());
                String lng = String.valueOf(getList.getGeometryData().getLocation().getLng());
                Intent intent = new Intent();
                intent.putExtra("ADDRESS",getList.getName()+", "+getList.getFormatted_address());
                intent.putExtra("LAT", lat);
                intent.putExtra("LONG", lng);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @OnClick({R.id.btnBackBottom, R.id.btnlistView,R.id.iv_back,R.id.iv_noti})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackBottom:
                onBackPressed();
                break;
            case R.id.btnlistView:
                startActivityForResult(MapCategoriesShowActivity.getIntent(this, selectedCategory), 111);
                break;


            case R.id.iv_back:
                Intent intent = new Intent(MapCategoryListActivity.this, NavDrawerActivity.class);
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

    private String makeLocationUrl(String name) {
        String location = "&location=" + cLat + "," + cLong;
        String type = name;
        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=type=" + type + location + "&radius=100&key=" + Api_key;

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        return url;


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


            resultResponse = new Gson().fromJson(result, CategoriesResultResponse.class);
            if (resultResponse != null) {
                for (int i = 0; i < resultResponse.getResults().size(); i++) {

                    categoryListResponse = new ArrayList<>();
                    categoryListResponse = resultResponse.getResults();
                    rv_option_show.setLayoutManager(new LinearLayoutManager(MapCategoryListActivity.this));
                    categoryListAdapter = new MapCategoryListAdapter(MapCategoryListActivity.this, categoryListResponse);
                    rv_option_show.setAdapter(categoryListAdapter);

                    autoSugestdata();
                    getitemClickData();
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


    private void autoSugestdata1() {


        ArrayAdapter<CategoriesResultInner> adapter = new ArrayAdapter<CategoriesResultInner>(
                this, android.R.layout.simple_dropdown_item_1line, categoryListResponse);

        edt_search.setThreshold(1);
        edt_search.setAdapter(adapter);
        edt_search.setTextColor(Color.RED);

        edt_search.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            CategoriesResultInner selected = (CategoriesResultInner) arg0.getAdapter().getItem(arg2);
            Toast.makeText(MapCategoryListActivity.this,
                    "Clicked " + arg2 + " name: " + selected.getName(),
                    Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 111){
            if(resultCode == Activity.RESULT_OK){
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
                startActivity(new Intent(MapCategoryListActivity.this, SignUpOptions.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    }


