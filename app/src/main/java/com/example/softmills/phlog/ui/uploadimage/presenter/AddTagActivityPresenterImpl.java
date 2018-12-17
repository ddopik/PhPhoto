package com.example.softmills.phlog.ui.uploadimage.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.Utiltes.ErrorUtils;
import com.example.softmills.phlog.Utiltes.PrefUtils;
import com.example.softmills.phlog.base.commonmodel.Tag;
import com.example.softmills.phlog.network.BaseNetworkApi;
import com.example.softmills.phlog.ui.MainActivity;
import com.example.softmills.phlog.ui.uploadimage.view.AddTagActivityView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by abdalla_maged on 10/28/2018.
 */
public class AddTagActivityPresenterImpl implements AddTagActivityPresenter {
    private String TAG = AddTagActivityPresenterImpl.class.getSimpleName();
    private Context context;
    private AddTagActivityView addTagActivityView;

    public AddTagActivityPresenterImpl(Context context, AddTagActivityView addTagActivityView) {

        this.context = context;
        this.addTagActivityView = addTagActivityView;
    }

    @SuppressLint("CheckResult")
    @Override
    public void uploadPhoto(String imagePath, String imageCaption, String location, String draftState, HashMap<String, String> imageType, List<Tag> tagList) {
        addTagActivityView.viewUploadProgress(true);
        HashMap<String, String> tagsSelected = new HashMap<String, String>();
        for (int i = 0; i < tagList.size(); i++) {
            tagsSelected.put("tags[" + i + "]", tagList.get(i).name);
        }


        if (imageType.get(BaseNetworkApi.IMAGE_TYPE_CAMPAIGN) != null) {
//            --->user Uploading CampaignPhoto
            addTagActivityView.viewUploadProgress(true);
            BaseNetworkApi.uploadCampaignPhoto(PrefUtils.getUserToken(context), imageCaption, location, new File(imagePath), tagsSelected, imageType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uploadImgResponse -> {
                        addTagActivityView.viewUploadProgress(false);
                        addTagActivityView.viewMessage(context.getResources().getString(R.string.photo_uploaded));
                    }, throwable -> {
                        ErrorUtils.setError(context, TAG, throwable);
                        addTagActivityView.viewUploadProgress(false);
                    });

        } else if (imageType.get(BaseNetworkApi.IMAGE_TYPE_PROFILE) != null) {
            //user is uploading Profile Photo
            addTagActivityView.viewUploadProgress(true);
            BaseNetworkApi.uploadProfileImg(new File(imagePath))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uploadProfileImgResponse -> {
                        addTagActivityView.viewUploadProgress(false);
                        MainActivity.navigationManger.navigate(Constants.NavigationHelper.PROFILE);
                    }, throwable -> {
                        ErrorUtils.setError(context, TAG, throwable);
                        addTagActivityView.viewUploadProgress(false);
                    });

        } else {
            //user is uploading Normal Photo
            addTagActivityView.viewUploadProgress(true);
            BaseNetworkApi.uploadPhotoGrapherPhoto(PrefUtils.getUserToken(context), imageCaption, location, new File(imagePath), tagsSelected)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(uploadImgResponse -> {
                        addTagActivityView.viewUploadProgress(false);
                        addTagActivityView.viewMessage(context.getResources().getString(R.string.photo_uploaded));
                    }, throwable -> {
                        ErrorUtils.setError(context, TAG, throwable);
                        addTagActivityView.viewUploadProgress(false);
                    });
        }


    }


    private void uploadPhotoGrapherPhoto() {

    }

}
