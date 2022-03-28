package com.pagin.azul.match.socketFileUploader;

/**
 * Created by promatics on 2/23/2018.
 */

import org.json.JSONException;

public interface UploadFileMoreDataReqListener {
    void uploadChunck(int place, int percent);
    void err(JSONException e);
}
