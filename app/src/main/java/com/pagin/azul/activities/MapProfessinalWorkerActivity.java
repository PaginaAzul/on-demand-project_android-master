package com.pagin.azul.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.AddWorkImagesAdapter;
import com.pagin.azul.adapter.AddressListAdapter;
import com.pagin.azul.adapter.MapCategoriesList;
import com.pagin.azul.adapter.PlaceAutocompleteAdapter;
import com.pagin.azul.adapter.SelectTimeAdapter;
import com.pagin.azul.adapter.ThreeLevelListAdapter;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.bean.CateResponse;
import com.pagin.azul.bean.CategoryResponse;
import com.pagin.azul.bean.CommonModel;
import com.pagin.azul.bean.CommonResponse;
import com.pagin.azul.bean.GetAddressList;
import com.pagin.azul.bean.GetLocation;
import com.pagin.azul.bean.MyProfileResponse;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.bean.OrderDetailResponse;
import com.pagin.azul.bean.RequestOrderResponse;
import com.pagin.azul.bean.SelectTimeBean;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.bean.ViewAllOfferResponse;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.NotificationFragment;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.helper.MyInterface;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.NLevelItem;
import com.pagin.azul.utils.TakeImage;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kCategoryId;
import static com.pagin.azul.Constant.Constants.kCategoryName;
import static com.pagin.azul.Constant.Constants.kCategoryOption;
import static com.pagin.azul.Constant.Constants.kEnglish;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kOrderId;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;


public class MapProfessinalWorkerActivity extends AppCompatActivity implements SelectTimeAdapter.CallBack, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, MyInterface, AddressListAdapter.Listener, CommonListener {
    private final int CAMERA_PIC_REQUEST = 7469, REQ_CODE_PICK_IMAGE = 8260;
    static String address = "";
    private static String addressPickUp = "";
    private static String addressDroup = "";
    //private static LatLng latLngDest = new LatLng(19.076090, 72.877426);
    protected GoogleApiClient mGoogleApiClient;
    private Dialog dialogAcceptOffer;
    List<NLevelItem> list;
    @BindView(R.id.rv_options)
    RecyclerView rv_options;
    @BindView(R.id.rvAddImages)
    RecyclerView rvAddImages;
    @BindView(R.id.rl_option)
    RelativeLayout rl_option;
    double pLat, pLong, dLat, dLong;
    CopyOnWriteArrayList<String> dataData;
    @BindView(R.id.rlMainDown)
    ConstraintLayout relativeLayoutDown;
    @BindView(R.id.rlMainSelectService)
    RelativeLayout rlMainSelectService;
    //    @BindView(R.id.btn_go_order)
//    ImageView ivRequestOrder;
    @BindView(R.id.btn_go_order)
    Button ivRequestOrder;
    boolean isFirstTime = false;
    SharedPreferences sharedpreferences;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    Geocoder geocoder;
    List<Address> addresses;
    @BindView(R.id.imgPickuptimeIcon)
    ImageView imgPickuptimeIcon;
    @BindView(R.id.imageView9)
    ImageView imageView9;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.edtDestinationLocation)
    TextView edtDestinationLocation;
    @BindView(R.id.edtPickupLocation)
    TextView edtPickUpLocation;
    @BindView(R.id.imgDownLocationIcon)
    ImageView ivDroupLocationIcon;
    @BindView(R.id.ivReqImage)
    ImageView ivReqImage;
    @BindView(R.id.imgDroptimeIcon)
    ImageView ivDroupTimeIcon;
    @BindView(R.id.edtDownLocation)
    TextView autoDestLocation;
    @BindView(R.id.tvReqImage)
    TextView tvReqImage;
    @BindView(R.id.tv_prof_wkr)
    TextView tv_prof_wkr;
    @BindView(R.id.tvProfessional)
    TextView tvProfessional;
    @BindView(R.id.edt_addDetails)
    EditText edtAddDetails;
    @BindView(R.id.rlNewOrder)
    RelativeLayout rlNewOrder;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    ArrayList<LatLng> markerPoints;
    /**
     * The Parent Group Names.
     */
    String[] parent = new String[]{"Movies", "Games"}; // comment this when uncomment bottom
    /**
     * The Movies Genre List.
     */
    // We have two  main category. (third one is left for example addition)
    String[] movies = new String[]{"Horror", "Action", "Thriller/Drama"};
    /**
     * The Games Genre List.
     */
    String[] games = new String[]{"Fps", "Moba", "Rpg", "Racing"};
    /**
     * The Horror movie list.
     */
    // movies category has further genres
    String[] horror = new String[]{"Conjuring", "Insidious", "The Ring"};
    /**
     * The Action Movies List.
     */
    String[] action = new String[]{"Jon Wick", "Die Hard", "Fast 7", "Avengers"};
    /**
     * The Thriller Movies List.
     */
    String[] thriller = new String[]{"Imitation Game", "Tinker, Tailer, Soldier, Spy", "Inception", "Manchester by the Sea"};
    /**
     * The Fps games.
     */
    // games category has further genres
    String[] fps = new String[]{"CS: GO", "Team Fortress 2", "Overwatch", "Battlefield 1", "Halo II", "Warframe"};
    /**
     * The Moba games.
     */
    String[] moba = new String[]{"Dota 2", "League of Legends", "Smite", "Strife", "Heroes of the Storm"};
    /**
     * The Rpg games.
     */
    String[] rpg = new String[]{"Witcher III", "Skyrim", "Warcraft", "Mass Effect II", "Diablo", "Dark Souls", "Last of Us"};
    /**
     * The Racing games.
     */
    String[] racing = new String[]{"NFS: Most Wanted", "Forza Motorsport 3", "EA: F1 2016", "Project Cars"};
    /**
     * Datastructure for Third level movies.
     */
    LinkedHashMap<String, String[]> thirdLevelMovies = new LinkedHashMap<>();
    /**
     * Datastructure for Third level games.
     */
    LinkedHashMap<String, String[]> thirdLevelGames = new LinkedHashMap<>();
    /**
     * The Second level.
     */
    List<String[]> secondLevel = new ArrayList<>();
    /**
     * The Data.
     */
    String[] spin1 = {"20", "30", "40", "50", "60", "70", "80", "90", "100"};

    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String MY_API_KEY = "AIzaSyDbeg8Dh2fnyHpmMcuL2PtUPN9kqvQFDdY";
    private String[] reason = {"Select Reason", "Not suitable for me", "Other reason of cancellation"};
    private ArrayList<NormalUserPendingOrderInner> viewOfferList;
    private Dialog dialog;
    private RecyclerView rv_add_list;
    private String orderID;
    private String subCategoryName;
    private String categoryName;
    private String serviceCategoryId;
    private String serviceSubCategoryId;
    private String portugueseCategoryName="";
    private String portugueseSubCategoryName="";
    private AddressListAdapter addressListAdapter;
    private List<AddressList> addressesList;
    private ArrayList<CateResponse> categoryList;
    private MapCategoriesList categoriesAdapter;
    private AddressList addressItem;
    private String fromLocation = "";
    private Dialog cancelDialog;
    private ExpandableListView expandableListView;
    private SelectTimeAdapter selectTimeAdapter;
    private String type = "";
    private String commingType = "";
    private CategoryResponse categoryResponse;
    private CommonResponse[] commonResponses;
    private CategoryResponse categorySubResponse;
    private CategoryResponse categorySubSubResponse;
    private String lat1;
    private String lat2;
    private String deslat1;
    private String deslat2;
    private String address1 = "";
    private GPSTracker gpsTracker;
    private LatLng latlong;
    private String imagePath;
    public Boolean isTrue = false;
    /**
     * The Serials Genre List.
     */
    // String[] serials = new String[]{"Crime", "Family", "Comedy"};
    private LatLng latlong_;
    private LatLng latlongDest_;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private PlaceAutocompleteAdapter placeAutocompleteAdapterDropDown;
    private GoogleMap mMap;
    private Marker marker;
    private Dialog offerHoldTimeDialog;
    private ArrayList<File> docList;
    private AddWorkImagesAdapter addWorkImagesAdapter;
    private ArrayList<SelectTimeBean> selectTimeList;
    // serials genre list
    /*String[] crime = new String[]{"CSI: MIAMI", "X-Files", "True Detective", "Sherlock (BBC)", "Fargo", "Person of Interest"};
    String[] family = new String[]{"Andy Griffith", "Full House", "The Fresh Prince of Bel-Air", "Modern Family", "Friends"};
    String[] comedy = new String[]{"Family Guy", "Simpsons", "The Big Bang Theory", "The Office"};
*/
    //address picker
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = places -> {
        if (!places.getStatus().isSuccess()) {
            // Request did not complete successfully
            places.release();
            return;
        }
        // Get the Place object from the buffer.
        final Place place = places.get(0);
        latlong = place.getLatLng();
        pLat = latlong.latitude;
        pLong = latlong.longitude;

        latlong_ = latlong;
        places.release();
    };
    //address picker
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallbackDroup
            = places -> {
        if (!places.getStatus().isSuccess()) {
            // Request did not complete successfully
            places.release();
            return;
        }
        // Get the Place object from the buffer.
        final Place place = places.get(0);
        /*latLngDest = place.getLatLng();
        latlongDest_ = latLngDest;
        dLat = latlongDest_.latitude;
        dLong = latlongDest_.longitude;*/
        places.release();

    };

    /**
     * Datastructure for Third level Serials.
     */
    // LinkedHashMap<String, String[]> thirdLevelSerials = new LinkedHashMap<>();
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence upUp = item.getPrimaryText(null);
            addressPickUp = item.getFullText(null).toString();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            markerAtDest();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListenerDroup = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = placeAutocompleteAdapterDropDown.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            addressDroup = item.getFullText(null).toString();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackDroup);
            ivDroupLocationIcon.setImageResource(R.drawable.drop_s);
            ivDroupTimeIcon.setImageResource(R.drawable.time_s);
            markerAtDest();

        }
    };

    //    @BindView(R.id.iv_backDelivery)
