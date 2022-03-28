package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
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

public class SignUpMobileActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edtSignInNumber)
    EditText edtPhoneNumber;
    private HashMap<String, Object> parameter;

    private String mVerificationId;


    private FirebaseAuth mAuth;


    public static Intent getIntent(Context context, HashMap<String, Object> parameter) {

        Intent intent = new Intent(context, SignUpMobileActivity.class);
        intent.putExtra("kData", parameter);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_mobile);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.signup_now);
        parameter = new HashMap<>();

        if (getIntent() != null) {
            parameter = (HashMap<String, Object>) getIntent().getSerializableExtra("kData");
        }
    }

    private void numberVerification() {
        try {
            MyDialog.getInstance(this).showDialog(SignUpMobileActivity.this);
            String token = SharedPreferenceWriter.getInstance(SignUpMobileActivity.this).getString(kToken);

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<NumberVerificationResponse> call = apiInterface.numberVerification(parameter.get("kCountryCode").toString(),
                        edtPhoneNumber.getText().toString().trim(),SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                call.enqueue(new Callback<NumberVerificationResponse>() {
                    @Override
                    public void onResponse(Call<NumberVerificationResponse> call, Response<NumberVerificationResponse> response) {
                        MyDialog.getInstance(SignUpMobileActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NumberVerificationResponse signUpWithMobileResp = response.body();
                            if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {

                                Gson gson = new Gson();
                                String data = gson.toJson(response.body());
                                SharedPreferenceWriter.getInstance(SignUpMobileActivity.this).writeStringValue("kSignUpData", data);

                                showBottomSheet();


                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(SignUpMobileActivity.this);
                            } else {
                                Toast.makeText(SignUpMobileActivity.this, "" + signUpWithMobileResp.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(SignUpMobileActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<NumberVerificationResponse> call, Throwable t) {
                        MyDialog.getInstance(SignUpMobileActivity.this).hideDialog();
                        t.printStackTrace();

                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnClick({R.id.signInSubmitBtn, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInSubmitBtn:
                if (edtPhoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(parameter != null) {
                    parameter.put("kPhone", edtPhoneNumber.getText().toString().trim());
                }
                numberVerification();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void showBottomSheet() {
        SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("commingTypeMobile", "SignUp");
        editor.commit();
        OTPBottomSheet sheet = new OTPBottomSheet();
        sheet.setCancelable(false);
        sheet.Data(parameter);
        sheet.show(getSupportFragmentManager(), "OTPBottomSheet");
//        finish();
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
