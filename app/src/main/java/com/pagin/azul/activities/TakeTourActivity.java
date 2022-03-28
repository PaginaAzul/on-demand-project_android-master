package com.pagin.azul.activities;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.ContentModel;
import com.pagin.azul.bean.StaticContentResponse;
import com.pagin.azul.bean.StaticMainResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.NetworkChangeReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeTourActivity extends AppCompatActivity {
    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private LinearLayout dotLayout;
    private TextView btnSkip;
    private ImageView left_nav;
    private ImageView right_nav;
    private int[] layouts;
    private TextView tvTitle;
    private TextView tvDescription;
    private List<StaticContentResponse> staticMainResponse;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private ArrayList<ContentModel> containts;
    private boolean isFirstTime = false;
    private NetworkChangeReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_tour);
        ButterKnife.bind(this);
        staticMainResponse = new ArrayList<>();
        containts = new ArrayList<>();

        mNetworkReceiver = new NetworkChangeReceiver();
        mNetworkReceiver.registerCallback(new NetworkChangeReceiver.InternetResponse() {
            @Override
            public void onInternetCheck(boolean isConnect) {
                if (isConnect)
                    disableofflineAnimation();
                else
                    runofflineAnimation();

            }
        });

        getContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerNetworkBroadCast();

        if (isNetworkAvailable()) {

        } else {
            Toast.makeText(TakeTourActivity.this, "Internet Connection Lost!", Toast.LENGTH_LONG).show();

        }

    }

    //////run offline animatin based on network connctivity/////////////
    public void runofflineAnimation() {

        lottieAnimationView.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        lottieAnimationView.setAnimation("no_internet_connection.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
    }

    //////disable offline animatin based on network connctivity/////////////
    public void disableofflineAnimation() {
        if (lottieAnimationView != null) {
            lottieAnimationView.pauseAnimation();
        }
        viewPager.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.GONE);
        getContent();

    }

    private void getContent() {
        try {
            MyDialog.getInstance(TakeTourActivity.this).hideDialog();
            MyDialog.getInstance(TakeTourActivity.this).showDialog(TakeTourActivity.this);
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            Call<StaticMainResponse> call = apiInterface.staticContent();
            call.enqueue(new Callback<StaticMainResponse>() {
                @Override
                public void onResponse(Call<StaticMainResponse> call, Response<StaticMainResponse> response) {
                    MyDialog.getInstance(TakeTourActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                            staticMainResponse = response.body().getResponse();
                            for (int i = 0; i < staticMainResponse.size(); i++) {
                                if (staticMainResponse.get(i).getType().equalsIgnoreCase("WalkingPage1")) {
                                    containts.add(new ContentModel(staticMainResponse.get(i).getTitle(), staticMainResponse.get(i).getPortDescription()));
                                    //containts.add(new ContentModel("Bem-vindo à PAGINAZUL", staticMainResponse.get(i).getPortDescription()));

                                }  if (staticMainResponse.get(i).getType().equalsIgnoreCase("WalkingPage2")) {
                                    containts.add(new ContentModel(staticMainResponse.get(i).getTitle(), staticMainResponse.get(i).getPortDescription()));
                                    //containts.add(new ContentModel("Melhor Preços", staticMainResponse.get(i).getPortDescription()));

                                }  if (staticMainResponse.get(i).getType().equalsIgnoreCase("WalkingPage3")) {
                                    containts.add(new ContentModel(staticMainResponse.get(i).getTitle(), staticMainResponse.get(i).getPortDescription()));
                                    //containts.add(new ContentModel("COMUNICAÇÃO DIRETA", staticMainResponse.get(i).getPortDescription()));
                                } if (staticMainResponse.get(i).getType().equalsIgnoreCase("WalkingPage4")) {
                                    containts.add(new ContentModel(staticMainResponse.get(i).getTitle(), staticMainResponse.get(i).getPortDescription()));
                                    //containts.add(new ContentModel("Seguir Seu Pedido", staticMainResponse.get(i).getPortDescription()));

                                }
                            }

                            final ViewPager viewPager = findViewById(R.id.viewPager);
                            dotLayout = findViewById(R.id.viewDots);
                            btnSkip = findViewById(R.id.textSkip);

                            left_nav = findViewById(R.id.left_nav);
                            right_nav = findViewById(R.id.right_nav);

                            right_nav.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true));

                            left_nav.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true));

                            btnSkip.setOnClickListener(view -> onSkip());
                            // layouts of all welcome sliders
                            // add few more layouts if you want
                            layouts = new int[]{
                                    R.layout.slide_a_layout,
                                    R.layout.slide_b_layout,
                                    R.layout.slide_c_layout,
                                    R.layout.slide_d_layout};

                            //adding bottom dots
                            addBottomDots(0);
                            SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter();
                            viewPager.setAdapter(sectionPagerAdapter);
                            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

                        } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(TakeTourActivity.this);

                        } else {
                            Toast.makeText(TakeTourActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TakeTourActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<StaticMainResponse> call, Throwable t) {
                    MyDialog.getInstance(TakeTourActivity.this).hideDialog();
                    t.printStackTrace();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void onSkip() {
        //  startActivity(AddTicketActivity.getIntent(this, "3"));
        startActivity(TermsConditionActivity.getIntent(this, staticMainResponse));
        finish();
//        startActivity(MoreActivity.getIntent(this));
    }

    private void addBottomDots(int currentPage) {
        if (currentPage == 0) {
            left_nav.setVisibility(View.INVISIBLE);
        } else {
            left_nav.setVisibility(View.VISIBLE);

        }
        if (currentPage == 3) {
            right_nav.setOnClickListener(v -> onSkip());

        }
        TextView[] dots = new TextView[layouts.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void registerNetworkBroadCast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) TakeTourActivity.this

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Log.e("Network Testing", "***Available***");

            return true;

        }

        Log.e("Network Testing", "***Not Available***");

        return false;

    }

    class SectionPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);
            tvTitle = view.findViewById(R.id.tv_title);
            tvDescription = view.findViewById(R.id.tv_description);


            tvTitle.setText(containts.get(position).getTitle());
            tvDescription.setText(containts.get(position).getDescription());
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
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
