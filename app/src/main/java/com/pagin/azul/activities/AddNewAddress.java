package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.AddressApi;
import com.pagin.azul.bean.UpdateSettingNoti;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
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

public class AddNewAddress extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_title)
    EditText et_title;

    @BindView(R.id.et_landmark)
    EditText et_landmark;

    @BindView(R.id.et_youraddress)
    EditText et_youraddress;

    String title, landmark, youraddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        ButterKnife.bind(this);
        tv_title.setText("Add New Address");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String add = bundle.getString("Add");
                String land = bundle.getString("Land");
                String title = bundle.getString("Title");
                et_youraddress.setText(add);
                et_landmark.setText(land);
                et_title.setText(title);
            }

        }

    }

    private boolean validation() {
        if (et_title.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please provide title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_landmark.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please provide landmark", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_youraddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please provide address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @OnClick({R.id.iv_back, R.id.btn_submit})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_submit:
                if (validation()) {
                    if (getIntent() != null) {
                        Bundle bundle = getIntent().getExtras();
                        if (bundle.getString("From").equalsIgnoreCase("Edit")) {
                            String addressId = bundle.getString("addressId");
                            updateAddressApi(addressId);
                        } else if (bundle.getString("From").equalsIgnoreCase("ManageAdd")) {
                            addressApiHit();
                        }
                    }
                }
                break;
        }
    }

    private void updateAddressApi(String id) {
        try {
            MyDialog.getInstance(this).showDialog(AddNewAddress.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("address", et_youraddress.getText().toString().trim());
                jsonObject.put("title", et_title.getText().toString().trim());
                jsonObject.put("landmark", et_landmark.getText().toString().trim());
                jsonObject.put("addressId", id);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UpdateSettingNoti> beanCall = apiInterface.updateAddress(token,body);

                beanCall.enqueue(new Callback<UpdateSettingNoti>() {
                    @Override
                    public void onResponse(Call<UpdateSettingNoti> call, Response<UpdateSettingNoti> response) {
                        MyDialog.getInstance(AddNewAddress.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(AddNewAddress.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewAddress.this, ManageAddressActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(AddNewAddress.this);
                            } else {
                                Toast.makeText(AddNewAddress.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateSettingNoti> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }

    }

    private void addressApiHit() {
        try {
            MyDialog.getInstance(this).showDialog(AddNewAddress.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("address", et_youraddress.getText().toString().trim());
                jsonObject.put("title", et_title.getText().toString().trim());
                jsonObject.put("landmark", et_landmark.getText().toString().trim());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<AddressApi> beanCall = apiInterface.addAddress(token,body);

                beanCall.enqueue(new Callback<AddressApi>() {
                    @Override
                    public void onResponse(Call<AddressApi> call, Response<AddressApi> response) {
                        MyDialog.getInstance(AddNewAddress.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(AddNewAddress.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewAddress.this, ManageAddressActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(AddNewAddress.this);
                            } else {
                                Toast.makeText(AddNewAddress.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AddressApi> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
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
