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
import com.pagin.azul.bean.NumberVerificationResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;

public class SignIn extends AppCompatActivity {
    private CountryCodePicker ccp;
    private String countryCodeAndroid = "+966";

    @BindView(R.id.signInSubmitBtn)
    Button signInSubmitBtn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edtSignInNumber)
    EditText edtPhoneNumber;
    private HashMap<String, Object> parameter;
    @BindView(R.id.iv_back)
    ImageView iv_back;




    public static Intent getIntent(Context context) {
        return new Intent(context, SignIn.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        CommonUtility.getDeviceToken(this);
        //iv_back.setVisibility(View.GONE);
        tvTitle.setText(R.string.signInNow);
        parameter = new HashMap<>();
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setCountryForPhoneCode(244);
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
        }else {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
        }
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCodeWithPlus();

                android.util.Log.d("Country Code", countryCodeAndroid);
            }
        });


    }


    @OnClick({R.id.signInSubmitBtn, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInSubmitBtn:
                if (edtPhoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                } else {
                    checkNumberForSignin();
                }

                parameter.put("kPhone", edtPhoneNumber.getText().toString().trim());
                parameter.put("kCountryCode", ccp.getSelectedCountryCodeWithPlus());


                // signIn();

                break;
            case R.id.iv_back:
                onBackPressed();
        }
    }


    private void checkNumberForSignin() {
        try {
            MyDialog.getInstance(this).showDialog(SignIn.this);
            String token = SharedPreferenceWriter.getInstance(SignIn.this).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            Call<NumberVerificationResponse> call = apiInterface.checkNumberForSignin(ccp.getSelectedCountryCodeWithPlus(),
                    edtPhoneNumber.getText().toString().trim(),"User",SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
            call.enqueue(new Callback<NumberVerificationResponse>() {
                @Override
                public void onResponse(Call<NumberVerificationResponse> call, Response<NumberVerificationResponse> response) {
                    MyDialog.getInstance(SignIn.this).hideDialog();
                    if (response.isSuccessful()) {
                        NumberVerificationResponse signUpWithMobileResp = response.body();
                        if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {

                            showBottomSheet();

                            //Toast.makeText(SignIn.this, "" + signUpWithMobileResp.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignIn.this, "" + signUpWithMobileResp.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                        CommonUtility.showDialog1(SignIn.this);
                    } else {
                        Toast.makeText(SignIn.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<NumberVerificationResponse> call, Throwable t) {
                    MyDialog.getInstance(SignIn.this).hideDialog();
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showBottomSheet() {

        SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("commingTypeMobile", "SignIn");
        editor.commit();

        OTPBottomSheet sheet = new OTPBottomSheet();
        sheet.Data(parameter);
        sheet.setCancelable(false);
        sheet.show(getSupportFragmentManager(), "OTPBottomSheet");

    }
//Pick and Show location from Map
//PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//        startActivityForResult(builder.build(this), 1);
//    } catch (
//    GooglePlayServicesRepairableException e) {
//        e.printStackTrace();
//    } catch (
//    GooglePlayServicesNotAvailableException e) {
//        e.printStackTrace();
//    }

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
