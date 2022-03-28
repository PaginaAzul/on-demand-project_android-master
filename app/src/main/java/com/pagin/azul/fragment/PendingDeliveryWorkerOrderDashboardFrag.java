package com.pagin.azul.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.adapter.PendingDeliveryWorkerDashboardAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;


public class PendingDeliveryWorkerOrderDashboardFrag extends Fragment implements PendingDeliveryWorkerDashboardAdapter.OnReviewClickPending {
    private PendingDeliveryWorkerDashboardAdapter pendingAdapter;
    private ArrayList<NormalUserPendingOrderInner> pendingList;

    @BindView(R.id.rv_pending)
    RecyclerView rvPending;
    @BindView(R.id.id_nodata)
    TextView id_nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_delivery_worker_order_dashboard_fraghboar, container, false);
        ButterKnife.bind(this, view);

        pendingList = new ArrayList<>();

        getActivity().registerReceiver(pushNotifyReceiver,
                new IntentFilter("Tap Successful"));

        getPendingOrderDPApi();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //      REGISTER LocalBroadcastManager TO HANDLE PUSH
        getActivity().registerReceiver(pushNotifyReceiver,
                new IntentFilter("Tap Successful"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("Check", "onDestroy");

        //      UNREGISTER LocalBroadcastManager
        getActivity().unregisterReceiver(pushNotifyReceiver);
    }


    //  BROADCAST RECEIVER CLASS Object : TO tap
    private BroadcastReceiver pushNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                Bundle bundle = intent.getExtras();

                String p_id = bundle.getString("offerAvailable");

                getPendingOrderDPApi();

            }


            Log.w(PendingDeliveryWorkerOrderDashboardFrag.class.getSimpleName(), "Tap Successful");
        }
    };

    @Override
    public void onReviewClick(NormalUserPendingOrderInner getRatingData) {
        startActivity(UserDetailsActivity.getIntent(getActivity(), getRatingData, "FromPendingDP"));

    }


    private void getPendingOrderDPApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("lat", SharedPreferenceWriter.getInstance(getActivity()).getString(kLat));
                jsonObject.put("long", SharedPreferenceWriter.getInstance(getActivity()).getString(kLong));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getPendingOrderDP(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    pendingList = response.body().getDataList();
                                    rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    pendingAdapter = new PendingDeliveryWorkerDashboardAdapter(getActivity(), pendingList, PendingDeliveryWorkerOrderDashboardFrag.this);
                                    rvPending.setAdapter(pendingAdapter);
                                    id_nodata.setVisibility(View.GONE);
                                    rvPending.setVisibility(View.VISIBLE);
                                } else {
                                    id_nodata.setVisibility(View.VISIBLE);
                                    rvPending.setVisibility(View.GONE);
                                }

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<NormalUserPendingOrderResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
