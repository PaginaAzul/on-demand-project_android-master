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
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.OrderDetailsOnMapActivity;
import com.pagin.azul.activities.ProfessionalWorkerOrderDashboardActivity;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.adapter.NewProfessionalWorkerDashboardAdapter;
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

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProfessionalWorkerDashboardFrag extends Fragment implements NewProfessionalWorkerDashboardAdapter.OnContentClickProfessionalNew {
    ArrayList<NormalUserPendingOrderInner> newList;
    @BindView(R.id.rv_new)
    RecyclerView rvNew;
    @BindView(R.id.no_data)
    TextView no_data;
    private NewProfessionalWorkerDashboardAdapter professionalAdapter;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String offerType = intent.getStringExtra("offerAcceptOfDelivery");
            if (offerType != null) {
                if (offerType.equalsIgnoreCase("orderAvailableForProfessional")) {
                    NewOrderForProfessionalWorkerApi();
                } else if (offerType.equalsIgnoreCase("offerAcceptOfProfessional")) {

                    startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(getActivity(), "offerAcceptOfProfessional"));

                    getActivity().finish();
                }
            }
        }
    };

    public NewProfessionalWorkerDashboardFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_professional_worker_dashboard, container, false);
        ButterKnife.bind(this, view);

        newList = new ArrayList<>();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);    //for hideKeyboard.......

        NewOrderForProfessionalWorkerApi();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("Tap Successful"));
    }

    @Override
    public void onClickReview(NormalUserPendingOrderInner data) {
        startActivity(UserDetailsActivity.getIntent(getActivity(), data, "FromNewPW"));
    }

    @Override
    public void onClickMakeOffer(NormalUserPendingOrderInner data) {
//        if (minimumOffer.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please enter offer amount", Toast.LENGTH_SHORT).show();
////        } else if (approxtime.equalsIgnoreCase("")) {
////            Toast.makeText(getActivity(), "Please enter Approx time", Toast.LENGTH_SHORT).show();
//        } else {
//
//            callMakeOfferApi(id, minimumOffer, message, approxtime);
//        }

        callcheckOrderAcceptOrNotApi(data);


    }

    //call Find New Order List Api
    private void NewOrderForProfessionalWorkerApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("lat", SharedPreferenceWriter.getInstance(getActivity()).getString(kLat));
                jsonObject.put("long", SharedPreferenceWriter.getInstance(getActivity()).getString(kLong));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> call = apiInterface.getNewOrderForProfessionalWorker(token, body);
                call.enqueue(new retrofit2.Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse editProfileResponce = response.body();
                            if (editProfileResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    newList = response.body().getDataList();
                                    rvNew.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    professionalAdapter = new NewProfessionalWorkerDashboardAdapter(getActivity(), newList, NewProfessionalWorkerDashboardFrag.this);
                                    rvNew.setAdapter(professionalAdapter);
                                    rvNew.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                } else {
                                    rvNew.setVisibility(View.GONE);
                                    no_data.setVisibility(View.VISIBLE);
                                }

                                //Toast.makeText(getActivity(), "" + editProfileResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "" + editProfileResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NormalUserPendingOrderResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyDialog.getInstance(getActivity()).hideDialog();
                        String s = "";
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

                Call<RequestOrderResponse> beanCall = apiInterface.checkOrderAcceptOrNot(token, body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                startActivity(OrderDetailsOnMapActivity.getIntent(getActivity(), offerdata, "Professional"));
                                getActivity().finish();
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                NewOrderForProfessionalWorkerApi();
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


    //MakeOfferApiHIt....
    private void callMakeOfferApi(String orderId, String minimumOffer, String message, String apprxTime) {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("orderId", orderId);
                jsonObject.put("minimumOffer", minimumOffer);
                jsonObject.put("message", message);
                jsonObject.put("apprxTime", apprxTime);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<RequestOrderResponse> beanCall = apiInterface.makeAOfferByProfessionalWorker(token, body);
                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                NewOrderForProfessionalWorkerApi();

                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }
}
