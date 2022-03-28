package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.R;
import com.pagin.azul.bean.MessageChat;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.pagin.azul.Constant.Constants.kUserId;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int RIGHT_BUBBLE = 1;
    private static final int LEFT_BUBBLE = 0;
    private List<MessageChat> messageList;
    private Context context;

    public MessageAdapter(List<MessageChat> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewHolder = null;
        RecyclerView.ViewHolder rcv = null;
        try {
            switch (i) {
                case RIGHT_BUBBLE:
                    viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_right_side_chat, parent, false);
                    rcv = new RightBubbleViewHolder(viewHolder);
                    break;

                case LEFT_BUBBLE:
                    viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_left_side_nu, parent, false);
                    rcv = new LeftBubbleViewHolder(viewHolder);
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rcv;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (SharedPreferenceWriter.getInstance(context).getString(kUserId).matches(messageList.get(position).getSenderId()))
                return RIGHT_BUBBLE;
            else
                return LEFT_BUBBLE;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;


    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        try {
            switch (viewHolder.getItemViewType()) {
                case RIGHT_BUBBLE:

                    RightBubbleViewHolder rightSideViewHolder = (RightBubbleViewHolder) viewHolder;


                    if (messageList.get(position).getProfilePic() != null) {
                        Glide.with(context).load(messageList.get(position).getProfilePic()).apply(new RequestOptions().placeholder(R.drawable.profile_default).override(200, 200)).into(rightSideViewHolder.userprfRight);
                    }

                    if (messageList.get(position).getMessageType().equalsIgnoreCase("Text")) {

                        rightSideViewHolder.rloutgoing.setVisibility(View.GONE);
                        rightSideViewHolder.tvMsgRight.setVisibility(View.VISIBLE);
                        rightSideViewHolder.outgoing_image.setVisibility(View.GONE);
                        rightSideViewHolder.tvMsgRight.setText(messageList.get(position).getMessage());


                    }else if(messageList.get(position).getMessageType().equalsIgnoreCase("Location")){
                        if (messageList.get(position).getMessage() != null && !messageList.get(position).getMessage().equalsIgnoreCase("")) {
                            rightSideViewHolder.tvMsgRight.setVisibility(View.GONE);
                            rightSideViewHolder.rloutgoing.setVisibility(View.VISIBLE);
                            rightSideViewHolder.outgoing_image.setVisibility(View.VISIBLE);

                            String locationType = messageList.get(position).getLocationType();
                            if(locationType != null && !locationType.equalsIgnoreCase("")){
                                if(locationType.equalsIgnoreCase("Dropoff")) {
                                    rightSideViewHolder.tvLocation.setVisibility(View.VISIBLE);
                                    rightSideViewHolder.tvLocation.setText(R.string.droff_location);
                                }else{
                                    rightSideViewHolder.tvLocation.setVisibility(View.VISIBLE);
                                    rightSideViewHolder.tvLocation.setText(R.string.pickup_location_2);

                            }
                            }else {
                                rightSideViewHolder.tvLocation.setVisibility(View.GONE);
                            }
                            Glide.with(context).load(messageList.get(position).getMessage()).apply(new RequestOptions().placeholder(R.drawable.default_cat).override(200, 200)).into(rightSideViewHolder.outgoing_image);
                        }

                    } else {
                        Log.e("right","yes");
                        if (messageList.get(position).getMedia() != null && !messageList.get(position).getMedia().equalsIgnoreCase("")) {
                            rightSideViewHolder.tvMsgRight.setVisibility(View.GONE);
                            rightSideViewHolder.rloutgoing.setVisibility(View.VISIBLE);
                            rightSideViewHolder.outgoing_image.setVisibility(View.VISIBLE);
                            rightSideViewHolder.tvLocation.setVisibility(View.GONE);
                            rightSideViewHolder.rlprogress.setVisibility(View.GONE);

                            //Picasso.with(context).load(messageList.get(position).getMedia()).error(R.drawable.place_holder).into(rightSideViewHolder.outgoing_image);
                            try {
                                Glide.with(context).load(messageList.get(position).getMedia()).apply(new RequestOptions().placeholder(R.drawable.default_cat).error(R.drawable.default_cat)).into(rightSideViewHolder.outgoing_image);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                Log.v("ex" ,e.getMessage());
                            }


                        }

                    }

                        String getDate = messageList.get(position).getCreatedAt();
                        String server_format = getDate;    //server comes format ?
                        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
                        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

                        try {
                            Date date = sdf.parse(server_format);
                            System.out.println(date);
                            String your_format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                            System.out.println(your_format);
                            String[] splitted = your_format.split(" ");
                            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
                            // Now you can set the TextView here
                            rightSideViewHolder.tv_date.setText(String.valueOf(splitted[0]));
                            rightSideViewHolder.tvTimeRight.setText(String.valueOf(splitted[1]));
                        } catch (Exception e) {
                            System.out.println(e.toString()); //date format error
                        }




                    break;

                case LEFT_BUBBLE:

                    LeftBubbleViewHolder leftSideViewHolder = (LeftBubbleViewHolder) viewHolder;

                    //leftSideViewHolder.tvTime.setText(messageList.get(position).getCreayedTime());

                    if (messageList.get(position).getProfilePic() != null) {
                        Glide.with(context).load(messageList.get(position).getProfilePic()).apply(new RequestOptions().placeholder(R.drawable.profile_default).override(170, 170)).into(leftSideViewHolder.userprf);
                    }

                    if (messageList.get(position).getMessageType().equalsIgnoreCase("Text")) {
                        leftSideViewHolder.llleft_lay.setVisibility(View.GONE);
                        leftSideViewHolder.tvMsgLeft.setVisibility(View.VISIBLE);
                        leftSideViewHolder.incoming_image.setVisibility(View.GONE);
                        leftSideViewHolder.tvMsgLeft.setText(messageList.get(position).getMessage());
                    }else if(messageList.get(position).getMessageType().equalsIgnoreCase("Location")){

                        leftSideViewHolder.llleft_lay.setVisibility(View.VISIBLE);
                        leftSideViewHolder.tvMsgLeft.setVisibility(View.GONE);
                        leftSideViewHolder.incoming_image.setVisibility(View.VISIBLE);

                        String locationType = messageList.get(position).getLocationType();
                        if(locationType != null && !locationType.equalsIgnoreCase("")){
                            if(locationType.equalsIgnoreCase("Dropoff")) {
                                leftSideViewHolder.tvLocation.setVisibility(View.VISIBLE);
                                leftSideViewHolder.tvLocation.setText(R.string.droff_location);
                            }else{
                                leftSideViewHolder.tvLocation.setVisibility(View.VISIBLE);
                                leftSideViewHolder.tvLocation.setText(R.string.pickup_location_2);
                        }}
                        Glide.with(context).load(messageList.get(position).getMessage()).apply(new RequestOptions().placeholder(R.drawable.default_cat).override(200, 200)).into(leftSideViewHolder.incoming_image);
//                        if (messageList.get(position).getMessage() != null && !messageList.get(position).getMessage().equalsIgnoreCase("")) {
//                            Glide.with(context).load(messageList.get(position).getMessage()).apply(new RequestOptions().placeholder(R.drawable.default_p).override(170, 170)).into(leftSideViewHolder.incoming_image);
//                        }

                    } else {
                        Log.e("left","yes");
                        leftSideViewHolder.llleft_lay.setVisibility(View.VISIBLE);
                        leftSideViewHolder.tvMsgLeft.setVisibility(View.GONE);
                        leftSideViewHolder.incoming_image.setVisibility(View.VISIBLE);
                        leftSideViewHolder.tvLocation.setVisibility(View.GONE);
                        if (messageList.get(position).getMedia() != null && !messageList.get(position).getMedia().equalsIgnoreCase("")) {
                            Glide.with(context).load(messageList.get(position).getMedia()).apply(new RequestOptions().placeholder(R.drawable.default_cat).override(170, 170)).into(leftSideViewHolder.incoming_image);
                        }
                    }

                    String getDate1 = messageList.get(position).getCreatedAt();
                    String server_format12 = getDate1;    //server comes format ?
                    String server_format11 = "2019-04-04T13:27:36.591Z";    //server comes format ?
                    String myFormat1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
                    SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                    sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));

                    try {
                        Date date = sdf1.parse(server_format12);
                        System.out.println(date);
                        String your_format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
                        System.out.println(your_format);
                        String[] splitted = your_format.split(" ");
                        System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
                        // Now you can set the TextView here
                        leftSideViewHolder.tv_date_left.setText(String.valueOf(splitted[0]));
                        leftSideViewHolder.tvTime.setText(String.valueOf(splitted[1]));
                    } catch (Exception e) {
                        System.out.println(e.toString()); //date format error
                    }
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ex" ,e.getMessage());
        }

    }

    public void updateMessage(List<MessageChat> messageData) {

        messageList = messageData;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (messageList != null)
            return messageList.size();
        else
            return 0;
    }

    private class RightBubbleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView tvMsgRight;
        TextView tvTimeRight, outgoing_time,tv_date,tvLocation;
        ImageView userprfRight, outgoing_image;
        ConstraintLayout rloutgoing;
        ProgressBar rlprogress;

        RightBubbleViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvMsgRight = (view).findViewById(R.id.txtmsg);
            tvTimeRight = (view).findViewById(R.id.tv_right_time);
            userprfRight = (view).findViewById(R.id.userprfRight);
            outgoing_image = (view).findViewById(R.id.outgoing_image);
            outgoing_time = (view).findViewById(R.id.outgoing_time);
            rloutgoing = (view).findViewById(R.id.outgoing_msg);
            tv_date = (view).findViewById(R.id.tv_date);
            tvLocation = (view).findViewById(R.id.tvLocation);
            rlprogress=(view).findViewById(R.id.progress);
            outgoing_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.outgoing_image:
                    if(messageList.get(getAdapterPosition()).getMessageType().equalsIgnoreCase("Location")){
                        String url = messageList.get(getAdapterPosition()).getUrl();

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);

                    }else {

                        showDialog1(messageList.get(getAdapterPosition()).getMedia());
                    }

                    break;
            }
        }
    }


    private class LeftBubbleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView tvMsgLeft;
        TextView tvTime,tv_date_left,tvLocation;
        ImageView userprf, incoming_image;
        LinearLayout llleft_lay;

        LeftBubbleViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvMsgLeft = (view).findViewById(R.id.tvshowmsg);
            tvTime = (view).findViewById(R.id.textView21);
            userprf = (view).findViewById(R.id.userprfLeft);
            incoming_image = (view).findViewById(R.id.incoming_image);
            llleft_lay = (view).findViewById(R.id.left_lay);
            tv_date_left = (view).findViewById(R.id.tv_date_left);
            tvLocation = (view).findViewById(R.id.tvLocation);

            incoming_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.incoming_image:
                    showDialog1(messageList.get(getAdapterPosition()).getMedia());
                    break;
            }
        }
    }


    private void showDialog1(String img) {
        final Dialog dialog1 = new Dialog(context, R.style.ThemeDialogCustom);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.image_show_layout);

        ImageView yes_btn = (ImageView) dialog1.findViewById(R.id.img_main_iv);
        ImageView no_btn = (ImageView) dialog1.findViewById(R.id.img_close_iv);


//        aQuery.id(yes_btn).image(img, false, false);

        Glide.with(context)
                .load(img)
                .apply(new RequestOptions().placeholder(R.drawable.default_p))
                .into(yes_btn);

        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.semitransparent)));

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();

    }
}
