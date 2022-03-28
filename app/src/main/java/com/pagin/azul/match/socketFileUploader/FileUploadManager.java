package com.pagin.azul.match.socketFileUploader;

/**
 * Created by promatics on 2/23/2018.
 */

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FileUploadManager
{
    private static final String TAG = "FileUploadManager";

    private long mFileSize;
    private File mFile;
    private BufferedInputStream stream;
    private String mData;
    private int mBytesRead;

    public boolean prepare(String fullFilePath, Context context) {
        mFile = new File(fullFilePath);
        mFileSize = mFile.length();
        try
        {    FileInputStream fileInputStream= new FileInputStream(mFile);
             stream = new BufferedInputStream(fileInputStream);
            Log.e(TAG, "stream stream:-- ");
        }
        catch (FileNotFoundException e) {
           Log.e(TAG, "stream prepare err");
        }
        return true;
    }

    public String getFileName() {
        return mFile.getName();
    }

    public long getFileSize() {
        return mFileSize;
    }

    public long getBytesRead() {
        return mBytesRead;
    }

    public String getData() {
        return mData;
    }

    public void read(int byteOffset)
    {
        try {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            mBytesRead = stream.read(buffer);
            byteBuffer.write(buffer, 0, mBytesRead);
            Log.e(TAG, "Read :" + mBytesRead);
            mData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    public void close()
    {
        if (stream != null)
        {
            try
            {
                mFile.deleteOnExit();
                stream.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            stream = null;
        }
    }
}
