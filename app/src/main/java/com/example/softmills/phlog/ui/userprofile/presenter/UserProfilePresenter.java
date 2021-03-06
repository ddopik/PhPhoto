package com.example.softmills.phlog.ui.userprofile.presenter;

public interface UserProfilePresenter {

    void getUserProfileData(String userID);

    void getUserPhotos(String userID,int page);

    void followUser(String userId);
    void unFollowUser(String userID);
}
