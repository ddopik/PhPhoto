package com.example.softmills.phlog.ui.campaigns.inner.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.softmills.phlog.Utiltes.ErrorUtils;
import com.example.softmills.phlog.Utiltes.PrefUtils;
import com.example.softmills.phlog.Utiltes.Utilities;
import com.example.softmills.phlog.network.BaseNetworkApi;
import com.example.softmills.phlog.ui.campaigns.inner.ui.CampaignInnerPhotosFragmentView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by abdalla_maged on 10/8/2018.
 */
public class CampaignInnerPhotosFragmentPresenterImpl implements CampaignInnerPhotosFragmentPresenter {


    private String TAG = CampaignInnerPhotosFragmentPresenterImpl.class.getSimpleName();
    private Context context;
    private CampaignInnerPhotosFragmentView campaignInnerPhotosFragmentView;

    public CampaignInnerPhotosFragmentPresenterImpl(Context context, CampaignInnerPhotosFragmentView campaignInnerPhotosFragmentView) {
        this.context = context;
        this.campaignInnerPhotosFragmentView = campaignInnerPhotosFragmentView;

    }

    @SuppressLint("CheckResult")
    @Override
    public void getCampaignInnerPhotos(String campaignID, int page) {
        campaignInnerPhotosFragmentView.viewCampaignInnerPhotosProgress(true);
        BaseNetworkApi
                .getCampaignInnerPhotos(PrefUtils.getUserToken(context), campaignID, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(campaignInnerPhotosResponse -> {
                    campaignInnerPhotosFragmentView.viewCampaignInnerPhotosProgress(false);
                    campaignInnerPhotosFragmentView.getInnerCampaignPhotos(campaignInnerPhotosResponse.data.data);
                    if (campaignInnerPhotosResponse.data.nextPageUrl != null) {
                        campaignInnerPhotosFragmentView.setNextPageUrl(Utilities.getNextPageNumber(context, campaignInnerPhotosResponse.data.nextPageUrl));

                    } else {
                        campaignInnerPhotosFragmentView.setNextPageUrl(null);
                    }

                }, throwable -> {
                    campaignInnerPhotosFragmentView.viewCampaignInnerPhotosProgress(false);
                    ErrorUtils.Companion.setError(context, TAG, throwable);
                });


    }



}
