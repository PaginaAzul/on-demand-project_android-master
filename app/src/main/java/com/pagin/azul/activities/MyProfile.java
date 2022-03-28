package com.pagin.azul.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbb20.CountryCodePicker;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.MyProfileInner;
import com.pagin.azul.bean.MyProfileResponse;
import com.pagin.azul.bean.ProfessionalWorkerResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.FoodAndGroceryActivity;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.LocaleHelper;
import com.pagin.azul.utils.NetworkChangeReceiver;
import com.pagin.azul.utils.TakeImage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class MyProfile extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    @BindView(R.id.contactAdminBtnProfile)
    Button contactAdminBtnProfile;
    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.myCaptionBtn)
    Button myCaptionBtn;
    @BindView(R.id.conMainLayout)
    ConstraintLayout conMainLayout;
    @BindView(R.id.myRateBtn)
    Button myRateBtn;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.tvProfileEdit)
    TextView tvProfileEdit;
    @BindView(R.id.tvMyProfileID)
    TextView tvMyProfileID;
    @BindView(R.id.ivMyProfile)
    ImageView ivMyProfile;
    @BindView(R.id.edtProfileName)
    EditText edtProfileName;
    @BindView(R.id.edtProfileGender)
    TextView edtProfileGender;
    @BindView(R.id.spinnergender)
    Spinner spinnergender;
    @BindView(R.id.spinnerAppLang)
    Spinner spinnerAppLang;
    @BindView(R.id.spinnerSpeakLang)
    Spinner spinnerSpeakLang;
    @BindView(R.id.edtProfileDate)
    TextView edtProfileDate;
    @BindView(R.id.edtProfileEmailID)
    EditText edtProfileEmailID;
    @BindView(R.id.edtProfileUserName)
    EditText edtProfileUserName;
    @BindView(R.id.edtProfilePhoneNumber)
    TextView edtProfilePhoneNumber;
    @BindView(R.id.tvSpeakLanguageProfile)
    TextView tvSpeakLanguageProfile;
    @BindView(R.id.tvAppLanguageProfile)
    TextView tvAppLanguageProfile;
    private MyProfileInner profileInner;

    private NetworkChangeReceiver mNetworkReceiver;
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();

        }

    };
    private DatePickerDialog dialog;
    private String day = "";


