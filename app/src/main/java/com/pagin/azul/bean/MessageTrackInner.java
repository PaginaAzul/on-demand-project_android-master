package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageTrackInner implements Serializable {
    @SerializedName("invoiceStatus")
    private String invoiceStatus;
    @SerializedName("arrivedStatus")
    private String arrivedStatus;
    @SerializedName("workDoneStatus")
    private String workDoneStatus;
    @SerializedName("goStatus")
    private String goStatus;
    @SerializedName("_id")
    private String _id;
    @SerializedName("roomId")
    private String roomId;
    @SerializedName("invoicePdf")
    private String invoicePdf;
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoicePdf() {
        return invoicePdf;
    }

    public void setInvoicePdf(String invoicePdf) {
        this.invoicePdf = invoicePdf;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getArrivedStatus() {
        return arrivedStatus;
    }

    public void setArrivedStatus(String arrivedStatus) {
        this.arrivedStatus = arrivedStatus;
    }

    public String getWorkDoneStatus() {
        return workDoneStatus;
    }

    public void setWorkDoneStatus(String workDoneStatus) {
        this.workDoneStatus = workDoneStatus;
    }

    public String getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(String goStatus) {
        this.goStatus = goStatus;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
