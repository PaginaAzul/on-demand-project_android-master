package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class ProfilePicBean {
    @SerializedName("profilePic")
    private String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
