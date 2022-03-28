package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.MyProfile;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.LocaleHelper;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.pagin.azul.Constant.Constants.kAppLaunchMode;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivOffers)
    ImageView ivOffers;

    @BindView(R.id.ivGrocery)
    ImageView ivGrocery;

    @BindView(R.id.ivCategory)
    ImageView ivCategory;

    @BindView(R.id.tvGrocery)
    TextView tvGrocery;

    @BindView(R.id.tvCategoryName)
    TextView tvCategoryName;

    @BindView(R.id.progressbarOffer)
    ProgressBar progressbarOffer;

    @BindView(R.id.progressbarShopping)
    ProgressBar progressbarShopping;

    @BindView(R.id.progressbarServices)
    ProgressBar progressbarServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_main_on_phase);
        ButterKnife.bind(this);
        SharedPreferenceWriter.getInstance(this).writeStringValue(kAppLaunchMode, "true");
        getIntentData();
        setSideMenuIcon();
        serviceGetDashboardData();
    }

    @OnClick({R.id.clOffers, R.id.clCategory, R.id.clGrocery, R.id.ivOffer, R.id.ivMeals, R.id.ivShopping, R.id.ivServices})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clOffers:
            case R.id.ivOffer:
                dispatchOffer();
                break;
            case R.id.clCategory:
            case R.id.ivServices:
                dispatchHomeMain();
                break;
            case R.id.clGrocery:
            case R.id.ivMeals:
            case R.id.ivShopping:
                dispatchGrocery();
                break;
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            if (Objects.requireNonNull(getIntent().getStringExtra(ParamEnum.TYPE.theValue())).equalsIgnoreCase(FoodAndGroceryActivity.class.getSimpleName())) {
                Intent intent = new Intent(MainActivity.this, FoodAndGroceryActivity.class);
                intent.putExtra(ParamEnum.TYPE.theValue(), MyProfile.class.getSimpleName());
                startActivity(intent);
            } else {
                startActivity(HomeMainActivity.getIntent(MainActivity.this, MyProfile.class.getSimpleName()));
            }
        }
    }

    private void dispatchOffer() {
        startActivity(new Intent(this, OffersActivity.class));
    }

    private void dispatchHomeMain() {
        startActivity(HomeMainActivity.getIntent(MainActivity.this, ""));
    }

    private void dispatchGrocery() {
        startActivity(new Intent(this, FoodAndGroceryActivity.class));
    }

    private void setSideMenuIcon() {
        tvTitle.setText(getString(R.string.home));
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MainActivity.this, NavDrawerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLanguage() {
        // check language from data base
        if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")) {
            if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                LocaleHelper.setLocale(this, "en");
            } else {
                LocaleHelper.setLocale(this, "pt");
            }
        } else {
            LocaleHelper.setLocale(this, "pt");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE, "true");
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void serviceGetDashboardData() {
        try {
            MyDialog.getInstance(this).showDialog(this);
            //String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            //if (!token.isEmpty()) {
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            Call<RestaurantModel> beanCall = apiInterface.getDashboardData();

            beanCall.enqueue(new Callback<RestaurantModel>() {
                @Override
                public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                    MyDialog.getInstance(MainActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        RestaurantModel restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                            setData(restaurantModel.getData());

                        } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RestaurantModel> call, Throwable t) {
                }
            });
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(RestaurantResponse data) {
        String image = data.getHomeBanner().getImage();
        if (image != null && !image.equals("")) {
            setImage(progressbarOffer, image, ivOffers);
        } else {
            progressbarOffer.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.default_cat).override(110, 150).into(ivOffers);
        }

        RestaurantResponse shoppingDetails = data.getMainService().get(0);
        String imageShopping = shoppingDetails.getImage();
        if (imageShopping != null && !imageShopping.equals("")) {
            setImage(progressbarShopping, imageShopping, ivGrocery);
        } else {
            progressbarShopping.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.default_cat).override(110, 150).into(ivGrocery);
        }

        RestaurantResponse serviceDetails = data.getMainService().get(1);
        String imageService = serviceDetails.getImage();
        if (imageService != null && !imageService.equals("")) {
            setImage(progressbarServices, imageService, ivCategory);
        } else {
            progressbarServices.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.default_cat).override(110, 150).into(ivCategory);
        }

        /*if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
            tvGrocery.setText(shoppingDetails.getEnglishName());
            tvCategoryName.setText(serviceDetails.getEnglishName());
        } else {
            tvGrocery.setText(shoppingDetails.getPortName());
            tvCategoryName.setText(serviceDetails.getPortName());
        }*/
        tvGrocery.setText(shoppingDetails.getEnglishName());
        tvCategoryName.setText(serviceDetails.getEnglishName());
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

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")) {
                if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                } else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            } else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        } else {
            super.attachBaseContext(newBase);
        }
    }
}