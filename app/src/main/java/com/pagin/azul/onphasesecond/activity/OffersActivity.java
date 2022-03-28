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
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.OfferAdapter;
import com.pagin.azul.onphasesecond.adapters.OfferRightAdapter;
import com.pagin.azul.onphasesecond.model.ProductModel;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvOfferLeft)
    RecyclerView rvOfferLeft;

    @BindView(R.id.rvOfferRight)
    RecyclerView rvOfferRight;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    /*private int[] lastVisiblePositions=null;
    private int lastVisibleItem;
    private StaggeredGridLayoutManager layoutManager;
    private int duration;
    private int durationRight;
    private int pixelsToMove;
    private int pixelsToMoveRight;
    private Handler mHandler;
    private Handler mHandlerRight;
    private Runnable SCROLLING_RUNNABLE;
    private Runnable SCROLLING_RUNNABLERight;
    private LinearLayoutManager layoutManagerLeft;
    private LinearLayoutManager layoutManagerRight;*/
    private Handler mHandler;
    private Handler mHandlerRight;
    private Runnable SCROLLING_RUNNABLE;
    private Runnable SCROLLING_RUNNABLERight;
    private OfferAdapter offerAdapter;
    private OfferRightAdapter offerRightAdapter;
    private ArrayList<ProductResponse> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.offers));
        addOnTextChangeListener();
        serviceGetOfferCategory();
    }

    private void addOnTextChangeListener() {
        final ArrayList<ProductResponse> filterList = new ArrayList<>();
        edtSearchFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                /*offerAdapter.getFilter().filter(editable);
                offerRightAdapter.getFilter().filter(editable);*/
                if(edtSearchFrag.getText().toString().trim().length()>0){
                    filterList.clear();
                    filterList.addAll(offerList);
                    List<ProductResponse> list = filter(edtSearchFrag.getText().toString(), filterList, true);
                    filterList.clear();
                    filterList.addAll(list);

                    ArrayList<ProductResponse> leftOfferList = new ArrayList<>();
                    ArrayList<ProductResponse> rightOfferList = new ArrayList<>();
                    for(int i=0;i<filterList.size();i++){
                        if(i%2==0){
                            leftOfferList.add(filterList.get(i));
                        }else {
                            rightOfferList.add(filterList.get(i));
                        }
                    }

                    offerAdapter.updateLeftAdapter(leftOfferList,leftOfferList.size());
                    offerRightAdapter.updateRightAdapter(rightOfferList,rightOfferList.size());

                    // stop auto scrolling
                    mHandler.removeCallbacksAndMessages(null);
                    mHandlerRight.removeCallbacksAndMessages(null);

                    /*if(main_data.size()>0){
                        tvNoData.setVisibility(View.GONE);
                    }else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }*/
                }else{
                    filterList.clear();
                    filterList.addAll(offerList);

                    ArrayList<ProductResponse> leftOfferList = new ArrayList<>();
                    ArrayList<ProductResponse> rightOfferList = new ArrayList<>();
                    for(int i=0;i<filterList.size();i++){
                        if(i%2==0){
                            leftOfferList.add(filterList.get(i));
                        }else {
                            rightOfferList.add(filterList.get(i));
                        }
                    }

                    offerAdapter.updateLeftAdapter(leftOfferList,Integer.MAX_VALUE);
                    offerRightAdapter.updateRightAdapter(rightOfferList,Integer.MAX_VALUE);

                    // start auto scrolling
                    mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
                    mHandlerRight.postDelayed(SCROLLING_RUNNABLERight, 1000);

                }
                edtSearchFrag.requestFocus();
            }
        });
    }

    private List<ProductResponse> filter(String string, Iterable<ProductResponse> iterable, boolean b) {
        if (iterable == null)
            return new LinkedList<ProductResponse>();
        else {
            List<ProductResponse> collected = new LinkedList<ProductResponse>();
            Iterator<ProductResponse> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                ProductResponse item = iterator.next();
                try{
                    if (item.getName().toLowerCase().contains(string.toLowerCase())){
                        collected.add(item);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return collected;
        }
    }

    /*private void setUpRecyclerView(ArrayList<ProductResponse> data) {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvOffer.setLayoutManager(layoutManager);
        offerAdapter = new OfferAdapter(this,data);
        rvOffer.setAdapter(offerAdapter);
        *//*rvOffer.startAutoScroll();
        rvOffer.setLoopEnabled(true);
        rvOffer.setCanTouch(true);
        rvOffer.pauseAutoScroll(true);*//*

        duration = 10;
        pixelsToMove = 31;
        mHandler = new Handler(Looper.getMainLooper());
        SCROLLING_RUNNABLE = new Runnable() {
            @Override
            public void run() {
                rvOffer.smoothScrollBy(0, pixelsToMove);
                mHandler.postDelayed(this, duration);
            }
        };
        rvOffer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(lastVisiblePositions==null)
                    lastVisiblePositions = new int[layoutManager.getSpanCount()]; //for 2 columns
                lastVisiblePositions = layoutManager.findLastCompletelyVisibleItemPositions(lastVisiblePositions);
                lastVisibleItem = Math.max(lastVisiblePositions[0], lastVisiblePositions[1]);//findMax(lastPositions);

                if(lastVisibleItem == layoutManager.getItemCount()-1){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(() -> {
                        rvOffer.setLayoutManager(null);
                        rvOffer.setAdapter(null);
                        rvOffer.setLayoutManager(layoutManager);
                        rvOffer.setAdapter(offerAdapter);
                        mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
                    }, 1000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);

        rvOffer.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                mHandler.removeCallbacksAndMessages(null);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
            }
            return false;
        });
    }*/

    private void serviceGetOfferCategory(){
        try {
            MyDialog.getInstance(this).showDialog(this);
            //if (!token.isEmpty()) {
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

            Call<ProductModel> beanCall = apiInterface.getOfferCategory();

            beanCall.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    MyDialog.getInstance(OffersActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        ProductModel model = response.body();
                        if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                            //setUpRecyclerView(model.getData());
                            offerList = model.getData();
                            setDiffAdapters(offerList);

                        } else {
                            Toast.makeText(OffersActivity.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
                }
            });
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDiffAdapters(ArrayList<ProductResponse> offerList) {
        ArrayList<ProductResponse> leftOfferList = new ArrayList<>();
        ArrayList<ProductResponse> rightOfferList = new ArrayList<>();
        for(int i=0;i<offerList.size();i++){
            if(i%2==0){
                leftOfferList.add(offerList.get(i));
            }else {
                rightOfferList.add(offerList.get(i));
            }
        }
        setUpLeftRecyclerView(leftOfferList);
        setUpRightRecyclerView(rightOfferList);
    }

    private void setUpLeftRecyclerView(ArrayList<ProductResponse> data) {
        LinearLayoutManager layoutManagerLeft = new LinearLayoutManager(this);
        rvOfferLeft.setLayoutManager(layoutManagerLeft);
        offerAdapter = new OfferAdapter(this,data,Integer.MAX_VALUE);
        rvOfferLeft.setAdapter(offerAdapter);
        /*rvOfferLeft.startAutoScroll();
        rvOfferLeft.setLoopEnabled(true);
        rvOfferLeft.setCanTouch(true);*/
        //rvOfferLeft.pauseAutoScroll(true);

        //final int duration = 10;
        final int duration = 6;
        final int pixelsToMove = 31;
        mHandler = new Handler(Looper.getMainLooper());
        SCROLLING_RUNNABLE = new Runnable() {
            @Override
            public void run() {
                rvOfferLeft.smoothScrollBy(0, pixelsToMove);
                mHandler.postDelayed(this, duration);
            }
        };
        /*rvOfferLeft.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = layoutManagerLeft.findLastCompletelyVisibleItemPosition();
                if(lastItem == layoutManagerLeft.getItemCount()-1){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvOfferLeft.setAdapter(null);
                            rvOfferLeft.setAdapter(offerAdapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
                        }
                    }, 1000);
                }
            }
        });*/
        mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);

        rvOfferLeft.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                mHandler.removeCallbacksAndMessages(null);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
            }
            return false;
        });

        // click on recycler view item while auto scrolling
        /*rvOfferLeft.addOnItemTouchListener(new RecyclerTouchListener(this, rvOfferLeft, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try{
                    position = position % data.size();
                }catch (ArithmeticException e){
                    e.printStackTrace();
                }
                int finalPosition = position;
                Intent intent = new Intent(OffersActivity.this, SubOfferActivity.class);
                intent.putExtra(ParamEnum.TITLE.theValue(),data.get(finalPosition).getName());
                intent.putExtra(ParamEnum.ID.theValue(),data.get(finalPosition).get_id());
                startActivity(intent);
            }
        }));*/
        /*rvOfferLeft.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                rvOfferLeft.pauseAutoScroll(true);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                rvOfferLeft.startAutoScroll();
            }
            return false;
        });*/
    }

    private void setUpRightRecyclerView(ArrayList<ProductResponse> data) {
        LinearLayoutManager layoutManagerRight = new LinearLayoutManager(this);
        rvOfferRight.setLayoutManager(layoutManagerRight);
        offerRightAdapter = new OfferRightAdapter(this,data,Integer.MAX_VALUE);
        rvOfferRight.setAdapter(offerRightAdapter);
        /*rvOfferRight.startAutoScroll();
        rvOfferRight.setLoopEnabled(true);
        rvOfferRight.setCanTouch(true);*/
        //rvOfferRight.pauseAutoScroll(true);


        //final int durationRight = 8;
        final int durationRight = 4;
        final int pixelsToMoveRight = 31;
        mHandlerRight = new Handler(Looper.getMainLooper());
        SCROLLING_RUNNABLERight = new Runnable() {
            @Override
            public void run() {
                rvOfferRight.smoothScrollBy(0, pixelsToMoveRight);
                mHandlerRight.postDelayed(this, durationRight);
            }
        };
        /*rvOfferRight.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManagerRight.findLastCompletelyVisibleItemPosition(); // get last visible item position completely
                if(lastVisibleItem == layoutManagerRight.getItemCount()-1){
                    mHandlerRight.removeCallbacks(SCROLLING_RUNNABLERight);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(() -> {
                        rvOfferRight.setAdapter(null);
                        rvOfferRight.setAdapter(offerRightAdapter);
                        mHandlerRight.postDelayed(SCROLLING_RUNNABLERight, 1000);
                    }, 1000);
                }
            }
        });*/
        mHandlerRight.postDelayed(SCROLLING_RUNNABLERight, 1000);

        rvOfferRight.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                mHandlerRight.removeCallbacksAndMessages(null);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mHandlerRight.postDelayed(SCROLLING_RUNNABLERight, 1000);
            }
            return false;
        });
        /*rvOfferRight.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                rvOfferRight.pauseAutoScroll(true);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                rvOfferRight.startAutoScroll();
            }
            return false;
        });*/
    }

    /*interface ClickListener {
        void onClick(View view, int position);
        //fun onLongClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private Context context;
        private RecyclerView recycleView;
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, RecyclerView recycleView, ClickListener clicklistener) {
            this.context = context;
            this.recycleView = recycleView;
            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }*/

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