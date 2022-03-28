package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.SignUpMobileActivity;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.adapters.CategoryAdapter;
import com.pagin.azul.onphasesecond.adapters.MenuAdapter;
import com.pagin.azul.onphasesecond.adapters.MenuDetailsAdapter;
import com.pagin.azul.onphasesecond.adapters.MenuPagerAdapter;
import com.pagin.azul.onphasesecond.bottomsheet.MenuItemBottomSheet;
import com.pagin.azul.onphasesecond.bottomsheet.RestroInfoBottomSheet;
import com.pagin.azul.onphasesecond.fragments.DynamicFragment;
import com.pagin.azul.onphasesecond.model.ExclusiveOfferModel;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
import com.pagin.azul.onphasesecond.model.MenuItem;
import com.pagin.azul.onphasesecond.model.ProductModel;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RatingResponse;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class RestaurantDetails extends AppCompatActivity implements CommonListener{

    @BindView(R.id.recycerAddToCartHori)
    RecyclerView recycerAddToCartHori;

    @BindView(R.id.recyclerAddToCartVertically)
    RecyclerView recyclerAddToCartVertically;

    @BindView(R.id.ivSearchAddToCart)
    SearchView ivSearchAddToCart;

    @BindView(R.id.tvMenu)
    TextView tvMenu;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.ivRestroImage)
    ImageView ivRestroImage;

    @BindView(R.id.ivFav)
    ImageView ivFav;

    @BindView(R.id.tvdistance)
    TextView tvdistance;

    @BindView(R.id.tvRestroTitle)
    TextView tvRestroTitle;

    @BindView(R.id.tvAvgRating)
    TextView tvAvgRating;

    @BindView(R.id.tvReview)
    TextView tvReview;

    @BindView(R.id.tvRestroAddress)
    TextView tvRestroAddress;

    @BindView(R.id.tvTimeAddToCard)
    TextView tvTimeAddToCard;

    @BindView(R.id.tvMinOrderAddToCard)
    TextView tvMinOrderAddToCard;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.llCousine)
    LinearLayout llFoodType;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @BindView(R.id.ivRating)
    RatingBar ivRating;

    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;

    @BindView(R.id.rvCuisine)
    RecyclerView rvCuisine;

    private String token;
    private String userId;
    private String langCode;
    private String latitude;
    private String longitude,lat="",lon="";
    private String resAndStoreId;
    String otime="",clostime="";

    private static final int FILTER_REQ = 21;
    private boolean sweet;
    private boolean salty;
    private boolean mixed;
    private boolean veg;
    private boolean nonVeg;
    private boolean lunch;
    private boolean breakfast;
    private boolean dinner;
    private boolean taste;
    private boolean type;
    private boolean eatType;


    //  private RestaurantResponse intentData;
    private RestaurantResponse details;
    private ArrayList<RatingResponse> ratingList;
    MenuPagerAdapter menuPagerAdapter;
    private CommonListener commonListener;
    private boolean isFav=false;
    Map<String,RecyclerView> commonListeners;
    ArrayList<RestaurantResponse> cuisineData;
    private CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commonListeners=new HashMap<>();
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
        GPSTracker gpsTracker = new GPSTracker(RestaurantDetails.this,RestaurantDetails.this);
        lat = String.valueOf(gpsTracker.getLatitude());
        lon = String.valueOf(gpsTracker.getLongitude());
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        ivSearchAddToCart.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //this code will be executed on devices running PI or later
                if (hasFocus) {
                    tvMenu.setVisibility(View.GONE);
                }
            } else {
                if (hasFocus) {
                    tvMenu.setVisibility(View.GONE);
                } else {
                    tvMenu.setVisibility(View.VISIBLE);
                }
            }
        });
        ivSearchAddToCart.setOnCloseListener(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (ivSearchAddToCart.getQuery().toString().isEmpty())
                    tvMenu.setVisibility(View.VISIBLE);
            }
            return false;
        });
        addOnQueryTextChangeListener();
        getIntentData();
    }

    @OnClick({R.id.ivInfo, R.id.tvAddToCart, R.id.ivBackAddToCart, R.id.ivFilterAddToCart
            , R.id.ivSideMenu, R.id.tvReview, R.id.ivDistance,R.id.ivFav})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivInfo:
                openRestroInfoSheet();
                break;
            case R.id.ivSideMenu:
                /*MenuItemBottomSheet menuItemBottomSheet = new MenuItemBottomSheet();
                menuItemBottomSheet.show(getSupportFragmentManager(), "");*/
                break;
            case R.id.tvAddToCart:
                dispatchMyCartAct();
                break;
            case R.id.ivFilterAddToCart:
                dispatchMenuFilter();
                break;
            case R.id.ivBackAddToCart:
                onBackPressed();
                break;
            case R.id.tvReview:
                if(token!=null && !token.equals("")){
                    dispatchRatingReview();
                }else{
                    CommonUtilities.showDialog(this);
                }
                break;
            case R.id.ivDistance:
                CommonUtilities.dispatchGoogleMap(this,latitude,longitude);
                break;
            case R.id.ivFav:
                if(token!=null && !token.isEmpty()) {
                    serviceAddToFav();
                }
                else {
                    CommonUtilities.showDialog(this);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILTER_REQ && resultCode== Activity.RESULT_OK && data!=null){
            ArrayList<RestaurantResponse> list = data.getParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue());
            if (list != null) {

                ((MenuDetailsAdapter)commonListeners.get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString()).getAdapter()).updateList(list);


            }
            sweet = data.getBooleanExtra("sweet",false);
            salty = data.getBooleanExtra("salty",false);
            mixed = data.getBooleanExtra("mixed",false);
            veg = data.getBooleanExtra("veg",false);
            nonVeg = data.getBooleanExtra("nonVeg",false);
            lunch = data.getBooleanExtra("lunch",false);
            breakfast = data.getBooleanExtra("breakfast",false);
            dinner = data.getBooleanExtra("dinner",false);
            taste = data.getBooleanExtra("taste",false);
            type = data.getBooleanExtra("type",false);
            eatType = data.getBooleanExtra("eatType",false);
        }
    }

    private void dispatchMenuFilter() {
       /* if(commonListener!=null)
        {
            try{
                String cuisine = (String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText();
                commonListener.onMenuFilterClick(cuisine);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }*/
        String cuisine = (String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText();

        Intent intent = new Intent(this, MenuFilterActivity.class);
        intent.putExtra(ParamEnum.ID.theValue(),resAndStoreId);
        intent.putExtra(ParamEnum.CUISINE.theValue(),cuisine);
        intent.putExtra("sweet",sweet);
        intent.putExtra("salty",salty);
        intent.putExtra("mixed",mixed);
        intent.putExtra("veg",veg);
        intent.putExtra("nonVeg",nonVeg);
        intent.putExtra("lunch",lunch);
        intent.putExtra("breakfast",breakfast);
        intent.putExtra("dinner",dinner);
        intent.putExtra("taste",taste);
        intent.putExtra("type",type);
        intent.putExtra("eatType",eatType);
        startActivityForResult(intent,FILTER_REQ);

    }

    private void dispatchMyCartAct() {
        if(token!=null && !token.equals("")){
            Intent intent = new Intent(this, MyCartActivity.class);
            intent.putExtra(ParamEnum.DATA.theValue(),details);
            startActivity(intent);
        }else{
            CommonUtilities.showDialog(this);
        }
    }

    private void dispatchRatingReview() {
        if(ratingList!=null && !ratingList.isEmpty()){
            Intent intent = new Intent(this, RatingAndReviewActivity.class);
            intent.putExtra(ParamEnum.DATA.theValue(),details);
            intent.putParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue(),ratingList);
            startActivity(intent);
        }
    }

    private void openRestroInfoSheet() {
        RestroInfoBottomSheet resInfoSheet = new RestroInfoBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ParamEnum.DATA.theValue(),details);
        bundle.putParcelableArrayList(ParamEnum.DATA_LIST.theValue(),ratingList);
        resInfoSheet.setArguments(bundle);
        resInfoSheet.show(getSupportFragmentManager(), "");
    }

    private void setUpRecyclerView() {
        List<MenuItem> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MenuItem menuItem = new MenuItem();
            if (i == 0) {
                menuItem.setMenu("Most Popular");
                menuItem.setCheck(true);
            } else if (i == 1) {
                menuItem.setMenu("Main Course");
                menuItem.setCheck(false);
            } else if (i == 2) {
                menuItem.setMenu("Snacks");
                menuItem.setCheck(false);
            } else if (i == 3) {
                menuItem.setMenu("Dessert");
                menuItem.setCheck(false);
            }
            list.add(menuItem);
        }
        recycerAddToCartHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycerAddToCartHori.setAdapter(new MenuAdapter(this, list));

        recyclerAddToCartVertically.setLayoutManager(new LinearLayoutManager(this));
        recyclerAddToCartVertically.setAdapter(new MenuDetailsAdapter(this));
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            if(getIntent().getBooleanExtra("fromExclusiveOffer",false)){

                resAndStoreId = getIntent().getStringExtra("id");

            }else{
                RestaurantResponse intentData = getIntent().getParcelableExtra(ParamEnum.DATA.theValue());

                resAndStoreId = intentData.get_id();
            }

            SharedPreferenceWriter.getInstance(RestaurantDetails.this).writeStringValue(SharedPreferenceKey.rest,resAndStoreId.toString() );

            serviceResAndStoreDetail(resAndStoreId);
            //serviceGetMenuList(resAndStoreId);
            serviceGetCuisineListForUser(resAndStoreId);
        }
    }

    private void serviceResAndStoreDetail(String resAndStoreId) {
        try {
            MyDialog.getInstance(this).showDialog(this);

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", (userId.trim().isEmpty()?null:userId));
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lon);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.resAndStoreDetail(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(RestaurantDetails.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                                setDetails(restaurantModel.getData());
                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(RestaurantDetails.this);
                            } else {
                                Toast.makeText(RestaurantDetails.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<RestaurantModel> call, Throwable t) {
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDetails(RestaurantResponse data) {
        ratingList = data.getRating();
        details = data.getResAndStoreDetail();
        String image = details.getImage();
        if(image!=null && !image.equals("")){
            setImage(progressbar,image,ivRestroImage);
        }else {
            progressbar.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.food_thali).override(110, 150).into(ivRestroImage);
        }
        tvRestroTitle.setText(details.getName());
        String avgRating = details.getAvgRating();
        tvAvgRating.setText(avgRating);
        try{
            ivRating.setRating(Float.parseFloat(avgRating));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        tvReview.setText("("+details.getTotalRating()+getString(R.string.small_reviews)+")");
        tvRestroAddress.setText(details.getAddress());
        latitude = details.getLatitude();
        longitude = details.getLongitude();
        String otime="",clostime="";
        otime=details.getOpeningTime();
        clostime=details.getClosingTime();
        SharedPreferenceWriter.getInstance(RestaurantDetails.this).writeStringValue(SharedPreferenceKey.otime,otime );
        SharedPreferenceWriter.getInstance(RestaurantDetails.this).writeStringValue(SharedPreferenceKey.ctime,clostime  );


        ArrayList<RestaurantResponse> cuisineList = details.getCuisineData();
        if(cuisineList!=null && !cuisineList.isEmpty()){
            for (int i = 0; i < cuisineList.size(); i++) {
                TextView textView = new TextView(this);
                textView.setText(cuisineList.get(i).getName());
                textView.setCompoundDrawablePadding(10);
                textView.setTextSize(11);
                //textView.setTextColor(ContextCompat.getColor(this, R.color.light_grey));
                textView.setPadding(0, 0, 15, 0);
                textView.setMaxLines(1);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_dot_circle, 0, 0, 0);
                llFoodType.addView(textView);
            }
        }else {
            TextView textView = new TextView(this);
            textView.setText(R.string.no_cuisine);
            textView.setCompoundDrawablePadding(10);
            textView.setTextSize(11);
            textView.setMaxLines(1);
            llFoodType.addView(textView);
        }
        isFav=details.isFav();
        if(isFav)
            ivFav.setImageResource(R.drawable.fav);
        else
            ivFav.setImageResource(R.drawable.fav_un);
        try {
            //double distanceInKm = 1.60934  * intentData.getDist().getCalculated(); // miles to km
            double distanceInKm = 0.001  * details.getDist().getCalculated(); // meter to km
            //DecimalFormat df = new DecimalFormat("#.##");
            //tvdistance.setText(df.format(distanceInKm)+"km");
            tvdistance.setText(distanceInKm+"km");
        } catch (Exception e) {
            tvdistance.setText(details.getDistance()+"km");
        }
        String deliveryTime = details.getDeliveryTime();
        tvTimeAddToCard.setText(deliveryTime!=null?(deliveryTime+getString(R.string.mins)):"0"+getString(R.string.mins));
        String minValue = details.getMinimumValue();
        String currency = details.getCurrency();
        tvMinOrderAddToCard.setText(minValue!=null?(getString(R.string.min_order)+CommonUtilities.getPriceFormat(minValue)+" "+currency):getString(R.string.min_order)+"0 " + currency);
    }

    private void setImage( ProgressBar progressBar,final String imageUri,final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }

    private void serviceGetMenuList(String resAndStoreId){
        try {
            MyDialog.getInstance(this).showDialog(this);

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.getRestaurantMenuList(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        MyDialog.getInstance(RestaurantDetails.this).hideDialog();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                initTabLayout(model.getData(),resAndStoreId);

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(RestaurantDetails.this);
                            } else {
                                Toast.makeText(RestaurantDetails.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FavoriteModel> call, Throwable t) {
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTabLayout(ArrayList<RestaurantResponse> data, String resAndStoreId) {
        /*for (int i = 0; i <4; i++) {
            if (i == 0) {
                tabLayout.addTab(tabLayout.newTab().setText("Most Popular"));
            } else if (i == 1) {
                tabLayout.addTab(tabLayout.newTab().setText("Main Course"));
            } else if (i == 2) {
                tabLayout.addTab(tabLayout.newTab().setText("Snacks"));
            } else {
                tabLayout.addTab(tabLayout.newTab().setText("Dessert"));
            }
        }*/
        cuisineData=data;

        for (int i = 0; i <data.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(data.get(i).getCuisine()));
        }
         menuPagerAdapter = new MenuPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),data,resAndStoreId);
        viewPager.setAdapter(menuPagerAdapter);
        //viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() == 2) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void serviceAddToFav(){
        try {
            //MyDialog.getInstance(this).showDialog(this);
            startHeartAnimation();
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.addToFavourite(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        //MyDialog.getInstance(RestaurantDetails.this).hideDialog();
                        stopHeartAnimation();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if(isFav) {
                                    ivFav.setImageResource(R.drawable.fav_un);
                                    isFav = false;
                                }
                                else {
                                    ivFav.setImageResource(R.drawable.fav);
                                    isFav = true;
                                }


                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(RestaurantDetails.this);
                            } else {
                                Toast.makeText(RestaurantDetails.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FavoriteModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOnQueryTextChangeListener() {


        ivSearchAddToCart.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*if(commonListener!=null)
                     commonListener.onFilter(newText);



                if(commonListener!=null)
                    commonListener.onFilter(newText);*/


                ;
                //((MenuDetailsAdapter)commonListeners.get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString()).getAdapter()).getFilter().filter(newText);

                if(categoryAdapter!=null)
                    categoryAdapter.getFilter().filter(newText);

                return true;
            }
        });
    }

    public void setOnCommonListener(CommonListener commonListener){
        this.commonListener = commonListener;
        //commonListeners.add(commonListener);
    }

    public void addMenuDetailAdapter(RecyclerView menuDetailsRecycler,String cuisineName){
        commonListeners.put(cuisineName,menuDetailsRecycler);

    }

    ////start heart animation when user click on fav icon
    private void startHeartAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation("layer.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

    }

    private void stopHeartAnimation() {
        lottieAnimationView.setVisibility(GONE);
    }

    private void serviceGetCuisineListForUser(String resAndStoreId){
        try {
            MyDialog.getInstance(this).showDialog(this);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resAndStoreId", resAndStoreId);
            jsonObject.put("langCode", langCode);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<ProductModel> beanCall = apiInterface.getCuisineListForUser(body);

            beanCall.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    MyDialog.getInstance(RestaurantDetails.this).hideDialog();
                    if (response.isSuccessful()) {
                        ProductModel model = response.body();
                        if (model.getResponse_code().equalsIgnoreCase("200")) {

                            setUpCuisineRecyclerView(model.getData());

                        } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(RestaurantDetails.this);
                        } else {
                            Toast.makeText(RestaurantDetails.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpCuisineRecyclerView(ArrayList<ProductResponse> data) {
        if(data!=null && !data.isEmpty()){
            tvNoData.setVisibility(GONE);
            rvCuisine.setLayoutManager(new GridLayoutManager(this,2));
            categoryAdapter = new CategoryAdapter(this,this,data);
            rvCuisine.setAdapter(categoryAdapter);
        }else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCategoryClick(int position, ArrayList<ProductResponse> categoryList) {
        Intent intent = new Intent(this,MenuActivity.class);
        intent.putExtra("cuisine",categoryList.get(position).getName());
        intent.putExtra("resAndStoreId",resAndStoreId);
        intent.putExtra("openingtime",resAndStoreId);
        intent.putExtra("closingtime",resAndStoreId);


        startActivity(intent);
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