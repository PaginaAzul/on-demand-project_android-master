package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.activity.SubOfferActivity;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> implements Filterable {

    private Context context;
    private ArrayList<ProductResponse> data;
    private ArrayList<ProductResponse> mainList;
    private int size;

    public OfferAdapter(Context context, ArrayList<ProductResponse> data, int size) {
        this.context = context;
        this.data = data;
        this.mainList = data;
        this.size = size;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_offer_item, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        try{
            position = position % data.size();
        }catch (ArithmeticException e){
            e.printStackTrace();
        }
        try{
            Glide.with(context)
                    .load(data.get(position).getImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.food_thali)
                            .error(R.drawable.food_thali))
                    .into(holder.ivCategory);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        holder.tvSubCategory.setText(data.get(position).getName());

        int finalPosition = position;
        holder.cvCategory.setOnClickListener(view -> {
            Intent intent = new Intent(context, SubOfferActivity.class);
            intent.putExtra(ParamEnum.TITLE.theValue(),holder.tvSubCategory.getText().toString());
            intent.putExtra(ParamEnum.ID.theValue(),data.get(finalPosition).get_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        //return data!=null?data.size():0;
        /*int size = data.size();
        if(size==0)
            return size;
        else
            return Integer.MAX_VALUE;*/
        return size;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults results=new FilterResults();
                final ArrayList<ProductResponse> tempFilterData=new ArrayList<>();
                if(constraint==null||constraint.toString().trim().length()==0)
                {
                    results.values=mainList;
                    size = Integer.MAX_VALUE;
                }else {
                    String constrainString=constraint.toString().toLowerCase();
                    for(ProductResponse data:mainList)
                    {
                        if(data.getName().toLowerCase().contains(constrainString))
                        {
                            tempFilterData.add(data);
                            Log.w("charSeq",constrainString);
                        }
                        Log.w("charSeq",constrainString);

                    }
                    results.count=tempFilterData.size();
                    results.values=tempFilterData;
                    size = results.count;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                if(results.values!=null)
                {
                    data=(ArrayList<ProductResponse>)results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSubCategory)
        TextView tvSubCategory;

        @BindView(R.id.ivCategory)
        ImageView ivCategory;

        @BindView(R.id.cvCategory)
        CardView cvCategory;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        /*@OnClick({R.id.cvCategory})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cvCategory:
                    Intent intent = new Intent(context, SubOfferActivity.class);
                    intent.putExtra(ParamEnum.TITLE.theValue(),tvSubCategory.getText().toString());
                    intent.putExtra(ParamEnum.ID.theValue(),data.get(getAdapterPosition()).get_id());
                    context.startActivity(intent);
                    break;
            }
        }*/
    }

    public void updateLeftAdapter(ArrayList<ProductResponse> filterList,int size){
        this.data = filterList;
        this.size = size;
        notifyDataSetChanged();
    }
}
