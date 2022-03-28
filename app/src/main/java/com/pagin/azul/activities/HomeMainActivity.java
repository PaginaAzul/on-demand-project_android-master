package com.pagin.azul.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.HomeCategoryFrag;
import com.pagin.azul.fragment.MyProfileFrag;
import com.pagin.azul.fragment.NormalMyOrderDashboardFragment;
import com.pagin.azul.fragment.NotificationFragment;
import com.pagin.azul.helper.AddressResultReceiver;
import com.pagin.azul.helper.FetchAddressService;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.helper.StartGooglePlayServices;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.LocaleHelper;
import com.pagin.azul.utils.NetworkChangeReceiver;

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;

public class HomeMainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private final static String TAG = HomeMainActivity.class.getSimpleName();
    private NetworkChangeReceiver mNetworkReceiver;
    protected final int REQ_CODE_GPS_SETTINGS = 150;
    private final int REQ_CODE_LOCATION = 107;

    @BindView(R.id.tv_title)
    public TextView tv_title;
    boolean doubleBackToExitPressedOnce = false;
    StartGooglePlayServices startGooglePlayServices = new StartGooglePlayServices(this);
    @BindView(R.id.shop_cons_lay)
    ConstraintLayout shopCon;
    @BindView(R.id.myorder_cons_lay)
    ConstraintLayout myOrderCon;
    @BindView(R.id.noti_cons_lay)
    ConstraintLayout notiCons;
    @BindView(R.id.captain_cons_lay)
    ConstraintLayout captain_const;
    @BindView(R.id.iv_shhop)
    ImageView iv_shhop;
    @BindView(R.id.iv_myOrder)
    ImageView iv_myOrder;
    @BindView(R.id.iv_notitn)
    ImageView iv_notitn;
    @BindView(R.id.iv_captain)
    ImageView iv_captain;
    @BindView(R.id.tvdummy)
    TextView tvdummy;
    @BindView(R.id.tvNotification)
    TextView tvNotification;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.tvMyOrder)
    TextView tvMyOrder;
    @BindView(R.id.tvPrfile)
    TextView tvPrfile;
    private String longitudeFromSearch = "";
    private String latitudeFromSearch = "";
    private boolean isFirstTimeLocation = true;
    private Double latitudeFromPicker = 0.0;
    private Double longitudeFromPicker = 0.0;
    private String addressFromSearch = "";
    private LocationRequest locationRequest;
    private LatLng currentLocationLatLngs;
    private Location mLastLocation;
    private boolean isHomeSet = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private GoogleApiClient googleApiClient;
    private boolean isLocServiceStarted = false;
    private LocationCallback locationCallback;
    private double lat = 0.0, lng = 0.0;
    private double Lat, Long;
    private NormalMyOrderDashboardFragment normalMyOrderDashboardFrag;
    private HomeMapActivity homeMapFrag;
    private HomeCategoryFrag homeCategoryFrag;
    private NotificationFragment notificationFrag;
    private GPSTracker gpsTracker;
    private boolean isFirstTime = false;

    private String commingFrom = "";
    private HashMap<String, Object> stringObjectHashMap;


    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, HomeMainActivity.class);
        intent.putExtra("kData", type);
        return intent;
    }


    public static Intent getIntent(Context context, HashMap<String, Object> stringObjectHashMap) {
        Intent intent = new Intent(context, HomeMainActivity.class);
        intent.putExtra("kMap", stringObjectHashMap);
        return intent;
    }

    //for Internet connnection check...././
    public static boolean isNetworkAvailable(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting()
                        || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.map_home2_layout);
        ButterKnife.bind(this);

        isFirstTime = true;
        // startLocationFunctioning();

        homeMapFrag = new HomeMapActivity();
        homeCategoryFrag = new HomeCategoryFrag();
        normalMyOrderDashboardFrag = new NormalMyOrderDashboardFragment();
        notificationFrag = new NotificationFragment();

        gpsTracker = new GPSTracker(this, this);
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));
//        if (gpsTracker != null) {
//            if (gpsTracker.canGetLocation()) {
//                Location location = gpsTracker.getLocation();
//                if (location != null) {
//                    SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
//                    SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));
//                } else {
//                    takeTime();
//                }
//            } else {
//                takeTime();
//            }
//        } else {
//            Toast.makeText(this, "Can't Fetch Location", Toast.LENGTH_SHORT).show();
//        }

        mNetworkReceiver = new NetworkChangeReceiver();
        mNetworkReceiver.registerCallback(isConnect -> {

            if (isConnect) {
                //reloadFragment();
            }else{
                Toast.makeText(HomeMainActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

        }
        });

//        gpsTracker = new GPSTracker(this, this);
//        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
//        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));

        if (getIntent() != null) {
            if ((String) getIntent().getStringExtra("kData") != null) {
                commingFrom = (String) getIntent().getStringExtra("kData");
                if (commingFrom.equalsIgnoreCase("OrderScreen")) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, normalMyOrderDashboardFrag).commit();
                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    tvNotification.setTextColor(Color.BLACK);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.WHITE);
                    tvPrfile.setTextColor(Color.BLACK);


                }else if (commingFrom.equalsIgnoreCase(MyProfile.class.getSimpleName())) {

                    captain_const.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    tvNotification.setTextColor(Color.BLACK);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.BLACK);
                    tvPrfile.setTextColor(Color.WHITE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyProfileFrag()).commit();

                } else {

                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeMapActivity()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeCategoryFrag).commit();

                    shopCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    tvHome.setTextColor(Color.WHITE);

                }
            } else {
                stringObjectHashMap = (HashMap<String, Object>) getIntent().getSerializableExtra("kMap");
                if (!stringObjectHashMap.get("kFrom").toString().equalsIgnoreCase("DeliveryOffer")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("from", stringObjectHashMap.get("kFromCat").toString());
                    bundle.putString("fromCategory", stringObjectHashMap.get("kFrom").toString());
                    normalMyOrderDashboardFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, normalMyOrderDashboardFrag).commit();

                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    tvNotification.setTextColor(Color.BLACK);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.WHITE);
                    tvPrfile.setTextColor(Color.BLACK);


                } else if (stringObjectHashMap.get("kFrom").toString().equalsIgnoreCase("Professional")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("from", stringObjectHashMap.get("kFromCat").toString());
                    bundle.putString("fromCategory", stringObjectHashMap.get("kFrom").toString());
                    normalMyOrderDashboardFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, normalMyOrderDashboardFrag).commit();

                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    tvNotification.setTextColor(Color.BLACK);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.WHITE);
                    tvPrfile.setTextColor(Color.BLACK);


                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString("from", stringObjectHashMap.get("kFromCat").toString());
                    bundle.putString("fromCategory", stringObjectHashMap.get("kFrom").toString());
                    homeMapFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeMapFrag).commit();

                    captain_const.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));

                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));


                }
            }

        }

    }


    //for popup open after some time
    public void takeTime() {
// take time
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//Do something after 100ms
                showSettingsAlert(1124, HomeMainActivity.this);
            }
        }, 1000);
    }


    //for location alert turn on gps
    public void showSettingsAlert(final int requestCode, final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeMainActivity.this);
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

    }


    //    METHOD: TO START FULL PROCESS FOR FIRST TIME..
    private void startLocationFunctioning() {
        if (!isNetworkAvailable(this)) {
            //Toast.makeText(this.getApplicationContext(), R.string.internat_not_available, Toast.LENGTH_SHORT).show();
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
                .enableAutoManage(this, 1 /* clientId*/, this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();

        createLocationRequest();


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

        mResultReceiver = new AddressResultReceiver(HomeMainActivity.this, tvdummy, null, isFirstTimeLocation);

    }

    // LOCATION SETTINGS SET UP
    public void setUpLocationSettingsTaskStuff() {
        /*Initialize the pending result and LocationSettingsRequest */
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
                        resolvable.startResolutionForResult(HomeMainActivity.this, REQ_CODE_GPS_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        sendEx.printStackTrace();
                    }
                }
            }
        });
    }

    // METHOD TO GET CURRENT LOCATION OF DEVICE
    public void loadCurrentLoc() {

// Marshmallow +
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
// Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                    isLocServiceStarted = false;
                                    Log.w(TAG, "addOnSuccessListener: location: " + null);
// MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
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


        } else // PRE-Marshmallow
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
// Toast.makeText(CreateYourBookClub.this, "Make sure that Location is Enabled on the device.", Toast.LENGTH_LONG).show();

                                    isLocServiceStarted = false;
                                    Log.w(TAG, "addOnSuccessListener: location: " + null);
