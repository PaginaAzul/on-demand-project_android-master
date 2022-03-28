package com.pagin.azul.fragment;


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
import com.pagin.azul.activities.RatingAndRiviewActivity;
import com.pagin.azul.adapter.NUPastDeliveryPersonAdapter;
import com.pagin.azul.adapter.NUPastProfWorkerAdapter;
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
public class NUPastDeliveryPersonFrag extends Fragment implements NUPastDeliveryPersonAdapter.OnClickContentListener {
    private NUPastDeliveryPersonAdapter pastAdapter;
    private ArrayList<NormalUserPendingOrderInner> pastList;

    private NUPastProfWorkerAdapter pastProfAdapter;


    @BindView(R.id.rv_past)
    RecyclerView rvPast;
    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.iv_blank)
    ImageView iv_blank;

    public NUPastDeliveryPersonFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nupast_delivery_person, container, false);
        ButterKnife.bind(this, view);

        pastList = new ArrayList<>();


        if (getArguments() != null) {
            String from = getArguments().getString(kFrom);
            String fromCat = getArguments().getString(kFromCategory);

            if (from.equalsIgnoreCase("delivery")) {
                //callNormalUserPastOrderApi();

            } else {
                if (from.equalsIgnoreCase("professional")) {
                    callNormalUserProfPastOrderApi();

                }

            }
        }


        return view;
    }

    @Override
    public void onReviewClick(NormalUserPendingOrderInner getData) {
        startActivity(RatingAndRiviewActivity.getIntent(getActivity(), getData, "PastNUD"));

    }

    private void callNormalUserPastOrderApi() {
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
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getNormalUserPastOrder(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    pastList = response.body().getDataList();
                                    rvPast.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    pastAdapter = new NUPastDeliveryPersonAdapter(getActivity(), pastList, NUPastDeliveryPersonFrag.this);
                                    rvPast.setAdapter(pastAdapter);
                                    no_data.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.GONE);
                                    rvPast.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    rvPast.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.VISIBLE);
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


    private void callNormalUserProfPastOrderApi() {
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
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getNormalUserPastOrder(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    pastList = response.body().getDataList();
                                    rvPast.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    pastProfAdapter = new NUPastProfWorkerAdapter(getActivity(), pastList);
                                    rvPast.setAdapter(pastProfAdapter);
                                    no_data.setVisibility(View.GONE);
                                    iv_blank.setVisibility(View.GONE);
                                    rvPast.setVisibility(View.VISIBLE);
                                } else {
                                    iv_blank.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.VISIBLE);
                                    rvPast.setVisibility(View.GONE);
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