//    @BindView(R.id.tvCountryProfile)
//    TextView tvCountryProfile;
    private String month = "";
    private String years = "";
    private String deliveryPerson, professionalWorker;
    private CountryCodePicker ccp;
    private String countryCodeAndroid = "+966";
    private String spanString = "My Id #23562365";
    private String[] gender = {"Gender", "Male", "Female"};
    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                edtProfileGender.setText(gender[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private String[] applung = {"Language", "English", "Portuguese"};
    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                //tvAppLanguageProfile.setText(applung[position]);
                tvAppLanguageProfile.setText(parent.getItemAtPosition(position).toString());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private String[] speaklung = {"Langage", "English", "Portuguese"};
    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                //tvSpeakLanguageProfile.setText(speaklung[position]);
                tvSpeakLanguageProfile.setText(parent.getItemAtPosition(position).toString());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);


//        mNetworkReceiver = new NetworkChangeReceiver();
//        mNetworkReceiver.registerCallback(new NetworkChangeReceiver.InternetResponse() {
//            @Override
//            public void onInternetCheck(boolean isConnect) {
//
//                if (isConnect)
//                    disableofflineAnimation();
//                else
//                    runofflineAnimation();
//
//            }
//        });


//        SpannableString spannableString = new SpannableString("My Id #23562365");
//        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPink)), 6, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvMyProfileID.setText(spannableString);
        myCalendar = Calendar.getInstance();
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        spinnergender.setAdapter(adapter);
        spinnergender.setOnItemSelectedListener(onItemSelectedListener);

        //ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, applung);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.languages));
        spinnerAppLang.setAdapter(adapter1);
        spinnerAppLang.setOnItemSelectedListener(onItemSelectedListener1);

        //ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, speaklung);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.languages));
        spinnerSpeakLang.setAdapter(adapter2);
        spinnerSpeakLang.setOnItemSelectedListener(onItemSelectedListener2);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCodeWithPlus();


                android.util.Log.d("Country Code", countryCodeAndroid);
            }
        });

        hideKeyboard(this);
        if(isNetworkAvailable(this)){
            myProfileApi();
        }else {
            Toast.makeText(this, "Internet connection lost", Toast.LENGTH_SHORT).show();
         finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       // registerNetworkBroadCast();

        if(isNetworkAvailable(this)){
            //myProfileApi();
        }else {
            Toast.makeText(this, "Internet connection lost", Toast.LENGTH_SHORT).show();

        }
    }

    @OnClick({R.id.contactAdminBtnProfile, R.id.myCaptionBtn, R.id.myRateBtn, R.id.save,
            R.id.tvProfileEdit, R.id.edtProfileGender, R.id.tvAppLanguageProfile,
            R.id.tvSpeakLanguageProfile, R.id.edtProfileDate, R.id.ivMyProfile, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactAdminBtnProfile:
                dispatchContactAdmin();
                break;
            case R.id.myCaptionBtn:
                //captionBtnClick();
                if (!Validation()) {
                    return;
                }
                callUpdateProfileService();
                break;
            case R.id.myRateBtn:
                dispatchMyRate();
                break;
            case R.id.save:
//                if (!Validation()) {
//                    return;
//                }
//                callUpdateProfileService();
                break;
            case R.id.tvProfileEdit:
                dipatchUpdatePhone();
                break;
            case R.id.edtProfileGender:
                spinnergender.performClick();
                break;
            case R.id.tvAppLanguageProfile:
                spinnerAppLang.performClick();
                break;
            case R.id.tvSpeakLanguageProfile:
                spinnerSpeakLang.performClick();
                break;

            case R.id.edtProfileDate:
                if (!day.equalsIgnoreCase("") || !month.equalsIgnoreCase("") || !years.equalsIgnoreCase(""))
                    showDate(Integer.parseInt(years), Integer.parseInt(month), Integer.parseInt(day), R.style.NumberPickerStyle);
                else
                    showDate(1991, 0, 1, R.style.NumberPickerStyle);

//                dialog = new DatePickerDialog(MyProfile.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//                dialog.show();
                break;
            case R.id.ivMyProfile:
                selectImage();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void captionBtnClick() {
        if(profileInner != null) {
            if (profileInner.getSignupWithDeliveryPerson().equalsIgnoreCase("true")) {
                if(profileInner.getAdminVerifyDeliveryPerson().equalsIgnoreCase("true")) {
                    startActivity(CaptionProfileActivity.getIntent(MyProfile.this, profileInner));
                }else {
                    Toast.makeText(this, "Your request for become Delivery Person is under approval.", Toast.LENGTH_SHORT).show();
                }
            } else if (profileInner.getSignupWithProfessionalWorker().equalsIgnoreCase("true")) {
                if(profileInner.getAdminVerifyProfessionalWorker().equalsIgnoreCase("true")) {
                    startActivity(CaptionProfileActivity.getIntent(MyProfile.this, profileInner));

                }else {
                    Toast.makeText(this, "Your request for become Professional worker is under approval.", Toast.LENGTH_SHORT).show();
                }
            } else {
                captionProfileDialog();
                //startActivity(BecomeDeliveryPersonActivity.getIntent(MyProfile.this));
            }
        }
    }

    private void captionProfileDialog() {
        Dialog dialog = new Dialog(MyProfile.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);

        textView.setText("Please register as a delivery or professional worker.");
        registerNow.setText("Professional Worker");
        notNow.setText("Delivery Person");

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, BecomeDeliveryPersonActivity.class));
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, BecomeProfessionalWorkerActivity.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }





    private void captionProfileShowTextDialog() {
        Dialog dialog = new Dialog(MyProfile.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);

        textView.setText("Please register as a delivery or professional worker.");
        registerNow.setText("Professional Worker");
        notNow.setText("Delivery Person");

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, BecomeDeliveryPersonActivity.class));
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, BecomeProfessionalWorkerActivity.class));
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void dispatchMyRate() {
        Intent intent = new Intent(this, MyRate.class);
        startActivity(intent);
    }

    private void dispatchContactAdmin() {
        Intent intent = new Intent(this, ContactAdmin.class);
        startActivity(intent);
    }

    private void dipatchUpdatePhone() {
        startActivity(UpdatePhone.getIntentProfile(this, "Profile"));
        finish();
//        Intent intent = new Intent(this, UpdatePhone.class);
//        startActivity(intent);
    }

    private void myProfileApi() {
        try {
            MyDialog.getInstance(this).showDialog(MyProfile.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<MyProfileResponse> beanCall = apiInterface.getUserDetails(token, body);

                beanCall.enqueue(new Callback<MyProfileResponse>() {
                    @Override
                    public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                        MyDialog.getInstance(MyProfile.this).hideDialog();
                        if (response.isSuccessful()) {
                            MyProfileResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body().getProfileInner() != null && !response.body().getProfileInner().equals("")) {


                                    profileInner = response.body().getProfileInner();
                                    String fullName = response.body().getProfileInner().getFullName();
                                    if(fullName!=null){
                                        if (fullName.equalsIgnoreCase("true")) {
                                            edtProfileName.setText(response.body().getProfileInner().getName());
                                        } else {
                                            edtProfileName.setHint("Full Name");
                                            edtProfileName.setFocusable(false);
                                        }
                                    }else {
                                        edtProfileName.setText(response.body().getProfileInner().getName());
                                    }

                                    edtProfileGender.setText(response.body().getProfileInner().getGender());
                                    String userName = response.body().getProfileInner().getUserName();
                                    if(userName!=null){
                                        if (userName.equalsIgnoreCase("true")) {
                                            edtProfileUserName.setText(response.body().getProfileInner().getName());
                                        } else {
                                            edtProfileUserName.setHint("User Name");
                                            edtProfileUserName.setFocusable(false);
                                        }
                                    }else{
                                        edtProfileUserName.setText(response.body().getProfileInner().getName());
                                    }
                                    //tvMyProfileID.setText("My ID: " + response.body().getProfileInner().get_id());
                                    tvMyProfileID.setText(response.body().getProfileInner().getName());
                                    edtProfileDate.setText(response.body().getProfileInner().getDob());
                                    edtProfileEmailID.setText(response.body().getProfileInner().getEmail());
                                    edtProfilePhoneNumber.setText(response.body().getProfileInner().getMobileNumber());
                                    if(response.body().getProfileInner().getAppLanguage().equalsIgnoreCase("English")){
                                        tvAppLanguageProfile.setText(getString(R.string.english));
                                    }else {
                                        tvAppLanguageProfile.setText(getString(R.string.portuguese));
                                    }
                                    //tvAppLanguageProfile.setText(response.body().getProfileInner().getAppLanguage());
                                    tvSpeakLanguageProfile.setText(response.body().getProfileInner().getSpeakLanguage());
                                    // countryCodeAndroid = response.body().getProfileInner().getMobileNumber();

                                    String cpp1 = response.body().getProfileInner().getCountryCode();

                                    int spp = Integer.parseInt(cpp1);

                                    ccp.setCountryForPhoneCode(spp);
                                    if(SharedPreferenceWriter.getInstance(MyProfile.this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                                        ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
                                    }else {
                                        ccp.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
                                    }

//                                    if (!response.body().getProfileInner().getProfilePic().equalsIgnoreCase("")) {
//                                        Picasso.with(MyProfile.this).load(response.body().getProfileInner().getProfilePic()).into(ivMyProfile);
//                                    }

                                    Glide.with(MyProfile.this)
                                            .load(response.body().getProfileInner().getProfilePic())
                                            .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                                                    .error(R.drawable.profile_default))
                                            .into(ivMyProfile);

                                }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                    CommonUtility.showDialog1(MyProfile.this);
                                } else {
                                    Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                                //Toast.makeText(MyProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MyProfile.this);
                            } else {
                                Toast.makeText(MyProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private Map<String, RequestBody> setUpMapData() throws JSONException {

        Map<String, RequestBody> fields = new HashMap<>();
        RequestBody name;
        if (!edtProfileName.getText().toString().equalsIgnoreCase("")) {
            name = RequestBody.create(MediaType.parse("text/plain"), edtProfileName.getText().toString());
        } else {
            name = RequestBody.create(MediaType.parse("text/plain"), edtProfileUserName.getText().toString());
        }
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(kToken));
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), edtProfileGender.getText().toString());
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), edtProfileDate.getText().toString());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), ccp.getSelectedCountryName());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), edtProfileEmailID.getText().toString());
        //RequestBody appLanguage = RequestBody.create(MediaType.parse("text/plain"), tvAppLanguageProfile.getText().toString());
        RequestBody speakLanguage = RequestBody.create(MediaType.parse("text/plain"), "English");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(kUserId));

        fields.put("token", token);
        fields.put("userId", userId);
        fields.put("name", name);
        fields.put("gender", gender);
        fields.put("dob", dob);
        fields.put("country", country);
        fields.put("email", email);
        //fields.put("appLanguage", appLanguage);
        fields.put("speakLanguage", speakLanguage);
        if(tvAppLanguageProfile.getText().toString().equalsIgnoreCase(getString(R.string.english))){
            fields.put("appLanguage", RequestBody.create(MediaType.parse("text/plain"), "English"));
            fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), Constants.kEnglish));
        }else {
            fields.put("appLanguage", RequestBody.create(MediaType.parse("text/plain"), "Portuguese"));
            fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), Constants.kPortuguese));
        }

        return fields;
    }

    private void callUpdateProfileService() {
        try {
            MyDialog.getInstance(this).showDialog(MyProfile.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            Call<ProfessionalWorkerResponse> call;
            if (!token.isEmpty()) {
                RequestBody profile_body;
                MultipartBody.Part prfpic;
                if (imagePath != null) {
                    File file = new File(imagePath);
                    profile_body = RequestBody.create(MediaType.parse("image/*"), file);
                    prfpic = MultipartBody.Part.createFormData("profilePic", file.getName(), profile_body);
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.updateProfile(setUpMapData(), prfpic);
                } else {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.updateProfile(setUpMapData());
                }
                call.enqueue(new retrofit2.Callback<ProfessionalWorkerResponse>() {
                    @Override
                    public void onResponse(Call<ProfessionalWorkerResponse> call, Response<ProfessionalWorkerResponse> response) {
                        MyDialog.getInstance(MyProfile.this).hideDialog();

                        if (response.isSuccessful()) {

                            ProfessionalWorkerResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                SharedPreferenceWriter.getInstance(MyProfile.this).writeStringValue(kProfile, signUpResponce.getSignUpInner().getProfilePic());
                                SharedPreferenceWriter.getInstance(MyProfile.this).writeStringValue(kUserName,signUpResponce.getSignUpInner().getName());
                                String appLanguage = response.body().getSignUpInner().getAppLanguage();
                                if(appLanguage.equalsIgnoreCase("English")){
                                    LocaleHelper.setLocale(MyProfile.this,"en");
                                    SharedPreferenceWriter.getInstance(MyProfile.this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kEnglish);
                                    SharedPreferenceWriter.getInstance(MyProfile.this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
                                }else {
                                    LocaleHelper.setLocale(MyProfile.this, "pt");
                                    SharedPreferenceWriter.getInstance(MyProfile.this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
                                    SharedPreferenceWriter.getInstance(MyProfile.this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
                                }

                                Intent intent = new Intent(MyProfile.this, MainActivity.class);
                                intent.putExtra(ParamEnum.TYPE.theValue(),Objects.requireNonNull(getIntent().getStringExtra(ParamEnum.TYPE.theValue())));
                                startActivity(intent);
                                finishAffinity();
                                /*if(Objects.requireNonNull(getIntent().getStringExtra(ParamEnum.TYPE.theValue())).equalsIgnoreCase(FoodAndGroceryActivity.class.getSimpleName())){
                                    Intent intent = new Intent(MyProfile.this,FoodAndGroceryActivity.class);
                                    intent.putExtra(ParamEnum.TYPE.theValue(),MyProfile.class.getSimpleName());
                                    startActivity(intent);
                                }else {
                                    startActivity(HomeMainActivity.getIntent(MyProfile.this, MyProfile.class.getSimpleName()));
                                }
                                finish();*/
                                Toast.makeText(MyProfile.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyProfile.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(MyProfile.this);
                        } else {
                            Toast.makeText(MyProfile.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfessionalWorkerResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyDialog.getInstance(MyProfile.this).hideDialog();
                        String s = "";
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void selectImage() {
        final CharSequence[] items = {
//                getResources().getString(R.string.Take_Photo),
//                getResources().getString(R.string.Choose_from_Library),
                "take photo", "take libarary",
                getResources().getString(R.string.cancel)};

        final Dialog dialog = new Dialog(this, R.style.MyDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_imagecapture);


        TextView txt_takephoto = (TextView) dialog.findViewById(R.id.txt_takephoto);
        TextView txt_choosefromlibrary = (TextView) dialog.findViewById(R.id.txt_choosefromgallery);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_button);
        txt_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyProfile.this, TakeImage.class);
                intent.putExtra("from", "camera");
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                dialog.dismiss();
            }
        });
        txt_choosefromlibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfile.this, TakeImage.class);
                intent.putExtra("from", "gallery");
                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if ((requestCode == CAMERA_PIC_REQUEST || requestCode == REQ_CODE_PICK_IMAGE) && (resultCode == RESULT_OK)) {
            if (data != null) {
                imagePath = data.getStringExtra("filePath");
                if (imagePath != null) {
                    fileFlyer = new File(imagePath);
                }

                if (fileFlyer.exists() && fileFlyer != null) {
                    CropImage.activity(Uri.fromFile(fileFlyer))
                            .start(this);
                    //ivSignUp.setImageURI(Uri.fromFile(fileFlyer));
                    /*Glide.with(this)
                            .load(imagePath)
                            .apply(RequestOptions.placeholderOf(R.drawable.loader)
                                    .error(R.drawable.place_holder))
                            .into(ivSignUp);*/
                }
            }
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // Uri resultUri = result.getUri();
                Uri resultUri = result.getUri();
                imagePath=resultUri.getPath();
                Glide.with(this)
                        .load(imagePath)
                        .apply(RequestOptions.placeholderOf(R.drawable.loader)
                                .error(R.drawable.place_holder))
                        .into(ivMyProfile);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean Validation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (edtProfileUserName.getText().toString().isEmpty()) {

            Toast.makeText(this, getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();

            return false;
        }
        /*if (edtProfileGender.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();

            return false;

        } else*/ else if (edtProfileEmailID.getText().toString().isEmpty()) {

            Toast.makeText(this, getString(R.string.please_enter_email_id), Toast.LENGTH_SHORT).show();

            return false;


        } else if (!edtProfileEmailID.getText().toString().matches(emailPattern)) {

            Toast.makeText(getApplicationContext(), getString(R.string.please_enter_valid_email_address), Toast.LENGTH_SHORT).show();
            return false;


        } else if (tvAppLanguageProfile.getText().toString().isEmpty()) {

            Toast.makeText(this, getString(R.string.please_select_app_languages), Toast.LENGTH_SHORT).show();

            return false;


        } /*else if (tvSpeakLanguageProfile.getText().toString().isEmpty()) {

            Toast.makeText(this, R.string.please_select_speak_lanugae, Toast.LENGTH_SHORT).show();

            return false;

        }*/

        return true;

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtProfileDate.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        //edDob.setText(simpleDateFormat.format(calendar.getTime()));

        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                + "/" + String.valueOf(year);

        day = String.valueOf(dayOfMonth);
        month = String.valueOf(monthOfYear + 1);
        years = String.valueOf(year);

        edtProfileDate.setText(date);
    }

    void showDate(int dayOfMonth, int monthOfYear, int year, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(MyProfile.this)
                .callback(MyProfile.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(dayOfMonth, monthOfYear, year)
                .build()
                .show();
    }


    //////run offline animatin based on network connctivity/////////////
    public void runofflineAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        conMainLayout.setVisibility(View.GONE);
        lottieAnimationView.setAnimation("no_internet_connection.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
    }

    //////disable offline animatin based on network connctivity/////////////
    public void disableofflineAnimation() {
        if (lottieAnimationView != null) {
            lottieAnimationView.pauseAnimation();
        }
        conMainLayout.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.GONE);
        myProfileApi();

    }

    private void registerNetworkBroadCast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterNetworkChanges();
    }


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting()
                        || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
// TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
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
