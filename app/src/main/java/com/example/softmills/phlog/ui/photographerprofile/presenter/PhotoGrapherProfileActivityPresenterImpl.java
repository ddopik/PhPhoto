package com.example.softmills.phlog.ui.photographerprofile.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.softmills.phlog.Utiltes.PrefUtils;
import com.example.softmills.phlog.network.BaseNetworkApi;
import com.example.softmills.phlog.ui.photographerprofile.view.PhotoGrapherProfileActivityView;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoGrapherProfileActivityPresenterImpl implements PhotoGrapherProfileActivityPresenter {

    private static String TAG = PhotoGrapherProfileActivityPresenterImpl.class.getSimpleName();

    private PhotoGrapherProfileActivityView photoGrapherProfileActivityView;
    private Context context;

    public PhotoGrapherProfileActivityPresenterImpl(Context context,PhotoGrapherProfileActivityView photoGrapherProfileActivityView) {
        this.photoGrapherProfileActivityView = photoGrapherProfileActivityView;
        this.context=context;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getPhotoGrapherProfileData() {
        BaseNetworkApi.getProfileInfo(PrefUtils.getUserToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(profilePhotoGrapherInfoResponse -> {
                    if (profilePhotoGrapherInfoResponse.state.equals(BaseNetworkApi.STATUS_OK)) {
                        photoGrapherProfileActivityView.showPhotoGrapherProfileData(profilePhotoGrapherInfoResponse.data);
                    } else {
                        photoGrapherProfileActivityView.showMessage("Couldn't get profile data");
                        Log.e(TAG, "getPhotoGrapherProfileData() --->" + profilePhotoGrapherInfoResponse.state);
                    }
                }, throwable -> {
                    Log.e(TAG, "getPhotoGrapherProfileData() --->" + throwable.getMessage());
                });

    }
}