// MyApplication.makeASnack(CreateYourBookClub.this.binding.getRoot(), getString(R.string.no_location_detected));
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
            SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(mLastLocation.getLatitude()));
            SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(mLastLocation.getLongitude()));

            latitudeFromPicker = mLastLocation.getLatitude();
            longitudeFromPicker = mLastLocation.getLongitude();

//            if (getIntent().getExtras()!=null)
//            {
//                if (getIntent().getExtras().getString("type").equalsIgnoreCase("delivered")
//                        || getIntent().getExtras().getString("type").equalsIgnoreCase("rejected"))
//                {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("type", PreviousFrag.class.getSimpleName());
//// MyOrdersFrag fragment = new MyOrdersFrag();
//                    myOrdersFrag.setArguments(bundle);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myOrdersFrag).commit();
//                    bottomNavigationView.setSelectedItemId(R.id.bottomOrders);
//                }
//                else
//                {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myOrdersFrag).commit();
//                    bottomNavigationView.setSelectedItemId(R.id.bottomOrders);
//                }
//            }
//            else
//            {
//                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new HomeFrag()).commit();
//            }

        }

        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(mLastLocation.getLatitude()));
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(mLastLocation.getLongitude()));

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

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

// ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
// public void onRequestPermissionsResult(int requestCode, String[] permissions,
// int[] grantResults)
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

    @OnClick({R.id.shop_cons_lay, R.id.myorder_cons_lay, R.id.noti_cons_lay, R.id.captain_cons_lay, R.id.iv_back, R.id.clHelp})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.shop_cons_lay:
                shopCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                tvNotification.setTextColor(Color.BLACK);
                tvHome.setTextColor(Color.WHITE);
                tvMyOrder.setTextColor(Color.BLACK);
                tvPrfile.setTextColor(Color.BLACK);

                //tv_title.setVisibility(View.GONE);

                //getSupportFragmentManager().beginTransaction().replace(R.id.container, homeMapFrag).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeCategoryFrag).commit();
                break;

            case R.id.myorder_cons_lay:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText(R.string.nav_my_order);

                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    tvNotification.setTextColor(Color.BLACK);
                    captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    tvNotification.setTextColor(Color.BLACK);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.WHITE);
                    tvPrfile.setTextColor(Color.BLACK);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, normalMyOrderDashboardFrag).commit();
                } else {
                    showDialog();
                }

                break;

            case R.id.noti_cons_lay:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {

                    notiCons.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    tvNotification.setTextColor(Color.WHITE);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.BLACK);
                    tvPrfile.setTextColor(Color.BLACK);
                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    captain_const.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    tv_title.setText(R.string.notification);
                    tv_title.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, notificationFrag).commit();
                } else {
                    showDialog();
                }


                break;
            case R.id.captain_cons_lay:

                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    captain_const.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    tvNotification.setTextColor(Color.BLACK);
                    tvHome.setTextColor(Color.BLACK);
                    tvMyOrder.setTextColor(Color.BLACK);
                    tvPrfile.setTextColor(Color.WHITE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyProfileFrag()).commit();
                } else {
                    showDialog();
                }

                /*tv_title.setText("See Offers for You");
                tv_title.setVisibility(View.VISIBLE);
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    captain_const.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    shopCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));

                    notiCons.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    //                //Put the value
                    HomeMapActivity homefgt = new HomeMapActivity();
                    Bundle args = new Bundle();
                    args.putString("from", "Captains");
                    homefgt.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homefgt).commit();
                } else {
                    showDialog();
                }*/

                break;

            case R.id.iv_back:
                Intent intent = new Intent(HomeMainActivity.this, NavDrawerActivity.class);
                startActivity(intent);
                break;

            case R.id.clHelp:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase(""))
                    startActivity(new Intent(this, ContactActivity.class));
                else
                    CommonUtilities.showDialog(this);
                break;

        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(HomeMainActivity.this);
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
                startActivity(new Intent(HomeMainActivity.this, SignUpOptions.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w(TAG, "onConnected");

        setUpLocationSettingsTaskStuff(); // On Connected
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


    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient != null) {
            startLocationUpdates();
            googleApiClient.connect();
        }

        registerNetworkBroadCast();

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
 unregisterNetworkChanges();

        if (googleApiClient != null)
            googleApiClient.disconnect();
    }



    private void registerNetworkBroadCast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


