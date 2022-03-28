package com.pagin.azul.retrofit;


/**
 * Created by mobulous2 on 16/3/17.
 */
public class ServiceProgressDialog
{
//    public boolean isShowing=false;
//    Activity activity;
//    public CustomDialog customDialog;
//    public ProgressDialog progressDialog;
//    public String text;
//    public ServiceProgressDialog(Activity activity) {
//        this.activity = activity;
//    }
//    public void showProgressDialog(boolean isloader)
//    {
//        if (isloader)
//        {
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setMessage("");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            isShowing=true;
//            if (!progressDialog.isShowing()) {
//                progressDialog.show();
//                isShowing = true;
//            }
//        }
//    }
//    public void showProgressDialog(String text)
//    {
//        progressDialog = new ProgressDialog(activity);
//        progressDialog.setMessage(text);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        isShowing=true;
//        if (!progressDialog.isShowing()) {
//            progressDialog.show();
//            isShowing=true;
//        }
//    }
//    public void showCustomProgressDialog(String text)
//    {
//        customDialog = CustomDialog.show(activity, text, true);
//        customDialog.show();
//        isShowing=true;
//    }
//    public void showCustomProgressDialog()
//    {
//        customDialog = CustomDialog.show(activity,"", true);
//        customDialog.show();
//        isShowing=true;
//    }
//    public void hideProgressDialog()
//    {
//        isShowing=false;
//        if (progressDialog!=null&&progressDialog.isShowing())
//        {   progressDialog.dismiss();   }
//        if (customDialog!=null && customDialog.isShowing())
//        {   customDialog.dismiss(); }
//    }
//
//    //    CustomDialog
//    public static class CustomDialog extends Dialog {
//        public CustomDialog(Context context) {
//            super(context);
//        }
//
//        public CustomDialog(Context context, int themeResId) {
//            super(context, themeResId);
//        }
//
//        public static CustomDialog show(Context context, String text, boolean cancelable) {
//            CustomDialog dialog = new CustomDialog(context);
////        dialog.setTitle(context.getResources().getString(R.string.loading));
//            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            LoaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.loader, null, false);
//            dialog.setContentView(binding.getRoot());
//             dialog.setCancelable(false);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
//
//            dialog.getWindow().getAttributes().gravity= Gravity.CENTER;
//            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//            lp.dimAmount=0.4f;
//            dialog.getWindow().setAttributes(lp);
//            //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//            dialog.show();
//            return dialog;
//        }
//    }

}