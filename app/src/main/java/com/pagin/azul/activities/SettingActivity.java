package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.MyProfileResponse;
import com.pagin.azul.bean.SettingResponse;
import com.pagin.azul.bean.UpdateSettingNoti;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

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

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_contactUs)
    TextView tv_contactUs;

    @BindView(R.id.tv_aboutUs)
    TextView tv_aboutUs;

    @BindView(R.id.tv_terms_condition)
    TextView tv_terms_condition;

    @BindView(R.id.tv_privacyPolicy)
    TextView tv_privacyPolicy;

    @BindView(R.id.toggleBtn)
    ToggleButton toggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getSetting();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toggleBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                notificationApi(true);
            } else {
                notificationApi(false);
            }
        });
    }


    @OnClick({R.id.tv_contactUs, R.id.tv_aboutUs, R.id.tv_terms_condition, R.id.tv_privacyPolicy, R.id.tv_logout})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_contactUs:{
                startActivity(new Intent(SettingActivity.this, ContactActivity.class));
                /*Intent intent = new Intent(SettingActivity.this, CommonWebpage.class);
                intent.putExtra("Page", "tv_contactUs");
                startActivity(intent);*/
            }
            break;

            case R.id.tv_aboutUs:

                Intent intent = new Intent(SettingActivity.this, CommonWebpage.class);
                intent.putExtra("Page", "tv_aboutUs");
                startActivity(intent);
                break;

            case R.id.tv_terms_condition:
                Intent intent1 = new Intent(SettingActivity.this, CommonWebpage.class);
                intent1.putExtra("Page", "tv_terms_condition");
                startActivity(intent1);
                break;

            case R.id.tv_privacyPolicy:
                Intent intent2 = new Intent(SettingActivity.this, CommonWebpage.class);
                intent2.putExtra("Page", "tv_privacyPolicy");
                startActivity(intent2);
                break;

            case R.id.tv_logout:
                showDialog();
                break;


        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText(R.string.areyou_sure);
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
                logOutApi();
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void logOutApi() {
        try {
            MyDialog.getInstance(this).showDialog(SettingActivity.this);
            String token = SharedPreferenceWriter.getInstance(SettingActivity.this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<MyProfileResponse> beanCall = apiInterface.logOut(token, body);

                beanCall.enqueue(new Callback<MyProfileResponse>() {
                    @Override
                    public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                        MyDialog.getInstance(SettingActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putBoolean("kIsFirstTime", true);
                                editor.commit();

                                SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(kToken, "");
                                SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN,"");
                                //startActivity(HomeMainActivity.getIntent(SettingActivity.this, ""));
                                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                                finishAffinity();
                                Toast.makeText(SettingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(SettingActivity.this);
                            } else {
                                Toast.makeText(SettingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                        Log.e("Failure", t.getMessage());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Failure", e.getMessage());
        }


    }

    private void notificationApi(Boolean noti) {
        try {
            MyDialog.getInstance(this).showDialog(SettingActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("normalUserNotification", true);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UpdateSettingNoti> beanCall = apiInterface.onOff(token, body);

                beanCall.enqueue(new Callback<UpdateSettingNoti>() {
                    @Override
                    public void onResponse(Call<UpdateSettingNoti> call, Response<UpdateSettingNoti> response) {
                        MyDialog.getInstance(SettingActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                //Toast.makeText(SettingActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateSettingNoti> call, Throwable t) {
                        Log.e("Failure", t.getMessage());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Failure", e.getMessage());
        }
    }


    private void getSetting() {
        try {
            MyDialog.getInstance(this).showDialog(SettingActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<SettingResponse> beanCall = apiInterface.getSetting(token, body);
                beanCall.enqueue(new Callback<SettingResponse>() {
                    @Override
                    public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                        MyDialog.getInstance(SettingActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getResponse().toString().equalsIgnoreCase("true")) {
                                    toggleBtn.setChecked(true);
                                } else if (response.body().getResponse().toString().equalsIgnoreCase("false")) {
                                    toggleBtn.setChecked(false);
                                }


                            } else {
                                Toast.makeText(SettingActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SettingResponse> call, Throwable t) {
                        Log.e("Failure", t.getMessage());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Failure", e.getMessage());
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