//    ImageView btnBack;
    private Handler handler;
    //  BROADCAST RECEIVER CLASS Object : TO tap
    private BroadcastReceiver pushNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                Bundle bundle = intent.getExtras();

                String type = bundle.getString("offerAvailable");

                if (type != null) {
                    if(type.equalsIgnoreCase("orderCancelFromDelivery")){
                        getOfferListApi("");
                    }else if(type.equalsIgnoreCase("offerAvailableProfessional")){
                        getOfferListApi("");
                    }else if(type.equalsIgnoreCase("offerAvailable")){
                        getOfferListApi("");
                    }else if(type.equalsIgnoreCase("orderCancelFromProfessional")) {
                        getOfferListApi("");
                    }
                }

            }

            Log.w(DeliveryMakeOfferActivity.class.getSimpleName(), "offerAvailable");
        }
    };

    public static Intent getSubCategoryIntent(Context context, String type, String subCategoryName,
                                              String categoryName,String serviceCategoryId,String serviceSubCategoryId,String portugueseCategoryName,
                                              String portugueseSubCategoryName) {
        Intent intent = new Intent(context, MapProfessinalWorkerActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("subCategoryName", subCategoryName);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("serviceCategoryId", serviceCategoryId);
        intent.putExtra("serviceSubCategoryId", serviceSubCategoryId);
        intent.putExtra("portugueseCategoryName", portugueseCategoryName);
        intent.putExtra("portugueseSubCategoryName", portugueseSubCategoryName);
        return intent;
    }

    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, MapProfessinalWorkerActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    public static Intent getIntent(Context context, String type, String orderID) {
        Intent intent = new Intent(context, MapProfessinalWorkerActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("orderID", orderID);
        return intent;
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String
            _name, String totalJobs) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_marker, null);

/*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*/
        TextView txt_name = (TextView) marker.findViewById(R.id.tvMarker);
        //  TextView Jobs=(TextView)marker.findViewById(R.id.tv_jobMarker);
        //txt_name.setText("              " + addressPickUp);
        txt_name.setText(address);
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

    /*public static Bitmap createCustomMarkerDest(Context context,
                                                @DrawableRes int resource, String _name, String totalJobs) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_marker_dest, null);
*//*CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
markerImage.setImageResource(resource);*//*
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latLngDest.latitude, latLngDest.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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
        //txt_name.setText("                " + addressDroup);
        txt_name.setText(address);
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
    }*/

    private void markerAtDest() {
        GPSTracker gpsTracker = new GPSTracker(MapProfessinalWorkerActivity.this, MapProfessinalWorkerActivity.this);
        /*if (latLngDest != null && latlong == null) {
            getRoute(latLngDest.latitude, latLngDest.longitude, gpsTracker.getLatitude(), gpsTracker.getLongitude());

        } else if (latLngDest != null && latlong != null) {
            getRoute(latLngDest.latitude, latLngDest.longitude, latlong.latitude, latlong.longitude);

        } else if (latLngDest == null && latlong != null) {
            getRoute(gpsTracker.getLatitude(), gpsTracker.getLongitude(), latlong.latitude, latlong.longitude);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_professinal_worker);
        ButterKnife.bind(this);
        markerPoints = new ArrayList<>();
        setupUI(rlNewOrder);

        tvTitle.setText(R.string.new_order);
        tvProfessional.setText("0 "+getString(R.string.service_provider_within));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        String[] selectTime = getResources().getStringArray(R.array.select_time);
        selectTimeList = new ArrayList<>();
        for(int i=0;i<selectTime.length;i++){
            SelectTimeBean selectTimeBean = new SelectTimeBean();
            selectTimeBean.setTime(selectTime[i]);
            selectTimeBean.setSelected(false);
            selectTimeList.add(selectTimeBean);
        }

        if (getIntent() != null) {
            type = (String) getIntent().getSerializableExtra("type");
            if (type.equalsIgnoreCase("btn_prfsnal_worker")) {
                relativeLayoutDown.setVisibility(View.GONE);
                //rlMainSelectService.setVisibility(View.VISIBLE);
                rl_option.setVisibility(View.GONE);
                //ivRequestOrder.setText("Go Order(Yallah!)");
                ivRequestOrder.setText(R.string.place_order);
                // ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.request_btn));
                subCategoryName = getIntent().getStringExtra("subCategoryName");
                categoryName = getIntent().getStringExtra("categoryName");
                serviceCategoryId = getIntent().getStringExtra("serviceCategoryId");
                serviceSubCategoryId = getIntent().getStringExtra("serviceSubCategoryId");
                portugueseCategoryName = getIntent().getStringExtra("portugueseCategoryName");
                portugueseSubCategoryName = getIntent().getStringExtra("portugueseSubCategoryName");

                ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.captain_spinner, R.id.tv_number, spin1);
                spinner1.setAdapter(adapter1);

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

            } else if (type.equalsIgnoreCase("orderCancelFromDelivery")) {
                orderID = getIntent().getStringExtra("orderID");
                useHandlerforApi("Delivery");
                offerHoldTime("Delivery");

            } else if (type.equalsIgnoreCase("orderCancelFromProfessional")) {
                orderID = getIntent().getStringExtra("orderID");
                useHandlerforApi("Professional");
                offerHoldTime("Professional");
            } else {
                ivRequestOrder.setText("Request Service(Yallah!)");
                //ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.btn));
                relativeLayoutDown.setVisibility(View.VISIBLE);
                rlMainSelectService.setVisibility(View.GONE);
                rl_option.setVisibility(View.VISIBLE);
            }
        }
//        ...>>>>>>>>> ...........


        //CategoryAdapterSetup...//

        categoryList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_options.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new MapCategoriesList(this, categoryList);
        rv_options.setAdapter(categoriesAdapter);


        //getPickUpAddress();
        // getDroupLocation();

        //category calling api
        // getCategory();
        //orderCategoryList();

        // getDroupLocation();

        //listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<NLevelItem>();
        dataData = new CopyOnWriteArrayList<>();
        prepareData();


        gpsTracker = new GPSTracker(MapProfessinalWorkerActivity.this, MapProfessinalWorkerActivity.this);

        getCurrentLocation(gpsTracker);

        pLat = gpsTracker.getLatitude();
        pLong = gpsTracker.getLongitude();
        //autoFillForm();

        addressApiHit();

        preParedData();


        //testUIHandler();

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        docList = new ArrayList<>();
        rvAddImages.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        addWorkImagesAdapter = new AddWorkImagesAdapter(this,docList,this);
        rvAddImages.setAdapter(addWorkImagesAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
            addressApiHit();
        }
        //      REGISTER LocalBroadcastManager TO HANDLE PUSH
        registerReceiver(pushNotifyReceiver,
                new IntentFilter("Tap Successful"));
    }

    private void autoFillForm() {
        SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);

        isFirstTime = sharedpreferences.getBoolean("kIsFirstTime", true); // getting String
        if (isFirstTime) {

        } else {
            callGetOrderApi();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String aa = SharedPreferenceWriter.getInstance(this).getString(kCategoryOption);

        if (!aa.isEmpty()) {
            edtDestinationLocation.setText(aa);
            //  dialog.dismiss();
        } else {
            edtDestinationLocation.setText("Select Service");
        }


    }

    private void getCurrentLocation(GPSTracker gpsTracker) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            addressPickUp = address.toString();
            //  addressDroup=address;

            addressPickUp = address.toString();
            addressDroup = address;
            //edtPickUpLocation.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void getPickUpAddress() {
        //auto pick address
        autoDestLocation.setOnItemClickListener(mAutocompleteClickListenerDroup);
        //edtPickUpLocation.setOnItemClickListener(mAutocompleteClickListener);
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(MapProfessinalWorkerActivity.this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        placeAutocompleteAdapterDropDown = new PlaceAutocompleteAdapter(MapProfessinalWorkerActivity.this, mGoogleApiClient, null);
        autoDestLocation.setAdapter(placeAutocompleteAdapterDropDown);
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(MapProfessinalWorkerActivity.this, mGoogleApiClient, null);
        //edtPickUpLocation.setAdapter(placeAutocompleteAdapter);
    }*/

    @OnClick({R.id.rlMainSelectService, R.id.iv_back, R.id.tv_select_time, R.id.btn_go_order,
            R.id.btnComments, R.id.edtPickupLocation, R.id.imageView6, R.id.imageView9,
            R.id.imgDroptimeIcon, R.id.imgPickuptimeIcon, R.id.iv_noti, R.id.edtDownLocation, R.id.edtDestinationLocation,R.id.ivAddImage})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtDestinationLocation:
            case R.id.rlMainSelectService:
                //getCategory();
                orderCategoryList();
                showDialog();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_select_time:
                openBottomSheetBanner();
                break;
            case R.id.btn_go_order:
                if (getIntent() != null) {
                    type = (String) getIntent().getSerializableExtra("type");
                    if (type.equalsIgnoreCase("btn_prfsnal_worker")) {
                        if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                            if (!validation1())
                                return;
                            showDialogProfInstruction();
                            //professionalWorkeOrderApi();
                        } else {
                            //showDialogLogin();
                            startActivity(new Intent(MapProfessinalWorkerActivity.this, SignUpOptions.class));
                        }

                        /*if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                            professionalWorkeOrderApi();
                        } else {
                            startActivity(new Intent(MapProfessinalWorkerActivity.this, SignUpOptions.class));
                        }*/

                    } else {
                        if (!validation()) {
                            return;
                        }
                        if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                            callRequestOrderApi();
                        } else {
                            startActivity(new Intent(MapProfessinalWorkerActivity.this, SignUpOptions.class));
                        }
                    }
                }
                SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
                isFirstTime = sharedpreferences.getBoolean("kIsFirstTime", true); // getting String
                if (isFirstTime) {
                    //startActivity(SignUpOptions.getIntent(this));
                    //finish();
                } else {
                    commingType = sharedpreferences.getString("commingType", null);
                    if (commingType.equalsIgnoreCase("SignUp")) {
                        // dialogTermsCondition(commingType);
                        // SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("commingType", "SignUp");
                        editor.commit();
                        //callGetOrderApi();
                    } else {
                        // SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("commingType", "SignIn");
                        editor.commit();
                        //callGetOrderApi();
                    }
                }
                // startActivity(SignUpOptions.getIntent(this));
                // dialogTermsCondition();
                break;
            case R.id.btnComments:
                onBackPressed();
                break;


            case R.id.edtPickupLocation:
            case R.id.imgPickuptimeIcon:
                //if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                //startActivityForResult(AddressPickerAct.getIntent(this, "PICKUP", edtPickUpLocation.getText().toString()), 100);
                startActivityForResult(AddressPickerAct.getIntent(this, "PICKUP", address), 100);
                //
                break;
            case R.id.imageView9:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    addressApiHit();
                    if (addressesList != null && addressesList.size() > 0) {
                        showDialogAddressList();
                    } else {
                        Toast.makeText(this, getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                    }
                    fromLocation = "pickUpLocation";
                } else {
                    showDialogLogin();
                }
                break;
            case R.id.imgDroptimeIcon:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
