package com.pagin.azul.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.pagin.azul.BottomSheet.OTPBottomSheet;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.SignUpWithMobileResp;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.TakeImage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    private CountryCodePicker ccp;
    private String countryCodeAndroid = "+966";

    private String day = "";
    private String month = "";
    private String years = "";

    private GoogleApiClient mGoogleApiClient;
    // private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView locationSearchActv;// instance of AutoCompleteText View
    private TextView addressTv, locationDataTv; // TextViews Used to display the adddress selected by the user

    private GeoDataClient mGeoDataClient;
    //private PlaceAutoCompleteAdapter mPlaceAutocompleteAdapter;

    private static final LatLngBounds LAT_LNG_BOUNDS_DEFAULT = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362)); // The suggestion within this geometrical boundary will be displayed on top.


    private double Lat, Long;
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    private Calendar myCalendar;
    private DatePickerDialog dialog;
    @BindView(R.id.signUpBtn)
    Button signUpBtn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edtBirthYear)
    TextView edtBirthYear;

    @BindView(R.id.tvAppLanguageSignUp)
    TextView tvAppLanguageSignUp;
    @BindView(R.id.tvSpeakLanguageSignUp)
    TextView tvSpeakLanguageSignUp;

    @BindView(R.id.tvCountrySignUp)
    CountryCodePicker tvCountrySignUp;

    @BindView(R.id.edtEnterName)
    EditText edtEnterName;

    @BindView(R.id.edtEmailSignUp)
    EditText edtEmailSignUp;


    @BindView(R.id.ivSignUp)
    ImageView ivSignUp;
    RadioButton radioButton;
    private String gender = "Male";

    private String[] country = {"Select Your Country", "India", "Qatar", "Saudi Arabia"};
    private String[] language = {"Select App Language", "English", "Portuguese"};
    private String[] speakLanguage = {"Select Speak Language", "English", "Portuguese"};


    @BindView(R.id.spinnerCountry)
    Spinner spinnerCountry;
    @BindView(R.id.spinnerAppLag)
    Spinner spinnerAppLag;
    @BindView(R.id.spinnerSpeakLang)
    Spinner spinnerSpeakLang;
    private SignUpWithMobileResp signUpWithMobileResp;
    private File file;


    private HashMap<String, Object> signUpData;

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

    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                //tvAppLanguageSignUp.setText(language[position]);
                tvAppLanguageSignUp.setText(parent.getItemAtPosition(position).toString());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {
                return;
            } else {
                //tvSpeakLanguageSignUp.setText(speakLanguage[position]);
                tvSpeakLanguageSignUp.setText(parent.getItemAtPosition(position).toString());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @BindView(R.id.rgSignUp)
    RadioGroup rgSignUp;

    @BindView(R.id.rgSignUp2)
    RadioGroup rgSignUp2;

    @BindView(R.id.rbFullName)
    RadioButton rbFullName;
    @BindView(R.id.rbUserName)
    RadioButton rbUserName;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    private String userName = "true", fullName = "false";


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SignUp.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.signup_now);

        myCalendar = Calendar.getInstance();
        signUpData = new HashMap<>();
        ccp = (CountryCodePicker) findViewById(R.id.tvCountrySignUp);
        ccp.setCountryForPhoneCode(244);
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
        }else {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.languages));
        spinnerAppLag.setAdapter(adapter);
        spinnerAppLag.setOnItemSelectedListener(onItemSelectedListener);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.languages));
        spinnerSpeakLang.setAdapter(adapter1);
        spinnerSpeakLang.setOnItemSelectedListener(onItemSelectedListener1);


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCodeWithPlus();
                android.util.Log.d("Country Code", countryCodeAndroid);
            }
        });


        rgSignUp2.setOnCheckedChangeListener((radioGroup, i) -> {

            int checked = rgSignUp2.getCheckedRadioButtonId();
            radioButton = findViewById(checked);
            switch (checked) {
                case R.id.rbFullName:

                    userName = "false";
                    fullName = "true";

                    break;
                case R.id.rbUserName:

                    fullName = "false";
                    userName = "true";

                    break;
            }
        });

        // sendOtp();
    }


    private void sendOtp() {

        Gson gson = new Gson();

        signUpWithMobileResp = gson.fromJson(SharedPreferenceWriter.getInstance(this).getString("kSignUpData"), SignUpWithMobileResp.class);
        // Log.d("Otp", signUpWithMobileResp.getData().getMobileOtp());
    }

    /*private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            *//*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              *//*
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();


            *//*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              *//*
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };*/

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                addressTv.setText(place.getAddress().toString());
                locationDataTv.setText("Latitude : " + String.valueOf(place.getLatLng().latitude) + "\n Longitude : " + String.valueOf(place.getLatLng().longitude));
                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                return;
            }
        }
    };


    @OnClick({R.id.signUpBtn, R.id.iv_back, R.id.ivSignUp, R.id.tvAppLanguageSignUp,
            R.id.tvSpeakLanguageSignUp, R.id.tvCountrySignUp, R.id.edtBirthYear, R.id.rgSignUp2, R.id.rgSignUp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpBtn:
                if (!Validation()) {
                    return;
                }
                HashMap<String, Object> prepare = getData();
                startActivity(SignUpMobileActivity.getIntent(this, prepare));
                finish();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ivSignUp:
                selectImage();
                break;

            case R.id.tvAppLanguageSignUp:
                spinnerAppLag.performClick();
                break;

            case R.id.tvSpeakLanguageSignUp:
                spinnerSpeakLang.performClick();
                break;

            case R.id.tvCountrySignUp:
                spinnerCountry.performClick();
                break;
            case R.id.edtBirthYear:
                if (!day.equalsIgnoreCase("") || !month.equalsIgnoreCase("") || !years.equalsIgnoreCase(""))
                    showDate(Integer.parseInt(years), Integer.parseInt(month), Integer.parseInt(day), R.style.NumberPickerStyle);
                else
                    showDate(1991, 0, 1, R.style.NumberPickerStyle);

//                dialog = new DatePickerDialog(SignUp.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
////                c_event_s_date.setText(myCalendar.get(Calendar.DAY_OF_MONTH));
//                dialog.show();
                break;


            case R.id.rgSignUp:

                break;
        }
    }

    private HashMap<String, Object> getData() {
        if (fileFlyer != null) {
            signUpData.put("kProfile", fileFlyer);

        } else {
            signUpData.put("kProfile", "");

        }
        signUpData.put("kFullName", fullName);
        signUpData.put("kUserName", userName);
        signUpData.put("kName", edtEnterName.getText().toString());
        signUpData.put("kGender", gender);
        signUpData.put("kEmailId", edtEmailSignUp.getText().toString());
        signUpData.put("kDate", edtBirthYear.getText().toString());
        try {
            signUpData.put("kCountryCode", ccp.getSelectedCountryCodeWithPlus());
            signUpData.put("kCountry", ccp.getSelectedCountryName());
        }catch (Exception e){
            e.printStackTrace();
        }
        signUpData.put("kAppLanguage", tvAppLanguageSignUp.getText().toString());
        signUpData.put("kSpeakLanguage", tvSpeakLanguageSignUp.getText().toString());

        return signUpData;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = false;

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbFullName:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.rbUserName:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }


    private void showWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.welcome_dialog, null);  // this line
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();
        Button btnOk = dialog.findViewById(R.id.okBtn);
        TextView tvWelcomeToJokar = dialog.findViewById(R.id.tvWelcomeToJokar);

        SpannableString spannableString = new SpannableString(getString(R.string.hello_nihar_welcome_to_jokar_family));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purpalMedium)), 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvWelcomeToJokar.setText(spannableString);

        btnOk.setOnClickListener(v1 -> {

            startActivity(HomeMapActivity.getIntent(SignUp.this, ""));
            finish();
            dialog.dismiss();

        });

    }


