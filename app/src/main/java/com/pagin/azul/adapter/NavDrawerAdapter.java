package com.pagin.azul.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pagin.azul.R;
import com.pagin.azul.activities.ChangeLanguageActivity;
import com.pagin.azul.activities.ContactActivity;
import com.pagin.azul.activities.ManageAddressActivity;
import com.pagin.azul.activities.SettingActivity;
import com.pagin.azul.activities.SignUpOptions;
import com.pagin.azul.bean.NavDrawerModel;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.CustomerStoriesActivity;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.onphasesecond.activity.MyFavoritesActivity;
import com.pagin.azul.onphasesecond.activity.MyOrdersActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pagin.azul.Constant.Constants.kToken;

public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.MyViewHolder> {
    private ArrayList<NavDrawerModel> navlist;
    private Context context;
    private MemberInterface listener;

    public NavDrawerAdapter(Context context, ArrayList<NavDrawerModel> navlist) {
        this.context = context;
        this.navlist = navlist;

    }

    @Override
    public NavDrawerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemtype = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_left_nav, parent, false);
        return new NavDrawerAdapter.MyViewHolder(itemtype);
    }

    @Override
    public void onBindViewHolder(NavDrawerAdapter.MyViewHolder holder, int position) {
        holder.itemName.setText(navlist.get(position).getItemName());
        //int image =navlist.get(position).getImage();
        Glide.with(context).load(navlist.get(position).getImage()).into(holder.itemImg);
        /*if (position == 4 || position == 5) {
            holder.rlMainNav.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryThin));
        } else {
            holder.rlMainNav.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if (position == 8 || position == 9 || position == 10) {
            holder.itemName.setTextColor(context.getResources().getColor(R.color.purpalMedium));
        } else {
            holder.itemName.setTextColor(context.getResources().getColor(R.color.blacklight));
        }*/

        if(position==5){
            if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                holder.itemName.setText(R.string.nav_setting);
                holder.itemName.setTextColor(context.getResources().getColor(R.color.blacklight));
            } else {
                holder.itemName.setText(R.string.login_sign_up);
                holder.itemName.setTextColor(Color.BLACK);
            }
        }

    }

    @Override
    public int getItemCount() {
        return navlist.size();
    }

    public void setListener(MemberInterface listener) {
        this.listener = listener;
    }

    public interface MemberInterface {

        void onDelivery();

        void onProffessional();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        ImageView itemImg;

        @BindView(R.id.tv_name)
        TextView itemName;

        @BindView(R.id.rl_main_nav)
        RelativeLayout rlMainNav;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            rlMainNav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    performAction(getAdapterPosition());
                }
            });


        }


    }


    public void performAction(int position) {
        switch (position) {
            case 0:
                //context.startActivity(HomeMainActivity.getIntent(context, ""));
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            case 1:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    context.startActivity(new Intent(context, MyOrdersActivity.class));
                } else {
                    showDialog();
                }
                break;
            case 2:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    context.startActivity(new Intent(context, MyFavoritesActivity.class));
                } else {
                    showDialog();
                }
                break;
            case 3:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    context.startActivity(new Intent(context, CustomerStoriesActivity.class));
                } else {
                    showDialog();
                }
                break;
            case 4:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    context.startActivity(new Intent(context, ManageAddressActivity.class));
                } else {
                    showDialog();
                }
                break;
//            case 3:
//                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
//                    context.startActivity(new Intent(context, MyProfile.class));
//                } else {
//                    showDialog();
//                }
//                break;
//            case 4:
//                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
//                    listener.onDelivery();
//                    //  context.startActivity(new Intent(context, BecomeDeliveryPersonActivity.class));
//                } else {
//                    showDialog();
//                }
//                break;
//            case 5:
//                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
//                    listener.onProffessional();
//                    //context.startActivity(new Intent(context, BecomeProfessionalWorkerActivity.class));
//                } else {
//                    showDialog();
//                }
//                break;
            case 5:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    context.startActivity(new Intent(context, SettingActivity.class));
                } else {
                    context.startActivity(new Intent(context, SignUpOptions.class));
                    //showDialog();
                }
                break;
            case 6:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    context.startActivity(new Intent(context, ChangeLanguageActivity.class));
                } else {
                    showDialog();
                }
                break;
            /*case 4:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    //context.startActivity(new Intent(context, RatingAndRiviewActivity.class));
                    Toast.makeText(context, R.string.will_be_redirect_after_release_app, Toast.LENGTH_SHORT).show();
                }else {
                    showDialog();
                }
                break;*/
            case 7:
                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Paginazul");
                    //sharingIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.welcome_paginazul_family)+"\nhttps://paginazul.page.link/?link=http://mobulous.co.in/Paginazul/marketing.html&apn=com.pagin.azul&efr=1");
                    //sharingIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.welcome_paginazul_family)+"\nAndroid app link:- https://paginazul.page.link/qL6j\n\niOS app link:- https://paginzul.page.link/jdF1");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.welcome_paginazul_family)+"\nAndroid app link:- https://play.google.com/store/apps/details?id=com.pagin.azul\n\niOS app link:- https://apps.apple.com/br/app/paginazul/id1531074209");
                    context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.invite_your_frd)));

//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
//                    String shareMessage = "\nLet me recommend you this application\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                    context.startActivity(Intent.createChooser(shareIntent, "choose one")
                } catch (Exception e) {
                    //e.toString();
                }

                break;
            case 8:
                if (SharedPreferenceWriter.getInstance(context).getString(kToken) != null && !SharedPreferenceWriter.getInstance(context).getString(kToken).equalsIgnoreCase("")) {
                    //context.startActivity(new Intent(context, ContactAdmin.class));
                    context.startActivity(new Intent(context, ContactActivity.class));
//                    Intent intent = new Intent(context, CommonWebpage.class);
//                    intent.putExtra("Page", "tv_contactUs");
//                    context.startActivity(intent);
                }else {
                    showDialog();
                }
                break;

        }
    }


    private void showDialog() {
        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText(R.string.you_are_not_logged_in_please_login_sign_up_further_proced);
        registerNow.setText(R.string.ok_upper_case);
        notNow.setText(R.string.cancel);


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SignUpOptions.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


}
