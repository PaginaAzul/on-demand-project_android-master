package com.pagin.azul.fragment;


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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.CancellationActivity;
import com.pagin.azul.activities.ViewAllOffersActivity;
import com.pagin.azul.adapter.NUPendingDeliveryPersonAdapter;
import com.pagin.azul.adapter.NUPendingProfessionalWorkerAdapter;
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
import static com.pagin.azul.Constant.Constants.kOrderId;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;
import static com.pagin.azul.Constant.Constants.kUseroffer;

/**
 * A simple {@link Fragment} subclass.
 */
public class NUPendingDeliverPersongFrag extends Fragment {
    private NUPendingDeliveryPersonAdapter pendingAdapter;
    private NUPendingProfessionalWorkerAdapter pendingProfAdapter;

    private ArrayList<NormalUserPendingOrderInner> pendingList;

    @BindView(R.id.rv_pending)
    RecyclerView rvPending;
    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.iv_blank)
    ImageView iv_blank;
    private String from;

    //  BROADCAST RECEIVER CLASS Object : TO tap
    private BroadcastReceiver pushNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                Bundle bundle = intent.getExtras();
                String p_id = bundle.getString("offerAvailable");
                if (from.equalsIgnoreCase("delivery")) {
                    callGetOrderApi();

                } else {
                    if (p_id != null && p_id.equalsIgnoreCase("offerAvailableProfessional")) {
                        callGetOrderProfApi();

                    }
                }
            }
        }
    };


    public NUPendingDeliverPersongFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nupending_deliver_persong, container, false);
        ButterKnife.bind(this, view);

        pendingList = new ArrayList<>();


        if (getArguments() != null) {
            from = getArguments().getString(kFrom);
            String fromCat = getArguments().getString(kFromCategory);

            /*if (from.equalsIgnoreCase("delivery")) {
                callGetOrderApi();

            } else {
                if (from.equalsIgnoreCase("professional")) {
                    callGetOrderProfApi();

                }

            }*/
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (from.equalsIgnoreCase("delivery")) {
            callGetOrderApi();

        } else {
            if (from.equalsIgnoreCase("professional")) {
                callGetOrderProfApi();

            }

        }

        getActivity().registerReceiver(pushNotifyReceiver,
                new IntentFilter("Tap Successful"));
    }

    private void allClick() {

        pendingAdapter.setListener(new NUPendingDeliveryPersonAdapter.ViewOffersInterface() {
            @Override
            public void onAllOfferClick(View v, int pos, String orderId, String userId) {
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kOrderId, orderId);
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kUseroffer, userId);

                startActivity(ViewAllOffersActivity.getIntent(getActivity(), "FromNUDP"));

            }

            @Override
            public void onEditClick(View v, int pos) {

            }

            @Override
            public void onCancelClick(NormalUserPendingOrderInner getData) {
                startActivity(CancellationActivity.getIntent(getActivity(), getData,"FromNUDP"));
            }
        });
    }


    private void callGetOrderApi() {
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

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getPendingOrder(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                if (response.body().getPendingOrderInner().getOrderdocData() != null && response.body().getPendingOrderInner().getOrderdocData().size() > 0) {
                                    pendingList = response.body().getPendingOrderInner().getOrderdocData();
                                    rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    pendingAdapter = new NUPendingDeliveryPersonAdapter(getActivity(), pendingList);
                                    rvPending.setAdapter(pendingAdapter);
                                    no_data.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.GONE);
                                    rvPending.setVisibility(View.VISIBLE);

                                    allClick();

                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    iv_blank.setVisibility(View.VISIBLE);
                                    rvPending.setVisibility(View.GONE);
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


    private void callGetOrderProfApi() {
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

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getPendingOrder(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                if (response.body().getPendingOrderInner().getOrderdocData() != null && response.body().getPendingOrderInner().getOrderdocData().size() > 0) {
                                    pendingList = response.body().getPendingOrderInner().getOrderdocData();
                                    rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    pendingProfAdapter = new NUPendingProfessionalWorkerAdapter(getActivity(), pendingList);
                                    rvPending.setAdapter(pendingProfAdapter);
                                    no_data.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.GONE);
                                    rvPending.setVisibility(View.VISIBLE);
                                    allProfClick();

                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    iv_blank.setVisibility(View.VISIBLE);
                                    rvPending.setVisibility(View.GONE);
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


    private void allProfClick() {

        pendingProfAdapter.setListener(new NUPendingProfessionalWorkerAdapter.ViewOffersInterface() {

            @Override
            public void onAllOfferClick(View v, int pos, String id, String userId) {
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kOrderId, id);
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kUseroffer, userId);

                startActivity(ViewAllOffersActivity.getIntent(getActivity(), "FromNUPW"));
            }

            @Override
            public void onEditClick(View v, int pos) {
                getActivity().finish();
            }

            @Override
            public void onCancelClick(NormalUserPendingOrderInner getData) {
                startActivity(CancellationActivity.getIntent(getActivity(), getData,"FromNUPW"));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(pushNotifyReceiver);
    }
}
