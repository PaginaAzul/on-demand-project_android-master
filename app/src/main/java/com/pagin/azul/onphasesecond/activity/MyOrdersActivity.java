package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.fragments.OngoingOrdersFragment;
import com.pagin.azul.onphasesecond.fragments.PastOrdersFragment;
import com.pagin.azul.onphasesecond.fragments.UpcomingOrdersFragment;
import com.pagin.azul.onphasesecond.model.GetCartCountModel;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class MyOrdersActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvPast)
    TextView tvPast;

    @BindView(R.id.tvOnGoing)
    TextView tvOnGoing;

    @BindView(R.id.tvUpcoming)
    TextView tvUpcoming;

    @BindView(R.id.cartLayout)
    ConstraintLayout cartLayout;

    @BindView(R.id.countTv)
    TextView countTv;

    private UpcomingOrdersFragment upcomingOrdersFragment;
    private OngoingOrdersFragment ongoingOrdersFragment;
    private PastOrdersFragment pastOrdersFragment;
    private String Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);
        setToolbar();
        initFrag();
    }

    @OnClick({R.id.tvPast,R.id.tvOnGoing,R.id.tvUpcoming,R.id.cartLayout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPast:
                loadFragment("Past");
                break;
            case R.id.tvOnGoing:
                loadFragment("Ongoing");
                break;
            case R.id.tvUpcoming:
                loadFragment("Upcoming");
                break;
            case R.id.cartLayout:
                dispatchAddToCart();
                break;
        }
    }

    private void dispatchAddToCart() {
        if(Type.equalsIgnoreCase(ParamEnum.PRODUCT.theValue())){
            Intent intent = new Intent(this, ScheduleMyCart.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        }
    }

    private void setToolbar() {
        cartLayout.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.my_orders);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
    }

    private void initFrag() {
        upcomingOrdersFragment = new UpcomingOrdersFragment();
        ongoingOrdersFragment = new OngoingOrdersFragment();
        pastOrdersFragment = new PastOrdersFragment();
        loadFragment("Upcoming");
    }

    private void loadFragment(String tab) {
        if (tab.equalsIgnoreCase("Past")) {
            tvPast.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvPast.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_layout_past));
            tvOnGoing.setBackground(null);
            tvOnGoing.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tvUpcoming.setBackground(null);
            tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            getSupportFragmentManager().beginTransaction().replace(R.id.frameMyOrders, pastOrdersFragment).commit();
        } else if (tab.equalsIgnoreCase("Ongoing")) {
            tvOnGoing.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvOnGoing.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_layout_ongoing));
            tvPast.setBackground(null);
            tvPast.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tvUpcoming.setBackground(null);
            tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            getSupportFragmentManager().beginTransaction().replace(R.id.frameMyOrders, ongoingOrdersFragment).commit();
        } else if (tab.equalsIgnoreCase("Upcoming")) {
            tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvUpcoming.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_layout_upcoming));
            tvOnGoing.setBackground(null);
            tvOnGoing.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tvPast.setBackground(null);
            tvPast.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            getSupportFragmentManager().beginTransaction().replace(R.id.frameMyOrders, upcomingOrdersFragment).commit();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            return true;
        }/*else if (item.getItemId() == R.id.ivCart) {
            Intent intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCartCount();
    }

    private void getCartCount() {
        try {
            //MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(MyOrdersActivity.this).getString(kToken);
            if(!token.isEmpty()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<GetCartCountModel> beanCall = apiInterface.getCartCount(token,body);

                beanCall.enqueue(new Callback<GetCartCountModel>() {
                    @Override
                    public void onResponse(Call<GetCartCountModel> call, Response<GetCartCountModel> response) {
                        //MyDialog.getInstance(SubCategoryActivity.this).hideDialog();
                        if (response.isSuccessful()) {

                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                                if(response.body().getData()<=0){
                                    countTv.setVisibility(View.GONE);
                                }else{
                                    countTv.setVisibility(View.VISIBLE);
                                    countTv.setText(response.body().getData()+"");
                                }

                                Type = response.body().getType();


                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MyOrdersActivity.this);
                            } else {
                                Toast.makeText(MyOrdersActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<GetCartCountModel> call, Throwable t) {
                    }
                });
            }

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