//                    if (autoDestLocation.getText().toString().equalsIgnoreCase("")) {
//                        Toast.makeText(this, "Please enter drop address to save", Toast.LENGTH_SHORT).show();
//                    } else {

                    if (!autoDestLocation.getText().toString().equalsIgnoreCase("")) {
                        startActivityForResult(AddressPickerAct.getIntent(this, "DROPOFF", autoDestLocation.getText().toString()), 101);
                    } else {
                        startActivityForResult(AddressPickerAct.getIntent(this, "DROPOFF", address), 101);
                    }
                    // }
                } else {
                    showDialogLogin();
                }
                break;
            case R.id.imageView6:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    addressApiHit();
                    if (addressesList != null && addressesList.size() > 0) {
                        showDialogAddressList();
                    } else {
                        Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
                    }
                    fromLocation = "dropLocation";
                } else {
                    showDialogLogin();
                }
                break;

            case R.id.iv_noti:
                if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                    startActivity(NotificationFragment.getIntent(this));
                } else {
                    showDialogLogin();
                }
                break;

            case R.id.edtDownLocation:
                //if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
                if (!autoDestLocation.getText().toString().equalsIgnoreCase("")) {
                    startActivityForResult(AddressPickerAct.getIntent(this, "DROPOFF", autoDestLocation.getText().toString()), 101);
                } else {
                    startActivityForResult(AddressPickerAct.getIntent(this, "DROPOFF", address), 101);
                }
