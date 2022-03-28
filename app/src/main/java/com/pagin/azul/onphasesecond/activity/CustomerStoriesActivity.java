package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.CustomrerStoriesAdapter;
import com.pagin.azul.onphasesecond.adapters.RestroListingAdapter;
import com.pagin.azul.onphasesecond.fragments.HomeFrag;
import com.pagin.azul.onphasesecond.model.GetCartCountModel;
import com.pagin.azul.onphasesecond.model.GetCustomerStoryDataModel;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class CustomerStoriesActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvCustomerStories)
    RecyclerView rvCustomerStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_stories);
        ButterKnife.bind(this);
        setToolbar();
        setUpRecyclerView();

        getCustomerStory();
    }

    private void setToolbar() {
        tvTitle.setText(R.string.customer_services);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
    }

    private void setUpRecyclerView() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCustomerStory() {
        try {
            MyDialog.getInstance(this).showDialog(this);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));


            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            String token = SharedPreferenceWriter.getInstance(CustomerStoriesActivity.this).getString(kToken);
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            Call<GetCustomerStoryDataModel> beanCall = apiInterface.getCustomerStory(token,body);

            beanCall.enqueue(new Callback<GetCustomerStoryDataModel>() {
                @Override
                public void onResponse(Call<GetCustomerStoryDataModel> call, Response<GetCustomerStoryDataModel> response) {
                    MyDialog.getInstance(CustomerStoriesActivity.this).hideDialog();
                    if (response.isSuccessful()) {

                        if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                            rvCustomerStories.setLayoutManager(new LinearLayoutManager(CustomerStoriesActivity.this));
                            rvCustomerStories.setAdapter(new CustomrerStoriesAdapter(CustomerStoriesActivity.this,response.body().getData()));


                        } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(CustomerStoriesActivity.this);
                        } else {
                            Toast.makeText(CustomerStoriesActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<GetCustomerStoryDataModel> call, Throwable t) {
                    Log.d(CustomerStoriesActivity.class.getSimpleName(),"onFailure");
                }
            });
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                }else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            }else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        }
        else {
            super.attachBaseContext(newBase);
        }
    }

}