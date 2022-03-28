package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.AddressPickerAct;
import com.pagin.azul.activities.ContactActivity;
import com.pagin.azul.activities.MyProfile;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.activities.SearchCategoryAct;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.MyProfileFrag;
import com.pagin.azul.helper.AddressResultReceiver;
import com.pagin.azul.helper.FetchAddressService;
import com.pagin.azul.helper.StartGooglePlayServices;
import com.pagin.azul.onphasesecond.fragments.HomeFrag;
import com.pagin.azul.onphasesecond.fragments.MyOrdersFrag;
import com.pagin.azul.onphasesecond.fragments.SearchFrag;
import com.pagin.azul.onphasesecond.model.GetCartCountModel;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.LocationClass;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.onphasesecond.utilty.SwitchFragment;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Objects;

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

public class FoodAndGroceryActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = FoodAndGroceryActivity.class.getSimpleName();
    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.edtSearchPlace)
    TextView edtSearchPlace;

    @BindView(R.id.tvHome)
    TextView tvHome;

    @BindView(R.id.tvMyOrder)
    TextView tvMyOrder;

    @BindView(R.id.tvSearch)
    TextView tvSearch;

    @BindView(R.id.tvPrfile)
    TextView tvPrfile;

    @BindView(R.id.ivFilter)
    ImageView ivFilter;

    @BindView(R.id.ivArrowDown)
    ImageView ivArrowDown;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.shop_cons_lay)
    ConstraintLayout shopCon;

    @BindView(R.id.clSearch)
    ConstraintLayout clSearch;

    @BindView(R.id.clMyOrders)
    ConstraintLayout clMyOrders;

    @BindView(R.id.captain_cons_lay)
    ConstraintLayout captain_cons_lay;

    @BindView(R.id.iv_shhop)
    ImageView iv_shhop;

    @BindView(R.id.ivSearch)
    ImageView ivSearch;

    @BindView(R.id.ivMyOrder)
    ImageView ivMyOrder;

    @BindView(R.id.iv_captain)
    ImageView iv_captain;

    @BindView(R.id.cartLayout)
    ConstraintLayout cartLayout;

    @BindView(R.id.countTv)
    TextView countTv;

    private HomeFrag homeFrag;
    private SearchFrag searchFrag;
    private MyOrdersFrag myOrdersFrag;
    private MyProfileFrag myProfile;
    private MenuItem menuItem;
    private String token;
    private boolean isLocServiceStarted = false;
    private LocationCallback locationCallback;
    private double lat = 0.0, lng = 0.0;
    private double Lat, Long;
    private boolean isFirstTimeLocation = true;
    private Double latitudeFromPicker = 0.0;
    private Double longitudeFromPicker = 0.0;
    private String addressFromSearch = "";
    private LocationRequest locationRequest;
    private LatLng currentLocationLatLngs;
    private Location mLastLocation;
    boolean doubleBackToExitPressedOnce = false;
    private boolean isHomeSet = false;
    protected final int REQ_CODE_GPS_SETTINGS = 150;
    private final int REQ_CODE_LOCATION = 107;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private LocationClass locationClass;
    StartGooglePlayServices startGooglePlayServices = new StartGooglePlayServices(this);
    private boolean isFirstTime = false;
    private GoogleApiClient googleApiClient;
    private CommonListener commonListener;
    private String Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_home);
        ButterKnife.bind(this);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        isFirstTime = true;
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        setToolbar();
        initFrag();
        startLocationFunctioning();

        cartLayout.setVisibility(View.VISIBLE);

        //setBottomNavigationAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCartCount();
    }

    @OnClick({R.id.ivFilter, R.id.edtSearchPlace, R.id.shop_cons_lay, R.id.clSearch, R.id.clMyOrders, R.id.captain_cons_lay, R.id.cartLayout, R.id.clHelp})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFilter:
                //dispatchFilterAct();
                break;
            case R.id.edtSearchPlace:
                startActivityForResult(AddressPickerAct.getIntent(this, FoodAndGroceryActivity.class.getSimpleName(),
                        edtSearchPlace.getText().toString()), 100);
                break;
            case R.id.shop_cons_lay:
                reloadFragment(HomeFrag.class.getSimpleName());
                break;
            case R.id.clSearch:
                reloadFragment(SearchFrag.class.getSimpleName());
                /*if (token != null && !token.equals(""))
                    reloadFragment(SearchFrag.class.getSimpleName());
                else
                    CommonUtilities.showDialog(this);*/
                break;
            case R.id.clMyOrders:
                if (token != null && !token.equals(""))
                    reloadFragment(MyOrdersFrag.class.getSimpleName());
                else
                    CommonUtilities.showDialog(this);
                break;
            case R.id.captain_cons_lay:
                if (token != null && !token.equals(""))
                    reloadFragment(MyProfileFrag.class.getSimpleName());
                else
                    CommonUtilities.showDialog(this);
                break;
            case R.id.cartLayout:
                dispatchAddToCart();
                break;
            case R.id.clHelp:
                if (token != null && !token.equals(""))
                    startActivity(new Intent(this, ContactActivity.class));
                else
                    CommonUtilities.showDialog(this);
                break;
        }
    }

    private void dispatchAddToCart() {
        if (token != null && !token.equals("")) {
            Intent intent;
            if (Type.equalsIgnoreCase(ParamEnum.PRODUCT.theValue()))
                intent = new Intent(this, ScheduleMyCart.class);
            else
                intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        } else {
            CommonUtilities.showDialog(this);
        }
    }

    private void dispatchFilterAct() {
        if (commonListener != null)
            commonListener.onFilterClick();
    }

    private void initFrag() {
        homeFrag = new HomeFrag();
        searchFrag = new SearchFrag();
        myOrdersFrag = new MyOrdersFrag();
        myProfile = new MyProfileFrag();
        if (isFirstTimeLocation == false)
            reloadFragment(HomeFrag.class.getSimpleName());
        //SwitchFragment.changeFragment(getSupportFragmentManager(), homeFrag, true, false);
    }

    private void setToolbar() {
        tvTitle.setVisibility(View.GONE);
        edtSearchPlace.setVisibility(View.VISIBLE);
        ivArrowDown.setVisibility(View.VISIBLE);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       *//* getMenuInflater().inflate(R.menu.menu_cart, menu);
        menuItem = menu.findItem(R.id.ivCart);
        return true;*//*
        return false;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ivCart) {
            if (token != null && !token.equals("")) {
                Intent intent = new Intent(this, MyCartActivity.class);
                startActivity(intent);
            } else {
                CommonUtilities.showDialog(this);
            }
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadFragment(String simpleName) {
        if (simpleName.equals(HomeFrag.class.getSimpleName())) {
            shopCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            clSearch.setBackgroundColor(getResources().getColor(R.color.white));
            ivSearch.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            captain_cons_lay.setBackgroundColor(getResources().getColor(R.color.white));
            iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            clMyOrders.setBackgroundColor(getResources().getColor(R.color.white));
            ivMyOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            tvMyOrder.setTextColor(Color.BLACK);
            tvHome.setTextColor(Color.WHITE);
            tvSearch.setTextColor(Color.BLACK);
            tvPrfile.setTextColor(Color.BLACK);
            SwitchFragment.changeFragment(getSupportFragmentManager(), homeFrag, true, false);
        } else if (simpleName.equals(SearchFrag.class.getSimpleName())) {
            shopCon.setBackgroundColor(getResources().getColor(R.color.white));
            iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            clMyOrders.setBackgroundColor(getResources().getColor(R.color.white));
            ivMyOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            tvMyOrder.setTextColor(Color.BLACK);
            captain_cons_lay.setBackgroundColor(getResources().getColor(R.color.white));
            iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            clSearch.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ivSearch.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            tvHome.setTextColor(Color.BLACK);
            tvSearch.setTextColor(Color.WHITE);
            tvPrfile.setTextColor(Color.BLACK);
            SwitchFragment.changeFragment(getSupportFragmentManager(), new SearchFrag(), true, true);
        } else if (simpleName.equals(MyOrdersFrag.class.getSimpleName())) {
            clMyOrders.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ivMyOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            tvMyOrder.setTextColor(Color.WHITE);
            tvHome.setTextColor(Color.BLACK);
            tvSearch.setTextColor(Color.BLACK);
            tvPrfile.setTextColor(Color.BLACK);
            shopCon.setBackgroundColor(getResources().getColor(R.color.white));
            iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            clSearch.setBackgroundColor(getResources().getColor(R.color.white));
            ivSearch.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            captain_cons_lay.setBackgroundColor(getResources().getColor(R.color.white));
            iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            SwitchFragment.changeFragment(getSupportFragmentManager(), myOrdersFrag, true, true);
        } else {
            captain_cons_lay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            shopCon.setBackgroundColor(getResources().getColor(R.color.white));
            iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            clSearch.setBackgroundColor(getResources().getColor(R.color.white));
            ivSearch.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            clMyOrders.setBackgroundColor(getResources().getColor(R.color.white));
            ivMyOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            tvMyOrder.setTextColor(Color.BLACK);
            tvHome.setTextColor(Color.BLACK);
            tvSearch.setTextColor(Color.BLACK);
            tvPrfile.setTextColor(Color.WHITE);
            SwitchFragment.changeFragment(getSupportFragmentManager(), myProfile, true, true);
        }
    }

    private void setBottomNavigationAction() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottomHome:
                        SwitchFragment.changeFragment(getSupportFragmentManager(), homeFrag, true, true);
                        return true;
                    case R.id.bottomSearch:
                        SwitchFragment.changeFragment(getSupportFragmentManager(), searchFrag, true, true);
                        return true;
                    case R.id.bottomOrders:
                        SwitchFragment.changeFragment(getSupportFragmentManager(), myOrdersFrag, true, true);
                        return true;
                    case R.id.bottomMyProfile:
                        SwitchFragment.changeFragment(getSupportFragmentManager(), myProfile, true, true);
                        return true;
                }
                return false;
            }
        });
    }

    public void setMainToolbar(String title, int searchVisibility, int filterVisibility, int titleVisibility, boolean isVisible) {
        edtSearchPlace.setVisibility(searchVisibility);
        ivArrowDown.setVisibility(searchVisibility);
        //ivFilter.setVisibility(filterVisibility);
        tvTitle.setVisibility(titleVisibility);
        tvTitle.setText(title);
        if (menuItem != null)
            menuItem.setVisible(isVisible);
    }

    @Override
    public void onBackStackChanged() {
        int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        String stackEntryName = getSupportFragmentManager().getBackStackEntryAt(entryCount - 1).getName();
        if (stackEntryName != null) {
            if (stackEntryName.equals(HomeFrag.class.getName())) {
                setMainToolbar("", View.VISIBLE, View.GONE, View.GONE, true);
            } else if (stackEntryName.equals(SearchFrag.class.getName())) {
                setMainToolbar(getString(R.string.search), View.GONE, View.VISIBLE, View.VISIBLE, true);
            } else if (stackEntryName.equals(MyOrdersFrag.class.getName())) {
                setMainToolbar(getString(R.string.my_orders), View.GONE, View.GONE, View.VISIBLE, true);
            } else if (stackEntryName.equals(MyProfileFrag.class.getName())) {
                setMainToolbar(getString(R.string.my_profile), View.GONE, View.GONE, View.VISIBLE, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        /*int entryCount = getSupportFragmentManager().getBackStackEntryCount();
        String stackEntryName = getSupportFragmentManager().getBackStackEntryAt(entryCount-1).getName();
        if (stackEntryName != null) {
            if(stackEntryName.equalsIgnoreCase(HomeFrag.class.getName()))
                finish();
            else
                SwitchFragment.changeFragment(getSupportFragmentManager(), homeFrag, true, true);
        }*/
        //getSupportFragmentManager().popBackStack(HomeFrag.class.getName(),0);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w(TAG, "onConnected");

        setUpLocationSettingsTaskStuff();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended: " + i);
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //   METHOD: TO  Create an instance of the GoogleAPIClient AND LocationRequest
    private void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 1 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();

        createLocationRequest();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient != null) {
            startLocationUpdates();
            googleApiClient.connect();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null) {

            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
                googleApiClient.disconnect();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterNetworkChanges();
        isFirstTimeLocation = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterNetworkChanges();

        if (googleApiClient != null)
            googleApiClient.disconnect();
    }

    //Method: To create location request and set its priorities
    public void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // CREATE A FUSED LOCATION CLIENT PROVIDER OBJECT
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //      Initialize AddressResultReceiver class object
        mResultReceiver = new AddressResultReceiver(FoodAndGroceryActivity.this, edtSearchPlace, null, isFirstTimeLocation);

    }

    // LOCATION SETTINGS SET UP
    public void setUpLocationSettingsTaskStuff() {
        /* Initialize the pending result and LocationSettingsRequest */
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.

                // GPS is already enabled no need of review_dialog
                Log.w(TAG, "onResult: Success 1");

                loadCurrentLoc(); //GET CURRENT LOCATION
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a review_dialog.
                    Log.w(TAG, "REQ_CODE_GPS_SETTINGS: REQ " + 0);

                    try {
                        // Show the review_dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        Log.w(TAG, "REQ_CODE_GPS_SETTINGS: REQ " + 1);
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(FoodAndGroceryActivity.this, REQ_CODE_GPS_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        sendEx.printStackTrace();
                    }
                }
            }
        });
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

    private void stopLocationUpdates() {
        boolean b = mFusedLocationClient != null;
        if (b && locationCallback != null)
            mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    //    METHOD TO GET CURRENT LOCATION OF DEVICE
    public void loadCurrentLoc() {

        //      Marshmallow +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    startGooglePlayServices.showDenyRationaleDialog(this, "You need to allow access to Device Location", new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case BUTTON_POSITIVE:
                                    dialog.dismiss();
                                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQ_CODE_LOCATION);

                                    break;
                                case BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    finish();
                                    break;
                            }

                        }
                    });

                    return;
                }

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_CODE_LOCATION);

                return;
            }


            /*DO THE LOCATION STUFF*/

            try {

                mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object

                                    Log.w(TAG, "addOnSuccessListener: location: " + location);

                                    locationCallBack(location);


                                } else {
//                                    Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                    isLocServiceStarted = false;
                                    Log.w(TAG, "addOnSuccessListener: location: " + null);
//                                    MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
                                }
                            }
                        });


                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            if (location != null) {
                                Log.w(TAG, "LocationCallback:" + location);

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

            /*DO THE LOCATION STUFF*/

            try {

                mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object

                                    Log.w(TAG, "addOnSuccessListener: location: " + location);

                                    locationCallBack(location);


                                } else {
//                                    Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                    isLocServiceStarted = false;
                                    Log.w(TAG, "addOnSuccessListener: location: " + null);
//                                    MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
                                }
                            }
                        });


                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                            if (location != null) {
                                Log.w(TAG, "LocationCallback:" + location);

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

    public void locationCallBack(Location location) {

        mLastLocation = location;

        if (isFirstTimeLocation) {

            latitudeFromPicker = mLastLocation.getLatitude();
            longitudeFromPicker = mLastLocation.getLongitude();

            SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(latitudeFromPicker));
            SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(longitudeFromPicker));

            if (getIntent().getExtras() != null) {
                String simpleName = MyProfile.class.getSimpleName();
                if (Objects.requireNonNull(getIntent().getStringExtra(ParamEnum.TYPE.theValue())).equalsIgnoreCase(simpleName))
                    reloadFragment(simpleName);
            } else
                reloadFragment(HomeFrag.class.getSimpleName());
        }
        Lat = mLastLocation.getLatitude();
        Long = mLastLocation.getLongitude();

        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(FetchAddressService.FIND_BY, FetchAddressService.FIND_BY_LOCATION);
        intent.putExtra(FetchAddressService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressService.LOCATION, mLastLocation);
        this.startService(intent);
        isLocServiceStarted = true;

        currentLocationLatLngs = new LatLng(lat, lng);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && resultCode == Activity.RESULT_OK) {
            String address = data.getStringExtra("ADDRESS");
            String lat = data.getStringExtra("LAT");
            String lon = data.getStringExtra("LONG");
            SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, lat);
            SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, lon);
            edtSearchPlace.setText(address);
            //homeFrag.serviceGetRestaurantAndStoreData(lat,lon);
            homeFrag.serviceGetExclusiveOfferList(lat, lon, true);
        }
    }

    public void setOnFilterClickListener(CommonListener commonListener) {
        this.commonListener = commonListener;
    }


    private void getCartCount() {
        try {
            //MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                String token = SharedPreferenceWriter.getInstance(FoodAndGroceryActivity.this).getString(kToken);
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<GetCartCountModel> beanCall = apiInterface.getCartCount(token, body);

                beanCall.enqueue(new Callback<GetCartCountModel>() {
                    @Override
                    public void onResponse(Call<GetCartCountModel> call, Response<GetCartCountModel> response) {
                        //MyDialog.getInstance(FoodAndGroceryActivity.this).hideDialog();
                        if (response.isSuccessful()) {

                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getData() <= 0) {
                                    countTv.setVisibility(View.GONE);
                                } else {
                                    countTv.setVisibility(View.VISIBLE);
                                    countTv.setText(response.body().getData() + "");
                                }

                                Type = response.body().getType();


                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(FoodAndGroceryActivity.this);
                            } else {
                                Toast.makeText(FoodAndGroceryActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCartCountModel> call, Throwable t) {
                    }
                });
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
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