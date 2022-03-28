package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.SignUpWithMobileResp;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.PreviousAdapter;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class RatingAndRiviewActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.ll_very_bad)
    LinearLayout ll_very_bad;
    @BindView(R.id.ll_bad)
    LinearLayout ll_bad;
    @BindView(R.id.ll_good)
    LinearLayout ll_good;
    @BindView(R.id.ll_very_good)
    LinearLayout ll_very_good;
    @BindView(R.id.ll_excellent)
    LinearLayout ll_excellent;
    @BindView(R.id.iv_very_bad)
    ImageView iv_very_bad;
    @BindView(R.id.tv_very_bad)
    TextView tv_very_bad;
    @BindView(R.id.iv_bad)
    ImageView iv_bad;
    @BindView(R.id.tv_bad)
    TextView tv_bad;
    @BindView(R.id.iv_good)
    ImageView iv_good;
    @BindView(R.id.tv_good)
    TextView tv_good;
    @BindView(R.id.iv_very_good)
    ImageView iv_very_good;
    @BindView(R.id.tv_very_good)
    TextView tv_very_good;
    @BindView(R.id.iv_excellent)
    ImageView iv_excellent;
    @BindView(R.id.tv_excellent)
    TextView tv_excellent;
    @BindView(R.id.edt_comment)
    EditText edt_comment;
    @BindView(R.id.rl_quick_service)
    RelativeLayout rl_quick_service;
    @BindView(R.id.tv_quick_service)
    TextView tv_quick_service;
    @BindView(R.id.rl_gental)
    RelativeLayout rl_gental;
    @BindView(R.id.tv_gental)
    TextView tv_gental;
    @BindView(R.id.rl_professional)
    RelativeLayout rl_professional;
    @BindView(R.id.tv_professional)
    TextView tv_professional;
    @BindView(R.id.rl_responsive)
    RelativeLayout rl_responsive;
    @BindView(R.id.tv_responsive)
    TextView tv_responsive;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private String rate = "5";
    private String ratingMessage = "Quick Service";
    private NormalUserPendingOrderInner getDataInner;
    private String fromcomming;

    public static Intent getIntent(Context context, NormalUserPendingOrderInner getDataInner, String fromcomming) {
        Intent intent = new Intent(context, RatingAndRiviewActivity.class);
        intent.putExtra("kData", (Serializable) getDataInner);
        intent.putExtra("kComming", (String) fromcomming);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_and_riview);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            getDataInner = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            fromcomming = (String) getIntent().getStringExtra("kComming");
        }


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @OnClick({R.id.btn_back, R.id.ll_very_bad, R.id.ll_bad, R.id.ll_good, R.id.ll_very_good,
            R.id.ll_excellent, R.id.rl_quick_service, R.id.rl_gental, R.id.rl_professional, R.id.rl_responsive, R.id.btn_submit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.rl_quick_service:
                ratingMessage = "";
                ratingMessage = "Quick Service";
                rl_quick_service.setBackground(getResources().getDrawable(R.drawable.bg_sign_in));
                rl_gental.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_professional.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_responsive.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                tv_quick_service.setTextColor(getResources().getColor(R.color.white));
                tv_gental.setTextColor(getResources().getColor(R.color.black));
                tv_professional.setTextColor(getResources().getColor(R.color.black));
                tv_responsive.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.rl_gental:
                ratingMessage = "";
                ratingMessage = "Gental";
                rl_quick_service.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_gental.setBackground(getResources().getDrawable(R.drawable.bg_sign_in));
                rl_professional.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_responsive.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                tv_quick_service.setTextColor(getResources().getColor(R.color.black));
                tv_gental.setTextColor(getResources().getColor(R.color.white));
                tv_professional.setTextColor(getResources().getColor(R.color.black));
                tv_responsive.setTextColor(getResources().getColor(R.color.black));

                break;
            case R.id.rl_professional:
                ratingMessage = "";
                ratingMessage = "Professional";
                rl_quick_service.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_gental.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_professional.setBackground(getResources().getDrawable(R.drawable.bg_sign_in));
                rl_responsive.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                tv_quick_service.setTextColor(getResources().getColor(R.color.black));
                tv_gental.setTextColor(getResources().getColor(R.color.black));
                tv_professional.setTextColor(getResources().getColor(R.color.white));
                tv_responsive.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.rl_responsive:
                ratingMessage = "";
                ratingMessage = "Responsive";
                rl_quick_service.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_gental.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_professional.setBackground(getResources().getDrawable(R.drawable.edit_txt_back));
                rl_responsive.setBackground(getResources().getDrawable(R.drawable.bg_sign_in));
                tv_quick_service.setTextColor(getResources().getColor(R.color.black));
                tv_gental.setTextColor(getResources().getColor(R.color.black));
                tv_professional.setTextColor(getResources().getColor(R.color.black));
                tv_responsive.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.ll_very_bad:
                rate = "";
                rate = "1";
                iv_very_bad.setImageDrawable(getResources().getDrawable(R.drawable.very_bad_s));
                iv_bad.setImageDrawable(getResources().getDrawable(R.drawable.bad));
                iv_good.setImageDrawable(getResources().getDrawable(R.drawable.good));
                iv_very_good.setImageDrawable(getResources().getDrawable(R.drawable.very_good));
                iv_excellent.setImageDrawable(getResources().getDrawable(R.drawable.excellent));
                tv_very_good.setTextColor(getResources().getColor(R.color.black));
                tv_excellent.setTextColor(getResources().getColor(R.color.black));
                tv_good.setTextColor(getResources().getColor(R.color.black));
                tv_bad.setTextColor(getResources().getColor(R.color.black));
                tv_very_bad.setTextColor(getResources().getColor(R.color.colorPink));
                break;
            case R.id.ll_bad:
                rate = "";
                rate = "2";
                iv_very_bad.setImageDrawable(getResources().getDrawable(R.drawable.very_bad));
                iv_bad.setImageDrawable(getResources().getDrawable(R.drawable.bad_s));
                iv_good.setImageDrawable(getResources().getDrawable(R.drawable.good));
                iv_very_good.setImageDrawable(getResources().getDrawable(R.drawable.very_good));
                iv_excellent.setImageDrawable(getResources().getDrawable(R.drawable.excellent));
                tv_very_good.setTextColor(getResources().getColor(R.color.black));
                tv_excellent.setTextColor(getResources().getColor(R.color.black));
                tv_good.setTextColor(getResources().getColor(R.color.black));
                tv_bad.setTextColor(getResources().getColor(R.color.colorPink));
                tv_very_bad.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.ll_good:
                rate = "";
                rate = "3";
                iv_very_bad.setImageDrawable(getResources().getDrawable(R.drawable.very_bad));
                iv_bad.setImageDrawable(getResources().getDrawable(R.drawable.bad));
                iv_good.setImageDrawable(getResources().getDrawable(R.drawable.good_s));
                iv_very_good.setImageDrawable(getResources().getDrawable(R.drawable.very_good));
                iv_excellent.setImageDrawable(getResources().getDrawable(R.drawable.excellent));
                tv_very_good.setTextColor(getResources().getColor(R.color.black));
                tv_excellent.setTextColor(getResources().getColor(R.color.black));
                tv_good.setTextColor(getResources().getColor(R.color.colorPink));
                tv_bad.setTextColor(getResources().getColor(R.color.black));
                tv_very_bad.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.ll_very_good:
                rate = "";
                rate = "4";
                iv_very_bad.setImageDrawable(getResources().getDrawable(R.drawable.very_bad));
                iv_bad.setImageDrawable(getResources().getDrawable(R.drawable.bad));
                iv_good.setImageDrawable(getResources().getDrawable(R.drawable.good));
                iv_very_good.setImageDrawable(getResources().getDrawable(R.drawable.very_good_s));
                iv_excellent.setImageDrawable(getResources().getDrawable(R.drawable.excellent));
                tv_very_good.setTextColor(getResources().getColor(R.color.colorPink));
                tv_excellent.setTextColor(getResources().getColor(R.color.black));
                tv_good.setTextColor(getResources().getColor(R.color.black));
                tv_bad.setTextColor(getResources().getColor(R.color.black));
                tv_very_bad.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.ll_excellent:
                rate = "";
                rate = "5";
                iv_very_bad.setImageDrawable(getResources().getDrawable(R.drawable.very_bad));
                iv_bad.setImageDrawable(getResources().getDrawable(R.drawable.bad));
                iv_good.setImageDrawable(getResources().getDrawable(R.drawable.good));
                iv_very_good.setImageDrawable(getResources().getDrawable(R.drawable.very_good));
                iv_excellent.setImageDrawable(getResources().getDrawable(R.drawable.excellent_s));
                tv_excellent.setTextColor(getResources().getColor(R.color.colorPink));
                tv_very_good.setTextColor(getResources().getColor(R.color.black));
                tv_good.setTextColor(getResources().getColor(R.color.black));
                tv_bad.setTextColor(getResources().getColor(R.color.black));
                tv_very_bad.setTextColor(getResources().getColor(R.color.black));
                break;

            case R.id.btn_submit:
                if(fromcomming!=null){
                    if (fromcomming.equalsIgnoreCase("PastDP")) {
                        callRatingApi(getDataInner.getOfferAcceptedById(), getDataInner.getRealOrderId(), "DeliveryPerson", "NormalUser");
                    } else if (fromcomming.equalsIgnoreCase("PastNUD")) {
                        callRatingApi(getDataInner.getOfferAcceptedOfId(), getDataInner.getOfferId(), "NormalUser", "DeliveryPerson");
                    } else if (fromcomming.equalsIgnoreCase("PastNUP")) {
                        callRatingApi(getDataInner.getOfferAcceptedOfId(), getDataInner.getOfferId(), "NormalUser", "ProfessionalWorker");
                    } else if (fromcomming.equalsIgnoreCase("PastPW")) {
                        callRatingApi(getDataInner.getOfferAcceptedById(), getDataInner.getRealOrderId(), "ProfessionalWorker", "NormalUser");
                    }else if (fromcomming.equalsIgnoreCase(DeliveryMessageTrackActivity.class.getSimpleName())) {
                        if(getDataInner.getServiceType().equalsIgnoreCase("DeliveryPersion"))
                            callRatingApi(getDataInner.getOfferAcceptedById(), getDataInner.getRealOrderId(), "DeliveryPerson", "NormalUser");
                        else
                            callRatingApi(getDataInner.getOfferAcceptedById(), getDataInner.getRealOrderId(), "ProfessionalWorker", "NormalUser");
                    }else if (fromcomming.equalsIgnoreCase(NormalMessageTrackActivity.class.getSimpleName())) {
                        if(getDataInner.getServiceType().equalsIgnoreCase("DeliveryPersion"))
                            callRatingApi(getDataInner.getOfferAcceptedById(), getDataInner.getRealOrderId(), "NormalUser", "DeliveryPerson");
                        else
                            callRatingApi(getDataInner.getOfferAcceptedById(), getDataInner.getOfferId(), "NormalUser", "ProfessionalWorker");
                    }else if (fromcomming.equalsIgnoreCase(PreviousAdapter.class.getSimpleName())){
                        serviceResAndStoreRating(getIntent().getStringExtra(ParamEnum.ID.theValue()),getIntent().getStringExtra(ParamEnum.ORDER_ID.theValue()));
                    }else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void callRatingApi(String ratingTo, String OrderId, String ratingByType, String ratingToType) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ratingBy", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("ratingTo", ratingTo);
                jsonObject.put("ratingMessage", ratingMessage);
                jsonObject.put("comments", edt_comment.getText().toString().trim());
                jsonObject.put("rate", rate);
                jsonObject.put("orderId", OrderId);
                jsonObject.put("ratingByType", ratingByType);
                jsonObject.put("ratingToType", ratingToType);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<SignUpWithMobileResp> beanCall = apiInterface.rating(token,body);
                beanCall.enqueue(new Callback<SignUpWithMobileResp>() {
                    @Override
                    public void onResponse(Call<SignUpWithMobileResp> call, Response<SignUpWithMobileResp> response) {
                        MyDialog.getInstance(RatingAndRiviewActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            SignUpWithMobileResp signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if(fromcomming.equalsIgnoreCase(DeliveryMessageTrackActivity.class.getSimpleName())){
                                    if(getDataInner.getServiceType().equalsIgnoreCase("DeliveryPersion")){
                                        startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(RatingAndRiviewActivity.this,RatingAndRiviewActivity.class.getSimpleName()));
                                        finish();
                                    } else{
                                        startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(RatingAndRiviewActivity.this,RatingAndRiviewActivity.class.getSimpleName()));
                                        finish();
                                    }
                                }else if(fromcomming.equalsIgnoreCase("PastNUP")){
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("kFromCat", "Professional");
                                    hashMap.put("kFrom", "Past");

                                    startActivity(HomeMainActivity.getIntent(RatingAndRiviewActivity.this, hashMap));

                                }else if(fromcomming.equalsIgnoreCase("PastNUD")){

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("kFromCat", "Delivery");
                                    hashMap.put("kFrom", "Past");

                                    startActivity(HomeMainActivity.getIntent(RatingAndRiviewActivity.this, hashMap));

                                } else {
                                    finish();
                                }

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(RatingAndRiviewActivity.this);
                            } else {
                                Toast.makeText(RatingAndRiviewActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpWithMobileResp> call, Throwable t) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serviceResAndStoreRating(String resAndStoreId, String orderId) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("orderId", orderId);
                jsonObject.put("rating", rate);
                jsonObject.put("review", edt_comment.getText().toString().trim());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<SignUpWithMobileResp> beanCall = apiInterface.resAndStoreRating(token,body);
                beanCall.enqueue(new Callback<SignUpWithMobileResp>() {
                    @Override
                    public void onResponse(Call<SignUpWithMobileResp> call, Response<SignUpWithMobileResp> response) {
                        MyDialog.getInstance(RatingAndRiviewActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            SignUpWithMobileResp signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                finish();

                                Toast.makeText(RatingAndRiviewActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(RatingAndRiviewActivity.this);
                            } else {
                                Toast.makeText(RatingAndRiviewActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpWithMobileResp> call, Throwable t) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if(fromcomming!=null){
            if(fromcomming.equalsIgnoreCase(DeliveryMessageTrackActivity.class.getSimpleName())){
                if(getDataInner.getServiceType().equalsIgnoreCase("DeliveryPersion")){
                    startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(RatingAndRiviewActivity.this,RatingAndRiviewActivity.class.getSimpleName()));
                    finish();
                } else{
                    startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(RatingAndRiviewActivity.this,RatingAndRiviewActivity.class.getSimpleName()));
                    finish();
                }
            }else{
                //startActivity(HomeMainActivity.getIntent(this, ""));
                finish();
            }
        }else{
            //startActivity(HomeMainActivity.getIntent(this, ""));
            finish();
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