//                } else {
//                    showDialogLogin();
//                }
                break;

            case R.id.ivAddImage:
                selectImage();
                break;
        }
    }

    private void showDialog() {
        //Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.service_main);
        EditText header = (EditText) dialog.findViewById(R.id.edt_search);
        expandableListView = (ExpandableListView) dialog.findViewById(R.id.expandible_listview);
        // second level category names (genres)
        secondLevel.add(movies);
        secondLevel.add(games);
        // secondLevel.add(serials);
        // movies category all data
        thirdLevelMovies.put(movies[0], horror);
        thirdLevelMovies.put(movies[1], action);
        thirdLevelMovies.put(movies[2], thriller);
        // games category all data
        thirdLevelGames.put(games[0], fps);
        thirdLevelGames.put(games[1], moba);
        thirdLevelGames.put(games[2], rpg);
        thirdLevelGames.put(games[3], racing);
        // serials category all data
      /*  thirdLevelSerials.put(serials[0], crime);
        thirdLevelSerials.put(serials[1], family);
        thirdLevelSerials.put(serials[2], comedy);
*/
        // all data
        data.add(thirdLevelMovies);
        data.add(thirdLevelGames);
        //data.add(thirdLevelSerials);
        // expandable listview
        // parent adapter
        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(this, commonResponses, this);
        // set adapter
        expandableListView.setAdapter(threeLevelListAdapterAdapter);
/*
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        categoryResponse.getData().get(groupPosition).getId() + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });*/
        // OPTIONAL : Show one list at a time
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
                SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).writeStringValue(kCategoryId, commonResponses[groupPosition].get_id());
                //getSubCategory(categoryResponse.getData().get(groupPosition).getId());
                /*Toast.makeText(getApplicationContext(),
                        categoryResponse.getData().get(groupPosition).getId() + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });
        dialog.show();
    }

    private void prepareData() {
        dataData.add("Within 1 hour");
        dataData.add("Within 2 hour");
        dataData.add("Within 3 hour");
        dataData.add("Within 4 hour");
        dataData.add("Within 5+ hour");
        dataData.add("Within 1 day");
        dataData.add("Within 2 days");
        dataData.add("Within 3 days");
    }

    private void openBottomSheetBanner() {
        View view = getLayoutInflater().inflate(R.layout.custom_buttonsheet_layout, null);
        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheetAnimation);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.rv_bottom_sheet);
        /*String[] selectTime = getResources().getStringArray(R.array.select_time);
        ArrayList<SelectTimeBean> selectTimeList = new ArrayList<>();
        for(int i=0;i<selectTime.length;i++){
            SelectTimeBean selectTimeBean = new SelectTimeBean();
            selectTimeBean.setTime(selectTime[i]);
            selectTimeBean.setSelected(false);
            selectTimeList.add(selectTimeBean);
        }*/
        selectTimeAdapter = new SelectTimeAdapter(this, selectTimeList);
        recyclerView.setAdapter(selectTimeAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setHorizontalFadingEdgeEnabled(true);
        //   selectTimeAdapter.notifyDataSetChanged();
        TextView textView = view.findViewById(R.id.tv_order);
        selectTimeAdapter.onClickAtAdapter(adapterPosition -> {
            //tvSelectTime.setText(dataData.get(adapterPosition));
            //tvSelectTime.setText(selectTime[adapterPosition]);
            tvSelectTime.setText(selectTimeList.get(adapterPosition).getTime());
        });
        if (type.equalsIgnoreCase("btn_prfsnal_worker")) {
            textView.setText(R.string.i_want_my_service);
        } else {
            textView.setText("I want My Order");
        }
        view.findViewById(R.id.ok_txt).setOnClickListener(view1 -> mBottomSheetDialog.dismiss());
    }

    private void dialogTermsCondition(String commingType) {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_professional_terms_condition_layout);
        //TextView header = (TextView) dialog.findViewById(R.id.header);
        TextView btnOK = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView btnCancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        if (commingType.equalsIgnoreCase("SignUp")) {
            dialog.findViewById(R.id.checkbox).setVisibility(View.VISIBLE);
        } else {
            dialog.findViewById(R.id.checkbox).setVisibility(View.GONE);
        }
        //header.setText(R.string.leaveorg_title);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialogSubmitSuccess();

                updateUserIdApi();
                useHandlerforApi("Professional");
                offerHoldTime("Professional");


                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showDialogInstruction() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_instraction);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            dialog.dismiss();
            if (type.equalsIgnoreCase("btn_prfsnal_worker")) {
                updateUserIdApi();
                startActivity(RequireProfessionalWorkerActivity.getIntent(MapProfessinalWorkerActivity.this));
            } else {
                updateUserIdApi();
                useHandlerforApi("Delivery");
                offerHoldTime("Delivery");

                //startActivity(OfferScreenActivity.getIntent(MapProfessinalWorkerActivity.this, ""));
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showDialogProfInstruction() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_professional_instraction);
        dialog.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            professionalWorkeOrderApi(dialog);
            /*updateUserIdApi();
            useHandlerforApi("Professional");
            offerHoldTime("Professional");
            dialog.dismiss();*/
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        try{
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //For Professional Order Submit//
    private void dialogSubmitSuccess() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_submit_successfully_layout);
        TextView btnOK = (TextView) dialog.findViewById(R.id.tv_ok);
        btnOK.setOnClickListener(view -> {
            updateUserIdApi();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("kFromCat", "Professional");
            hashMap.put("kFrom", "Pending");

            startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));


