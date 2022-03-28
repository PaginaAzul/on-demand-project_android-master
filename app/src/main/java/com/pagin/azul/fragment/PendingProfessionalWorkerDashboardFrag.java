package com.pagin.azul.fragment;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.CancellationActivity;
import com.pagin.azul.activities.ContactAdmin;
import com.pagin.azul.activities.ReportOrderActivity;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.adapter.PendingProfessionalWorkerDashboardAdapter;
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


public class PendingProfessionalWorkerDashboardFrag extends Fragment implements PendingProfessionalWorkerDashboardAdapter.OnContentClickPendingPro {
    private PendingProfessionalWorkerDashboardAdapter pendingAdapter;
    ArrayList<NormalUserPendingOrderInner> pendingList;
    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.rv_pending)
    RecyclerView rvPending;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_professional_worker_dashboard, container, false);
        ButterKnife.bind(this, view);

        pendingList = new ArrayList<>();


        //callPendingOrderPWorkerApi();
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        //      REGISTER LocalBroadcastManager TO HANDLE PUSH
        getActivity().registerReceiver(pushNotifyReceiver,
                new IntentFilter("offerAvailableProfessional"));
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

                String p_id = bundle.getString("offerAvailableProfessional");

                //callPendingOrderPWorkerApi();

            }


            Log.w(PendingProfessionalWorkerDashboardFrag.class.getSimpleName(), "offerAvailableProfessional");
        }
    };


    @Override
    public void onReviewClick(NormalUserPendingOrderInner data) {
        startActivity(UserDetailsActivity.getIntent(getActivity(), data, "FromPastPW"));
    }

    @Override
    public void onReportOrderClick(NormalUserPendingOrderInner getReportData) {
        startActivity(ReportOrderActivity.getIntent(getActivity(), getReportData, "PendingPrf"));
    }

    @Override
    public void onCancelClick(NormalUserPendingOrderInner getData) {
        startActivity(CancellationActivity.getIntent(getActivity(), getData));
    }

    @Override
    public void onContactAdminClick(int position) {
        startActivity(new Intent(getActivity(), ContactAdmin.class));
    }


    private void callPendingOrderPWorkerApi() {
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
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getPendingOrderPWorker(token,body);

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
                                    pendingAdapter = new PendingProfessionalWorkerDashboardAdapter(getActivity(), pendingList, PendingProfessionalWorkerDashboardFrag.this);
                                    rvPending.setAdapter(pendingAdapter);
                                    no_data.setVisibility(View.GONE);
                                    rvPending.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
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


    private void showMakeNewOfferDialog() {
        final Dialog dialog = new Dialog(getContext(), R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_make_new_offer_dialog);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
