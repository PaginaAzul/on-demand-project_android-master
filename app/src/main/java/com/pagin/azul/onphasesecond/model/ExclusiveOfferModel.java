package com.pagin.azul.onphasesecond.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ExclusiveOfferModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;


    protected ExclusiveOfferModel(Parcel in) {
        status = in.readString();
        responseMessage = in.readString();
        data = in.createTypedArrayList(Datum.CREATOR);
    }

    public static final Creator<ExclusiveOfferModel> CREATOR = new Creator<ExclusiveOfferModel>() {
        @Override
        public ExclusiveOfferModel createFromParcel(Parcel in) {
            return new ExclusiveOfferModel(in);
        }

        @Override
        public ExclusiveOfferModel[] newArray(int size) {
            return new ExclusiveOfferModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(responseMessage);
        parcel.writeTypedList(data);
    }


    public static class SellerData implements Parcelable {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("totalRating")
        @Expose
        private Integer totalRating;
        @SerializedName("avgRating")
        @Expose
        private String avgRating;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("storeType")
        @Expose
        private String storeType;

        protected SellerData(Parcel in) {
            id = in.readString();
            if (in.readByte() == 0) {
                totalRating = null;
            } else {
                totalRating = in.readInt();
            }
            if (in.readByte() == 0) {
                avgRating = null;
            } else {
                avgRating = in.readString();
            }
            image = in.readString();
            address = in.readString();
            storeType = in.readString();
        }

        public static final Creator<SellerData> CREATOR = new Creator<SellerData>() {
            @Override
            public SellerData createFromParcel(Parcel in) {
                return new SellerData(in);
            }

            @Override
            public SellerData[] newArray(int size) {
                return new SellerData[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getTotalRating() {
            return totalRating;
        }

        public void setTotalRating(Integer totalRating) {
            this.totalRating = totalRating;
        }

        public String getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            if (totalRating == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(totalRating);
            }
            if (avgRating == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeString(avgRating);
            }
            parcel.writeString(image);
            parcel.writeString(address);
            parcel.writeString(storeType);
        }
    }

    public static class Datum implements Parcelable {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("resAndStoreId")
        @Expose
        private String resAndStoreId;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("sellerData")
        @Expose
        private SellerData sellerData;

        protected Datum(Parcel in) {
            id = in.readString();
            status = in.readString();
            image = in.readString();
            resAndStoreId = in.readString();
            createdAt = in.readString();
            updatedAt = in.readString();
            sellerData = in.readParcelable(SellerData.class.getClassLoader());
        }

        public static final Creator<Datum> CREATOR = new Creator<Datum>() {
            @Override
            public Datum createFromParcel(Parcel in) {
                return new Datum(in);
            }

            @Override
            public Datum[] newArray(int size) {
                return new Datum[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getResAndStoreId() {
            return resAndStoreId;
        }

        public void setResAndStoreId(String resAndStoreId) {
            this.resAndStoreId = resAndStoreId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public SellerData getSellerData() {
            return sellerData;
        }

        public void setSellerData(SellerData sellerData) {
            this.sellerData = sellerData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(status);
            parcel.writeString(image);
            parcel.writeString(resAndStoreId);
            parcel.writeString(createdAt);
            parcel.writeString(updatedAt);
            parcel.writeParcelable(sellerData, i);
        }
    }
}
