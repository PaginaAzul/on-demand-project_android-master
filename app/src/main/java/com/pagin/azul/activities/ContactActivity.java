package com.pagin.azul.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.utils.CommonUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tvContact)
    TextView tvContact;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    private static final int PERMISSION_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        tv_title.setText(R.string.contact_us);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.iv_back, R.id.clContact, R.id.clEmail})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.clContact: {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tvContact.getText().toString().trim()));
                startActivity(intent);
            }

            break;

            case R.id.clEmail: {
                //if (checkPermissions())
                    dispatchGmail();
            }
            break;
        }
    }

    private void dispatchGmail() {
        /*try {
            final Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setType("plain/text")
                    .setData(Uri.parse("mailto:"))
                    .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail")
                    .putExtra(Intent.EXTRA_EMAIL, tvEmail.getText().toString());
                //.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"})
                //.putExtra(Intent.EXTRA_SUBJECT, "test")
                //.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my demo app :-)");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(ContactActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }*/
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{tvEmail.getText().toString()});
        /*i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");*/
        try {
            startActivity(Intent.createChooser(i, getString(R.string.choose_an_email_client)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, getString(R.string.there_are_no_mail), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    try {
                        dispatchGmail();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Toast.makeText(this, "Please accept permissions due to security purpose", Toast.LENGTH_SHORT).show();
            //checkPermissions();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")) {
                if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                } else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            } else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        } else {
            super.attachBaseContext(newBase);
        }
    }

}