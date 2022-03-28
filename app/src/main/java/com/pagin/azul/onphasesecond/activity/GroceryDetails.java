package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.HomeServicesCategoryAct;
import com.pagin.azul.adapter.HomeCategoryAdapter;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.adapters.CategoryAdapter;
import com.pagin.azul.onphasesecond.adapters.MenuDetailsAdapter;
import com.pagin.azul.onphasesecond.bottomsheet.RestroInfoBottomSheet;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
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

public class GroceryDetails extends AppCompatActivity implements CommonListener {

    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;

    @BindView(R.id.ivSearchAddToCart)
    SearchView ivSearchAddToCart;

    @BindView(R.id.tvMenu)
    TextView tvMenu;

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

    private String token;
    private String resAndStoreId;
    private String userId;
    private String langCode;
    private String latitude;
    private String longitude, lat = "", lon = "";
    private RestaurantResponse details;
    private ArrayList<RatingResponse> ratingList;
    private boolean isFav = false;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_details);
        ButterKnife.bind(this);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        GPSTracker gpsTracker = new GPSTracker(this, this);
        lat = String.valueOf(gpsTracker.getLatitude());
        lon = String.valueOf(gpsTracker.getLongitude());
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        ivSearchAddToCart.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvMenu.setVisibility(View.GONE);
            } else {
                tvMenu.setVisibility(View.VISIBLE);
            }
        });
        addOnQueryTextChangeListener();
        getIntentData();
    }

    @OnClick({R.id.ivBackAddToCart, R.id.ivInfo, R.id.tvReview, R.id.ivDistance, R.id.ivFav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackAddToCart:
                onBackPressed();
                break;
            case R.id.ivInfo:
                openRestroInfoSheet();
                break;
            case R.id.tvReview:
                if (token != null && !token.equals("")) {
                    dispatchRatingReview();
                } else {
                    CommonUtilities.showDialog(this);
                }
                break;
            case R.id.ivDistance:
                CommonUtilities.dispatchGoogleMap(this, latitude, longitude);
                break;
            case R.id.ivFav:
                if (token != null && !token.isEmpty())
                    serviceAddToFav();
                else
                    CommonUtilities.showDialog(this);
        }
    }

    private void dispatchRatingReview() {
        if(ratingList!=null && !ratingList.isEmpty()){
            Intent intent = new Intent(this, RatingAndReviewActivity.class);
            intent.putExtra(ParamEnum.DATA.theValue(), details);
            intent.putParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue(), ratingList);
            startActivity(intent);
        }
    }

    private void openRestroInfoSheet() {
        RestroInfoBottomSheet resInfoSheet = new RestroInfoBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ParamEnum.DATA.theValue(), details);
        bundle.putParcelableArrayList(ParamEnum.DATA_LIST.theValue(), ratingList);
        resInfoSheet.setArguments(bundle);
        resInfoSheet.show(getSupportFragmentManager(), "");
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getBooleanExtra("fromExclusiveOffer", false)) {
                resAndStoreId = getIntent().getStringExtra("id");
            } else {
                RestaurantResponse intentData = getIntent().getParcelableExtra(ParamEnum.DATA.theValue());
                resAndStoreId = intentData.get_id();
            }
            serviceResAndStoreDetail(resAndStoreId);
        }
        serviceGetCategoryList();
    }

    private void setUpRecyclerView(ArrayList<ProductResponse> data) {
        if(data!=null && !data.isEmpty()){
            tvNoData.setVisibility(GONE);
            rvCategory.setLayoutManager(new GridLayoutManager(this, 2));
            categoryAdapter = new CategoryAdapter(this, this, data);
            rvCategory.setAdapter(categoryAdapter);
        }else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCategoryClick(int position, ArrayList<ProductResponse> categoryList) {
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra(ParamEnum.ID.theValue(), categoryList.get(position).get_id());
        intent.putExtra(ParamEnum.RES_AND_STORE_ID.theValue(), resAndStoreId);
        startActivity(intent);
    }

    private void serviceResAndStoreDetail(String resAndStoreId) {
        try {
            MyDialog.getInstance(this).showDialog(this);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", (userId.trim().isEmpty() ? null : userId));
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
                    MyDialog.getInstance(GroceryDetails.this).hideDialog();
                    if (response.isSuccessful()) {
                        RestaurantModel restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                            setDetails(restaurantModel.getData());
                        } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(GroceryDetails.this);
                        } else {
                            Toast.makeText(GroceryDetails.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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
        if (image != null && !image.equals("")) {
            setImage(progressbar, image, ivRestroImage);
        } else {
            progressbar.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.food_thali).override(110, 150).into(ivRestroImage);
        }
        tvRestroTitle.setText(details.getName());
        String avgRating = details.getAvgRating();
        tvAvgRating.setText(avgRating);
        try {
            ivRating.setRating(Float.parseFloat(avgRating));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        SharedPreferenceWriter.getInstance(GroceryDetails.this).writeStringValue(SharedPreferenceKey.otime,details.getOpeningTime() );
        SharedPreferenceWriter.getInstance(GroceryDetails.this).writeStringValue(SharedPreferenceKey.ctime,details.getClosingTime()  );
        tvReview.setText("(" + details.getTotalRating() + getString(R.string.small_reviews) + ")");
        tvRestroAddress.setText(details.getAddress());
        latitude = details.getLatitude();
        longitude = details.getLongitude();
        /*ArrayList<RestaurantResponse> cuisineList = details.getCuisineData();
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
        }*/
        isFav = details.isFav();
        if (isFav)
            ivFav.setImageResource(R.drawable.fav);
        else
            ivFav.setImageResource(R.drawable.fav_un);
        try {
            //double distanceInKm = 1.60934  * intentData.getDist().getCalculated(); // miles to km
            double distanceInKm = 0.001 * details.getDist().getCalculated(); // meter to km
            //DecimalFormat df = new DecimalFormat("#.##");
            //tvdistance.setText(df.format(distanceInKm) + "km");
            tvdistance.setText(distanceInKm + "km");
        } catch (Exception e) {
            tvdistance.setText(details.getDistance() + "km");
        }
        String deliveryTime = details.getDeliveryTime();
        tvTimeAddToCard.setText(deliveryTime != null ? (deliveryTime + getString(R.string.mins)) : "0"+getString(R.string.mins));
        String minValue = details.getMinimumValue();
        String currency = details.getCurrency();
        tvMinOrderAddToCard.setText(minValue != null ? (getString(R.string.min_order) + CommonUtilities.getPriceFormat(minValue) + " " + currency) : getString(R.string.min_order)+"0 " + currency);
    }

    private void setImage(ProgressBar progressBar, final String imageUri, final ImageView imageView) {
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

    private void serviceAddToFav() {
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
                        //MyDialog.getInstance(GroceryDetails.this).hideDialog();
                        stopHeartAnimation();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (isFav) {
                                    ivFav.setImageResource(R.drawable.fav_un);
                                    isFav = false;
                                } else {
                                    ivFav.setImageResource(R.drawable.fav);
                                    isFav = true;
                                }

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(GroceryDetails.this);
                            } else {
                                Toast.makeText(GroceryDetails.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void serviceGetCategoryList() {
        try {
            MyDialog.getInstance(this).showDialog(this);
            //if (!token.isEmpty()) {
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resAndStoreId", resAndStoreId);
            jsonObject.put("langCode", langCode);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<ProductModel> beanCall = apiInterface.getCategoryList(body);

            beanCall.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    MyDialog.getInstance(GroceryDetails.this).hideDialog();
                    if (response.isSuccessful()) {
                        ProductModel model = response.body();
                        if (model.getResponse_code().equalsIgnoreCase("200")) {

                            setUpRecyclerView(model.getData());

                        } else {
                            Toast.makeText(GroceryDetails.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                }
            });
            //}
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

                if(categoryAdapter!=null)
                    categoryAdapter.getFilter().filter(newText);

                return true;
            }
        });
    }

    private void startHeartAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation("layer.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

    }

    private void stopHeartAnimation() {
        lottieAnimationView.setVisibility(GONE);
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