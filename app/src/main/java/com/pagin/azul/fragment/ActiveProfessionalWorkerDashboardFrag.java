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
import com.pagin.azul.activities.ActiveTrackingActivity;
import com.pagin.azul.activities.CancellationActivity;
import com.pagin.azul.activities.DeliveryMessageTrackActivity;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.NUMessageDeliveryPersonActivity;
import com.pagin.azul.activities.ProfessionalWorkerOrderDashboardActivity;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.adapter.ActiveProfessionalWorkerDashboardAdapter;
import com.pagin.azul.bean.MessageTrackResponse;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveProfessionalWorkerDashboardFrag extends Fragment implements ActiveProfessionalWorkerDashboardAdapter.OnContentClickProActive {
    @BindView(R.id.rv_active)
    RecyclerView rvActive;
    @BindView(R.id.nodata)
    TextView nodata;
    private ActiveProfessionalWorkerDashboardAdapter activeAdapter;
    private ArrayList<NormalUserPendingOrderInner> activeList;
    private ScheduledExecutorService scheduleTaskExecutor;
    private Socket mSocket;
    private GPSTracker gpsTracker;
    private double lat;
    private double lng;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("offerAccept").equalsIgnoreCase("orderCancel")) {
                startActivity(HomeMainActivity.getIntent(getActivity(), ""));
                getActivity().finish();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_professional_worker_dashboard, container, false);
        ButterKnife.bind(this, view);

        activeList = new ArrayList<>();

        callActiveOrderPWorkerApi();
        getCurrentLocation();
        return view;
    }

    private void getCurrentLocation() {
        gpsTracker = new GPSTracker(getContext(), getActivity());
        lat = gpsTracker.getLatitude();
        lng = gpsTracker.getLongitude();
    }

    @Override
    public void onReviewClick(NormalUserPendingOrderInner data) {
        startActivity(UserDetailsActivity.getIntent(getActivity(), data, "FromActivePW"));
    }

    @Override
    public void onMsgClick(NormalUserPendingOrderInner getData) {
        startActivity(NUMessageDeliveryPersonActivity.getIntent(getActivity(), getData, "FromPrfDashBoard"));
    }

    @Override
    public void onWorkDoneClick(int position, String orderId, NormalUserPendingOrderInner getData) {
        //showWorkDoneDialog(orderId);
        if (getData.getPopupStatus().equalsIgnoreCase("Show")) {
            showInstrucationDialog(getData);
        } else {
            startActivity(DeliveryMessageTrackActivity.getIntent(getActivity(), getData));
        }
    }

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


                        }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(getActivity());
                        } else {
                            Toast.makeText(getActivity(), "" + signUpWithMobileResp.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

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

    @Override
    public void onCancelOffer(NormalUserPendingOrderInner getData) {
        startActivity(CancellationActivity.getIntent(getActivity(), getData));
    }

    @Override
    public void onTrackClick(NormalUserPendingOrderInner getTrackdata) {
        startActivity(ActiveTrackingActivity.getIntent(getActivity(), getTrackdata, "ActivePW"));

    }

    @Override
    public void onArrivedClick(NormalUserPendingOrderInner getArrivedata) {
        //callArrivedApi(getArrivedata.get_id());
    }

    @Override
    public void onGoClick(int position, String orderID) {
        //callGoStatusApi(orderID);
    }


    //Make Connection for tracking
    private void trackingConnection() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("roomId", activeList.get(0).getOrderOwner() + activeList.get(0).getRealOrderId());
            mSocket.emit("room join", obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        callActiveOrderPWorkerApi();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("offerAccept"));
    }



    private void connectSockettest() {
        MyDialog.getInstance(getActivity()).hideDialog();
        try {
            mSocket = IO.socket("http://18.189.223.53:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        try {
            JSONObject obj = new JSONObject();
            obj.put("roomId", activeList.get(0).getOrderOwner() + activeList.get(0).getRealOrderId());
            mSocket.emit("room join", obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Share Updated Location
    private void updateLoaction() {

        try {
            JSONObject obj = new JSONObject();

            obj.put("roomId", activeList.get(0).getOrderOwner() + activeList.get(0).getRealOrderId());
            obj.put("lattitude", lat);
            obj.put("longitude", lng);
            mSocket.emit("tracking", obj);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Handler for sharelocation on server...
    private void startScheduleExecutorService() {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(6);
// This schedule a runnable task every 5 second
        scheduleTaskExecutor.scheduleAtFixedRate(() -> getActivity().runOnUiThread(this::updateLoaction), 0, 6, TimeUnit.SECONDS);
    }


    private void callActiveOrderPWorkerApi() {
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
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getActiveOrderPWorker(token, body);
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
                                    activeAdapter = new ActiveProfessionalWorkerDashboardAdapter(getActivity(), activeList, ActiveProfessionalWorkerDashboardFrag.this);
                                    rvActive.setAdapter(activeAdapter);
                                    nodata.setVisibility(View.GONE);
                                    rvActive.setVisibility(View.VISIBLE);
                                    connectSockettest();

                                    if (activeList.get(0).getGoStatus().equalsIgnoreCase("true")) {
                                        startScheduleExecutorService();
                                    }

                                } else {
                                    nodata.setVisibility(View.VISIBLE);
                                    rvActive.setVisibility(View.GONE);
                                }
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
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.workDoneByProfessionalWorker(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                ((ProfessionalWorkerOrderDashboardActivity) getActivity()).selectTab();
                                getFragmentManager().beginTransaction().replace(R.id.container, new PastProfessionalWorkerDashboardFrag()).commit();
//                                allClick();
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

                                //callActiveOrderPWorkerApi();

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

                                //callActiveOrderPWorkerApi();

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
                //callWorkDoneByDeliveryPersonApi(orderId);

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdownNow();
        }

        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mSocket != null)
            mSocket.disconnect();

        if (scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdownNow();
        }
    }
}
