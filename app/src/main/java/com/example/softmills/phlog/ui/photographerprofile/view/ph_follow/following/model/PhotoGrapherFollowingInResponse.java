package com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following.model;

import com.example.softmills.phlog.base.BaseApiResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abdalla_maged on 10/11/2018.
 */
public class PhotoGrapherFollowingInResponse extends BaseApiResponse {
    @SerializedName("data")
    @Expose
    public PhotoGrapherFollowingInData data;

}
