package com.pagin.azul.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.ChatResponse;
import com.pagin.azul.bean.MessageChat;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.match.socketFileUploader.FileUploadManager;
import com.pagin.azul.match.socketFileUploader.UploadFileMoreDataReqListener;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.TakeImage;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kProfile;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;


public class NUMessageDeliveryPersonActivity extends AppCompatActivity {
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    private final String SEND_MEDIA = "MEDIA";
    public static final int IMAGE_FILE = 5;
    private HashMap<String, String> media_path = new HashMap<>();
    private String msgType = "";
    private Socket socket;
    private Socket mSocket;
    private ArrayList<MessageChat> MessageList;


    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.btn_send)
    ImageView btn_send;
    @BindView(R.id.btn_attachment)
    ImageView btn_attachment;
    @BindView(R.id.rv_Chat_list)
    RecyclerView rv_Chat_list;
    boolean isFirstTime;
    @BindView(R.id.edt_typetxt)
    EditText edt_typetxt;
    private NormalUserPendingOrderInner getData;
    private MessageAdapter messageAdapter;


    public static Intent getIntent(Context context, NormalUserPendingOrderInner getData, String commingFrom) {
        Intent intent = new Intent(context, NUMessageDeliveryPersonActivity.class);
        intent.putExtra("from", commingFrom);
        intent.putExtra("kData", getData);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numessage_delivery_person);
        ButterKnife.bind(this);

        MessageList = new ArrayList<MessageChat>();

        if (getIntent() != null) {
            if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromNormal")) {
                getData = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");

            } else if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromDeliveryDash")) {
                getData = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");

            } else if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromNUPrf")) {
                getData = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            } else if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromPrfDashBoard")) {
                getData = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");

            }
        }
        Uri uri = Uri.parse("https://res.cloudinary.com/boss8055/image/upload/v1558617086/ikrm9elx95eayq2zjflv.jpg");
        callChatHistoryApi();
        connectSockettest();
        setUpRecyclerView();

        generateImageFromPdf(uri);
    }


    @OnClick({R.id.btn_send, R.id.btn_back, R.id.btn_attachment})
    void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_send):
                if (!edt_typetxt.getText().toString().trim().isEmpty()) {
                    if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromNormal")) {
                        sendMessage(getData.getOfferAcceptedOfId());
                    } else if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromDeliveryDash")) {
                        sendMessage(getData.getOfferAcceptedById());
                    } else if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromPrfDashBoard")) {
                        sendMessage(getData.getOfferAcceptedById());
                    } else if (getIntent().getSerializableExtra("from").toString().equalsIgnoreCase("FromNUPrf")) {
                        sendMessage(getData.getOfferAcceptedOfId());
                    }
                } else {
                    Toast.makeText(this, "Please type something", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_back:
                try {
                    JSONObject object = new JSONObject();
                    object.put("roomId", getData.getRoomId());
                    object.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                    mSocket.emit("room leave", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
                break;


            case R.id.btn_attachment:
                bottomSheetOpen();
                break;
        }
    }


    private void bottomSheetOpen() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_track_screen_layout, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView tv_cancel = sheetView.findViewById(R.id.tv_cancel);
        TextView tv_issue_bill = sheetView.findViewById(R.id.tv_issue_bill);
        TextView tv_share_img = sheetView.findViewById(R.id.tv_share_img);
        TextView tv_share_loc = sheetView.findViewById(R.id.tv_share_loc);

       // initView();


        tv_share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                bottomSheetDialog.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        runOnUiThread(() -> {
//            final Handler handler = new Handler();
//            handler.postDelayed(() -> {
//                // getHistory();
//
//                //add your code here
//            }, 2000);
//
//        });
    }

    //send message
    private void sendMessage(String receiverId) {

        try {
            JSONObject obj = new JSONObject();

            obj.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            obj.put("receiverId", receiverId);
            obj.put("roomId", getData.getRoomId());
            obj.put("message", edt_typetxt.getText().toString().trim());
            obj.put("messageType", "Text");
            obj.put("profilePic", SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kProfile));
            mSocket.emit("message", obj);

            edt_typetxt.setText("");
            msgType = "";
            msgType = "Text";


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void connectSockettest() {
        MyDialog.getInstance(NUMessageDeliveryPersonActivity.this).hideDialog();
        try {
            mSocket = IO.socket("http://18.217.0.63:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("room join", onLogin);

        mSocket.on("message", onNewMessage);

        mSocket.connect();

        try {
            JSONObject object = new JSONObject();
            object.put("roomId", getData.getRoomId());
            object.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            mSocket.emit("room join", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(NUMessageDeliveryPersonActivity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();

//                    for (int jj = 0; jj < args.length; jj++) {
//                        //DialogFactory.showLog("onLogin", "onLogin-- " + args[jj]);
//
//                    }
                }
            });
        }
    };

    // TODO: 25/1/18 messagechecking
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //MessageList.clear();
                    JSONObject data = (JSONObject) args[0];
                    String rooomId;
                    String msg;
                    String receiverId;
                    String senderId;
                    String msgType;
                    String time;
                    String media;
                    String profilePic;
                    String createdAt;

                    try {
                        rooomId = data.getString("roomId");
                        msg = data.getString("message");
                        receiverId = data.getString("receiverId");
                        senderId = data.getString("senderId");
                        msgType = data.getString("messageType");
                        profilePic = data.getString("profilePic");
                        createdAt = data.getString("createdAt");

                        if (msgType.equalsIgnoreCase("Text")) {
                            MessageList.add(new MessageChat(senderId, receiverId, msg, "Text", "", createdAt, profilePic,""));
                            rv_Chat_list.setLayoutManager(new LinearLayoutManager(NUMessageDeliveryPersonActivity.this));
                            messageAdapter = new MessageAdapter(MessageList, NUMessageDeliveryPersonActivity.this);
                            rv_Chat_list.setAdapter(messageAdapter);
                            rv_Chat_list.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                        } else if (msgType.equalsIgnoreCase("Media")) {
                            media = data.getString("media");
                            MessageList.add(new MessageChat(senderId, receiverId, "", "Media", media, createdAt, profilePic,""));
                            rv_Chat_list.setLayoutManager(new LinearLayoutManager(NUMessageDeliveryPersonActivity.this));
                            messageAdapter = new MessageAdapter(MessageList, NUMessageDeliveryPersonActivity.this);
                            rv_Chat_list.setAdapter(messageAdapter);
                            rv_Chat_list.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                        }

                        messageAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            });
        }
    };

    private void scrollToBottom() {
        rv_Chat_list.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnect = args -> runOnUiThread(() -> {
        //DialogFactory.showToast(getApplicationContext(), getString(R.string.connected));
        // Toast.makeText(NUMessageDeliveryPersonActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    });

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //DialogFactory.showToast(getApplicationContext(), getString(R.string.disconnected));
                    Toast.makeText(NUMessageDeliveryPersonActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // DialogFactory.showLog("ERROR CONNECT", "ERROR CONNECT");
                    Toast.makeText(NUMessageDeliveryPersonActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    private void setUpRecyclerView() {
        rv_Chat_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_Chat_list.setAdapter(messageAdapter);
    }


    private void callChatHistoryApi() {
        try {
            MyDialog.getInstance(NUMessageDeliveryPersonActivity.this).showDialog(NUMessageDeliveryPersonActivity.this);
            String token = SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kUserId));
                jsonObject.put("roomId", getData.getRoomId());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ChatResponse> beanCall = apiInterface.getChatHistory(token, body);

                beanCall.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        MyDialog.getInstance(NUMessageDeliveryPersonActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ChatResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    MessageList = response.body().getDataList();
                                    rv_Chat_list.setLayoutManager(new LinearLayoutManager(NUMessageDeliveryPersonActivity.this));
                                    messageAdapter = new MessageAdapter(MessageList, NUMessageDeliveryPersonActivity.this);
                                    rv_Chat_list.setAdapter(messageAdapter);
                                    messageAdapter.notifyDataSetChanged();

                                    no_data.setVisibility(View.GONE);
                                    rv_Chat_list.setVisibility(View.VISIBLE);

                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
//                                    recyler_view_comments.smoothScrollToPosition(main_data.size()-1);
                                            rv_Chat_list.scrollToPosition(MessageList.size() - 1);
                                            rv_Chat_list.setVisibility(View.VISIBLE);
                                            no_data.setVisibility(View.GONE);

                                        }
                                    });


                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    rv_Chat_list.setVisibility(View.GONE);
                                }
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NUMessageDeliveryPersonActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //////IMage Send//////////////

    public void sendAndGetBinaryData(String path, int type) {
        String uni_code = String.valueOf(System.currentTimeMillis());

        if (type == IMAGE_FILE) {
            MessageList.add(new MessageChat(SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kUserId), "", "", imagePath, "", "Just now", "User",""));
        }
        messageAdapter.notifyDataSetChanged();
        //scrollToBottom();
        media_path.put(uni_code, path);
        if (media_path.size() == 1) {
            uploadFileOnServer(media_path);
        }
    }


    private void uploadFileOnServer(HashMap<String, String> map) {
        if (map.size() > 0) {
            for (String entry : map.keySet()) {
                String key = entry;
                String value = map.get(key);
                // new FileUploadTask(value, key).execute();//Value ==== Media Path, key========Unicode
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new FileUploadTask(value, key).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

                break;
            }
        }
    }


    private class FileUploadTask extends AsyncTask<String, Integer, String> {
        private String file_path = "";
        private String room_id = "";
        private String receiver_id = "";
        private String attachment_type = SEND_MEDIA;
        private String uni_code = "";

        private UploadFileMoreDataReqListener callback;
        private FileUploadManager mFileUploadManager;

        public FileUploadTask(String file_path, String uni_code) {
            this.file_path = file_path;
            room_id = getData.getRoomId();
            receiver_id = SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kUserId);
            this.uni_code = uni_code;
            Log.e("path uni_code", "path uni_code-- " + file_path);

            attachment_type = SEND_MEDIA;

        }

        @Override
        protected void onPreExecute() {
            Log.e("mFileUploadManager", "in it mFileUploadManager for-- " + file_path);
            mFileUploadManager = new FileUploadManager();
        }

        @Override
        protected String doInBackground(String... params) {
            boolean isSuccess = mSocket.connected();
            if (isSuccess) {
                mFileUploadManager.prepare(file_path, NUMessageDeliveryPersonActivity.this);

                // This function gets callback when server requests more data
                setUploadFileMoreDataReqListener(mUploadFileMoreDataReqListener);
                // This function will get a call back when upload completes
                setUploadFileCompleteListener();
                // Tell server we are ready to start uploading ..
                if (mSocket.connected()) {
                    JSONArray jsonArr = new JSONArray();
                    JSONObject res = new JSONObject();
                    try {
                        res.put("Name", mFileUploadManager.getFileName());
                        res.put("Size", mFileUploadManager.getFileSize());
                        res.put("room_id", room_id);
                        res.put("message", room_id);
                        jsonArr.put(res);
                        mSocket.emit("uploadFileStart", jsonArr);
                    } catch (JSONException e) {
                        //TODO: Log errors some where..
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Log.e("onPostExecute result-- ", "onPostExecute result--");
            if (s == null) {
                return;
            }
            if (s.equalsIgnoreCase("OK")) {
                media_path.remove(uni_code);
                mFileUploadManager.close();
                mSocket.off("uploadFileMoreDataReq", uploadFileMoreDataReq);
                mSocket.off("uploadFileCompleteRes", onCompletedddd);
                uploadFileOnServer(media_path);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // super.onProgressUpdate(values);
            if (values[0] > 107) {
                if (media_path.containsKey(uni_code)) {
                    onPostExecute("OK");
                }
            }
        }


        private UploadFileMoreDataReqListener mUploadFileMoreDataReqListener = new UploadFileMoreDataReqListener() {
            @Override
            public void uploadChunck(int offset, int percent) {
                Log.e("CHAT_ACTIVITY", String.format("Uploading %d completed. offset at: %d", percent, offset));
                // Read the next chunk
                mFileUploadManager.read(offset);
                if (mSocket.connected()) {
                    JSONArray jsonArr = new JSONArray();
                    JSONObject res = new JSONObject();
                    try {
                        res.put("Name", mFileUploadManager.getFileName());
                        res.put("Data", mFileUploadManager.getData());
                        res.put("chunkSize", mFileUploadManager.getBytesRead());
                        res.put("room_id", room_id);
                        res.put("sender_id", SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kUserId));
                        res.put("receiver_id", receiver_id);
                        res.put("message", getString(R.string.read_attachement));
                        res.put("messageType", "Media");
                        res.put("attachment_type", attachment_type);
                        res.put("profilePic", SharedPreferenceWriter.getInstance(NUMessageDeliveryPersonActivity.this).getString(kProfile));
                        jsonArr.put(res);
                        // This will trigger server 'uploadFileChuncks' function
                        mSocket.emit("uploadFileChuncks", jsonArr);

                        msgType = "";
                        msgType = "Media";
                    } catch (JSONException e) {
                        //TODO: Log errors some where..
                    }
                }
            }

            @Override
            public void err(JSONException e) {
                // TODO Auto-generated method stub
            }
        };

        Emitter.Listener uploadFileMoreDataReq = new Emitter.Listener() {
            @SuppressLint("LongLogTag")
            @Override
            public void call(Object... args) {
                for (int jj = 0; jj < args.length; jj++) {
                    Log.e("setUploadFileMoreDataReqListener", "setUploadFileMoreDataReqListener-- " + args[jj]);
                }
                try {
                    JSONObject json_data = (JSONObject) args[0];
                    int place = json_data.getInt("Place");
                    int percent = json_data.getInt("Percent");
                    publishProgress(json_data.getInt("Percent"));
                    callback.uploadChunck(place, percent);

                } catch (JSONException e) {
                    callback.err(e);
                }
            }
        };


        Emitter.Listener onCompletedddd = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json_data = (JSONObject) args[0];
                if (json_data.has("IsSuccess")) {
                    publishProgress(110);
                    return;
                }
            }
        };

        private void setUploadFileMoreDataReqListener(final UploadFileMoreDataReqListener callbackk) {
            callback = callbackk;
            mSocket.on("uploadFileMoreDataReq", uploadFileMoreDataReq);
        }

        private void setUploadFileCompleteListener() {
            mSocket.on("uploadFileCompleteRes", onCompletedddd);
        }
    }

    //PdfiumAndroid (https://github.com/barteksc/PdfiumAndroid)
//https://github.com/barteksc/AndroidPdfViewer/issues/49
    void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            e.printStackTrace();
            //todo with exception
        }
    }


    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";

    private void saveImage(Bitmap bmp) {
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(folder, "PDF.png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            //todo with exception
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }
    }


    public void selectImage() {
        final CharSequence[] items = {
//                getResources().getString(R.string.Take_Photo),
//                getResources().getString(R.string.Choose_from_Library),
                "take photo", "take libarary",
                getResources().getString(R.string.cancel)};

        final Dialog dialog = new Dialog(this, R.style.MyDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_imagecapture);


        TextView txt_takephoto = (TextView) dialog.findViewById(R.id.txt_takephoto);
        TextView txt_choosefromlibrary = (TextView) dialog.findViewById(R.id.txt_choosefromgallery);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_button);
        txt_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NUMessageDeliveryPersonActivity.this, TakeImage.class);
                intent.putExtra("from", "camera");
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                dialog.dismiss();
            }
        });
        txt_choosefromlibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NUMessageDeliveryPersonActivity.this, TakeImage.class);
                intent.putExtra("from", "gallery");
                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (resultCode == RESULT_OK) {
            if (data.getStringExtra("filePath") != null) {
                imagePath = data.getStringExtra("filePath");
                sendAndGetBinaryData(imagePath, IMAGE_FILE);
                fileFlyer = new File(data.getStringExtra("filePath"));

                if (fileFlyer.exists() && fileFlyer != null) {
                    // ivMyProfile.setImageURI(Uri.fromFile(fileFlyer));
                }
            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("message", onNewMessage);
        mSocket.off("room join", onLogin);
    }
}
