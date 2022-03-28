package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.StaticContentResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.utils.CommonUtility;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pagin.azul.Constant.Constants.kType;

public class TermsConditionActivity extends AppCompatActivity {
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.tv_condition)
    TextView tvCondition;
    private List<StaticContentResponse> staticMainResponse;


    public static Intent getIntent(Context context, List<StaticContentResponse> staticMainResponse) {
        Intent intent = new Intent(context, TermsConditionActivity.class);
        intent.putExtra(kType, (Serializable) staticMainResponse);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            staticMainResponse = (List<StaticContentResponse>) getIntent().getSerializableExtra(kType);
            for(int i=0;i<staticMainResponse.size();i++){
                if(staticMainResponse.get(i).getType().equals("TermCondition")){
                    //String description = staticMainResponse.get(i).getDescription();
                    String description = staticMainResponse.get(i).getPortDescription();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        tvCondition.setText(Html.fromHtml((description==null?"":description), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvCondition.setText(Html.fromHtml((description==null?"":description)));
                    }
                    break;
                }
            }
            //tvCondition.setText(staticMainResponse.get(0).getDescription());
        }

    }


    //All click:
    @OnClick({R.id.btn_next,R.id.tv_terms_condition,R.id.tv_privacy})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_next:
                if (checkBox.isChecked()) {
                    //showDialogInstruction();
                    //startActivity(HomeMainActivity.getIntent(TermsConditionActivity.this, ""));
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    showDialog1();
                }
                break;


            case R.id.tv_terms_condition:
                Intent intent1 = new Intent(TermsConditionActivity.this, CommonWebpage.class);
                intent1.putExtra("Page", "tv_terms_condition");
                startActivity(intent1);
                break;

            case R.id.tv_privacy:
                Intent intent2 = new Intent(TermsConditionActivity.this, CommonWebpage.class);
                intent2.putExtra("Page", "tv_privacyPolicy");
                startActivity(intent2);
                break;
        }
    }


    private void showDialogInstruction() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_instraction);
        TextView tvInstraction = dialog.findViewById(R.id.tv_desc);
        tvInstraction.setText(staticMainResponse.get(5).getDescription());

        dialog.findViewById(R.id.tv_ok).setOnClickListener(v -> {
            dialog.dismiss();
            showDialog();
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> {

            dialog.dismiss();
        });


        dialog.show();

    }


    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_terms_condition_layout);
        TextView tvInstraction = dialog.findViewById(R.id.tv_desc);
        tvInstraction.setText(staticMainResponse.get(6).getDescription());

        dialog.findViewById(R.id.img_cancel2).setOnClickListener(v -> {
            startActivity(HomeMainActivity.getIntent(TermsConditionActivity.this, ""));
            finish();
            dialog.dismiss();
        });


        dialog.show();

    }


    private void showDialog1() {
        Dialog dialog = new Dialog(TermsConditionActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btnok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);

        textView.setText(R.string.please_accept_terms_and_condition);
        btnok.setText(R.string.ok_upper_case);
        btnok.setVisibility(View.VISIBLE);
        notNow.setVisibility(View.GONE);
        registerNow.setVisibility(View.GONE);


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
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
