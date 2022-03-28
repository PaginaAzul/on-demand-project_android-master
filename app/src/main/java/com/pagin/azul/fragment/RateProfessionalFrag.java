package com.pagin.azul.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.adapter.RateNormalUserAdapter;
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

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;


public class RateProfessionalFrag extends Fragment {
    private RateNormalUserAdapter rateNormalUserAdapter;
    private ArrayList<NormalUserPendingOrderInner> normalUserList;
    @BindView(R.id.rl_relative)
    RelativeLayout rl_relative;
    @BindView(R.id.rv_normal_user)
    RecyclerView rvNormalUser;
    @BindView(R.id.nodata)
    TextView nodata;

    @BindView(R.id.avg_rate)
    TextView avg_rate;
    @BindView(R.id.total_rating)
    TextView total_rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_normal_user, container, false);
        ButterKnife.bind(this, view);
        normalUserList = new ArrayList<>();


        GetRateApi();
        return view;
    }

    private void GetRateApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("ratingToType", "ProfessionalWorker");
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getRate(token,body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getPendingOrderInner().getOrderdocData() != null && response.body().getPendingOrderInner().getOrderdocData().size() > 0) {
                                    normalUserList = response.body().getPendingOrderInner().getOrderdocData();
                                    rvNormalUser.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    rateNormalUserAdapter = new RateNormalUserAdapter(getActivity(), normalUserList);
                                    rvNormalUser.setAdapter(rateNormalUserAdapter);
                                    nodata.setVisibility(View.GONE);
                                    rvNormalUser.setVisibility(View.VISIBLE);
                                } else {
                                    nodata.setVisibility(View.VISIBLE);
                                    rvNormalUser.setVisibility(View.GONE);
                                    rl_relative.setVisibility(View.GONE);
                                }

                                avg_rate.setText(response.body().getPendingOrderInner().getAvgRating());
                                total_rating.setText(response.body().getPendingOrderInner().getTotalRating()+" Rating");

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
