package com.pagin.azul.adapter;

import android.content.Context;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.CategoriesResultInner;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;

public class MapCategoryListAdapter extends RecyclerView.Adapter<MapCategoryListAdapter.CategoryHolder> {
    private String Api_key = "AIzaSyBL4ngANDBnJLusG09x2t7mkGwi_mX1SWo";
    private SelectCategoryInterface listener;
    private Context context;
    private ArrayList<CategoriesResultInner> cateList;

    public MapCategoryListAdapter(Context context, ArrayList<CategoriesResultInner> cateList) {
        this.context = context;
        this.cateList = cateList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_map_categories_list_layout, null, false);
        return new CategoryHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        holder.item_name.setText(cateList.get(position).getName());
        holder.item_type.setText("Address: " + cateList.get(position).getFormatted_address());
        String rating = String.valueOf(cateList.get(position).getRating());
        holder.rb_ratting.setRating(Float.parseFloat(rating));
        holder.rating_count.setText(String.valueOf(cateList.get(position).getRating()));
        holder.total_rating_count.setText("(" + String.valueOf(cateList.get(position).getUser_ratings_total()) + ")");

        if (cateList.get(position).getPhotos() != null) {
            for (int i = 0; i < cateList.get(position).getPhotos().size(); i++) ;
            String photoRef = cateList.get(position).getPhotos().get(0).getPhoto_reference();
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + Api_key;
            Picasso.with(context).load(url)
                    .fit().placeholder(R.drawable.default_image).into(holder.cate_img);
        }
        if (cateList.get(position).getOpening_hours() != null) {
            if (cateList.get(position).getOpening_hours().isOpen_now()) {
                holder.item_open.setText("Open Now");
            } else {
                holder.item_open.setText("Close");
            }
        }


        double lat, lat1, lng1, lng;
        lat = Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(kLat));
        lat1 = cateList.get(position).getGeometryData().getLocation().getLat();
        lng = Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(kLong));
        lng1 = cateList.get(position).getGeometryData().getLocation().getLng();


        Location locationA = new Location("point A");
        locationA.setLatitude(lat);
        locationA.setLongitude(lng);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat1);
        locationB.setLongitude(lng1);
        float distance = locationA.distanceTo(locationB);
        double mi, km;
        mi = distance;
        km = mi / 1000.0;

        String dis = new DecimalFormat("##.##").format(km);
        holder.item_distance.setText(dis + " km");

    }

    @Override
    public int getItemCount() {
        return cateList.size();
    }

    public void setListener(SelectCategoryInterface listener) {
        this.listener = listener;
    }

    public interface SelectCategoryInterface {

        void onItemClick(CategoriesResultInner getList);


    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_name)
        TextView item_name;
        @BindView(R.id.item_distance)
        TextView item_distance;
        @BindView(R.id.rating_count)
        TextView rating_count;
        @BindView(R.id.item_type)
        TextView item_type;
        @BindView(R.id.item_open)
        TextView item_open;
        @BindView(R.id.total_rating_count)
        TextView total_rating_count;
        @BindView(R.id.rb_ratting)
        RatingBar rb_ratting;
        @BindView(R.id.cate_img)
        ImageView cate_img;

        @BindView(R.id.rl_main)
        RelativeLayout rl_main;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rl_main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_main:
                    if (listener != null) {
                        listener.onItemClick(cateList.get(getAdapterPosition()));
                    }
                    break;
            }
        }
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
