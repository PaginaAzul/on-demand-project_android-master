package com.pagin.azul.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pagin.azul.R;
import com.pagin.azul.adapter.SelectTimeAdapter;
import com.pagin.azul.bean.ChatResponse;
import com.pagin.azul.bean.GetLocation;
import com.pagin.azul.bean.OrderResponse;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.NotificationFragment;
import com.pagin.azul.helper.AddressResultReceiver;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.helper.StartGooglePlayServices;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kAppLaunchMode;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class HomeMapActivity extends Fragment implements OnMapReadyCallback {

    private static String address = "";
    protected final int REQ_CODE_GPS_SETTINGS = 150;
    private final int REQ_CODE_LOCATION = 107;
    public Boolean isChecked = false;
    public Boolean isTrue = false;
    public Boolean isOfferChecked = false;
    public Boolean isOfferChecked1 = false;
    boolean doubleBackToExitPressedOnce = false;
    StartGooglePlayServices startGooglePlayServices = new StartGooglePlayServices(getActivity());
    @BindView(R.id.r_layout_main)
    RelativeLayout relativeLayoutMain;
    @BindView(R.id.btnComments)
    ImageView btnComments;
    @BindView(R.id.iv_back)
    ImageView navLeft;
    @BindView(R.id.iv_noti)
    ImageView ivNoti;
    @BindView(R.id.cantainer)
    ConstraintLayout clayoutServicesYou;
    @BindView(R.id.container_you_want)
    ConstraintLayout clayoutYouWant;
    @BindView(R.id.container_select_services)
    ConstraintLayout clayoutSelectServices;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinnerOffer1)
    Spinner spinnerOffer1;
    @BindView(R.id.spinnerOffer)
    Spinner spinnerOffer;
    @BindView(R.id.tv_prof_wkr)
    TextView tv_prof_wkr;
    String deliveryActiveOrder = "";
    String professionalActiveOrder = "";
    @BindView(R.id.tvProfessional)
    TextView tvProfessional;
    @BindView(R.id.tvDelivery)
    TextView tvDelivery;
    @BindView(R.id.textView23)
    TextView textView23;
    String[] spin = {"20", "30", "40", "50", "60", "70", "80", "90", "100"};
    String[] spin1 = {"20", "30", "40", "50", "60", "70", "80", "90", "100"};
    String[] spinOffer = {"20", "30", "40", "50", "60", "70", "80", "90", "100"};
    String[] spinOffer1 = {"20", "30", "40", "50", "60", "70", "80", "90", "100"};
    private GoogleApiClient googleApiClient;
    private String longitudeFromSearch = "";
    private String latitudeFromSearch = "";
    private Double latitudeFromPicker = 0.0;
    private Double longitudeFromPicker = 0.0;
    private String addressFromSearch = "";
    private LocationRequest locationRequest;
    private LatLng currentLocationLatLngs;
    private Location mLastLocation;
    private boolean isHomeSet = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private GPSTracker gpsTracker;
    private GoogleMap mMap;
    private double Lat, Long;
    private ArrayList<LatLng> markerPoints;
    private CopyOnWriteArrayList<String> dataData;
    private SelectTimeAdapter selectTimeAdapter;
    private DrawerLayout mDrawerLayout;
    private String checkNormalUser, checkDeliveryPersonAdminVerify, checkProfessionalWorkerAdminVerify,
            signupWithDeliveryPerson, signupWithProfessionalWorker;
    private String activityCommingType = "";


    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, HomeMapActivity.class);
        intent.putExtra("kData", type);
        return intent;
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name, String totalJobs) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_marker, null);

/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        TextView txt_name = (TextView) marker.findViewById(R.id.tvMarker);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        txt_name.setText("              " + address);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home_map, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            if (getArguments().getString("from").equalsIgnoreCase("Captains")) {
                clayoutSelectServices.setVisibility(View.GONE);
                clayoutServicesYou.setVisibility(View.VISIBLE);


            } else if (getArguments().getString("from").equalsIgnoreCase("DeliveryOffer")) {


            } else {
                clayoutSelectServices.setVisibility(View.GONE);
                clayoutServicesYou.setVisibility(View.VISIBLE);
            }

        } else {
            clayoutSelectServices.setVisibility(View.VISIBLE);
        }