//    private void reloadFragment(){
//        shopCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
//        myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
//        iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
//        captain_const.setBackgroundColor(getResources().getColor(R.color.white));
//        iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
//        notiCons.setBackgroundColor(getResources().getColor(R.color.white));
//        iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
//
//        tv_title.setVisibility(View.GONE);
//
//        //getSupportFragmentManager().beginTransaction().replace(R.id.container, homeMapFrag).commit();
//    }


//    @Override
//    public void onBackPressed() {
//       // super.onBackPressed();
//
//        shopCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
//        myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
//        iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
//        captain_const.setBackgroundColor(getResources().getColor(R.color.white));
//        iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
//        notiCons.setBackgroundColor(getResources().getColor(R.color.white));
//        iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
//
//        tv_title.setVisibility(View.GONE);
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeMapFrag).commit();
//
//    }




    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        shopCon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        iv_shhop.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        myOrderCon.setBackgroundColor(getResources().getColor(R.color.white));
        iv_myOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        captain_const.setBackgroundColor(getResources().getColor(R.color.white));
        iv_captain.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        notiCons.setBackgroundColor(getResources().getColor(R.color.white));
        iv_notitn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        tvNotification.setTextColor(Color.BLACK);
        tvHome.setTextColor(Color.WHITE);
        tvMyOrder.setTextColor(Color.BLACK);
        tvPrfile.setTextColor(Color.BLACK);

        //tv_title.setVisibility(View.GONE);

        //getSupportFragmentManager().beginTransaction().replace(R.id.container, homeMapFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeCategoryFrag).commit();
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLanguage();
    }*/

    private void setLanguage() {
        // check language from data base
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                LocaleHelper.setLocale(this,"en");
            }else {
                LocaleHelper.setLocale(this, "pt");
            }
        }else {
            LocaleHelper.setLocale(this,"pt");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
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
