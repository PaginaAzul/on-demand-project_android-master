package com.pagin.azul.onphasesecond.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.OnGoingOrderAdapter;
import com.pagin.azul.onphasesecond.adapters.OnUpcomingAdapter;
import com.pagin.azul.onphasesecond.model.MyOrderModel;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OngoingOrdersFragment#} factory method to
 * create an instance of this fragment.
 */
public class OngoingOrdersFragment extends Fragment implements CommonListener {

    public OngoingOrdersFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.rvOngoingOrder)
    RecyclerView rvOngoingOrder;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private Context mContext;
    private String token;
    private String userId;
    private String langCode;
    private OnGoingOrderAdapter onGoingOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ongoing_orders, container, false);
        ButterKnife.bind(this,view);
        mContext = getContext();
        token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(mContext).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE);
        serviceGetUserOrder();
        return view;
    }

    public void serviceGetUserOrder() {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("type", ParamEnum.ON_GOING.theValue());
            jsonObject.put("langCode", langCode);


            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<MyOrderModel> beanCall = apiInterface.getUserOrder(token,body);
            beanCall.enqueue(new Callback<MyOrderModel>() {
                @Override
                public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        MyOrderModel model = response.body();
                        if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                            setUpRecylerView(model.getData());

                        } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(getActivity());
                        } else {
                            Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<MyOrderModel> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpRecylerView(ArrayList<MyOrderResponse> data) {
        if(data!=null && !data.isEmpty()){
            tvNoData.setVisibility(View.GONE);
            rvOngoingOrder.setLayoutManager(new LinearLayoutManager(mContext));
            onGoingOrderAdapter = new OnGoingOrderAdapter(mContext,data,this);
            rvOngoingOrder.setAdapter(onGoingOrderAdapter);
        }else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onTimerFinish(int position, ArrayList<MyOrderResponse> data) {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", data.get(position).get_id());
            jsonObject.put("userId", userId);
            jsonObject.put("cancelStatus", true);
            jsonObject.put("langCode", langCode);


            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<MyOrderModel> beanCall = apiInterface.updateCancelStatus(token,body);
            beanCall.enqueue(new Callback<MyOrderModel>() {
                @Override
                public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        MyOrderModel model = response.body();
                        if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                            data.get(position).setCancelStatus(true);
                            onGoingOrderAdapter.update(data);

                        } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(getActivity());
                        } else {
                            Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<MyOrderModel> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelClick(int position, ArrayList<MyOrderResponse> data) {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("orderId", data.get(position).get_id());
            jsonObject.put("langCode", langCode);


            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<MyOrderModel> beanCall = apiInterface.cancelOrderByUser(token,body);
            beanCall.enqueue(new Callback<MyOrderModel>() {
                @Override
                public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        MyOrderModel model = response.body();
                        if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                            data.remove(data.get(position));
                            onGoingOrderAdapter.update(data);

                            Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();

                        } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(getActivity());
                        } else {
                            Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<MyOrderModel> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}