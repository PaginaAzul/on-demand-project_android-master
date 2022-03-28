package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProfessionalWorkerDashboardAdapter extends RecyclerView.Adapter<NewProfessionalWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> newList;
    private OnContentClickProfessionalNew onContentClickProfessionalNew;

    public NewProfessionalWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> newList, OnContentClickProfessionalNew onContentClickProfessionalNew) {
        this.context = context;
        this.newList = newList;
        this.onContentClickProfessionalNew = onContentClickProfessionalNew;
    }

    @NonNull
    @Override
    public NewProfessionalWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_new_professional_worker_dashboard, viewGroup, false);

        return new NewProfessionalWorkerDashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewProfessionalWorkerDashboardAdapter.MyViewHolder holder, int position) {
//        holder.tvProfileName.setText(newList.get(position));
//        SpannableString spannableString = new SpannableString("Minimum Offer 10+*");
//        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red)),17,18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.tvMinOffer.setText(spannableString);
        String getDate = newList.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here
            holder.tv_date.setText(String.valueOf(splitted[0]));
            holder.tv_time.setText(String.valueOf(splitted[1]+" "+splitted[2]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
        holder.phn_no.setText(newList.get(position).getMobileNumber());
        holder.order_no.setText(newList.get(position).getOrderNumber());
        holder.tvProfileName.setText(newList.get(position).getName());
        holder.tv_view_comments.setText("("+newList.get(position).getTotalRating() + " Ratings View all)");
        holder.tv_rat_num.setText(newList.get(position).getAvgRating());
        holder.mylocToDropOff.setText(newList.get(position).getCurrentToDrLocation() + " km");
        holder.order_detail.setText("Require " + newList.get(position).getSeletTime());
        holder.tv_dropAdd.setText(newList.get(position).getPickupLocation());
        holder.item_detail.setText(newList.get(position).getOrderDetails());
        holder.category_name.setText(newList.get(position).getSelectSubSubCategoryName());
//        if (!newList.get(position).getProfilePic().equalsIgnoreCase("")) {
//            Picasso.with(context).load(newList.get(position).getProfilePic()).into(holder.user_pic);
//        }

        Glide.with(context)
                .load(newList.get(position).getProfilePic())
                .apply(RequestOptions.placeholderOf(R.drawable.loader)
                        .error(R.drawable.profile_default))
                .into(holder.user_pic);
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_pic)
        ImageView user_pic;
        @BindView(R.id.category_name)
        TextView category_name;
        @BindView(R.id.tv_dropAdd)
        TextView tv_dropAdd;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.rl_mailRate)
        RelativeLayout rl_mailRate;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.mylocToDropOff)
        TextView mylocToDropOff;
        @BindView(R.id.order_detail)
        TextView order_detail;
        @BindView(R.id.phn_no)
        TextView phn_no;
        @BindView(R.id.item_detail)
        TextView item_detail;
        @BindView(R.id.tv_description)
        TextView tv_description;
        @BindView(R.id.edt_msg)
        EditText edt_msg;
        @BindView(R.id.edt_approxtym)
        EditText edt_approxtym;
        @BindView(R.id.edt_min_offer)
        EditText edt_min_offer;
        @BindView(R.id.rl_makeOffer)
        RelativeLayout rl_makeOffer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            rl_mailRate.setOnClickListener(this);
            rl_makeOffer.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_mailRate:
                    onContentClickProfessionalNew.onClickReview(newList.get(getAdapterPosition()));
                    break;
                case R.id.rl_makeOffer:
                    onContentClickProfessionalNew.onClickMakeOffer(newList.get(getAdapterPosition()));
                    break;
            }

        }
    }

    public interface OnContentClickProfessionalNew {
        void onClickReview(NormalUserPendingOrderInner data);

        void onClickMakeOffer(NormalUserPendingOrderInner data);
    }
}
