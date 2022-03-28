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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.IssuesMyOrderActivity;
import com.pagin.azul.activities.NormalMessageTrackActivity;
import com.pagin.azul.adapter.NUActiveDeliveryPersonAdapter;
import com.pagin.azul.adapter.NUActiveProWorkerAdapter;
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

import static com.pagin.azul.Constant.Constants.kFrom;
import static com.pagin.azul.Constant.Constants.kFromCategory;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

/**
 * A simple {@link Fragment} subclass.
 */
public class NUActiveDeliveryPersonFrag extends Fragment {
    private NUActiveProWorkerAdapter activeProfAdapter;
    private NUActiveDeliveryPersonAdapter activeAdapter;
    private ArrayList<NormalUserPendingOrderInner> activeList;

    @BindView(R.id.rv_active)
    RecyclerView rvActive;
    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.iv_blank)
    ImageView iv_blank;

    public NUActiveDeliveryPersonFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuactive_delivery_person, container, false);
        ButterKnife.bind(this, view);

        activeList = new ArrayList<>();

//        rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));
//        activeAdapter = new NUActiveDeliveryPersonAdapter(getActivity(), activeList);
//        rvActive.setAdapter(activeAdapter);


//        if (getArguments() != null) {
//            String from = getArguments().getString(kFrom);
//            String fromCat = getArguments().getString(kFromCategory);
//
//            if (from.equalsIgnoreCase("delivery")) {
//                callActiveOrderDeliveryPersonApi();
//
//            } else {
//                if (from.equalsIgnoreCase("professional")) {
//                    callActiveOrderProfApi();
//                }
//
//            }
//        }
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null) {
            String from = getArguments().getString(kFrom);
            String fromCat = getArguments().getString(kFromCategory);

            if (from.equalsIgnoreCase("delivery")) {
                callActiveOrderDeliveryPersonApi();

            } else {
                if (from.equalsIgnoreCase("professional")) {
                    callActiveOrderProfApi();
                }

            }
        }

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

                String p_id = bundle.getString("workDoneByDP");

                //callActiveOrderDeliveryPersonApi();

            }

            Log.w(NUActiveDeliveryPersonFrag.class.getSimpleName(), "workDoneByDP");
        }
    };


    private void findCallback() {

        activeAdapter.setListener(new NUActiveDeliveryPersonAdapter.ActiveRequireProff() {
            @Override
            public void onReportOrder(View v, int pos) {
                startActivity(IssuesMyOrderActivity.getIntent(getActivity()));
            }

            @Override
            public void onMessageTrack(NormalUserPendingOrderInner getData) {

                if (getData.getPopupStatus().equalsIgnoreCase("Show")) {
                    showInstrucationDialog(getData);

                } else {
                    startActivity(NormalMessageTrackActivity.getIntent(getActivity(), getData));
                }

            }

        });

    }


    private void findProfCallback() {

        activeProfAdapter.setListener(new NUActiveProWorkerAdapter.ActiveRequireProff() {
            @Override
            public void onReportOrder(View v, int pos) {
                startActivity(IssuesMyOrderActivity.getIntent(getActivity()));
            }

            @Override
            public void onCancelOrder(NormalUserPendingOrderInner getData) {
                //startActivity(CancellationActivity.getIntent(getActivity(), getData));
                if (getData.getPopupStatus().equalsIgnoreCase("Show")) {
                    showInstrucationDialog(getData);
                } else {
                    startActivity(NormalMessageTrackActivity.getIntent(getActivity(), getData));
                }
            }
        });


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

                startActivity(NormalMessageTrackActivity.getIntent(getActivity(), getData));

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
            jsonObject.put("orderId", getData.get_id());
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
                jsonObject.put("serviceType", "DeliveryPersion");
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");

                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getNormalUserActiveOrder(token, body);
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
                                    activeAdapter = new NUActiveDeliveryPersonAdapter(getActivity(), activeList);
                                    rvActive.setAdapter(activeAdapter);
                                    no_data.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.GONE);
                                    rvActive.setVisibility(View.VISIBLE);

                                    findCallback();
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    iv_blank.setVisibility(View.VISIBLE);
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


    private void callActiveOrderProfApi() {
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
                jsonObject.put("serviceType", "ProfessionalWorker");
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getNormalUserActiveOrder(token, body);
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
                                    activeProfAdapter = new NUActiveProWorkerAdapter(getActivity(), activeList);
                                    rvActive.setAdapter(activeProfAdapter);
                                    no_data.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.GONE);
                                    rvActive.setVisibility(View.VISIBLE);

                                    findProfCallback();
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    iv_blank.setVisibility(View.VISIBLE);
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

}
