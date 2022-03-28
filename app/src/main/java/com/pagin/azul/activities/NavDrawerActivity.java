package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.NavDrawerAdapter;
import com.pagin.azul.bean.NavDrawerModel;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
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

import static com.pagin.azul.Constant.Constants.kProfile;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;
import static com.pagin.azul.Constant.Constants.kUserName;

public class NavDrawerActivity extends AppCompatActivity {
    private ArrayList<NavDrawerModel> navDrawerModel;
    private String checktype, checkDeliveytype, checkProfessionaltrpe;
    private NavDrawerAdapter drawerAdapter;
    private boolean isClickDP = false;
    private boolean isClickPW = false;
    @BindView(R.id.rv_navdrawer)
    RecyclerView recyclerView;
    @BindView(R.id.deliveryWorker)
    ToggleButton deliveryWorker;
    @BindView(R.id.professionalWorker)
    ToggleButton professionalWorker;
    @BindView(R.id.ivMyProfile)
    ImageView ivMyProfile;
    @BindView(R.id.ll_userInfo)
    LinearLayout ll_userInfo;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    private String deliveryStatus = "", prfStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        ButterKnife.bind(this);
        navDrawerModel = new ArrayList<>();
        preParedData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerAdapter = new NavDrawerAdapter(this, navDrawerModel);
        recyclerView.setAdapter(drawerAdapter);

