package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class CreateInvoiceInner {

    @SerializedName("invoiceStatus")
    private String invoiceStatus;
    @SerializedName("_id")
    private String _id;
    @SerializedName("invoicePdf")
    private String invoicePdf;
    @SerializedName("invoiceSubtoatl")
    private String invoiceSubtoatl;
    @SerializedName("invoiceTax")
    private String invoiceTax;
    @SerializedName("invoiceTotal")
    private String invoiceTotal;
    @SerializedName("invoiceImage")
    private String invoiceImage;
 @SerializedName("deliveryOffer")
    private String deliveryOffer;

  @SerializedName("tax")
    private String tax;

  @SerializedName("total")
    private String total;


    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDeliveryOffer() {
        return deliveryOffer;
    }

    public void setDeliveryOffer(String deliveryOffer) {
        this.deliveryOffer = deliveryOffer;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getInvoicePdf() {
        return invoicePdf;
    }

    public void setInvoicePdf(String invoicePdf) {
        this.invoicePdf = invoicePdf;
    }

    public String getInvoiceSubtoatl() {
        return invoiceSubtoatl;
    }

    public void setInvoiceSubtoatl(String invoiceSubtoatl) {
        this.invoiceSubtoatl = invoiceSubtoatl;
    }

    public String getInvoiceTax() {
        return invoiceTax;
    }

    public void setInvoiceTax(String invoiceTax) {
        this.invoiceTax = invoiceTax;
    }

    public String getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(String invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public String getInvoiceImage() {
        return invoiceImage;
    }

    public void setInvoiceImage(String invoiceImage) {
        this.invoiceImage = invoiceImage;
    }
}

