package com.example.softmills.phlog.ui.search.view.profile.view;

import com.example.softmills.phlog.base.commonmodel.Photographer;
import com.example.softmills.phlog.ui.search.view.profile.model.ProfileSearchData;

import java.util.List;

/**
 * Created by abdalla_maged on 11/1/2018.
 */
public interface ProfileSearchFragmentView {

    void viewProfileSearchItems(ProfileSearchData profileSearchData);

    void viewProfileSearchProgress(Boolean state);

    void showMessage(String msg);
    void setNextPageUrl(String page);


}