//        //Retrieve the value
//        String value = getArguments().getString("from");
//
//
//
//        if(value.equalsIgnoreCase("Captains")){
//
//            clayoutServicesYou.setVisibility(View.VISIBLE);
//            clayoutYouWant.setVisibility(View.GONE);
//
//        }else {
//            Toast.makeText(getActivity(), "NO", Toast.LENGTH_SHORT).show();
//        }

        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kAppLaunchMode, "true");

//        if (getIntent() != null) {
//            activityCommingType = (String) getIntent().getSerializableExtra("kData");
//        }

        relativeLayoutMain.setVisibility(View.GONE);

        gpsTracker = new GPSTracker(getActivity(), getActivity());
        Lat = gpsTracker.getLatitude();
        Long = gpsTracker.getLongitude();


//        Lat = Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity()).getString(kLat));
//        Long = Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity()).getString(kLong));

        markerPoints = new ArrayList<>();

        if (activityCommingType.equalsIgnoreCase("my_order_deshbord")) {
            clayoutSelectServices.setVisibility(View.VISIBLE);
            clayoutYouWant.setVisibility(View.GONE);
        } else if (activityCommingType.equalsIgnoreCase("deliverydashboard")) {
            clayoutSelectServices.setVisibility(View.VISIBLE);
            clayoutYouWant.setVisibility(View.GONE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.mapview);
//        mapFragment.getMapAsync(this);

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        map.getMapAsync(this);

        // showDialog();
        dataData = new CopyOnWriteArrayList<>();
        ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), R.layout.captain_spinner, R.id.tv_number, spin1);
        spinner1.setAdapter(adapter1);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.captain_spinner, R.id.tv_number, spin);
        spinner.setAdapter(adapter);

        ArrayAdapter Offeradapter = new ArrayAdapter(getActivity(), R.layout.captain_spinner, R.id.tv_number, spinOffer);
        spinnerOffer.setAdapter(Offeradapter);

        ArrayAdapter Offeradapter1 = new ArrayAdapter(getActivity(), R.layout.captain_spinner, R.id.tv_number, spinOffer1);
        spinnerOffer1.setAdapter(Offeradapter1);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isTrue) {
                    deliverySpinApi("ProfessionalWorker", "ProfessionalWorker", position);

                } else {
                    isTrue = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerOffer1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isOfferChecked1) {
                    deliverySpinApi("ProfessionalWorker", "spinnerOffer1", position);
                } else {
                    isOfferChecked1 = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOffer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isOfferChecked) {
                    deliverySpinApi("DeliveryPerson", "spinnerOffer", position);
                } else {
                    isOfferChecked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isChecked) {
                    deliverySpinApi("DeliveryPerson", "DeliveryPerson", position);

                } else {
                    isChecked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (gpsTracker.getLocation() != null)
            getCurrentLocation(gpsTracker);

        prepareData();


        if (isNetworkAvailable(getActivity())) {
            if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase("")) {
                callUpdateLocationApi();
            }
            if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase("")) {
                callCheckUserTypeApi();
            }


            callcheckCurrentOrderApi();
            deliverySpinApi("DeliveryPerson", "DeliveryPerson", 0);

            deliverySpinApi("ProfessionalWorker", "ProfessionalWorker", 0);

        } else {
            //Toast.makeText(getActivity(), "Internet connection lost", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isNetworkAvailable(getActivity())) {
            if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase("")) {
                callcheckCurrentOrderApi();
                //initilizeMap();
            }
        }

        gpsTracker = new GPSTracker(getActivity(), getActivity());
        if (gpsTracker != null) {
            if (gpsTracker.canGetLocation()) {
                Location location = gpsTracker.getLocation();
                if (location != null) {
                    SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
                    SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));


                } else {
                    takeTime();
                }
            } else {
                takeTime();
            }
        } else {
            Toast.makeText(getActivity(), "Can't Fetch Location", Toast.LENGTH_SHORT).show();
        }

    }

    private void initilizeMap() {

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
        map.getMapAsync(this);
    }


    //for popup open after some time
    public void takeTime() {
// take time
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//Do something after 100ms
                showSettingsAlert(1124, getActivity());
            }
        }, 1000);
    }


    //for location alert turn on gps
    public void showSettingsAlert(final int requestCode, final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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

    //GetSetSpinnerdistanceValues...km..///
    private void deliverySpinApi(String spinType, String from, int position) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            // if (!token.isEmpty()) {
            RequestBody profile_body;

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userType", spinType);
            jsonObject.put("lat", Lat);
            jsonObject.put("long", Long);
            jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
            if (spinType.equalsIgnoreCase("DeliveryPerson")) {
                jsonObject.put("distance", Integer.parseInt(spinner.getSelectedItem().toString()));
            } else if (spinType.equalsIgnoreCase("ProfessionalWorker")) {
                jsonObject.put("distance", Integer.parseInt(spinner1.getSelectedItem().toString()));
            } else if (spinType.equalsIgnoreCase("DeliveryPerson") && from.equalsIgnoreCase("spinnerOffer")) {
                jsonObject.put("distance", Integer.parseInt(spinnerOffer.getSelectedItem().toString()));
            } else if (spinType.equalsIgnoreCase("fProfessionalWorker") && from.equalsIgnoreCase("spinnerOffer1")) {
                jsonObject.put("distance", Integer.parseInt(spinnerOffer1.getSelectedItem().toString()));
            }
            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            Call<GetLocation> beanCall = apiInterface.getLocation(token, body);
            beanCall.enqueue(new Callback<GetLocation>() {
                @Override
                public void onResponse(Call<GetLocation> call, Response<GetLocation> response) {
                    MyDialog.getInstance(getActivity()).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                            if (from.equalsIgnoreCase("DeliveryPerson")) {
                                tvDelivery.setText(response.body().getData() + " Captain delivery within ");
                                textView23.setText(spin[position] + " KM");
                            } else if (from.equalsIgnoreCase("ProfessionalWorker")) {
                                tvProfessional.setText(response.body().getData() + " Captain Professional within ");
                                tv_prof_wkr.setText(spin1[position] + " KM");
                            }


                            // Toast.makeText(HomeMapActivity.getActivity(), response.body().getResponse_message(), Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getActivity(), response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<GetLocation> call, Throwable t) {

                }
            });

            //}


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getCurrentLocation(GPSTracker gpsTracker) {

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            //  addressDroup=address;

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//buttomsheet navigation open

    private void prepareData() {
        dataData.add("WithIn 1 hour");
        dataData.add("WithIn 2 hour");
        dataData.add("WithIn 3 hour");
        dataData.add("WithIn 4 hour");
        dataData.add("WithIn 5+ hour");
        dataData.add("WithIn 10 hour");
        dataData.add("WithIn 11 hour");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng current = new LatLng(Lat, Long);

        mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(getActivity(), R.drawable.marker, "25k", "hello"))));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12));


        markerPoints.add(current);

    }

    private void openBottomSheetBanner() {
        View view = getLayoutInflater().inflate(R.layout.custom_buttonsheet_layout, null);
        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheetAnimation);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.rv_bottom_sheet);
        selectTimeAdapter = new SelectTimeAdapter(getActivity(), getResources().getStringArray(R.array.select_time));
        recyclerView.setAdapter(selectTimeAdapter);

        recyclerView.setLayoutManager(linearLayoutManager);
        selectTimeAdapter.notifyDataSetChanged();
    }

    //All Click..
    @OnClick({R.id.tv_select_time, R.id.iv_back, R.id.btn_required, R.id.btn_provide, R.id.btn_prfsnal_offer_near_you, R.id.btn_prfsnal_worker,
            R.id.btn_derlivery_offer_near_you, R.id.btn_derlivery_prsn, R.id.iv_noti})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_time:
                openBottomSheetBanner();
                break;
            case R.id.iv_back:
                Intent intent = new Intent(getActivity(), NavDrawerActivity.class);
                startActivity(intent);
                // mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_required:
                clayoutSelectServices.setVisibility(View.VISIBLE);
                clayoutYouWant.setVisibility(View.GONE);
                break;
            case R.id.btn_provide:
                clayoutServicesYou.setVisibility(View.VISIBLE);
                clayoutYouWant.setVisibility(View.GONE);
                break;
            case R.id.btn_prfsnal_offer_near_you:
                // startActivity(ProvideServiceProffOfferActivity.getIintent(HomeMapActivity.getActivity()));
                if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase("")) {
                    if (signupWithProfessionalWorker != null && !signupWithProfessionalWorker.equalsIgnoreCase("")) {
                        if (signupWithProfessionalWorker.equalsIgnoreCase("true")) {
                            if (checkProfessionalWorkerAdminVerify != null && !checkProfessionalWorkerAdminVerify.equalsIgnoreCase("")) {
                                if (checkProfessionalWorkerAdminVerify.equalsIgnoreCase("true")) {
                                    startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(getActivity(), professionalActiveOrder));
                                } else {
                                    Toast.makeText(getActivity(), "Your request for become Professional worker is under approval.", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                            }
                        } else {
                            startActivity(new Intent(getActivity(), BecomeProfessionalWorkerActivity.class));
                        }
                    }
                } else {
                    startActivity(new Intent(getActivity(), SignUpOptions.class));
                    //showDialogLogin();
                }
                break;
            case R.id.btn_derlivery_offer_near_you:
                //startActivity(HomeOfferDetailsActivity.getIntent(HomeMapActivity.getActivity()));
                if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase("")) {

                    Log.e("GKTEST", "  GOOD   " + checkDeliveryPersonAdminVerify);
                    if (signupWithDeliveryPerson != null && !signupWithDeliveryPerson.equalsIgnoreCase("")) {
                        if (signupWithDeliveryPerson.equalsIgnoreCase("true")) {
                            if (checkDeliveryPersonAdminVerify != null && !checkDeliveryPersonAdminVerify.equalsIgnoreCase("")) {
                                if (checkDeliveryPersonAdminVerify.equalsIgnoreCase("true")) {
                                    startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(getActivity(), deliveryActiveOrder));

                                } else {
                                    Toast.makeText(getActivity(), "Your request for become Delivery Person is under approval.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(new Intent(getActivity(), BecomeDeliveryPersonActivity.class));
                        }
                    }
                } else {
                    startActivity(new Intent(getActivity(), SignUpOptions.class));
                    //showDialogLogin();
                }

                break;
            case R.id.btn_prfsnal_worker:
                if (activityCommingType.equalsIgnoreCase("my_order_deshbord")) {
                    // MapProfessinalWorkerActivity.getIntent(getActivity(),"");
                    startActivity(NUProfessionalWorkerDashboardActivity.getIntent(getActivity(), ""));
                } else {
                    startActivity(MapProfessinalWorkerActivity.getIntent(getActivity(), "btn_prfsnal_worker"));
                }
                break;
            case R.id.btn_derlivery_prsn:
                if (activityCommingType.equalsIgnoreCase("my_order_deshbord")) {
                    //MapProfessinalWorkerActivity.getIntent(getActivity(),"");
                    startActivity(NUDeliveryPersonDashboardActivity.getIntent(getActivity(), ""));
                } else {
                    startActivity(MapProfessinalWorkerActivity.getIntent(getActivity(), "btn_derlivery_prsn"));
                }
                break;
            case R.id.iv_noti:
                if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase("")) {
                    startActivity(NotificationFragment.getIntent(getActivity()));
                } else {
                    showDialog();
                }
                break;

        }
    }

    private void callUpdateLocationApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("latitude", Lat);
                jsonObject.put("longitude", Long);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ChatResponse> beanCall = apiInterface.updateLocation(token, body);

                beanCall.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            ChatResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(HomeMapActivity.getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showDialog() {
        Dialog dialog = new Dialog(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                startActivity(new Intent(getActivity(), SignUpOptions.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void callCheckUserTypeApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.checkUserType(token, body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                //checktype = response.body().getTypeData();

                                checkNormalUser = response.body().getSignupWithNormalPerson();
                                signupWithDeliveryPerson = response.body().getSignupWithDeliveryPerson();
                                signupWithProfessionalWorker = response.body().getSignupWithProfessionalWorker();
                                checkDeliveryPersonAdminVerify = response.body().getAdminVerifyDeliveryPerson();
                                checkProfessionalWorkerAdminVerify = response.body().getAdminVerifyProfessionalWorker();


                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                                //Toast.makeText(MyOrderDashboardActivity.getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(HomeMapActivity.getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserTypeResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callcheckCurrentOrderApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<OrderResponse> beanCall = apiInterface.checkCurrentOrder(token, body);

                beanCall.enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            OrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                deliveryActiveOrder = response.body().getData().getDeliveryActiveOrder();
                                professionalActiveOrder = response.body().getData().getProfessionalActiveOrder();

                                // Toast.makeText(HomeMapActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                //Toast.makeText(HomeMapActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showDialogLogin() {
        Dialog dialog = new Dialog(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                startActivity(new Intent(getActivity(), SignUpOptions.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}
