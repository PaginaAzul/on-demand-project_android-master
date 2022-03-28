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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.DeliveryMessageTrackActivity;
import com.pagin.azul.activities.DeliveryWorkerOrderDashboardActivity;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.adapter.ActiveDeliveryWorkerDashboardAdapter;
import com.pagin.azul.bean.MessageTrackResponse;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

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


public class ActiveDeliveryWorkerOrderDashboardFrag extends Fragment implements ActiveDeliveryWorkerDashboardAdapter.OnClickContentListener {
    @BindView(R.id.rv_active)
    RecyclerView rvActive;
    @BindView(R.id.no_data)
    TextView no_data;
    private ActiveDeliveryWorkerDashboardAdapter activeAdapter;
    private ArrayList<NormalUserPendingOrderInner> activeList;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             if(intent.getStringExtra("offerAccept").equalsIgnoreCase("orderCancel")) {
                startActivity(HomeMainActivity.getIntent(getActivity(), ""));
                getActivity().finish();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_active_delivery_worker_order_dashboard, container, false);
        ButterKnife.bind(this, view);

        activeList = new ArrayList<>();


        callActiveOrderDeliveryPersonApi();

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        callActiveOrderDeliveryPersonApi();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("offerAccept"));
    }

    @Override
    public void onStop() {
        super.onStop();
        if(broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onWorkDoneClick(NormalUserPendingOrderInner data) {


        if (data.getPopupStatus().equalsIgnoreCase("Show")) {
            showInstrucationDialog(data);

        } else {
            startActivity(DeliveryMessageTrackActivity.getIntent(getActivity(), data));

        }


//        showWorkDoneDialog(orderId);
    }

//    @Override
//    public void onClickCancelActive(NormalUserPendingOrderInner getData) {
//        startActivity(CancellationActivity.getIntent(getActivity(), getData));
//    }
//
//    @Override
//    public void onMsgClick(NormalUserPendingOrderInner getData) {
//        startActivity(NUMessageDeliveryPersonActivity.getIntent(getActivity(), getData, "FromDeliveryDash"));
//    }
//
//    @Override
//    public void onTrackClick(NormalUserPendingOrderInner getTrackdata) {
//        startActivity(ActiveTrackingActivity.getIntent(getActivity(), getTrackdata, "ActiveDP"));
//    }

    @Override
    public void onReviewClick(NormalUserPendingOrderInner data) {
        startActivity(UserDetailsActivity.getIntent(getActivity(), data, "FromActiveDP"));
    }
//
//    @Override
//    public void onClickGo(int position, String orderID) {
//        callGoStatusApi(orderID);
//    }
//
//    @Override
//    public void onArrivedClick(NormalUserPendingOrderInner getArrivedata) {
//        callArrivedApi(getArrivedata.get_id());
//    }


    private void showInstrucationDialog(NormalUserPendingOrderInner getData) {

        Dialog dialog = new Dialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_instrucation_normal_layout, null, false);
        dialog.setContentView(view);

        Button btn_ok = view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showGreatDialog(getData);
                updatePopupStatusApi(getData);

                dialog.dismiss();

            }
        });

        dialog.show();

    }


    private void showGreatDialog(NormalUserPendingOrderInner getData) {

        Dialog dialog = new Dialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_great_layout, null, false);
        dialog.setContentView(view);

        Button btn_ok = view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(DeliveryMessageTrackActivity.getIntent(getActivity(), getData));

                dialog.dismiss();

            }
        });

        dialog.show();

    }


    private void updatePopupStatusApi(NormalUserPendingOrderInner getData) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("offerId", getData.get_id());
            jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            Call<MessageTrackResponse> call = apiInterface.updatePopupStatus(body);
            call.enqueue(new Callback<MessageTrackResponse>() {
                @Override
                public void onResponse(Call<MessageTrackResponse> call, Response<MessageTrackResponse> response) {
                    MyDialog.getInstance(getActivity()).hideDialog();
                    if (response.isSuccessful()) {
                        MessageTrackResponse signUpWithMobileResp = response.body();
                        if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {


                        } else {
                            Toast.makeText(getActivity(), "" + signUpWithMobileResp.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

                    }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                        CommonUtility.showDialog1(getActivity());
                    } else {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<MessageTrackResponse> call, Throwable t) {
                    MyDialog.getInstance(getActivity()).hideDialog();
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callActiveOrderDeliveryPersonApi() {
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
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getActiveOrderDeliveryPerson(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    activeList = response.body().getDataList();
                                    rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    activeAdapter = new ActiveDeliveryWorkerDashboardAdapter(getActivity(), activeList, ActiveDeliveryWorkerOrderDashboardFrag.this);
                                    rvActive.setAdapter(activeAdapter);
                                    no_data.setVisibility(View.GONE);
                                    rvActive.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    rvActive.setVisibility(View.GONE);
                                }


//                                allClick();

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
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


    private void callWorkDoneByDeliveryPersonApi(String orderId) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("orderId", orderId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.workDoneByDeliveryPerson(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                ((DeliveryWorkerOrderDashboardActivity) getActivity()).selectTab();

                                getFragmentManager().beginTransaction().replace(R.id.container, new PastDeliveryWorkerOrderDashboardFrag()).commit();

//                                allClick();

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
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

    private void callGoStatusApi(String orderID) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.goStatus(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                callActiveOrderDeliveryPersonApi();

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


    private void callArrivedApi(String orderID) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.arrivedStatus(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                callActiveOrderDeliveryPersonApi();

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


    private void showWorkDoneDialog(String orderId) {
        Dialog dialog = new Dialog(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText("Are you sure want to work done status update?");
        registerNow.setText("OK");
        notNow.setText("Cancel");


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWorkDoneByDeliveryPersonApi(orderId);

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
