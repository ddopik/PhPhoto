package com.example.softmills.phlog.ui.brand.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.ErrorUtils;
import com.example.softmills.phlog.network.BaseNetworkApi;
import com.example.softmills.phlog.ui.brand.view.BrandCampaignsView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by abdalla_maged On Dec,2018
 */
public class BrandCampaignsPresenterImpl implements BrandCampaignsPresenter {

    private static final String TAG = BrandCampaignsPresenterImpl.class.getSimpleName();
    private BrandCampaignsView brandCampaignsView;
    private Context context;

    public BrandCampaignsPresenterImpl(Context context, BrandCampaignsView brandCampaignsView) {
        this.brandCampaignsView = brandCampaignsView;
        this.context = context;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getBrandCampaigns(String brandId,String page) {
        brandCampaignsView.showAllCampaignProgress(true);
        BaseNetworkApi.getCampaignBrands(brandId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(campaignResponse -> {
                    brandCampaignsView.showAllCampaignProgress(false);
                    brandCampaignsView.viewAllCampaign(campaignResponse.data.data);
                    brandCampaignsView.showAllCampaignProgress(false);
                }, throwable -> {
                    brandCampaignsView.showAllCampaignProgress(false);
                    ErrorUtils.Companion.setError(context, TAG, throwable);
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void joinCampaign(String campaignID) {
        BaseNetworkApi.followCampaign(campaignID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(followCampaignResponse -> {
                    brandCampaignsView.showMessage(context.getResources().getString(R.string.campaign_followed));
                }, throwable -> {
                    ErrorUtils.Companion.setError(context, TAG, throwable);
                });
    }
}