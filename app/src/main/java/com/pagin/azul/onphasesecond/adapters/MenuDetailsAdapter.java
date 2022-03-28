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
import com.pagin.azul.onphasesecond.activity.RestaurantDetails;
import com.pagin.azul.onphasesecond.fragments.DynamicFragment;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

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

public class MenuDetailsAdapter extends RecyclerView.Adapter<MenuDetailsAdapter.MenuDetailsViewHolder> implements Filterable {

    private Context context;
    private ArrayList<RestaurantResponse> menuDataList,temp;
    private CommonListener commonListener;
    private String type;
    private boolean addListener;
    String check="0";

    public MenuDetailsAdapter(Context context) {
        this.context = context;
    }

    public MenuDetailsAdapter(Context mContext, ArrayList<RestaurantResponse> menuDataList,CommonListener commonListener,String type,boolean addListener) {
        this.context = mContext;
        this.menuDataList = menuDataList;
        this.temp = menuDataList;
        this.commonListener = commonListener;
        this.type = type;
        this.addListener=addListener;

    }

    @NonNull
    @Override
    public MenuDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_cart_vertically, parent, false);
        return new MenuDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDetailsViewHolder holder, int position)


    {        RestaurantResponse productData1 = menuDataList.get(position).getProductData();


        /*if(addListener)
            ((RestaurantDetails)context).setOnCommonListener(commonListener);*/




        if(type.equalsIgnoreCase(DynamicFragment.class.getSimpleName())){
            String image = menuDataList.get(position).getProductImage();

            if(image!=null && !image.equals("")){
                setImage(holder.progressbar,image,holder.ivImage);
            }else {
                holder.progressbar.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(holder.ivImage);
            }
            holder.tvName.setText(menuDataList.get(position).getProductName());
            holder.tvDes.setText(menuDataList.get(position).getDescription());
            holder.tvPrice.setText("Now "+CommonUtilities.getPriceFormat(menuDataList.get(position).getOfferPrice()) +" "+menuDataList.get(position).getCurrency());

            holder.tvQuantity.setText(context.getResources().getString(R.string.quantity)+menuDataList.get(position).getQuantity());






            if(menuDataList.get(position).getSellerData().getOfferStatus().equals("Active"))
            {
if(menuDataList.get(position).getoStatus().equals("Active"))
{
    if(menuDataList.get(position).getOfferPrice()!=null)

    {

        // if offer price is available then show old price and valid date


        if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
        {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            try{

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


                String str1 = formattedDate;
                Date date1 = formatter.parse(str1);

                String str2 = getOutputFormats(menuDataList.get(position).getEndDate());
                Date date2 = formatter.parse(str2);
                holder.tvValidTime.setText("This offer is valid from "+getOutputFormats(menuDataList.get(position).getStartDate())+" till "+getOutputFormats(menuDataList.get(position).getEndDate()));

                if (date2.compareTo(date1)>=0)
                {

                    holder.tvOldPrice.setVisibility(View.VISIBLE);
                    holder.tvValidTime.setVisibility(View.VISIBLE);
                    holder.tvOldPrice.setText(context.getResources().getString(R.string.was)+ CommonUtilities.getPriceFormat(menuDataList.get(position).getPrice()) +" "+menuDataList.get(position).getCurrency());
                    holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else
                {
                    holder.tvPrice.setText("");
                    holder.tvValidTime.setText("");
                    holder.tvOldPrice.setText(CommonUtilities.getPriceFormat(menuDataList.get(position).getPrice()) +" "+menuDataList.get(position).getCurrency());


                }

            }catch (ParseException e1){
                e1.printStackTrace();
            }




        }
        else
        {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            holder.tvValidTime.setText("Esta promoção é válida de "+getOutputFormats(menuDataList.get(position).getStartDate())+" a "+getOutputFormats(menuDataList.get(position).getEndDate()));

        }
    }
}


             else {
                    holder.tvOldPrice.setVisibility(GONE);
                    //   holder.tvValidTime.setVisibility(GONE);
                    holder.tvPrice.setText(CommonUtilities.getPriceFormat(menuDataList.get(position).getPrice())+" "+menuDataList.get(position).getCurrency());
                    holder.tvPrice.setTextColor(Color.BLACK);
                }

            }

            else
            {


                holder.tvOldPrice.setVisibility(GONE);
             //   holder.tvValidTime.setVisibility(GONE);
                holder.tvPrice.setText(CommonUtilities.getPriceFormat(menuDataList.get(position).getPrice())+" "+menuDataList.get(position).getCurrency());
                holder.tvPrice.setTextColor(Color.BLACK);
            }





            if(menuDataList.get(position).getProductType().equalsIgnoreCase(ParamEnum.VEG.theValue()))
                holder.IvDishType.setImageResource(R.drawable.veg);
            else
                holder.IvDishType.setImageResource(R.drawable.nonveg);
            RestaurantResponse cartData = menuDataList.get(position).getCartData();
            if(cartData!=null){
                holder.tvOrderCount.setText(String.valueOf(cartData.getQuantity()));
            }else {
                //holder.tvOrderCount.setText(String.valueOf(menuDataList.get(position).getQuantity()));
                holder.tvOrderCount.setText("0");
            }
        }else {



            RestaurantResponse productData = menuDataList.get(position).getProductData();
            String image = productData.getProductImage();
            if(image!=null && !image.equals("")){
                setImage(holder.progressbar,image,holder.ivImage);
            }else {
                holder.progressbar.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(holder.ivImage);
            }
            holder.tvName.setText(productData.getProductName());
            holder.tvDes.setText(productData.getDescription());
            holder.tvQuantity.setText(context.getResources().getString(R.string.quantity)+productData.getQuantity());







            if(menuDataList.get(position).getSellerData().getOfferStatus().equals("Active")) {
                if(productData.getoStatus().equals("Active"))
                {


                    if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                    {
                        Date c = Calendar.getInstance().getTime();

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);

                        try{

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


                            String str1 = formattedDate;
                            Date date1 = formatter.parse(str1);

                            String str2 = getOutputFormats(productData.getEndDate());
                            Date date2 = formatter.parse(str2);
                            holder.tvValidTime.setText("This offer is valid from "+getOutputFormats(productData.getStartDate())+" till "+getOutputFormats(productData.getEndDate()));

                            if (date2.compareTo(date1)>=0)
                            {

                                holder.tvOldPrice.setVisibility(View.VISIBLE);
                                holder.tvValidTime.setVisibility(View.VISIBLE);
                                holder.tvPrice.setText("Now "+CommonUtilities.getPriceFormat(productData.getOfferPrice()) +" "+productData.getCurrency());

                                holder.tvOldPrice.setText(context.getResources().getString(R.string.was)+ CommonUtilities.getPriceFormat(productData.getPrice()) +" "+productData.getCurrency());
                                holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            }
                            else
                            {
                                holder.tvPrice.setText("");
                                holder.tvValidTime.setText("");
                                holder.tvOldPrice.setText(CommonUtilities.getPriceFormat(productData.getPrice()) +" "+productData.getCurrency());


                            }

                        }catch (ParseException e1){
                            e1.printStackTrace();
                        }




                    }
                    else
                    {
                        Date c = Calendar.getInstance().getTime();

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        holder.tvValidTime.setText("Esta promoção é válida de "+getOutputFormats(productData.getStartDate())+" a "+getOutputFormats(productData.getEndDate()));

                    }

                }
                else
                {
                    holder.tvOldPrice.setVisibility(GONE);
                    //   holder.tvValidTime.setVisibility(GONE);
                    holder.tvPrice.setText(CommonUtilities.getPriceFormat(productData.getPrice())+" "+productData.getCurrency());
                    holder.tvPrice.setTextColor(Color.BLACK);
                }

//                if((productData.getSellerData().getOfferStatus().equals("Active")))
//                {
//                    if(productData.getOfferPrice()==null) {
//                        holder.tvPrice.setText("");
//                        holder.tvOldPrice.setText((productData.getPrice()) + " " + productData.getCurrency());
//                        //  holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    }
////                    else
////                    {
////                        Date c = Calendar.getInstance().getTime();
////
////                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
////                        String formattedDate = df.format(c);
////
////                        try{
////
////                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
////
////
////                            String str1 = formattedDate;
////                            Date date1 = formatter.parse(str1);
////
////                            String str2 = getOutputFormats(productData.getEndDate());
////                            Date date2 = formatter.parse(str2);
////                            holder.tvValidTime.setVisibility(View.VISIBLE);
////
////                            if (date2.compareTo(date1)>=0)
////                            {
////                                holder.tvValidTime.setText("This offer is valid from "+getOutputFormats(productData.getStartDate())+" till "+getOutputFormats(productData.getEndDate()));
////
////                            }
////                            else
////                            {
////                                holder.tvPrice.setText("");
////                                holder.tvValidTime.setText("");
////                                holder.tvOldPrice.setText( CommonUtilities.getPriceFormat(productData.getPrice()) +" "+productData.getCurrency());
////
////
////
////                            }
////
////                        }catch (ParseException e1){
////                            e1.printStackTrace();
////                        }
////
////
////                    }
//                }
//
//
//                else
//                {
//                    holder.tvPrice.setText("");
//                    holder.tvOldPrice.setText((productData.getPrice()) + " " + productData.getCurrency());
//                }
            }
            else
            {

                holder.tvPrice.setText("");
                holder.tvValidTime.setText("");
                holder.tvOldPrice.setText(CommonUtilities.getPriceFormat(productData.getPrice()) +" "+productData.getCurrency());


            }









            String productType = productData.getProductType();
            if(productType!=null){
                holder.IvDishType.setVisibility(View.VISIBLE);
                if(productType.equalsIgnoreCase(ParamEnum.VEG.theValue()))
                    holder.IvDishType.setImageResource(R.drawable.veg);
                else
                    holder.IvDishType.setImageResource(R.drawable.nonveg);
            }else{
                holder.IvDishType.setVisibility(GONE);
            }


            holder.tvOrderCount.setText(String.valueOf(menuDataList.get(position).getQuantity()));
        }
    }


    @Override
    public int getItemCount() {
        return menuDataList!=null?menuDataList.size():0;
    }

    public void updateList(ArrayList<RestaurantResponse> menuDataList){
        this.menuDataList=menuDataList;
        this.temp=menuDataList;
        this.notifyDataSetChanged();
    }

    public void updateDataList(RestaurantResponse menuDataList,int pos){
        this.menuDataList.set(pos,menuDataList);
        for(int i=0;i<temp.size();i++){
            if(menuDataList.get_id().equalsIgnoreCase(temp.get(i).get_id())){
                temp.set(i,menuDataList);
                break;
            }
        }
        notifyDataSetChanged();
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
                    check="0";

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
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("aaa");


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
            }        }

    }

    private String getReminingTimes() {
        String delegate = "hh:mm";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }
    private String getReminingTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }



    @NonNull
    //  @Override
    public Filter getFilter()
    {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults results=new FilterResults();
                final ArrayList<RestaurantResponse> tempFilterData=new ArrayList<>();
                if(constraint==null||constraint.toString().trim().length()==0)
                {
                    results.values=temp;
                }else {
                    String constrainString=constraint.toString().toLowerCase();
                    for(RestaurantResponse data:temp)
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
                    menuDataList=(ArrayList<RestaurantResponse>)results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }


    public class MenuDetailsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.IvDishType)
        ImageView IvDishType;
        @BindView(R.id.tvOrderCount)
        TextView tvOrderCount;
        @BindView(R.id.progressbar)
        ProgressBar progressbar;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.tvValidTime)
        TextView tvValidTime;
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;
        public MenuDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.tvAddItem,R.id.tvRemoveItem})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.tvAddItem:
                    if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime)==null|| SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime).isEmpty())
                    {
                        if(  SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime).isEmpty()|| SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.otime)==null)
                        {

                            commonListener.onAddItem(getAdapterPosition(),menuDataList);

                        }
                    }


                    else
                    {
                        checktime();

                        if(!check.equals("1"))
                        {
                            Toast.makeText(context,"Restaurant has been Closed",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            commonListener.onAddItem(getAdapterPosition(),menuDataList);

                        }
                    }


                    break;
                case R.id.tvRemoveItem:
                    commonListener.onRemoveItem(getAdapterPosition(),menuDataList);
                    break;
            }

        }

    }




    private void setImage( ProgressBar progressBar,final String imageUri,final ImageView imageView) {
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



}