//            if (type.equalsIgnoreCase("btn_prfsnal_worker")) {
//                updateUserIdApi();
//                startActivity(NUProfessionalWorkerDashboardActivity.getIntent(MapProfessinalWorkerActivity.this, ""));
//            } else {
//                updateUserIdApi();
//                startActivity(NUDeliveryPersonDashboardActivity.getIntent(MapProfessinalWorkerActivity.this, ""));
//            }
////                    RequireDeliveryPersonDashboardActivity,RequireProfessionalWorkerActivity
            dialog.dismiss();
        });
        dialog.show();

    }

    @Override
    public void onClickAtPosition(int adapterPosition) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (cancelDialog != null) {
            cancelDialog.dismiss();
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((this));
            mGoogleApiClient.disconnect();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("Check", "onDestroy");

        //      UNREGISTER LocalBroadcastManager
//        if(pushNotifyReceiver != null) {
//            unregisterReceiver(pushNotifyReceiver);
//        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap == null) {
            return;
        }
        getRoute(gpsTracker.getLatitude(), this.gpsTracker.getLongitude(), gpsTracker.getLatitude(), gpsTracker.getLongitude());

    }

    private void getRoute(double latitudeCurrent, double longitudeCurrent,
                          double latitudeDest, double longitudeDest) {
        markerPoints.clear();
        mMap.clear();
        LatLng current = new LatLng(latitudeCurrent, longitudeCurrent);
        LatLng desinetion = new LatLng(latitudeDest, longitudeDest);
      /*  mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())));
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapProfessinalWorkerActivity.this,R.drawable.marker, "25k","hello"))));
        marker.showInfoWindow();*/
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 12));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 12));
        // Creating MarkerOptions
        markerPoints.add(current);
        markerPoints.add(desinetion);
        MarkerOptions Curernt = new MarkerOptions();
        MarkerOptions Dest = new MarkerOptions();
        // Setting the position of the marker
        Curernt.position(current);
        Dest.position(desinetion);
        if (markerPoints.size() == 2) {
            Curernt.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapProfessinalWorkerActivity.this, R.drawable.marker, "25k", "hello")));
            //Dest.icon(BitmapDescriptorFactory.fromBitmap(createCustomMarkerDest(MapProfessinalWorkerActivity.this, R.drawable.marker, "25k", "hello")));
            mMap.addMarker(Curernt);
            //mMap.addMarker(Dest);
        }
        // Add new marker to the Google Map Android API V2
        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            String url = getDirectionsUrl(current, desinetion);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
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

    @Override
    public void onCategory(String text) {
        edtDestinationLocation.setText(text);
        dialog.dismiss();
    }

    @Override
    public void itemClick(AddressList addressItem) {
        this.addressItem = addressItem;
        if (fromLocation.equalsIgnoreCase("pickUpLocation")) {
            edtPickUpLocation.setText(addressItem.getAddress());
            pLat = Double.valueOf(addressItem.getLat());
            pLong = Double.valueOf(addressItem.getLongs());
        } else {
            autoDestLocation.setText(addressItem.getAddress());
            dLat = Double.valueOf(addressItem.getLat());
            dLong = Double.valueOf(addressItem.getLongs());
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

    public boolean validation() {
        boolean isValide = true;
        if (edtPickUpLocation.getText().toString().trim().isEmpty()) {
            isValide = false;
            Toast.makeText(getApplicationContext(), R.string.please_enter_pckup_location, Toast.LENGTH_SHORT).show();

        } /*else if (autoDestLocation.getText().toString().trim().isEmpty()) {
            isValide = false;
            Toast.makeText(getApplicationContext(), "Please enter DropOff Location", Toast.LENGTH_SHORT).show();

        }*/ else if (tvSelectTime.getText().toString().trim().isEmpty()) {
            isValide = false;
            Toast.makeText(getApplicationContext(), R.string.please_select_time, Toast.LENGTH_SHORT).show();

        } else if (edtAddDetails.getText().toString().trim().isEmpty()) {
            isValide = false;
            Toast.makeText(getApplicationContext(), R.string.please_enter_details_of_orders, Toast.LENGTH_SHORT).show();
        }
        return isValide;
    }

    public boolean validation1() {
        boolean isValide = true;
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(kEnglish)){
            if (edtPickUpLocation.getText().toString().trim().isEmpty()) {
                isValide = false;
                Toast.makeText(getApplicationContext(), "Please enter PickUp Location", Toast.LENGTH_SHORT).show();

            } /*else if (edtDestinationLocation.getText().toString().trim().equalsIgnoreCase("Select Service")) {
            isValide = false;
            Toast.makeText(getApplicationContext(), "Please select service", Toast.LENGTH_SHORT).show();

        }*/ else if (tvSelectTime.getText().toString().trim().isEmpty()) {
                isValide = false;
                Toast.makeText(getApplicationContext(), "Please select Time", Toast.LENGTH_SHORT).show();
            } else if (edtAddDetails.getText().toString().trim().isEmpty()) {
                isValide = false;
                Toast.makeText(getApplicationContext(), "Please enter details of order", Toast.LENGTH_SHORT).show();
            }/*else if (imagePath==null || imagePath.isEmpty()) {
            isValide = false;
            Toast.makeText(getApplicationContext(), "Please add image", Toast.LENGTH_SHORT).show();
        }*/
        }else{
            if (edtPickUpLocation.getText().toString().trim().isEmpty()) {
                isValide = false;
                Toast.makeText(getApplicationContext(), "Ponto de partida", Toast.LENGTH_SHORT).show();

            } /*else if (edtDestinationLocation.getText().toString().trim().equalsIgnoreCase("Select Service")) {
            isValide = false;
            Toast.makeText(getApplicationContext(), "Please select service", Toast.LENGTH_SHORT).show();

        }*/ else if (tvSelectTime.getText().toString().trim().isEmpty()) {
                isValide = false;
                Toast.makeText(getApplicationContext(), "Por favor selecione Hora", Toast.LENGTH_SHORT).show();
            } else if (edtAddDetails.getText().toString().trim().isEmpty()) {
                isValide = false;
                Toast.makeText(getApplicationContext(), "Digite os detalhes do pedido", Toast.LENGTH_SHORT).show();
            }/*else if (imagePath==null || imagePath.isEmpty()) {
            isValide = false;
            Toast.makeText(getApplicationContext(), "Please add image", Toast.LENGTH_SHORT).show();
        }*/
        }

        return isValide;
    }

    // Fetches data from url passed

    //api calling
    private void getCategory() {
        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
        MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
        ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
        Call<CategoryResponse> call = apiInterface.getCategory();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                        if (response.body() != null) {
                            categoryResponse = response.body();
                            getSubCategory(categoryResponse.getData().get(0).getId());
                        }
                    } else {
                        Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapProfessinalWorkerActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                t.printStackTrace();
            }
        });
    }

    //api calling
    private void orderCategoryList() {
        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
        MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
        ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
        Call<CommonModel> call = apiInterface.orderCategoryList();
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                if (response.isSuccessful()) {
                    CommonModel body = response.body();
                    if (body.getStatus().equalsIgnoreCase("SUCCESS")) {
                        if (response.body() != null) {
                            commonResponses = body.getData();
                            // getSubCategory(categoryResponse.getData().get(0).getId());
                        }
                    }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                        CommonUtility.showDialog1(MapProfessinalWorkerActivity.this);
                    } else {
                        Toast.makeText(MapProfessinalWorkerActivity.this, body.getResponse_message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapProfessinalWorkerActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                t.printStackTrace();
            }
        });
    }

    //api calling
    private void getSubCategory(String id) {
        try {
            //MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<CategoryResponse> call = apiInterface.getSubCategory(token, id);
                call.enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        //MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body() != null) {
                                    categorySubResponse = response.body();
                                    // getSubSubCategory(categorySubResponse.getData().get(0).getId());
                                }
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MapProfessinalWorkerActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        //MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        t.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSubSubCategory(String id) {
        try {
            MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                Call<CategoryResponse> call = apiInterface.getSubSubCategory(token, id);
                call.enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body() != null) {
                                    categorySubSubResponse = response.body();
                                }
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(MapProfessinalWorkerActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        t.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderId", SharedPreferenceWriter.getInstance(this).getString(kOrderId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<OrderDetailResponse> beanCall = apiInterface.getOrder(token, body);
                beanCall.enqueue(new Callback<OrderDetailResponse>() {
                    @Override
                    public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            OrderDetailResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

//                                autoDestLocation.setText(response.body().getDetailInner().getDropOffLocation());
//                                edtPickUpLocation.setText(response.body().getDetailInner().getPickupLocation());
//                                edtAddDetails.setText(response.body().getDetailInner().getOrderDetails());
//                                tvSelectTime.setText(response.body().getDetailInner().getSeletTime());

                                // Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                    }
                });
            }else{
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRequestOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service", "RequireService");
                jsonObject.put("serviceType", "DeliveryPersion");
                jsonObject.put("pickupLocation", edtPickUpLocation.getText().toString());
                jsonObject.put("pickupLat", pLat);
                jsonObject.put("pickupLong", pLong);
                jsonObject.put("dropOffLocation", autoDestLocation.getText().toString());
                jsonObject.put("dropOffLat", dLat);
                jsonObject.put("dropOffLong", dLong);
                jsonObject.put("termsAndCondition", "true");
                jsonObject.put("seletTime", tvSelectTime.getText().toString());
                jsonObject.put("orderDetails", edtAddDetails.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<RequestOrderResponse> beanCall = apiInterface.requestOrder(token, body);
                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                orderID = response.body().getRequestOrderInner().get_id();

                                showDialogInstruction();
                                autoDestLocation.setText("");
                                edtAddDetails.setText("");
                                tvSelectTime.setText("");

                                //Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOrderResponse> call, Throwable t) {
                    }
                });
            }else {
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void professionalWorkeOrderApi(Dialog dialog) {
        try {
            MyDialog.getInstance(this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                /*RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service", "RequireService");
                jsonObject.put("serviceType", "ProfessionalWorker");
                jsonObject.put("pickupLocation", edtPickUpLocation.getText().toString());
                jsonObject.put("pickupLat", pLat);
                jsonObject.put("pickupLong", pLong);
                jsonObject.put("selectCategoryName", "Category 1");
                jsonObject.put("selectSubCategoryName", "Sub-Category1");
                //jsonObject.put("selectSubSubCategoryName", edtDestinationLocation.getText().toString());
                jsonObject.put("selectSubSubCategoryName", subCategoryName);
                jsonObject.put("termsAndCondition", "true");
                jsonObject.put("seletTime", tvSelectTime.getText().toString());
                jsonObject.put("orderDetails", edtAddDetails.getText().toString());
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());*/
                Map<String, RequestBody> map = new HashMap<>();
                //map.put("service", RequestBody.create(MediaType.parse("text/plain"), "RequireService"));
                //map.put("serviceType", RequestBody.create(MediaType.parse("text/plain"),"ProfessionalWorker"));
                map.put("pickupLocation", RequestBody.create(MediaType.parse("text/plain"),edtPickUpLocation.getText().toString()));
                map.put("pickupLat", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(pLat)));
                map.put("pickupLong", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(pLong)));
                map.put("selectCategoryName", RequestBody.create(MediaType.parse("text/plain"), categoryName));
                map.put("selectSubCategoryName", RequestBody.create(MediaType.parse("text/plain"), subCategoryName));
                map.put("portugueseCategoryName", RequestBody.create(MediaType.parse("text/plain"), portugueseCategoryName));
                map.put("portugueseSubCategoryName", RequestBody.create(MediaType.parse("text/plain"), portugueseSubCategoryName));
                map.put("serviceCategoryId", RequestBody.create(MediaType.parse("text/plain"), serviceCategoryId));
                map.put("serviceSubCategoryId", RequestBody.create(MediaType.parse("text/plain"), serviceSubCategoryId));
                //map.put("selectSubSubCategoryName", RequestBody.create(MediaType.parse("text/plain"), subCategoryName));
                //map.put("termsAndCondition", RequestBody.create(MediaType.parse("text/plain"), "true"));
                map.put("seletTime", RequestBody.create(MediaType.parse("text/plain"), tvSelectTime.getText().toString()));
                map.put("orderDetails", RequestBody.create(MediaType.parse("text/plain"), edtAddDetails.getText().toString()));
                map.put("langCode", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE)));


                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
