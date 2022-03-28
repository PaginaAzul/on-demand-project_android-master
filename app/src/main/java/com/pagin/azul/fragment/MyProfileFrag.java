package com.pagin.azul.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hbb20.CountryCodePicker;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.MyProfile;
import com.pagin.azul.bean.MyProfileResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.FoodAndGroceryActivity;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFrag extends Fragment {

    public MyProfileFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.ivMyProfile)
    ImageView ivMyProfile;

    @BindView(R.id.tvMyProfileID)
    TextView tvMyProfileID;

    @BindView(R.id.tvEmailID)
    TextView tvEmailID;

    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;

    @BindView(R.id.tvAppLanguageProfile)
    TextView tvAppLanguageProfile;

    @BindView(R.id.tvSpeakLanguageProfile)
    TextView tvSpeakLanguageProfile;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick({R.id.btnEditProfile})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditProfile:
                dispatchMyProfileActivity();
                break;
        }
    }

    private void dispatchMyProfileActivity() {
        Context context = getContext();
        Intent intent = new Intent(context, MyProfile.class);
        if(context instanceof FoodAndGroceryActivity)
            intent.putExtra(ParamEnum.TYPE.theValue(),FoodAndGroceryActivity.class.getSimpleName());
        else
            intent.putExtra(ParamEnum.TYPE.theValue(),HomeMainActivity.class.getSimpleName());
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            ((HomeMainActivity)getActivity()).tv_title.setVisibility(View.VISIBLE);
            ((HomeMainActivity)getActivity()).tv_title.setText(R.string.my_profile);
            if(isNetworkAvailable(getContext())){
                myProfileApi();
            }else {
                Toast.makeText(getContext(), "Internet connection lost", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }catch (Exception e){
            //e.printStackTrace();
            if(isNetworkAvailable(getContext())){
                myProfileApi();
            }else {
                Toast.makeText(getContext(), "Internet connection lost", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
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
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }

    private void myProfileApi() {
        try {
            MyDialog.getInstance(getContext()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<MyProfileResponse> beanCall = apiInterface.getUserDetails(token, body);

                beanCall.enqueue(new Callback<MyProfileResponse>() {
                    @Override
                    public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                        MyDialog.getInstance(getContext()).hideDialog();
                        if (response.isSuccessful()) {
                            MyProfileResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body().getProfileInner() != null && !response.body().getProfileInner().equals("")) {

                                    //tvMyProfileID.setText("My ID: " + response.body().getProfileInner().get_id());
                                    tvMyProfileID.setText(response.body().getProfileInner().getName());
                                    tvEmailID.setText(response.body().getProfileInner().getEmail());
                                    tvPhoneNumber.setText(response.body().getProfileInner().getMobileNumber());
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
                                    if(SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                                        ccp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
                                    }else {
                                        ccp.changeDefaultLanguage(CountryCodePicker.Language.PORTUGUESE);
                                    }
//                                    if (!response.body().getProfileInner().getProfilePic().equalsIgnoreCase("")) {
//                                        Picasso.with(MyProfile.this).load(response.body().getProfileInner().getProfilePic()).into(ivMyProfile);
//                                    }

                                    Glide.with(getContext())
                                            .load(response.body().getProfileInner().getProfilePic())
                                            .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                                                    .error(R.drawable.profile_default))
                                            .into(ivMyProfile);

                                }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                    CommonUtility.showDialog1(getActivity());
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                                //Toast.makeText(MyProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
}
