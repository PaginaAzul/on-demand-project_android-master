package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.pagin.azul.onphasesecond.activity.MyCartActivity;
import com.pagin.azul.onphasesecond.activity.ProductDetails;
import com.pagin.azul.onphasesecond.activity.ScheduleMyCart;
import com.pagin.azul.onphasesecond.activity.SubOfferActivity;
import com.pagin.azul.onphasesecond.model.ProductDetailsResponse;
import com.pagin.azul.onphasesecond.model.ProductResponse;
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
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.getOutputFormats;

public class SubOfferAdapter extends RecyclerView.Adapter<SubOfferAdapter.SubOfferViewHolder> implements Filterable {

    private Context context;
    private ArrayList<ProductDetailsResponse> data;
    private ArrayList<ProductDetailsResponse> mainList;
    private CommonListener commonListener;

    public SubOfferAdapter(Context context, ArrayList<ProductDetailsResponse> data, CommonListener commonListener) {
        this.context = context;
        this.data = data;
        this.mainList = data;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public SubOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_sub_offer_item,parent, false);
        return new SubOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubOfferViewHolder holder, int position) {
        String image = data.get(position).getProductImage();
        if(image!=null && !image.equals("")){
            setImage(holder.progressBar,image,holder.ivImage);
        }else {
            holder.progressBar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali)/*.override(110, 150)*/.into(holder.ivImage);
        }
        holder.tvName.setText(data.get(position).getProductName());
        holder.tvStores.setText(data.get(position).getResAndStoreId().getName());
        holder.tvDes.setText(data.get(position).getDescription());


        if(data.get(position).getoStatus().equals("Active"))
        {
            if(data.get(position).getOfferPrice()!=null)

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

                        String str2 = getOutputFormats(data.get(position).getEndDate());
                        Date date2 = formatter.parse(str2);
                        holder.tvValidTime.setText("This offer is valid from "+getOutputFormats(data.get(position).getStartDate())+" till "+getOutputFormats(data.get(position).getEndDate()));

                        if (date2.compareTo(date1)>=0)
                        {
                            holder.itemView.setVisibility(View.VISIBLE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                            holder.tvOldPrice.setText(context.getResources().getString(R.string.was)+CommonUtilities.getPriceFormat(data.get(position).getPrice())+" "+data.get(position).getCurrency());
                            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvNewPrice.setText(context.getResources().getString(R.string.now)+CommonUtilities.getPriceFormat(data.get(position).getOfferPrice())+" "+data.get(position).getCurrency());

                            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                                holder.tvValidTime.setText("This offer is valid from "+getOutputFormat(data.get(position).getStartDate())+" till "+getOutputFormat(data.get(position).getEndDate()));
                            else
                                holder.tvValidTime.setText("Esta promoção é válida de "+getOutputFormat(data.get(position).getStartDate())+" a "+getOutputFormat(data.get(position).getEndDate()));
                            holder.clSubOffer.setVisibility(View.VISIBLE);
                        }
                        else

                        {
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 8));
                            holder.tvNewPrice.setText("");
                            holder.tvValidTime.setText("");
                            holder.tvOldPrice.setText(CommonUtilities.getPriceFormat(data.get(position).getPrice()) +" "+data.get(position).getCurrency());


                        }

                    }catch (ParseException e1){
                        e1.printStackTrace();
                    }




                }
                else
                {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 8));
                    Date c = Calendar.getInstance().getTime();


                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String formattedDate = df.format(c);
                    holder.tvValidTime.setText("Esta promoção é válida de "+getOutputFormats(data.get(position).getStartDate())+" a "+getOutputFormats(data.get(position).getEndDate()));

                }
            }
        }


        else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 8));
            holder.tvOldPrice.setVisibility(GONE);
            //   holder.tvValidTime.setVisibility(GONE);
            holder.tvNewPrice.setText(CommonUtilities.getPriceFormat(data.get(position).getPrice())+" "+data.get(position).getCurrency());
            holder.tvNewPrice.setTextColor(Color.BLACK);
        }





    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults results=new FilterResults();
                final ArrayList<ProductDetailsResponse> tempFilterData=new ArrayList<>();
                if(constraint==null||constraint.toString().trim().length()==0)
                {
                    results.values=mainList;
                }else {
                    String constrainString=constraint.toString().toLowerCase();
                    for(ProductDetailsResponse data:mainList)
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
                    data=(ArrayList<ProductDetailsResponse>)results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class SubOfferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvStores)
        TextView tvStores;

        @BindView(R.id.tvDes)
        TextView tvDes;

        @BindView(R.id.tvNewPrice)
        TextView tvNewPrice;

        @BindView(R.id.tvValidTime)
        TextView tvValidTime;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.clSubOffer)
        ConstraintLayout clSubOffer;

        public SubOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.clSubOffer,R.id.tvAddToCart})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.clSubOffer: {
                    Intent intent = new Intent(context, ProductDetails.class);
                    intent.putExtra(ParamEnum.TYPE.theValue(), SubOfferActivity.class.getSimpleName());
                    intent.putExtra(ParamEnum.ID.theValue(),data.get(getAdapterPosition()).get_id());
                    context.startActivity(intent);
                }
                    break;

                case R.id.tvAddToCart:
                    commonListener.onAddCart(getAdapterPosition(),data);
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

    public void update(ArrayList<ProductDetailsResponse> data){
        this.data = data;
        this.mainList = data;
        notifyDataSetChanged();
    }
}
