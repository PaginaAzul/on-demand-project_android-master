package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.getOutputFormat;
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.getOutputFormats;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private Context context;
    private CommonListener commonListener;
    private ArrayList<RestaurantResponse> productList;
    private ArrayList<RestaurantResponse> mainList;
    String check="0";

    public ProductAdapter(Context context,CommonListener commonListener) {
        this.context = context;
        this.commonListener = commonListener;
    }

    public ProductAdapter(Context context,CommonListener commonListener, ArrayList<RestaurantResponse> productList) {
        this.context = context;
        this.commonListener = commonListener;
        this.productList = productList;
        this.mainList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        RestaurantResponse productData = productList.get(position).getProductData();


        if(productData!=null){
            String image = productData.getProductImage();
            if(image!=null && !image.equals("")){
                setImage(holder.progressBar,image,holder.ivImage);
            }else {
                holder.progressBar.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.food_thali).into(holder.ivImage);
            }
            holder.tvName.setText(productData.getProductName());
            holder.tvDes.setText(productData.getDescription());
            holder.tvQuantity.setText(context.getResources().getString(R.string.quantity)+productData.getQuantity());
            String currency = productData.getCurrency();
            holder.tvOrderCount.setText(String.valueOf(productList.get(position).getQuantity()));
            String offerPrice = productData.getOfferPrice();



//
            if (productList.get(position).getSellerData().getOfferStatus().equals("Active")) {
                if (productData.getoStatus().equals("Active")) {


                    holder.tvNewPrice.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.tvOldPrice.setVisibility(View.VISIBLE);
                    holder.tvValidTime.setVisibility(View.VISIBLE);
                    holder.tvOldPrice.setText(context.getResources().getString(R.string.was) + CommonUtilities.getPriceFormat(productData.getPrice()) + " " + currency);
                    holder.tvNewPrice.setText(context.getResources().getString(R.string.now) + CommonUtilities.getPriceFormat(offerPrice) + " " + currency);


                    try {
                        if (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                            Date c1 = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c1);

                            try {

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


                                String str1 = formattedDate;
                                Date date1 = formatter.parse(str1);

                                holder.tvValidTime.setText("This offer is valid from " + getOutputFormats(productData.getStartDate()) + " till " + getOutputFormats(productData.getEndDate()));

                                String str2 = getOutputFormats(productData.getEndDate());
                                Date date2 = formatter.parse(str2);

                                if (date2.compareTo(date1) >= 0) {

                                    holder.tvOldPrice.setText(context.getResources().getString(R.string.was) + CommonUtilities.getPriceFormat(productData.getPrice()) + " " + currency);
                                    holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);






                                } else {
                                    holder.tvOldPrice.setText( CommonUtilities.getPriceFormat(productData.getPrice()) + " " + currency);

                                    holder.tvNewPrice.setText("");

                                    holder.tvValidTime.setText("");
                                }

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }



                    } else {
                        holder.tvOldPrice.setVisibility(GONE);
                        holder.tvValidTime.setVisibility(GONE);
                        holder.tvNewPrice.setText(CommonUtilities.getPriceFormat(productData.getPrice()) + " " + currency);
                        holder.tvNewPrice.setTextColor(Color.BLACK);
                    }

                }
                else
                {


                    holder.tvOldPrice.setVisibility(GONE);
                    holder.tvValidTime.setVisibility(GONE);
                    holder.tvNewPrice.setText(CommonUtilities.getPriceFormat(productData.getPrice())+" "+currency);
                    holder.tvNewPrice.setTextColor(Color.BLACK);
                }





        }
        else {

            String image = productList.get(position).getProductImage();
            if(image!=null && !image.equals("")){
                setImage(holder.progressBar,image,holder.ivImage);
            }else {
                holder.progressBar.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.food_thali).into(holder.ivImage);
            }
            holder.tvName.setText(productList.get(position).getProductName());
            holder.tvDes.setText(productList.get(position).getDescription());
            holder.tvQuantity.setText(context.getResources().getString(R.string.quantity)+productList.get(position).getQuantity());
            //holder.tvNewPrice.setText(productList.get(position).getPrice()+" "+productList.get(position).getCurrency());
            //holder.tvValidTime.setText("This offer is valid from 12/04/2019 till 18/04/2020");
            RestaurantResponse cartData = productList.get(position).getCartData();
            if(cartData!=null){
                holder.tvOrderCount.setText(String.valueOf(cartData.getQuantity()));
            }else {
                holder.tvOrderCount.setText("0");
            }
            String currency = productList.get(position).getCurrency();
            String offerPrice = productList.get(position).getOfferPrice();
            Date c = Calendar.getInstance().getTime();


            if(offerPrice!=null){


                if(productList.get(position).getSellerData().getOfferStatus().equals("Active"))
                {


                    if(productList.get(position).getoStatus().equals("Active")) {
                        holder.tvNewPrice.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        holder.tvOldPrice.setVisibility(View.VISIBLE);
                        holder.tvValidTime.setVisibility(View.VISIBLE);

                        holder.tvNewPrice.setText(context.getResources().getString(R.string.now) + CommonUtilities.getPriceFormat(offerPrice) + " " + currency);


                        try {
                            if (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                                Date c1 = Calendar.getInstance().getTime();

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c1);
                                holder.tvValidTime.setText("This offer is valid from " + getOutputFormats(productList.get(position).getStartDate()) + " till " + getOutputFormats(productList.get(position).getEndDate()));

                                try {

                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


                                    String str1 = formattedDate;
                                    Date date1 = formatter.parse(str1);

                                    String str2 = getOutputFormats(productList.get(position).getEndDate());
                                    Date date2 = formatter.parse(str2);

                                    if (date2.compareTo(date1) >= 0) {

                                        holder.tvOldPrice.setText(context.getResources().getString(R.string.was) + CommonUtilities.getPriceFormat(productList.get(position).getPrice()) + " " + currency);
                                        holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        if (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                                            holder.tvValidTime.setText("This offer is valid from " + getOutputFormats(productList.get(position).getStartDate()) + " till " + getOutputFormats(productList.get(position).getEndDate()));
                                        else
                                            holder.tvValidTime.setText("Esta promoção é válida de " + getOutputFormats(productList.get(position).getStartDate()) + " a " + getOutputFormats(productList.get(position).getEndDate()));
                                    } else {
                                        holder.tvNewPrice.setText("");

                                        holder.tvOldPrice.setText(  CommonUtilities.getPriceFormat(productList.get(position).getPrice()) + " " + currency);

                                        holder.tvValidTime.setText("");
                                    }

                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }


                            } else {
                                Date c2 = Calendar.getInstance().getTime();

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c2);
                                holder.tvValidTime.setText("Esta promoção é válida de " + getOutputFormats(productList.get(position).getStartDate()) + " a " + getOutputFormats(productList.get(position).getEndDate()));

                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    else {

                        holder.tvNewPrice.setVisibility(View.VISIBLE);

                        holder.tvOldPrice.setVisibility(GONE);
                        holder.tvValidTime.setVisibility(GONE);
                        holder.tvNewPrice.setText(CommonUtilities.getPriceFormat(productList.get(position).getPrice())+" "+currency);
                        holder.tvNewPrice.setTextColor(Color.BLACK);
                    }
                }

                else {

                    holder.tvNewPrice.setVisibility(View.VISIBLE);

                    holder.tvOldPrice.setVisibility(GONE);
                    holder.tvValidTime.setVisibility(GONE);
                    holder.tvNewPrice.setText(CommonUtilities.getPriceFormat(productList.get(position).getPrice())+" "+currency);
                    holder.tvNewPrice.setTextColor(Color.BLACK);
                }
                // if offer price is available then show old price and valid date



            }else {

                holder.tvNewPrice.setVisibility(View.VISIBLE);

                holder.tvOldPrice.setVisibility(GONE);
                holder.tvValidTime.setVisibility(GONE);
                holder.tvNewPrice.setText(CommonUtilities.getPriceFormat(productList.get(position).getPrice())+" "+currency);
                holder.tvNewPrice.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList!=null?productList.size():0;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults results=new FilterResults();
                final ArrayList<RestaurantResponse> tempFilterData=new ArrayList<>();
                if(constraint==null||constraint.toString().trim().length()==0)
                {
                    results.values=mainList;
                }else {
                    String constrainString=constraint.toString().toLowerCase();
                    for(RestaurantResponse data:mainList)
                    {
                        if(data.getProductName().toLowerCase().contains(constrainString))
                        {
                            tempFilterData.add(data);
                            Log.w("charSeq",constrainString);
                        }
                        Log.w("charSeq",constrainString);

                    }
                    results.count=tempFilterData.size();
                    results.values=tempFilterData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                if(results.values!=null)
                {
                    productList=(ArrayList<RestaurantResponse>)results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }


    void checktime()

    {

        String string3= SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime);
        String string8= SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.ctime);

        if(!(string3.contains("AM")||string3.contains("PM")))
        {
            try {


                Date mToday = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String curTime = sdf.format(mToday);


                Date time1 = new SimpleDateFormat("HH:mm").parse(string3);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                Date time2 = new SimpleDateFormat("HH:mm").parse(string8);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);

                String someRandomTime = curTime;
                Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();
                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                    check="1";
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    System.out.println(true);
                }
                else
                {

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else

        {
            try {



                String string1 = SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime);

                Date time1 = new SimpleDateFormat("HH:mm aaa").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                String string2 = SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.ctime);
                Date time2 = new SimpleDateFormat("HH:mm aaa").parse(string2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm aaa");
                Calendar calander = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aaa");


                String someRandomTime = simpleDateFormat.format(calander.getTime()).toString();
                Date d = new SimpleDateFormat("HH:mm aaa").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();


                try {
                    Date mToday = new Date();

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String curTime = sdf.format(mToday);
                    Date start = sdf.parse(string1);
                    Date end = sdf.parse(string2);
                    Date userDate = sdf.parse(curTime);

                    if(end.before(start))
                    {
                        Calendar mCal = Calendar.getInstance();
                        mCal.setTime(end);
                        mCal.add(Calendar.DAY_OF_YEAR, 1);
                        end.setTime(mCal.getTimeInMillis());
                    }

                    Log.d("curTime", userDate.toString());
                    Log.d("start", start.toString());
                    Log.d("end", end.toString());


                    if (userDate.after(start) && userDate.before(end)) {
                      check="1";
                    }
                    else{
                        check="0";
                    }
                } catch (ParseException e) {
                    // Invalid date was entered
                }






//                if(string1.contains("AM")&&string2.contains("AM"))
//                {
//
//                    String[] timeArray = string1.split(":");
//                    String[] timeArray1 = string2.split(":");
//                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
//
//
//                    int HH = Integer.parseInt(timeArray[0]);
//                    int HH1 = Integer.parseInt(timeArray1[0]);
//                    int HH2 = Integer.parseInt(timeArray2[0]);
//                    if(simpleDateFormat.format(calander.getTime()).toString().contains("PM"))
//                    {
//                        check="0";
//                    }
//                    else if(HH<=HH2&&HH1>=HH2)                    {
//                        check="1";
//                    }
//                    else
//                    {
//                        check="0";
//
//                    }
//                }
//                else if(string1.contains("PM")&&string2.contains("PM"))
//                {
//                    String[] timeArray = string1.split(":");
//                    String[] timeArray1 = string2.split(":");
//                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
//
//
//                    int HH = Integer.parseInt(timeArray[0]);
//                    int HH1 = Integer.parseInt(timeArray1[0]);
//                    int HH2 = Integer.parseInt(timeArray2[0]);
//                    Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();
//
//
//                    if(simpleDateFormat.format(calander.getTime()).toString().contains("AM"))
//                    {
//                        check="0";
//                    }
//
//
//                   else  if(HH<=HH2&&HH1>=HH2)                    {
//                        check="1";
//                    }
//                    else
//                    {
//                        check="0";
//
//                    }
//                }
//                else
//                {
//
//                    Toast.makeText(context, "yessss", Toast.LENGTH_SHORT).show();
//
//
//                    try {
//                        Date mToday = new Date();
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
//                        String curTime = sdf.format(mToday);
//                        Date start = sdf.parse(string1);
//                        Date end = sdf.parse(string2);
//                        Date userDate = sdf.parse(curTime);
//
//                        if(end.before(start))
//                        {
//                            Calendar mCal = Calendar.getInstance();
//                            mCal.setTime(end);
//                            mCal.add(Calendar.DAY_OF_YEAR, 1);
//                            end.setTime(mCal.getTimeInMillis());
//                        }
//
//                        Log.d("curTime", userDate.toString());
//                        Log.d("start", start.toString());
//                        Log.d("end", end.toString());
//
//
//                        if (userDate.after(start) && userDate.before(end)) {
//                            Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//                            Log.d("result", "falls between start and end , go to screen 1 ");
//                        }
//                        else{
//                            Toast.makeText(context, "nooo", Toast.LENGTH_SHORT).show();
//
//                            Log.d("result", "does not fall between start and end , go to screen 2 ");
//                        }
//                    } catch (ParseException e) {
//                        // Invalid date was entered
//                    }
////                    Toast.makeText(context, "third", Toast.LENGTH_SHORT).show();
////
////                    String[] timeArray = string1.split(":");
////                    String[] timeArray1 = string2.split(":");
////                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
////
////
////                    int HH = Integer.parseInt(timeArray[0]);
////                    int HH1 = Integer.parseInt(timeArray1[0]);
////                    int HH2 = Integer.parseInt(timeArray2[0]);
////
////
////                    if(simpleDateFormat.format(calander.getTime()).toString().contains("AM")&&HH<=HH2)
////                    {
////                        check="1";
////                        Toast.makeText(context, "f", Toast.LENGTH_SHORT).show();
////                    }
////                    else if(simpleDateFormat.format(calander.getTime()).toString().contains("PM")&&HH1>=HH2)
////                    {
////                        check="1";
////
////                    }
////
////                    else   if(HH<=HH2&&HH1>=HH2)                    {
////                        check="1";
////                    }
////
////                    else
////                    {
////
////                        check="0";
////
////                    }
//                }
            } catch (ParseException e) {


                e.printStackTrace();
            }
        }

    }

    private String getReminingTimes() {
        String delegate = "hh:mm";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }
    private String getReminingTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;
        @BindView(R.id.tvNewPrice)
        TextView tvNewPrice;
        @BindView(R.id.tvValidTime)
        TextView tvValidTime;
        @BindView(R.id.tvOrderCount)
        TextView tvOrderCount;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.progressbar)
        ProgressBar progressBar;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.clProductItem,R.id.tvAddItem,R.id.tvRemoveItem})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clProductItem:
                    commonListener.onProductClick(getAdapterPosition(),productList);
                    break;
                case R.id.tvAddItem:


                    if(  SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime).isEmpty()|| SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime)==null)
                    {

                        commonListener.onAddItem(getAdapterPosition(),productList);

                    }
                    else
                    {
                        checktime();

                        if(!check.equals("1"))
                        {
                            Toast.makeText(context,"Store has been closed",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            commonListener.onAddItem(getAdapterPosition(),productList);

                        }
                    }
                    break;
                case R.id.tvRemoveItem:
                    commonListener.onRemoveItem(getAdapterPosition(),productList);
                    break;
            }
        }
    }

    private void setImage(ProgressBar progressBar, final String imageUri, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context.getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }

    public void update(ArrayList<RestaurantResponse> productList){
        this.productList = productList;
        this.mainList = productList;
        notifyDataSetChanged();
    }

    private String getOutputFormat(String responseDate){
        SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        SimpleDateFormat outputFormate = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormate.parse(responseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate =  outputFormate.format(date);
        return formattedDate;
        //}
    }
}
