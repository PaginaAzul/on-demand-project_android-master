package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.CommentsAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;

public class UserDetailsActivity extends AppCompatActivity {
    private ArrayList<NormalUserPendingOrderInner> userCommentsList;
    private CommentsAdapter commentsAdapter;
    @BindView(R.id.rv_comments)
    RecyclerView recyclerViewComments;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_prfpic)
    ImageView iv_prfpic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_rat_num)
    TextView tv_rat_num;
    @BindView(R.id.tvAllRatings)
    TextView tvAllRatings;
    @BindView(R.id.no_data)
    TextView no_data;
    private String commingfrom;
    private NormalUserPendingOrderInner getDataInner;

    public static Intent getIntent(Context context, NormalUserPendingOrderInner getDataInner, String commingfrom) {
        Intent intent = new Intent(context, UserDetailsActivity.class);
        intent.putExtra("kData", (Serializable) getDataInner);
        intent.putExtra("kCome", (Serializable) commingfrom);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        userCommentsList = new ArrayList<>();

        if (getIntent() != null) {
            getDataInner = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            commingfrom = (String) getIntent().getStringExtra("kCome");
        }
        recyclerViewComments.addItemDecoration(new DividerItemRecyclerDecoration(this, R.drawable.canvas_recycler_diver_list));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });
        if (commingfrom.equalsIgnoreCase("FromNUActive")) {
            callAllRatingApi(getDataInner.getOfferAcceptedOfId());
        } else if (commingfrom.equalsIgnoreCase("FromNewDP")) {
            callAllRatingApi(getDataInner.getUserId());
        } else if (commingfrom.equalsIgnoreCase("FromActiveDP")) {
            callAllRatingApi(getDataInner.getOfferAcceptedById());
        } else if (commingfrom.equalsIgnoreCase("FromViewOffer")) {
            callAllRatingApi(getDataInner.getMakeOfferById());
        } else if (commingfrom.equalsIgnoreCase("FromNewPW")) {
            callAllRatingApi(getDataInner.getUserId());
        } else if (commingfrom.equalsIgnoreCase("FromPastPW")) {
            callAllRatingApi(getDataInner.getOrderOwner());
        } else if (commingfrom.equalsIgnoreCase("FromActivePW")) {
            callAllRatingApi(getDataInner.getOfferAcceptedById());
        } else if (commingfrom.equalsIgnoreCase("FromPendingDP")) {
            callAllRatingApi(getDataInner.getOrderOwner());
        }else if (commingfrom.equalsIgnoreCase(MapProfessinalWorkerActivity.class.getSimpleName())) {
            callAllRatingApi(getDataInner.getMakeOfferById());
        }else if (commingfrom.equalsIgnoreCase(DeliveryMakeOfferActivity.class.getSimpleName())) {
            callAllRatingApi(getDataInner.getUserId());
        }
    }

    private void callAllRatingApi(String userId) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getAllRating(token,body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(UserDetailsActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                tv_profile_name.setText(response.body().getName());
                                tvAllRatings.setText(response.body().getTotalRating() +" "+ getResources().getString(R.string.rating_all));
                                tv_rat_num.setText(response.body().getAvgRating());
                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    userCommentsList = response.body().getDataList();
                                    tvAllRatings.setText(response.body().getTotalRating() +" "+ getResources().getString(R.string.rating_all)+"(Given by "+userCommentsList.size()+" user)");

                                    recyclerViewComments.setLayoutManager(new LinearLayoutManager(UserDetailsActivity.this));
                                    commentsAdapter = new CommentsAdapter(UserDetailsActivity.this, userCommentsList);
                                    recyclerViewComments.setAdapter(commentsAdapter);
                                    no_data.setVisibility(View.GONE);
                                    recyclerViewComments.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    recyclerViewComments.setVisibility(View.GONE);
                                }


                                if (!response.body().getProfilePic().equalsIgnoreCase("")) {
                                    Picasso.with(UserDetailsActivity.this).load(response.body().getProfilePic()).into(iv_prfpic);
                                }



                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(UserDetailsActivity.this);
                            } else {
                                Toast.makeText(UserDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