        deliveryWorker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (isClickDP == false) {
                    dialog("deliveryWorker");
                    isClickDP = true;
                }
            } else {
//                dialog("deliveryWorker");
                isClickDP = false;


                if (deliveryStatus.equalsIgnoreCase("true")) {
                    deliveryWorker.setChecked(true);
                }

            }
        });

        professionalWorker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (isClickPW == false) {
                    dialog("professional");
                    isClickPW = true;
                }
            } else {
//                dialog("professional");
                isClickPW = false;
                if (prfStatus.equalsIgnoreCase("true")) {
                    professionalWorker.setChecked(true);
                }

            }
        });

        if (!SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
            callCheckUserTypeApi();
        }else {
            ll_userInfo.setVisibility(View.INVISIBLE);
            tvUserName.setVisibility(View.GONE);
        }
    }

    private void preParedData() {
        navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_home), R.drawable.home));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.my_order), R.drawable.listing_ic));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.my_favorites), R.drawable.sm_fav));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.customer_services), R.drawable.sm_stories));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_address), R.drawable.history));
        //navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_profile), R.drawable.profile));
        //navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_delivry_person), R.drawable.delivery));
        //navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_profsnal_wrkr), R.drawable.professional));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_setting), R.drawable.settings));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_arabic), R.drawable.language));
        //navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_rate_us), R.drawable.rate_us));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.nav_share_earn), R.drawable.share));
        navDrawerModel.add(new NavDrawerModel(getString(R.string.contact_us), R.drawable.contact));

        /*if (SharedPreferenceWriter.getInstance(this).getString(kToken) != null && !SharedPreferenceWriter.getInstance(this).getString(kToken).equalsIgnoreCase("")) {
            ll_userInfo.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(SharedPreferenceWriter.getInstance(this).getString(kProfile))
                    .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                            .error(R.drawable.profile_default))
                    .into(ivMyProfile);
            tvUserName.setVisibility(View.VISIBLE);
            tvUserName.setText(SharedPreferenceWriter.getInstance(this).getString(kUserName));
        }else {
            ll_userInfo.setVisibility(View.INVISIBLE);
            tvUserName.setVisibility(View.GONE);
        }*/
    }

    //    All Click
    @OnClick({R.id.nav_back,R.id.ivInsta,R.id.ivFB})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_back:
                onBackPressed();
                break;

            case R.id.ivInsta:
                followLink("https://www.instagram.com/_paginazul_/");
                break;

            case R.id.ivFB:
                followLink("https://www.facebook.com/102220028205764/");
                break;
        }
    }

    private void followLink(String url) {
        Uri uri =  Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void dialog(String toggleName) {
        Dialog dialog = new Dialog(NavDrawerActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);

        if (toggleName.equalsIgnoreCase("deliveryWorker")) {
            textView.setText("You are not registered as a delivery worker yet.");
            notNow.setText("Not Now");
            registerNow.setText("Register Now");
        } else if (toggleName.equalsIgnoreCase("professional")) {
            textView.setText("You are not registered as a professional worker yet.");
            registerNow.setText("Register Now");
            notNow.setText("Not Now");
        } else if (toggleName.equalsIgnoreCase("professionalSignup")) {
            textView.setText("Profile already submitted : you can find the submitted profile under captain profile in my profile section");
            registerNow.setVisibility(View.GONE);
            notNow.setVisibility(View.GONE);
            btn_ok.setVisibility(View.VISIBLE);
        } else if (toggleName.equalsIgnoreCase("deliverySignup")) {
            textView.setText("Profile already submitted : you can find the submitted profile under captain profile in my profile section");
            registerNow.setVisibility(View.GONE);
            notNow.setVisibility(View.GONE);
            btn_ok.setVisibility(View.VISIBLE);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (deliveryStatus.equalsIgnoreCase("true")) {
                    deliveryWorker.setChecked(true);
                    isClickDP = false;
                }else {
                    deliveryWorker.setChecked(false);
                }
                if (prfStatus.equalsIgnoreCase("true")) {
                    professionalWorker.setChecked(true);
                    isClickPW = false;
                }else {
                    professionalWorker.setChecked(false);
                }


            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleName.equalsIgnoreCase("deliveryWorker")) {
                    startActivity(new Intent(NavDrawerActivity.this, BecomeDeliveryPersonActivity.class));
                    dialog.dismiss();
                } else if (toggleName.equalsIgnoreCase("professional")) {
                    startActivity(new Intent(NavDrawerActivity.this, BecomeProfessionalWorkerActivity.class));
                    dialog.dismiss();
                }
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void callCheckUserTypeApi() {
        try {
            MyDialog.getInstance(this).showDialog(NavDrawerActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<UserTypeResponse> beanCall = apiInterface.checkUserType(token, body);
                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(NavDrawerActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

//                                if (response.body().getAdminVerifyDeliveryPerson().equalsIgnoreCase("true")) {
//                                    isClickDP = true;
//                                    deliveryWorker.setChecked(true);
//                                } else if (response.body().getAdminVerifyDeliveryPerson().equalsIgnoreCase("false")) {
//                                    deliveryWorker.setChecked(false);
//                                }
//                                if (response.body().getAdminVerifyProfessionalWorker().equalsIgnoreCase("true")) {
//                                    isClickPW = true;
//                                    professionalWorker.setChecked(true);
//                                } else if (response.body().getAdminVerifyProfessionalWorker().equalsIgnoreCase("false")) {
//                                    professionalWorker.setChecked(false);
//                                }
//
//                                deliveryStatus = response.body().getSignupWithDeliveryPerson();
                                prfStatus = response.body().getSignupWithProfessionalWorker();
                                ll_userInfo.setVisibility(View.VISIBLE);
                                tvUserName.setVisibility(View.VISIBLE);
                                Glide.with(NavDrawerActivity.this)
                                        .load(signUpResponce.getProfilePic())
                                        .apply(RequestOptions.placeholderOf(R.drawable.loader)
                                                .error(R.drawable.profile_default))
                                        .into(ivMyProfile);
                                tvUserName.setText(signUpResponce.getName());
                                getCallback();


                                //Toast.makeText(MyOrderDashboardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(NavDrawerActivity.this);
                            } else {
                                Toast.makeText(NavDrawerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getCallback() {

        drawerAdapter.setListener(new NavDrawerAdapter.MemberInterface() {
            @Override
            public void onDelivery() {
                if (!deliveryStatus.equalsIgnoreCase("true")) {
                    startActivity(new Intent(NavDrawerActivity.this, BecomeDeliveryPersonActivity.class));
                } else {
                    dialog("deliverySignup");
                }

            }

            @Override
            public void onProffessional() {
                if (!prfStatus.equalsIgnoreCase("true")) {
                    startActivity(new Intent(NavDrawerActivity.this, BecomeProfessionalWorkerActivity.class));

                } else {
                    dialog("professionalSignup");

                }

            }
        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(NavDrawerActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                startActivity(new Intent(NavDrawerActivity.this, SignUpOptions.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
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
