package com.pagin.azul.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.HomeServicesCategoryAct;
import com.pagin.azul.activities.MapProfessinalWorkerActivity;
import com.pagin.azul.adapter.HomeCategoryAdapter;
import com.pagin.azul.bean.CommonModelBean;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kAppLaunchMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCategoryFrag extends Fragment implements CommonListener {

    @BindView(R.id.rvCategoryHome)
    RecyclerView rvCategoryHome;

    private HomeCategoryAdapter homeCategoryAdapter;

    public HomeCategoryFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);
        ButterKnife.bind(this,view);
        setUpRecyclerView();
        orderCategoryList();
        return view;
    }

    private void setUpRecyclerView() {
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(kAppLaunchMode, "true");
        rvCategoryHome.setLayoutManager(new GridLayoutManager(getContext(),2));
        homeCategoryAdapter = new HomeCategoryAdapter(getContext(),this,new ArrayList<>(),HomeCategoryFrag.class.getSimpleName());
        rvCategoryHome.setAdapter(homeCategoryAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            ((HomeMainActivity)getActivity()).tv_title.setVisibility(View.VISIBLE);
            ((HomeMainActivity)getActivity()).tv_title.setText(R.string.home);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position, ArrayList<CommonResponseBean> commonResponses) {
        ArrayList<CommonResponseBean> subCategoryList = commonResponses.get(position).getSubCategoryData();
        if(subCategoryList!=null && subCategoryList.size()>0){
            Intent intent = new Intent(getContext(), HomeServicesCategoryAct.class);
            intent.putParcelableArrayListExtra(Constants.kData,subCategoryList);
            intent.putExtra(Constants.kCategoryId,commonResponses.get(position).get_id());
            intent.putExtra(Constants.kPortugueseCategoryName,commonResponses.get(position).getPortugueseCategoryName());
            startActivity(intent);
        }else {
            startActivity(MapProfessinalWorkerActivity.getSubCategoryIntent(getContext(), "btn_prfsnal_worker"
                    ,"",commonResponses.get(position).getCategoryName(),commonResponses.get(position).get_id(),
                    "",commonResponses.get(position).getPortugueseCategoryName(),""));
        }
    }

    //api calling
    private void orderCategoryList() {
        MyDialog.getInstance(getContext()).hideDialog();
        MyDialog.getInstance(getContext()).showDialog(getActivity());
        ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
        Call<CommonModelBean> call = apiInterface.getOrderCategoryList();
        call.enqueue(new Callback<CommonModelBean>() {
            @Override
            public void onResponse(Call<CommonModelBean> call, Response<CommonModelBean> response) {
                MyDialog.getInstance(getContext()).hideDialog();
                if (response.isSuccessful()) {
                    CommonModelBean body = response.body();
                    if (body.getStatus().equalsIgnoreCase("SUCCESS")) {
                        if (response.body() != null) {
                            ArrayList<CommonResponseBean> commonResponses = body.getData();
                            homeCategoryAdapter.update(commonResponses,HomeCategoryFrag.class.getSimpleName());
                            // getSubCategory(categoryResponse.getData().get(0).getId());
                        }
                    }else if (body.getStatus().equalsIgnoreCase("FAILURE") && body.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                        CommonUtility.showDialog1(getActivity());
                    } else {
                        Toast.makeText(getContext(), body.getResponse_message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonModelBean> call, Throwable t) {
                MyDialog.getInstance(getContext()).hideDialog();
                t.printStackTrace();
            }
        });
    }
}
