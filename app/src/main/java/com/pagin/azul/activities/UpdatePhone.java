package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.pagin.azul.BottomSheet.OTPBottomSheet;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.MyProfileResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

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

public class UpdatePhone extends AppCompatActivity {
    @BindView(R.id.updatePhoneSubmitBtn)
    Button updatePhoneSubmitBtn;
    @BindView(R.id.edtNumberUpdatePhone)
    EditText edtNumberUpdatePhone;
    @BindView(R.id.tv_title)
    TextView tvtitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    private CountryCodePicker ccp;
    private String countryCodeAndroid = "+1";
    private String cmFrom = "";

    private HashMap<String, Object> parameter;


    public static Intent getIntent(Context context, HashMap<String, Object> parameter) {

        Intent intent = new Intent(context, UpdatePhone.class);
        intent.putExtra("kData", parameter);

        return intent;
    }


    public static Intent getIntentProfile(Context context, String from) {

        Intent intent = new Intent(context, UpdatePhone.class);
        intent.putExtra("kFrom", from);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        ButterKnife.bind(this);

        tvtitle.setText(R.string.update_phone);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setCountryForPhoneCode(244);
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
        }else {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
        }
        parameter = new HashMap<>();

        if (getIntent() != null) {
            cmFrom = (String) getIntent().getStringExtra("kFrom");
        }


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCodeWithPlus();
                android.util.Log.d("Country Code", countryCodeAndroid);
            }
        });


    }

    @OnClick({R.id.updatePhoneSubmitBtn, R.id.iv_back})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updatePhoneSubmitBtn:
                if (edtNumberUpdatePhone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (cmFrom.equalsIgnoreCase("Profile")) {
                        callMobileNumberChangeApi();
                    }
                    //showBottomSheet();

                }
                parameter.put("kPhone", edtNumberUpdatePhone.getText().toString());
                parameter.put("kCountryCode", ccp.getSelectedCountryCodeWithPlus());

                break;


            case R.id.iv_back:
                onBackPressed();
                break;
        }

    }


    private void callMobileNumberChangeApi() {
        try {
            MyDialog.getInstance(this).showDialog(UpdatePhone.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("mobileNumber", edtNumberUpdatePhone.getText().toString());
                jsonObject.put("countryCode", ccp.getSelectedCountryCodeWithPlus());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<MyProfileResponse> beanCall = apiInterface.mobileNumberChange(token, body);
                beanCall.enqueue(new Callback<MyProfileResponse>() {
                    @Override
                    public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                        MyDialog.getInstance(UpdatePhone.this).hideDialog();
                        if (response.isSuccessful()) {
                            MyProfileResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                showBottomSheet();
                                //Toast.makeText(UpdatePhone.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(UpdatePhone.this);
                            } else {
                                Toast.makeText(UpdatePhone.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<MyProfileResponse> call, Throwable t) {

                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void showBottomSheet() {

        SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("commingTypeMobile", "Update Phone");
        editor.commit();

        OTPBottomSheet sheet = new OTPBottomSheet();
        sheet.Data(parameter);
        sheet.setCancelable(false);
        sheet.show(getSupportFragmentManager(), "OTPBottomSheet");

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