//    private void callSignupService() {
//        try {
//            MyDialog.getInstance(this).showDialog(SignUp.this);
//            String token = "kjesabckjb";
//
//            if (!token.isEmpty()) {
//                RequestBody profile_body;
//
//
//                if (imagePath != null) {
//
//                    File file = new File(imagePath);
//                    // profile_body = RequestBody.create(MediaType.parse("image/*"), file);
//
//                } else {
//
//                    // profile_body = RequestBody.create(MediaType.parse("image/*"), "");
//
//                }
//                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
//                Call<SignUpResponse> call = apiInterface.signup(edtEnterName.getText().toString(), userName, fullName, "Male", edtBirthYear.getText().toString(), tvCountrySignUp.getText().toString(),
//                        edtEmailSignUp.getText().toString(), "+91", tvAppLanguageSignUp.getText().toString(), tvSpeakLanguageSignUp.getText().toString(), signUpWithMobileResp.getData().getMobileNumber(), "android", "vdhbjhkbdfjvhb", imagePath);
//                call.enqueue(new retrofit2.Callback<SignUpResponse>() {
//                    @Override
//                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
//                        MyDialog.getInstance(SignUp.this).hideDialog();
//
//                        if (response.isSuccessful()) {
//
//                            SignUpResponse signUpResponce = response.body();
//                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
//
//
//                                Toast.makeText(SignUp.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();
//
//
//                            } else {
//                                Toast.makeText(SignUp.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//
//                        } else {
//                            Toast.makeText(SignUp.this, "Error!", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
//                        t.printStackTrace();
//                        MyDialog.getInstance(SignUp.this).hideDialog();
//                        String s = "";
//                    }
//                });
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

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
        txt_takephoto.setOnClickListener(v -> {

            Intent intent = new Intent(SignUp.this, TakeImage.class);
            intent.putExtra("from", "camera");
            SignUp.this.startActivityForResult(intent, CAMERA_PIC_REQUEST);
            dialog.dismiss();
        });
        txt_choosefromlibrary.setOnClickListener(v -> {

            Intent intent = new Intent(SignUp.this, TakeImage.class);
            intent.putExtra("from", "gallery");
            startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
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
                        .into(ivSignUp);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        } else if (requestCode == RESULT_OK)
            super.onActivityResult(requestCode, resultCode, data);
    }


    private void showBottomSheet() {
        SharedPreferences sharedpreferences = getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("commingType", "SignUp");
        editor.commit();


        OTPBottomSheet sheet = new OTPBottomSheet();
        sheet.show(getSupportFragmentManager(), "OTPBottomSheet");
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtBirthYear.setText(sdf.format(myCalendar.getTime()));

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private boolean Validation() {
        String MobilePattern = "[0-9]{10}";
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (edtEnterName.getText().toString().isEmpty()) {

            Toast.makeText(this, getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();

            return false;


        } /*else if (edtBirthYear.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please enter dob", Toast.LENGTH_SHORT).show();

            return false;

        }*/ else if (edtEmailSignUp.getText().toString().isEmpty()) {

            Toast.makeText(this, getString(R.string.please_enter_email_id), Toast.LENGTH_SHORT).show();

            return false;

        } else if (!edtEmailSignUp.getText().toString().matches(emailPattern)) {

            Toast.makeText(getApplicationContext(), getString(R.string.please_enter_valid_email_address), Toast.LENGTH_SHORT).show();
            return false;

        } else if (tvAppLanguageSignUp.getText().toString().isEmpty()) {

            Toast.makeText(this, getString(R.string.please_select_app_languages), Toast.LENGTH_SHORT).show();

            return false;

        } /*else if (tvSpeakLanguageSignUp.getText().toString().isEmpty()) {

            Toast.makeText(this, R.string.please_select_speak_lanugae, Toast.LENGTH_SHORT).show();

            return false;

        }*/

        return true;

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

        edtBirthYear.setText(date);
    }

    void showDate(int dayOfMonth, int monthOfYear, int year, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(SignUp.this)
                .callback(SignUp.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(dayOfMonth, monthOfYear, year)
                .build()
                .show();
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
