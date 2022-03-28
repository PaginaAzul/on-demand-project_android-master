package com.pagin.azul.fragment;


import android.app.Activity;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.DeliveryWorkerOrderDashboardActivity;
import com.pagin.azul.activities.OrderDetailsOnMapActivity;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.adapter.NewDeliveryWorkerDashboardAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.bean.RequestOrderResponse;
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

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;


public class NewOderDashboardDeliverWorkerFrag extends Fragment implements NewDeliveryWorkerDashboardAdapter.OnClickReview {
    private NewDeliveryWorkerDashboardAdapter dashboardAdapter;
    @BindView(R.id.rv_new)
    RecyclerView rvNew;
    @BindView(R.id.nodata)
    TextView nodata;

    ArrayList<NormalUserPendingOrderInner> newList;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String offerType = intent.getStringExtra("offerAcceptOfDelivery");
            if(offerType!=null) {
                if (offerType.equalsIgnoreCase("orderAvailableForDelivery")) {
                    getNewOrderDeliveryPersonApi();
                }else if (offerType.equalsIgnoreCase("offerAcceptOfDelivery")) {

                        startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(getActivity(), "DeliveryOffer"));

                        getActivity().finish();
                }
            }
        }
    };

    public NewOderDashboardDeliverWorkerFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_oder_dashboard_deliver_worker, container, false);
        ButterKnife.bind(this, view);

        newList = new ArrayList<>();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);    //for hideKeyboard.......


        getNewOrderDeliveryPersonApi();
        return view;
    }


    @Override
    public void onClickReview(NormalUserPendingOrderInner data) {
        startActivity(UserDetailsActivity.getIntent(getActivity(), data, "FromNewDP"));

    }

    @Override
    public void onClickMakeOffer(NormalUserPendingOrderInner offerdata) {

        callcheckOrderAcceptOrNotApi(offerdata);
        //startActivity(OrderDetailsOnMapActivity.getIntent(getActivity(),offerdata,"delivery"));


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("Tap Successful"));
    }

    private void getNewOrderDeliveryPersonApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("lat", "28.6258244");
                jsonObject.put("long", "77.3774374");
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getNODeliveryPerson(token,body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    newList = response.body().getDataList();
                                    rvNew.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    dashboardAdapter = new NewDeliveryWorkerDashboardAdapter(getActivity(), newList, NewOderDashboardDeliverWorkerFrag.this);
                                    rvNew.setAdapter(dashboardAdapter);
                                    rvNew.setVisibility(View.VISIBLE);
                                    nodata.setVisibility(View.GONE);
                                } else {
                                    rvNew.setVisibility(View.GONE);
                                    nodata.setVisibility(View.VISIBLE);
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

    private void callcheckOrderAcceptOrNotApi(NormalUserPendingOrderInner offerdata) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("orderId", offerdata.get_id());

                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RequestOrderResponse> beanCall = apiInterface.checkOrderAcceptOrNot(token,body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                           startActivity(OrderDetailsOnMapActivity.getIntent(getActivity(),offerdata,"delivery"));
                           getActivity().finish();

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getNewOrderDeliveryPersonApi();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOrderResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideNativeKeyboard(Activity pActivity) {
        if (pActivity != null) {
            if (pActivity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) pActivity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(pActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }
}
