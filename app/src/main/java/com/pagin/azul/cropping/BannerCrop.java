//package com.mobu.jokar.cropping;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.media.ExifInterface;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//import com.isseiaoki.simplecropview.CropImageView;
//import com.mobu.jokar.R;
//import com.mobu.jokar.helper.Controller;
//import com.mobu.jokar.utils.TakeImage;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
///**
// * Created by promatics on 2/1/2018.
// */
//
//public class BannerCrop extends AppCompatActivity implements View.OnClickListener{
//
//    CropImageView cropImageView;
//    String imagePath;
//    public Button done,cancel;
//    public static final String defaultPath ="def";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_banner_crop);
//        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_image));
//        Controller.setStatusBarGradiant(this);
//
//        done=(Button)findViewById(R.id.done);
//        cancel=(Button)findViewById(R.id.cancel);
//        done.setOnClickListener(this);
//        cancel.setOnClickListener(this);
//        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
//        cropImageView.setMinFrameSizeInDp(200);
//        cancel.setVisibility(View.VISIBLE);
//        done.setVisibility(View.VISIBLE);
//
//        if (getIntent().hasExtra("imageUri"))
//        {
//            try
//            {
//                File image = new File(getIntent().getStringExtra("imageUri"));
//                ExifInterface exif = null;
//                try
//                {
//                    exif = new ExifInterface(getIntent().getStringExtra("imageUri"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                if (image.exists())
//                {
//                    Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("imageUri"));
//                    Bitmap bmRotated = rotateBitmap(bitmap,orientation);
//                    cropImageView.setImageBitmap(bmRotated);
//                    cropImageView.setOverlayColor(0xAA1C1C1C);
//                    cropImageView.setInitialFrameScale(1.0f);
//                    cropImageView.setMinFrameSizeInPx(550);
//                    cropImageView.setFrameColor(getResources().getColor(R.color.colorButton));
//                    cropImageView.setHandleColor(getResources().getColor(R.color.black));
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.done:
//                saveToInternalStorage(cropImageView.getCroppedBitmap(),102);
//                if (imagePath != null){
//                    Intent intent = new Intent();
//                    intent.putExtra("PATH",imagePath);
//                    setResult(TakeImage.CROP_IMAGE,intent);
//                    finish();
//                }
//                break;
//            case R.id.cancel:
//                onBackPressed();
//                break;
//        }
//    }
//
////    @Override
////    public void onBackPressed()
////    {
////        Intent intent = new Intent();
////        intent.putExtra("PATH", defaultPath);
////        setResult(RegisterTwo.CROP_IMAGE,intent);
////        finish();
////    }
//
//    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
//        ExifInterface ei = new ExifInterface(image_absolute_path);
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotate(bitmap, 90);
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotate(bitmap, 180);
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotate(bitmap, 270);
//
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                return flip(bitmap, true, false);
//
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
//
//            default:
//                return bitmap;
//        }
//    }
//
//    public static Bitmap rotate(Bitmap bitmap, float degrees) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degrees);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }
//
//    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
//        Matrix matrix = new Matrix();
//        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }
//
//    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
//
//        Matrix matrix = new Matrix();
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_NORMAL:
//                return bitmap;
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                matrix.setScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                matrix.setRotate(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                matrix.setRotate(180);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_TRANSPOSE:
//                matrix.setRotate(90);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                matrix.setRotate(90);
//                break;
//            case ExifInterface.ORIENTATION_TRANSVERSE:
//                matrix.setRotate(-90);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                matrix.setRotate(-90);
//                break;
//            default:
//                return bitmap;
//        }
//        try {
//            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            bitmap.recycle();
//            return bmRotated;
//        }
//        catch (OutOfMemoryError e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String saveToInternalStorage(Bitmap bitmapImage, int source)
//    {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File dir= new File(Controller.getProfile_image_path());//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        File mypath = null;
//        if (source == 102)
//        {
//            if (!dir.exists())
//                dir.mkdirs();
//
//            mypath=new File(dir,timeStamp+"post.png");
//        }
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 40, fos);
//            //bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        imagePath = mypath.getPath();
//        return dir.getAbsolutePath();
//    }
//}
//
