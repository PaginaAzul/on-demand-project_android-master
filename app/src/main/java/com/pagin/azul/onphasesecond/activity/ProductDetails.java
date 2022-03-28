package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.bottomsheet.RestroInfoBottomSheet;
import com.pagin.azul.onphasesecond.model.ProductDetailModel;
import com.pagin.azul.onphasesecond.model.ProductDetailsResponse;
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

public class ProductDetails extends AppCompatActivity {

    @BindView(R.id.ivRestroImage)
    ImageView ivRestroImage;

    @BindView(R.id.ivFav)
    ImageView ivFav;

    @BindView(R.id.tvdistance)
    TextView tvdistance;

    @BindView(R.id.tvProductName)
    TextView tvProductName;

    @BindView(R.id.tvAvgRating)
    TextView tvAvgRating;

    @BindView(R.id.tvReview)
    TextView tvReview;

    @BindView(R.id.tvStoreName)
    TextView tvStoreName;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvDes)
    TextView tvDes;

    @BindView(R.id.tvQuantity)
    TextView tvQuantity;

    @BindView(R.id.ivRating)
    RatingBar ivRating;

    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    private String token;
    private String userId;
    private String langCode;
    private String type="";
    private ProductDetailsResponse details;
    private ArrayList<RatingResponse> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        getIntentData();
    }

    @OnClick({R.id.ivBackAddToCart,R.id.tvAddToCart,R.id.tvReview})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ivBackAddToCart:
                onBackPressed();
                break;
            case R.id.tvAddToCart:
                dispatchAddToCart();
                break;
            case R.id.tvReview:
                dispatchRatingReview();
                break;
        }
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            type = getIntent().getStringExtra(ParamEnum.TYPE.theValue());
            serviceProductDetail(getIntent().getStringExtra(ParamEnum.ID.theValue()));
        }
    }

    private void dispatchAddToCart() {
        if(token!=null && !token.equals("")){
            Intent intent = new Intent(this, ScheduleMyCart.class);
            intent.putExtra(ParamEnum.TYPE.theValue(),type);
            startActivity(intent);
        }else{
            CommonUtilities.showDialog(this);
        }
    }

    private void dispatchRatingReview() {
        if(token!=null && !token.equals("")){
            if(details.getRating()!=null && !details.getRating().isEmpty()){
                Intent intent = new Intent(this, RatingAndReviewActivity.class);
                intent.putExtra(ParamEnum.DATA.theValue(),details);
                intent.putExtra(ParamEnum.DATA_LIST.theValue(),details.getRating());
                startActivity(intent);
            }
        }else{
            CommonUtilities.showDialog(this);
        }
    }

    private void serviceProductDetail(String productId) {
        try {
            MyDialog.getInstance(this).showDialog(this);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("langCode", langCode);
            jsonObject.put("productId", productId);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<ProductDetailModel> beanCall = apiInterface.productDetail(token,body);

            beanCall.enqueue(new Callback<ProductDetailModel>() {
                @Override
                public void onResponse(Call<ProductDetailModel> call, Response<ProductDetailModel> response) {
                    MyDialog.getInstance(ProductDetails.this).hideDialog();
                    if (response.isSuccessful()) {
                        ProductDetailModel restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                            setData(restaurantModel.getData());
                        }else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(ProductDetails.this);
                        }else {
                            Toast.makeText(ProductDetails.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProductDetailModel> call, Throwable t) {
                    Log.d(ProductDetails.class.getSimpleName(),"onFailure()");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(ProductDetailsResponse details) {
        this.details = details;
        String image = details.getProductImage();
        if(image!=null && !image.equals("")){
            setImage(progressbar,image,ivRestroImage);
        }else {
            progressbar.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.food_thali).override(110, 150).into(ivRestroImage);
        }
        tvProductName.setText(details.getProductName());
        String avgRating = details.getAvgRating();
        tvAvgRating.setText(avgRating);
        try{
            ivRating.setRating(Float.parseFloat(avgRating));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        tvReview.setText("("+details.getTotalRating()+getString(R.string.small_reviews)+")");
        tvStoreName.setText(details.getResAndStoreId().getName()+getString(R.string.store_24));
        tvPrice.setText(CommonUtilities.getPriceFormat(details.getPrice())+" "+details.getCurrency());
        tvDes.setText(details.getDescription());
        tvQuantity.setText(getString(R.string.quantity)+details.getQuantity());
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