//                File file = new File(imagePath);
//                MultipartBody.Part body = MultipartBody.Part.createFormData("requestImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                Call<RequestOrderResponse> beanCall;
                if(imagePath!=null){
                    beanCall = apiInterface.requestOrder(token, map,getGroupImages());
                }else {
                    beanCall = apiInterface.requestOrder(token, map);
                }

                //Call<RequestOrderResponse> beanCall = apiInterface.requestOrder(token, body);
                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                orderID = response.body().getRequestOrderInner().get_id();
                                //dialogTermsCondition(commingType);
                                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                                //showDialogProfInstruction();
                                //autoDestLocation.setText("");
                                //edtAddDetails.setText("");
                                //tvSelectTime.setText("");
                                if(SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                                    SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).writeStringValue(kCategoryName, response.body().getRequestOrderInner().getSelectCategoryName());
                                }else {
                                    SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).writeStringValue(kCategoryName, response.body().getRequestOrderInner().getPortugueseCategoryName());
                                }

                                updateUserIdApi();
                                useHandlerforApi("Professional");
                                offerHoldTime("Professional");
                                dialog.dismiss();

                                //Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MapProfessinalWorkerActivity.this);
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOrderResponse> call, Throwable t) {
                    }
                });
            }else{
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserIdApi() {
        try {
            MyDialog.getInstance(this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<MyProfileResponse> beanCall = apiInterface.updateUserId(token, body);
                beanCall.enqueue(new Callback<MyProfileResponse>() {
                    @Override
                    public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            MyProfileResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                //Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MapProfessinalWorkerActivity.this);
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                    }
                });
            }else{
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addressApiHit() {
        try {
            MyDialog.getInstance(this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<GetAddressList> beanCall = apiInterface.getAddressList(token, body);
                beanCall.enqueue(new Callback<GetAddressList>() {
                    @Override
                    public void onResponse(Call<GetAddressList> call, Response<GetAddressList> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                                addressesList = response.body().getData().getDocs().get(0).getAddresses();

                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MapProfessinalWorkerActivity.this);
                            } else {
                                // Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAddressList> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });

            }else{
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
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
                startActivity(new Intent(MapProfessinalWorkerActivity.this, SignUpOptions.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showDialogAddressList() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setContentView(R.layout.dialog_address_list);

        ImageView right = dialog.findViewById(R.id.right);
        ImageView close = dialog.findViewById(R.id.ivclose);
        RecyclerView rv_add_list = dialog.findViewById(R.id.rv_add_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        addressListAdapter = new AddressListAdapter(MapProfessinalWorkerActivity.this, addressesList, this);
        rv_add_list.setLayoutManager(new LinearLayoutManager(this));
        rv_add_list.setAdapter(addressListAdapter);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {

                address1 = data.getStringExtra("ADDRESS");
                lat1 = data.getStringExtra("LAT");
                lat2 = data.getStringExtra("LONG");
                pLat = Double.parseDouble(lat1);
                pLong = Double.parseDouble(lat2);
                edtPickUpLocation.setText(address1);
            }

        } else if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                address1 = data.getStringExtra("ADDRESS");
                deslat1 = data.getStringExtra("LAT");
                deslat2 = data.getStringExtra("LONG");
                dLat = Double.parseDouble(deslat1);
                dLong = Double.parseDouble(deslat2);
                autoDestLocation.setText(address1);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                address1 = data.getStringExtra("ADDRESS");
                lat1 = data.getStringExtra("LAT");
                lat2 = data.getStringExtra("LONG");
                pLat = Double.parseDouble(lat1);
                pLong = Double.parseDouble(lat2);
                edtPickUpLocation.setText(address1);
            }
        } else if (requestCode == 233) {
            if (resultCode == Activity.RESULT_OK) {
                getOfferListApi("");
                dialogAcceptOffer.dismiss();

                if(dialogAcceptOffer.isShowing()){
                    dialogAcceptOffer.dismiss();
                }

            }

        }else if (requestCode == CAMERA_PIC_REQUEST || requestCode == REQ_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imagePath = data.getStringExtra("filePath");
                File fileFlyer = new File(imagePath);
                if (fileFlyer.exists()) {
                    if(docList.size()<2){
                        docList.add(fileFlyer);
                        //docList.set(docList.size()-1,fileFlyer);
                    }else {
                        //docList.add(fileFlyer);
                    }
                    addWorkImagesAdapter.updateList(docList);
                    //ivReqImage.setImageURI(Uri.fromFile(fileFlyer));
                    //tvReqImage.setVisibility(View.GONE);
                    /*Glide.with(this)
                            .load(imagePath)
                            .apply(RequestOptions.placeholderOf(R.drawable.loader)
                                    .error(R.drawable.place_holder))
                            .into(ivReqImage);*/
                }
            }

        }
    }//onActivityResult

    private void testUIHandler() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //add your code here
                        Toast.makeText(MapProfessinalWorkerActivity.this, "30000 UI", Toast.LENGTH_SHORT).show();
                    }
                }, 30000);

            }
        });
    }

    //......///////>>>>...Handler..>>>>//
    private void offerHoldTime(String cmFrom) {

        offerHoldTimeDialog = new Dialog(MapProfessinalWorkerActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        offerHoldTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        offerHoldTimeDialog.setContentView(R.layout.activity_offer_screen);
        Window window = offerHoldTimeDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        offerHoldTimeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        ImageView btn_back = offerHoldTimeDialog.findViewById(R.id.btn_back);
        ImageView iv_loading = offerHoldTimeDialog.findViewById(R.id.iv_loading);
        Button btn_cancel = offerHoldTimeDialog.findViewById(R.id.btn_cancel);


        RoteteLoader(iv_loading);

        TextView tv_msg = offerHoldTimeDialog.findViewById(R.id.tv_msg);
        if (cmFrom.equalsIgnoreCase("Professional"))
            tv_msg.setText(R.string.ur_order_submitted_prof_worker_will_an_offer_1);
        else
            tv_msg.setText(getString(R.string.order_offer));

        TextView tv_waiting_txt = offerHoldTimeDialog.findViewById(R.id.tv_waiting_txt);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showCancelDialog();
                if (cmFrom.equalsIgnoreCase("Delivery")) {
                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, "OrderScreen"));
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("kFromCat", "Professional");
                    hashMap.put("kFrom", "Pending");

                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));
                    finish();

                }
                //offerHoldTimeDialog.dismiss();
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
                //offerHoldTimeDialog.dismiss();
            }
        });
        offerHoldTimeDialog.show();

    }

    private void offerCancelDialog(String cmFrom) {

        cancelDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);


        cancelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelDialog.setContentView(R.layout.offer_cancel_layout);
        Window window = cancelDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (!MapProfessinalWorkerActivity.this.isFinishing()) {
            cancelDialog.show();
        }


        ImageView btn_back = cancelDialog.findViewById(R.id.btn_back);
        ImageView iv_loading = cancelDialog.findViewById(R.id.iv_loading);
        Button btn_cancel = cancelDialog.findViewById(R.id.btn_cancel);
        Button btn_try_again = cancelDialog.findViewById(R.id.btn_try_again);
        TextView tv_msg = cancelDialog.findViewById(R.id.tv_msg);
        if (cmFrom.equalsIgnoreCase("Professional"))
            tv_msg.setText(R.string.no_professional_captain_is_available);
        else
            tv_msg.setText(getString(R.string.no_delivery_captain));

        TextView tv_waiting_txt = cancelDialog.findViewById(R.id.tv_waiting_txt);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmFrom.equalsIgnoreCase("Delivery")) {
                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, "OrderScreen"));
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("kFromCat", "Professional");
                    hashMap.put("kFrom", "Pending");

                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));
                }
                //showCancelDialog();
                cancelDialog.dismiss();
            }
        });

        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOfferListApi(cmFrom);
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
                cancelDialog.dismiss();
            }
        });


    }

    private void showCancelDialog() {
        Dialog dialog = new Dialog(MapProfessinalWorkerActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_order_layout, null, false);
        dialog.setContentView(view);

        TextView tv_ok = view.findViewById(R.id.tv_ok);
        TextView cancel_txt = view.findViewById(R.id.cancel_txt);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        EditText edt_reason = view.findViewById(R.id.edt_reason);
        Spinner spinner_cancel = view.findViewById(R.id.spinner_cancel);
        RelativeLayout rl_cancel = view.findViewById(R.id.rl_cancel);


        ///////////////////
        Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } else {
                    //cancel_txt.setText(reason[position]);
                    cancel_txt.setText(parent.getItemAtPosition(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.reasons));
        spinner_cancel.setAdapter(adapter);
        spinner_cancel.setOnItemSelectedListener(onItemSelectedListener);


        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_cancel.performClick();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOrderCancelApi(cancel_txt.getText().toString(), edt_reason.getText().toString());

                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showAcceptOrderDialog(String usereId, String name, String offerAmt, String
            message, String currentToPick,
                                       String PickToDrop, String profilePic, int size, String cmFrom, String time, String
                                               avgRating,
                                       String totalRating, NormalUserPendingOrderInner normalUserPendingOrderInner) {
        dialogAcceptOffer = new Dialog(MapProfessinalWorkerActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_offer_accept_layout, null, false);
        dialogAcceptOffer.setContentView(view);

        Button btn_accept = view.findViewById(R.id.btn_accept);
        Button btn_reject = view.findViewById(R.id.btn_reject);
        TextView tv_profile_name = view.findViewById(R.id.tv_profile_name);
        TextView pickuptodrop = view.findViewById(R.id.pickuptodrop);
        TextView workertopickup = view.findViewById(R.id.workertopickup);
        TextView dilevery_tym = view.findViewById(R.id.dilevery_tym);
        dilevery_tym.setText(time);
        TextView msg = view.findViewById(R.id.msg);
        TextView offer_amt = view.findViewById(R.id.offer_amt);
        TextView tvoffer = view.findViewById(R.id.tvoffer);
        ImageView user_pic = view.findViewById(R.id.user_pic);
        ImageView imageView10 = view.findViewById(R.id.imageView10);
        TextView tv_rat_num = view.findViewById(R.id.tv_rat_num);
        TextView droptodelivered3 = view.findViewById(R.id.droptodelivered3);
        TextView droptodelivered4 = view.findViewById(R.id.droptodelivered4);
        TextView droptodelivered5 = view.findViewById(R.id.droptodelivered5);
        View view2 = view.findViewById(R.id.view2);
        if (cmFrom.equalsIgnoreCase("Professional")) {
            workertopickup.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            imageView10.setVisibility(View.GONE);
            droptodelivered4.setVisibility(View.GONE);
            droptodelivered3.setText(getString(R.string.prof_worker));
            droptodelivered5.setText(getString(R.string.service_loc));
            pickuptodrop.setText(normalUserPendingOrderInner.getCurrentToPicupLocation());
        } else {
            workertopickup.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            imageView10.setVisibility(View.VISIBLE);
            droptodelivered4.setVisibility(View.VISIBLE);
            droptodelivered3.setText(getString(R.string.delivery_worker_new_line));
        }

        tv_rat_num.setText(avgRating);
        TextView tv_view_comments = view.findViewById(R.id.tv_view_comments);
        tv_view_comments.setText("(" + totalRating + " "+getString(R.string.ratings_view_all)+")");

        tv_view_comments.setOnClickListener(v -> {
            startActivity(UserDetailsActivity.getIntent(this, normalUserPendingOrderInner, MapProfessinalWorkerActivity.class.getSimpleName()));
        });

        if (size > 0) {
            tvoffer.setVisibility(View.VISIBLE);
            tvoffer.setText(size + "  "+getString(R.string.offer));

        } else {
            tvoffer.setVisibility(View.GONE);

        }

        tv_profile_name.setText(name);
        workertopickup.setText(currentToPick);
        if (!PickToDrop.equalsIgnoreCase("NaN"))
            pickuptodrop.setText(PickToDrop);
        else
            pickuptodrop.setText("0.0 KM");
        String currency = normalUserPendingOrderInner.getCurrency()!=null?normalUserPendingOrderInner.getCurrency():"Kz";
        offer_amt.setText(offerAmt + " "+currency);
        msg.setText(message);
        //Glide.with(this).load(profilePic).into(user_pic);
        try{
            Glide.with(this)
                    .load(profilePic)
                    .apply(RequestOptions.placeholderOf(R.drawable.loader)
                            .error(R.drawable.profile_default))
                    .into(user_pic);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        btn_accept.setOnClickListener(v -> {
            showDialogA("Accept", usereId, cmFrom);
            //dialog.dismiss();
        });

        btn_reject.setOnClickListener(v -> {
            showDialogA("Reject", usereId, cmFrom);
            //dialog.dismiss();
        });


        //tvoffer.setOnClickListener(v -> startActivity(NormalAllOfferActivity.getIntent(MapProfessinalWorkerActivity.this, orderID)));
        if (cmFrom.equalsIgnoreCase("Professional")){
            tvoffer.setOnClickListener(v -> startActivityForResult(ViewAllOffersActivity.getIntent(MapProfessinalWorkerActivity.this, "FromNUPW", orderID), 233));
        }else{
            tvoffer.setOnClickListener(v -> startActivityForResult(ViewAllOffersActivity.getIntent(MapProfessinalWorkerActivity.this, "FromNUDP", orderID), 233));
        }


        try{
            dialogAcceptOffer.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void offernotacceptShowUI() {
//        iv_loading.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.close_o));
//        tv_waiting_txt.setVisibility(View.GONE);
//        tv_msg.setText(getResources().getString(R.string.no_delivery_captain));
//        btnTryAgain.setVisibility(View.VISIBLE);


    }

    private void getOfferListApi(String cmFrom) {
        try {
            MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("lat", SharedPreferenceWriter.getInstance(this).getString(kLat));
                jsonObject.put("long", SharedPreferenceWriter.getInstance(this).getString(kLong));
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getOfferList(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    viewOfferList = response.body().getDataList();


                                    String userID = viewOfferList.get(0).get_id();

                                    String name = viewOfferList.get(0).getOfferAcceptedByName();
                                    // String totalRating = viewOfferList.get(0).getTotalRating();
                                    //String avgRating = viewOfferList.get(0).getAvgRating();
                                    String offerAmt = viewOfferList.get(0).getMinimumOffer();
                                    String message = viewOfferList.get(0).getMessage();
                                    String currenttoPick = viewOfferList.get(0).getCurrentToPicupLocation();
                                    String pickToDrop = viewOfferList.get(0).getPickupToDropLocation();
                                    String UserPic = viewOfferList.get(0).getOfferAcceptedByProfilePic();
                                    String time = viewOfferList.get(0).getApprxTime();

                                    String cmFrom;

                                    if (viewOfferList.get(0).getServiceType().equalsIgnoreCase("DeliveryPersion")) {
                                        cmFrom = "Delivery";
                                    } else {
                                        cmFrom = "Professional";
                                    }

                                    showAcceptOrderDialog(userID, name, offerAmt, message,
                                            currenttoPick, pickToDrop, UserPic, viewOfferList.size(), cmFrom, time,
                                            viewOfferList.get(0).getAvgRating(), viewOfferList.get(0).getTotalRating(), viewOfferList.get(0));

                                } else {
                                    try{
                                        offerCancelDialog(cmFrom);
                                        offerHoldTimeDialog.dismiss();
                                        Toast.makeText(MapProfessinalWorkerActivity.this, R.string.try_again_for_new_offers, Toast.LENGTH_SHORT).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                                //Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NormalUserPendingOrderResponse> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callAcceptOfferApi(String offerId, String cmFrom) {
        try {
            MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("offerId", offerId);
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<ViewAllOfferResponse> beanCall = apiInterface.acceptOffer(token, body);
                beanCall.enqueue(new Callback<ViewAllOfferResponse>() {
                    @Override
                    public void onResponse(Call<ViewAllOfferResponse> call, Response<ViewAllOfferResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ViewAllOfferResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                // dialogSubmitSuccess();
                                //dialogSubmitSuccessAa(cmFrom);

                                if (cmFrom.equalsIgnoreCase("Delivery")) {

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("kFromCat", "Delivery");
                                    hashMap.put("kFrom", "Active");

                                    if (handler != null)
                                        handler.removeCallbacksAndMessages(null);

                                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));
                                    finish();

                                } else {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("kFromCat", "Professional");
                                    hashMap.put("kFrom", "Active");

                                    if (handler != null)
                                        handler.removeCallbacksAndMessages(null);

                                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));
                                    finish();
                                }


//                                getOfferListApi();
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ViewAllOfferResponse> call, Throwable t) {

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dialogSubmitSuccessAa(String cmFrom) {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_submit_successfully_layout);


        TextView txtMsg = (TextView) dialog.findViewById(R.id.tv_desc);
        TextView btnOK = (TextView) dialog.findViewById(R.id.tv_ok);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageDrawable(getResources().getDrawable(R.drawable.popup_logo));
        txtMsg.setText("Offer Accept Successfully!");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cmFrom.equalsIgnoreCase("Delivery")) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("kFromCat", "Delivery");
                    hashMap.put("kFrom", "Active");

                    if (handler != null)
                        handler.removeCallbacksAndMessages(null);

                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));

                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("kFromCat", "Professional");
                    hashMap.put("kFrom", "Active");

                    if (handler != null)
                        handler.removeCallbacksAndMessages(null);

                    startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, hashMap));
                }


                finish();
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    //OfferAcceptApi Hit//

    private void callRejectOfferApi(String offerId, String cmFrom) {
        try {
            MyDialog.getInstance(MapProfessinalWorkerActivity.this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(MapProfessinalWorkerActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("offerId", offerId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<ViewAllOfferResponse> beanCall = apiInterface.rejectOffer(token, body);
                beanCall.enqueue(new Callback<ViewAllOfferResponse>() {
                    @Override
                    public void onResponse(Call<ViewAllOfferResponse> call, Response<ViewAllOfferResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ViewAllOfferResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                useHandlerforApi(cmFrom);
                                offerHoldTime(cmFrom);
//                                finish();
//                                getOfferListApi();
                                //Toast.makeText(ViewAllOffersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ViewAllOfferResponse> call, Throwable t) {

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogA(String type, String id, String cmFrom) {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);
        registerNow.setText(R.string.ok);
        notNow.setText(R.string.cancel);

        if (type.equalsIgnoreCase("Accept")) {
            textView.setText(getString(R.string.are_you_sure_you_want_accept_this_offer));
            registerNow.setVisibility(View.VISIBLE);
            notNow.setVisibility(View.VISIBLE);
            btn_ok.setVisibility(View.GONE);
        } else {
            textView.setText(R.string.are_you_sure_you_want_reject_offer);
            registerNow.setVisibility(View.VISIBLE);
            notNow.setVisibility(View.VISIBLE);
            btn_ok.setVisibility(View.GONE);
        }

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("Accept")) {
                    callAcceptOfferApi(id, cmFrom);
                    dialog.dismiss();
                } else {
                    callRejectOfferApi(id, cmFrom);
                }
                dialog.dismiss();
            }
        });
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showCancelOderSuccessfully() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);
        registerNow.setText(getString(R.string.ok));
        notNow.setText(R.string.cancel);

        notNow.setVisibility(View.GONE);
        registerNow.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);

        textView.setText(R.string.cancelled_order_successfully);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                offerHoldTimeDialog.dismiss();
                startActivity(HomeMainActivity.getIntent(MapProfessinalWorkerActivity.this, ""));
                finish();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void useHandlerforApi(String cmFrom) {


        handler = new Handler();
        handler.postDelayed(() -> {

            getOfferListApi(cmFrom);

        }, 120000);


    }

    private void callOrderCancelApi(String cancelTxt, String reason) {
        try {
            MyDialog.getInstance(this).showDialog(MapProfessinalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", orderID);
                jsonObject.put("orderCanelReason", reason);
                jsonObject.put("orderCancelMessage", cancelTxt);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.orderCancel(token, body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                String checktype = response.body().getUserTypeResponse().getServiceType();

                                showCancelOderSuccessfully();

                                //Toast.makeText(CancellationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserTypeResponse> call, Throwable t) {

                    }
                });

            }else{
                MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void RoteteLoader(ImageView iv_loading) {

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        RotateAnimation animRotate = new RotateAnimation(0.0f, 360.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(1500);
        animRotate.setFillAfter(true);
        animRotate.setRepeatCount(Animation.INFINITE);
        animSet.addAnimation(animRotate);

        iv_loading.startAnimation(animSet);

    }

    @Override
    public void onDeleteClick(int position) {
        docList.remove(docList.get(position));
        addWorkImagesAdapter.updateList(docList);
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
            if (result != null) {
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
            }
            // Drawing polyline in the Google Map for the i-th route
            // mMap.addPolyline(lineOptions);
            if ((points) != null) {
                mMap.addPolyline(lineOptions);
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(pushNotifyReceiver != null){
            unregisterReceiver(pushNotifyReceiver);
        }
    }

    public void selectImage() {
        final CharSequence[] items = {
//                getResources().getString(R.string.Take_Photo),
//                getResources().getString(R.string.Choose_from_Library),
                "take photo", "take libarary",
                getResources().getString(R.string.cancel)};

        final Dialog dialog = new Dialog(this, R.style.MyDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_imagecapture);


        TextView txt_takephoto = (TextView) dialog.findViewById(R.id.txt_takephoto);
        TextView txt_choosefromlibrary = (TextView) dialog.findViewById(R.id.txt_choosefromgallery);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_button);
        txt_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapProfessinalWorkerActivity.this, TakeImage.class);
                intent.putExtra("from", "camera");
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                dialog.dismiss();
            }
        });
        txt_choosefromlibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapProfessinalWorkerActivity.this, TakeImage.class);
                intent.putExtra("from", "gallery");
                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /////////////convert image to array list/////////////
    private List<MultipartBody.Part> getGroupImages(){
        List<MultipartBody.Part> list = new ArrayList<>();
        for (int i=0;i<docList.size();i++){
            RequestBody profile_body = RequestBody.create(MediaType.parse("image/*"), docList.get(i));
            MultipartBody.Part menuPart = MultipartBody.Part.createFormData("orderImages[]", docList.get(i).getName(), profile_body);
            list.add(menuPart);
        }
        return list;
    }

    //GetSetSpinnerdistanceValues...km..///
    private void deliverySpinApi(String spinType, String from, int position) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            // if (!token.isEmpty()) {
            RequestBody profile_body;

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userType", spinType);
            jsonObject.put("lat", pLat);
            jsonObject.put("long", pLong);
            jsonObject.put("distance", Integer.parseInt(spinner1.getSelectedItem().toString()));
            jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            Call<GetLocation> beanCall = apiInterface.getLocation(token, body);
            beanCall.enqueue(new Callback<GetLocation>() {
                @Override
                public void onResponse(Call<GetLocation> call, Response<GetLocation> response) {
                    MyDialog.getInstance(MapProfessinalWorkerActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                            tvProfessional.setText(response.body().getData() + " "+getString(R.string.service_provider_within));
                            tv_prof_wkr.setText(spin1[position] + " KM");

                            // Toast.makeText(HomeMapActivity.getActivity(), response.body().getResponse_message(), Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onImageClick(int position) {
        showFullImage(docList.get(position).getAbsolutePath());
    }

    private void showFullImage(String img) {
        final Dialog dialog1 = new Dialog(this, R.style.ThemeDialogCustom);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.image_show_layout);

        ImageView yes_btn = (ImageView) dialog1.findViewById(R.id.img_main_iv);
        ImageView no_btn = (ImageView) dialog1.findViewById(R.id.img_close_iv);


//        aQuery.id(yes_btn).image(img, false, false);

        /*Glide.with(this)
                .load(img)
                .apply(new RequestOptions().placeholder(R.drawable.default_p))
                .into(yes_btn);*/

        Glide.with(this)
                .load(img)
                .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                        .error(R.drawable.default_image))
                .into(yes_btn);

        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semitransparent)));

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    CommonUtility.hideSoftKeyboard(MapProfessinalWorkerActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
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
