package com.pagin.azul.BottomSheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.MyProfile;
import com.pagin.azul.activities.SignIn;
import com.pagin.azul.activities.SignUpMobileActivity;
import com.pagin.azul.activities.SplashActivity;
import com.pagin.azul.activities.UpdatePhone;
import com.pagin.azul.bean.NumberVerificationResponse;
import com.pagin.azul.bean.SignInResponse;
import com.pagin.azul.bean.SignUpWithMobileResp;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.LocaleHelper;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kProfile;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;
import static com.pagin.azul.Constant.Constants.kUserName;

public class OTPBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.pinview)
    Pinview pinview;
    @BindView(R.id.edtNumberOTP)
    TextView edtPhoneNumber;
    @BindView(R.id.tvtimeOTP)
    TextView tvOtp;
    HashMap<String, Object> parameter;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    Context mContext;
    private String commingType = "";
    private NumberVerificationResponse signUpWithMobileResp;
    private String verificationCode;
    private FirebaseAuth mAuth;
    private CountDownTimer yourCountDownTimer;
    private int second = 0;
    private HashMap<String, RequestBody> userData;

    public void Data(HashMap<String, Object> para) {
        this.parameter = para;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        StartFirebaseLogin();

        if (parameter != null) {
            otpGeneretByFirebae(parameter.get("kCountryCode").toString(), parameter.get("kPhone").toString());
            edtPhoneNumber.setText(parameter.get("kCountryCode").toString() + " " + parameter.get("kPhone").toString());
        }

        userData = new HashMap<>();

        getOtpScreen();

        return view;
    }


    void getOtpScreen() {
        yourCountDownTimer = new CountDownTimer(180000, 1000) {                     //geriye sayma
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                try{
                    tvOtp.setText(f.format(min) + ":" + f.format(sec) + " "+getString(R.string.sec));
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }

                if (pinview.getValue().length() == 6) {
                    SharedPreferences sharedpreferences = mContext.getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
                    commingType = sharedpreferences.getString("commingTypeMobile", null);

                    if (commingType.equalsIgnoreCase("SignUp") && commingType != null) {
                        showConfirmation();
                       /* SharedPreferences.Editor editor = sharedpreferences.edit_white_back();
                        editor.putString("commingTypeMobile", "");
                        editor.commit();*/
                    } else {
                        showConfirmation();
                      /*  SharedPreferences.Editor editor = sharedpreferences.edit_white_back();
                        editor.putString("commingTypeMobile", "");
                        editor.commit();*/
                        //otpVerification();
                    }
                    yourCountDownTimer.cancel();

                }
            }

            public void onFinish() {
                try{
                    tvOtp.setText("00:00 "+getString(R.string.sec));
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }

            }
        }.start();

        //yourCountDownTimer.start();
    }


    private void showWelcomeDialog(String signIn, String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.welcome_dialog, null);  // this line
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();
        Button btnOk = dialog.findViewById(R.id.okBtn);
        TextView tvWelcomeToJokar = dialog.findViewById(R.id.tvWelcomeToJokar);
        SpannableString spannableString = new SpannableString(getString(R.string.hello_nihar_welcome_to_jokar_family));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purpalMedium)), 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //tvWelcomeToJokar.setText(getString(R.string.hello)+" " + name + " " + getString(R.string.welcome_to_));
        tvWelcomeToJokar.setText(R.string.hello_welcome_to_paginazul_family);
        btnOk.setOnClickListener(v1 -> {
            SharedPreferences sharedpreferences = mContext.getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("kIsFirstTime", false);
            editor.putString("commingType", signIn);

            editor.commit();


            //startActivity(HomeMainActivity.getIntent(mContext, ""));
            startActivity(new Intent(mContext, MainActivity.class));
            getActivity().finish();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            dialog.dismiss();

        });

    }



    //fire base otp method

    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //  Toast.makeText(SignupViaActivity.this, "verification completed", Toast.LENGTH_SHORT).show();
                //showProgress(false);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
                Log.e("error ", " error " + e);
                //  showProgress(false);
                Toast.makeText(mContext, R.string.failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(mContext, R.string.otp_sent, Toast.LENGTH_SHORT).show();
                // showProgress(false)8;

                // getOtpScreen();


                //  Toast.makeText(SignupViaActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void showConfirmation() {
        if (verificationCode != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, pinview.getValue());
            SigninWithPhone(credential);
        }
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        yourCountDownTimer.cancel();
                        // Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedpreferences = mContext.getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);

                        commingType = sharedpreferences.getString("commingTypeMobile", null);

                        if (commingType != null && commingType.equalsIgnoreCase("SignUp")) {
                            //showConfirmation();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("commingTypeMobile", "");
                            callSignupService();
                            editor.commit();
                        }else if (commingType != null && commingType.equalsIgnoreCase("Update Phone")) {
                            startActivity(new Intent(getContext(), MyProfile.class));
                            getActivity().finish();
                            //showConfirmation();
                            /*SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("commingTypeMobile", "");
                            callSignupService();
                            editor.commit();*/
                        } else {
                            signIn();
                            // showConfirmation();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("commingTypeMobile", "");

                            editor.commit();
                        }


                    } else {
                        Toast.makeText(mContext, R.string.verification_failed, Toast.LENGTH_SHORT).show();
                        //yourCountDownTimer.start();
                    }
                });

    }

    private void signIn() {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            Call<SignInResponse> call = apiInterface.signIn(SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.DEVICE_TOKEN), "android", parameter.get("kCountryCode").toString(), parameter.get("kPhone").toString());
            call.enqueue(new Callback<SignInResponse>() {
                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        SignInResponse signUpWithMobileResp = response.body();
                        if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {


                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kUserId, response.body().getResponse().getId());
                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kToken, response.body().getResponse().getJwtToken());

                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kProfile, response.body().getResponse().getProfilePic());


                            Log.e("Token", "Token" + response.body().getResponse().getJwtToken());


                            String name = response.body().getResponse().getName();
                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kUserName,name);

                            String appLanguage = response.body().getResponse().getAppLanguage();
                            if(appLanguage.equalsIgnoreCase("English")){
                                LocaleHelper.setLocale(getContext(),"en");
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kEnglish);
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
                            }else {
                                LocaleHelper.setLocale(getContext(),"pt");
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
                            }

                            showWelcomeDialog("SignIn", name);
                            //Toast.makeText(mContext, "" + signUpWithMobileResp.getResponseMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(mContext, "" + signUpWithMobileResp.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    MyDialog.getInstance(mContext).hideDialog();
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void otpGeneretByFirebae(String ccp, String phone) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(ccp + phone)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(getActivity())                 // Activity (for callback binding)
                .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        // OnVerificationStateChangedCallbacks
    }

    private void callSignupService() {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            Call<SignUpWithMobileResp> call;
            RequestBody profile_body;

            MultipartBody.Part prfpic;

            if (!parameter.get("kProfile").toString().equalsIgnoreCase("")) {
                File file = (File) parameter.get("kProfile");
                profile_body = RequestBody.create(MediaType.parse("image/*"), file);
                prfpic = MultipartBody.Part.createFormData("profilePic", file.getName(), profile_body);
                Map<String, RequestBody> map = preData();
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                call = apiInterface.signup(map, prfpic);
            } else {
                Map<String, RequestBody> map = preData();
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                call = apiInterface.signup(map);
            }

            call.enqueue(new Callback<SignUpWithMobileResp>() {
                @Override
                public void onResponse(Call<SignUpWithMobileResp> call, Response<SignUpWithMobileResp> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        SignUpWithMobileResp signUpResponce = response.body();


                        if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kUserId, response.body().getData().getId());
                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kToken, response.body().getData().getJwtToken());
                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kProfile, response.body().getData().getProfilePic());

                            Log.e("Token", "Token" + response.body().getData().getJwtToken());

                            String appLanguage = response.body().getData().getAppLanguage();
                            if(appLanguage.equalsIgnoreCase("English")){
                                LocaleHelper.setLocale(getContext(),"en");
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kEnglish);
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
                            }else {
                                LocaleHelper.setLocale(getContext(), "pt");
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
                                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
                            }

                            String name = response.body().getData().getName();
                            showWelcomeDialog("SignUp", name);
                            SharedPreferenceWriter.getInstance(mContext).writeStringValue(kUserName,name);

                            //Toast.makeText(mContext, "" + signUpResponce.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "" + signUpResponce.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show();
                        MyDialog.getInstance(mContext).hideDialog();

                    }
                }


                @Override
                public void onFailure(Call<SignUpWithMobileResp> call, Throwable t) {
                    t.printStackTrace();
                    MyDialog.getInstance(mContext).hideDialog();


                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private Map<String, RequestBody> preData() {

        userData.put("name", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kName").toString()));
        userData.put("userName", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kUserName").toString()));
        userData.put("fullName", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kFullName").toString()));
        userData.put("gender", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kGender").toString()));
        userData.put("dob", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kDate").toString()));
        userData.put("countryCode", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kCountryCode").toString()));
        userData.put("country", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kCountry").toString()));
        userData.put("email", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kEmailId").toString()));
        //userData.put("appLanguage", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kAppLanguage").toString()));
        userData.put("speakLanguage", RequestBody.create(MediaType.parse("text/plain"), "English"));
        userData.put("mobileNumber", RequestBody.create(MediaType.parse("text/plain"), parameter.get("kPhone").toString()));
        userData.put("deviceType", RequestBody.create(MediaType.parse("text/plain"), "android"));
        userData.put("deviceToken", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.DEVICE_TOKEN)));
        if(parameter.get("kAppLanguage").toString().equalsIgnoreCase(getString(R.string.english))){
            userData.put("appLanguage", RequestBody.create(MediaType.parse("text/plain"), "English"));
            userData.put("langCode", RequestBody.create(MediaType.parse("text/plain"), Constants.kEnglish));
        }else {
            userData.put("appLanguage", RequestBody.create(MediaType.parse("text/plain"), "Portuguese"));
            userData.put("langCode", RequestBody.create(MediaType.parse("text/plain"), Constants.kPortuguese));
        }

        return userData;

    }


    @OnClick({R.id.tvResendCode, R.id.edtNumberOTP, R.id.ivclose})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvResendCode:
                yourCountDownTimer.cancel();
                getOtpScreen();
                otpGeneretByFirebae(parameter.get("kCountryCode").toString(), parameter.get("kPhone").toString().trim());
                break;
            case R.id.edtNumberOTP:

                SharedPreferences sharedpreferences = mContext.getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
                commingType = sharedpreferences.getString("commingTypeMobile", null);
                if (commingType.equalsIgnoreCase("SignUp") && commingType != null) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("commingTypeMobile", "");
                    editor.commit();

//                    Intent intent = new Intent(mContext, SignUpMobileActivity.class);
//                    startActivity(intent);
                     startActivity(SignUpMobileActivity.getIntent(mContext,parameter));
                    getActivity().finish();

                } else if (commingType.equalsIgnoreCase("SignIn")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("commingTypeMobile", "");
                    editor.commit();

                    Intent intent = new Intent(mContext, SignIn.class);
                    startActivity(intent);


                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("commingTypeMobile", "");
                    editor.commit();

                    Intent intent = new Intent(mContext, UpdatePhone.class);
                    startActivity(intent);
                }

                break;

            case R.id.ivclose:
                //signIn();
                getActivity().finish();
                //dismiss();
                break;
            //startActivity(SignUpMobileActivity.getIntent(mContext,parameter));


        }


        //  resendCode();
    }



    public String formatE164Number(String countryCode, String phNum) {

        String e164Number;
        if (TextUtils.isEmpty(countryCode)) {
            e164Number = phNum;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                e164Number = PhoneNumberUtils.formatNumberToE164(phNum, countryCode);
            } else {
                try {
                    PhoneNumberUtil instance = PhoneNumberUtil.createInstance(getActivity());
                    Phonenumber.PhoneNumber phoneNumber = instance.parse(phNum, countryCode);
                    e164Number = instance.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

                } catch (NumberParseException e) {
                    // Log.e(TAG, "Caught: " + e.getMessage(), e);
                    e164Number = phNum;
                }
            }
        }

        return e164Number;
    }

}


//  imagePath = (String) parameter.get("kProfile");
//
//
//          if (imagePath != null) {
//          File file = new File(imagePath);
//          profile_body = RequestBody.create(MediaType.parse("image/*"), file);
//          profilePart = MultipartBody.Part.createFormData("profilePic", file.getName(), profile_body);
//          } else {
//          profilePart = MultipartBody.Part.createFormData("profilePic", "");
//          }



