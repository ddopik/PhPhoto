package com.example.softmills.phlog.ui.search.view.brand.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abdalla_maged on 10/31/2018.
 */
public class BrandSearchResponse {
    @SerializedName("data")
    @Expose
    public BrandSearchData data;

}
