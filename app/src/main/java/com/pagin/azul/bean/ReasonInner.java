package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class ReasonInner {
    @SerializedName("reportReason")
    private String reportReason;

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
}
