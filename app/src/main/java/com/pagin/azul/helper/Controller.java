//package com.mobu.jokar.helper;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.app.Application;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Build;
//import android.os.Environment;
//
//import android.support.multidex.MultiDex;
//import android.widget.DatePicker;
//import android.widget.TextView;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.imagepipeline.core.ImagePipelineConfig;
//import com.mobu.jokar.R;
//
//import java.io.File;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//
//
//public class Controller extends Application {
//
//    public static final String PACKAGE ="co.butterflies";
//    public static final String APP_NAME ="Butterflies";
//    public static String profile_image_path = null;
//    public static String profile_video_path = null;
//    public static String audio_path = null;
//    public static String getAudio_path() {
//        return audio_path;
//    }
//
//    public void onCreate()
//    {
//        super.onCreate();
//        //FontsOverride.setDefaultFont(this, "MONOSPACE", "quicksand_regular.ttf");
//        FacebookSdk.sdkInitialize(getApplicationContext());
//
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig
//                .newBuilder(getApplicationContext())
//                .setDownsampleEnabled(true)
//                //.setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier(activityManager))
//                .build();
//
//        Fresco.initialize(this,imagePipelineConfig);
//
//        makeDirectory();
//    }
//
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static void setStatusBarGradiant(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                      activity.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.toolbar_back));
//        }
//
//       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar_back);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(activity.getResources().getColor(R.color.colorTransparent));
//            window.setNavigationBarColor(activity.getResources().getColor(R.color.colorTransparent));
//            window.setBackgroundDrawable(background);
//        }*/
//    }
//
//    public static String getProfile_image_path() {
//        return profile_image_path;
//    }
//
//    public static String getProfile_video_path() {
//        return profile_video_path;
//    }
//
//    private void makeDirectory()
//    {
//        File directory = null;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//        {
//            directory = new File(Environment.getExternalStorageDirectory() + File.separator + APP_NAME);
//            if (!directory.exists())
//                directory.mkdirs();
//        }
//        else
//        {
//            directory = getApplicationContext().getDir(APP_NAME, Context.MODE_PRIVATE);
//            if (!directory.exists())
//                directory.mkdirs();
//        }
//
//        if (directory != null)
//        {
//            File profile_image = new File(directory + File.separator + "Profile");
//            File profile_video = new File(directory + File.separator + "Video");
//            File audio_path_coo = new File(directory + File.separator + "Audio");
//
//            if (!profile_image.exists())
//                profile_image.mkdirs();
//
//            if (!profile_video.exists())
//                profile_video.mkdirs();
//
//            if (!audio_path_coo.exists())
//                audio_path_coo.mkdirs();
//
//            profile_image_path = directory + File.separator + "Profile";
//            profile_video_path= directory + File.separator + "Video";
//            audio_path = directory + File.separator + "Audio";
//        }
//    }
//
//    public static String getTimeAgo(long time, Context context)
//    {
//        final int SECOND_MILLIS = 1000;
//        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
//        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
//        final int DAY_MILLIS = 24 * HOUR_MILLIS;
//        if (time < 1000000000000L) {
//            time *= 1000;
//        }
//        long now = System.currentTimeMillis();
//        if (time > now || time <= 0) {
//            return context.getString(R.string.time_just_now);
//        }
//        // TODO: localize
//        final long diff = now - time;
//        if (diff < MINUTE_MILLIS) {
//            return context.getString(R.string.time_just_now);
//        } else if (diff < 2 * MINUTE_MILLIS) {
//            return context.getString(R.string.time_minute_ago);
//        } else if (diff < 50 * MINUTE_MILLIS) {
//            return diff / MINUTE_MILLIS + " "+context.getString(R.string.time_min_ago);
//        } else if (diff < 90 * MINUTE_MILLIS) {
//            return context.getString(R.string.time_an_hr_ago);
//        } else if (diff < 24 * HOUR_MILLIS) {
//            return diff / HOUR_MILLIS + " "+context.getString(R.string.time_hr_ago);
//        } else if (diff < 48 * HOUR_MILLIS) {
//            return context.getString(R.string.time_yesterday);
//        } else {
//            return diff / DAY_MILLIS +" "+context.getString(R.string.time_day_ago);}
//    }
//
//    public static void showPermissionDialogNotShowing(Context context)
//    {
//        Dialog.createSimpleErrorDialog(context,context.getString(R.string.pop_up_permission_denied));
//    }
//
//    public static DatePickerDialog.OnDateSetListener date_picker(final TextView textView, final java.util.Calendar myCalendar) {
//        final DatePickerDialog.OnDateSetListener datee;
//        datee = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                myCalendar.set(java.util.Calendar.YEAR,year);
//                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
////                myCalendar.set(java.util.Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);
//                String myFormat = "yyyy-MM-dd";
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
//                textView.setText(sdf.format(myCalendar.getTime()));
//            }
//        };
//        return datee;
//    }
//
//    public static boolean validateTime(Context mContext, int hour, int minute, String date, String error_message){
//        Calendar temp= Calendar.getInstance();
//        temp.set(Calendar.HOUR_OF_DAY,hour);
//        temp.set(Calendar.MINUTE,minute);
//
//        Calendar c = Calendar.getInstance();
//        String myFormat = "yyyy-MM-dd";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
//        String today_Date=sdf.format(c.getTime());
//
//        DialogFactory.showLog("Todays date","Todays date-- "+today_Date);
//        DialogFactory.showLog("Comes date","Comes date-- "+date);
//
//        if(date.equals(today_Date))
//        {
//            if(temp.before(GregorianCalendar.getInstance())) {
//                DialogFactory.showToast(mContext, error_message);
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static boolean validateSameDatesTime(Context mContext, String start_time, String end_time, String error_message) {
//
//        String pattern = "HH:mm";
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//
//        DialogFactory.showLog("start_time","start_time-- "+start_time);
//        DialogFactory.showLog("end_time","end_time-- "+end_time);
//
//        try
//        {
//            Date date1 = sdf.parse(start_time);
//            Date date2 = sdf.parse(end_time);
//            if (date1.before(date2)) {
//                return true;
//            }
//            else
//                {
//                DialogFactory.showToast(mContext, error_message);
//                return false;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static boolean isDateInBetweenIncludingEndPoints(final String from_D, final String to_d, String dt)
//    {
//        String pattern = "yyyy-MM-dd";
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        try
//        {
//            Date min = sdf.parse(from_D);
//            Date max = sdf.parse(to_d);
//            Date date = sdf.parse(dt);
//
//            DialogFactory.showLog("min","min-- "+from_D);
//            DialogFactory.showLog("max","max-- "+to_d);
//            DialogFactory.showLog("come_","come_-- "+dt);
//            return !(date.before(min) || date.after(max));
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            DialogFactory.showLog("isDateInBetweenIncludingEndPoints Exception ","isDateInBetweenIncludingEndPoints Exception --"+e.toString()+"   "+e.getMessage());
//        }
//        return false;
//    }
//}
