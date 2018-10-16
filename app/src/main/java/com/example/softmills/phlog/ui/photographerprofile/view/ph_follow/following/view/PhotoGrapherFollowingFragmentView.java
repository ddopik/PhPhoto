package com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following.view;

import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following.model.PhotoGrapherFollowingObj;

import java.util.List;

/**
 * Created by abdalla_maged on 10/11/2018.
 */
public interface PhotoGrapherFollowingFragmentView {

    void viewPhotographerFollowingIn(List<PhotoGrapherFollowingObj> photoGrapherFollowingObjList);
    void viewPhotographerFollowingInProgress(boolean state);
    void viewMessage(String msg);
}