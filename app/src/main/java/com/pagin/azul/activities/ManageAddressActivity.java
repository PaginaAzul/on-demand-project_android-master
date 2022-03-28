package com.pagin.azul.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.ManageAddressesAdapter;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.bean.DeleteAddress;
import com.pagin.azul.bean.GetAddressList;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

public class ManageAddressActivity extends AppCompatActivity implements ManageAddressesAdapter.Listener {
    private ManageAddressesAdapter addressesAdapter;
    ArrayList<String> addressesList;
    @BindView(R.id.iv_back)
    ImageView iv_back;


    @BindView(R.id.rv_m_addresses)
    RecyclerView addressRecyclerView;

    private String address = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);
        ButterKnife.bind(this);
        addressesList = new ArrayList<>();
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCurrentLocation();
        addressApiHit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressApiHit();
    }

    /* private void preParedData() {

        addressesList.add("My Home");
        addressesList.add("My Office");
        addressesList.add("My Office");
        addressesList.add("My Home");
        addressesAdapter.notifyDataSetChanged();
    }*/

    private void addressApiHit() {
        try {
            MyDialog.getInstance(this).showDialog(ManageAddressActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<GetAddressList> beanCall = apiInterface.getAddressList(token,body);

                beanCall.enqueue(new Callback<GetAddressList>() {
                    @Override
                    public void onResponse(Call<GetAddressList> call, Response<GetAddressList> response) {
                        MyDialog.getInstance(ManageAddressActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                List<AddressList> addressesList = response.body().getData().getDocs().get(0).getAddresses();
                                addressesAdapter = new ManageAddressesAdapter(ManageAddressActivity.this, addressesList, ManageAddressActivity.this);
                                addressRecyclerView.setAdapter(addressesAdapter);
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ManageAddressActivity.this);
                            } else {
                                Toast.makeText(ManageAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<GetAddressList> call, Throwable t) {
                        Log.d("Fail", t.getMessage());
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }


    }


    @OnClick({R.id.rl_add_card, R.id.iv_back})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_card:
                startActivity(AddressPickerAct.getIntent(this,"PICKUP",address));
                //startActivity(PickAddressActivity.getIntent(this,"manageAddress",""));
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void getCurrentLocation() {
        GPSTracker gpsTracker = new GPSTracker(this, this);
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int position, String id) {


        showDialog1(position,id);

    }


    private void showDialog1(int position,String id) {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText(R.string.r_u_sure_delete_address);
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

                deleteApi(position, id);
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void deleteApi(int position, String id) {

        try {
            MyDialog.getInstance(this).showDialog(ManageAddressActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("addressId", id);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<DeleteAddress> beanCall = apiInterface.deleteAddress(token,body);

                beanCall.enqueue(new Callback<DeleteAddress>() {
                    @Override
                    public void onResponse(Call<DeleteAddress> call, Response<DeleteAddress> response) {
                        MyDialog.getInstance(ManageAddressActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                addressesAdapter.removeitem(position);

                                // recreate();
                                Toast.makeText(ManageAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ManageAddressActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteAddress> call, Throwable t) {
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
    public void onAddressClick(int position, List<AddressList> addressList) {
        Intent intent = new Intent();
        intent.putExtra(ParamEnum.DATA.theValue(),addressList.get(position));
        setResult(Activity.RESULT_OK,intent);
        finish();
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
