package com.example.softmills.phlog.ui.welcome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InitSlider {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public String deletedAt;
}
