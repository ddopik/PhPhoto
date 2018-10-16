package com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.base.BaseFragment;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.album.PhotoGrapherAlbumFragment;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.brand.PhotoGrapherBrandFragment;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following.view.PhotoGrapherFollowViewPagerAdapter;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following.view.PhotoGrapherFollowingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdalla_maged on 9/30/2018.
 */
public class PhotoGrapherFollowFragment extends BaseFragment {

    private View mainView;
    private TabLayout followTableLayout;
    private ViewPager followViewPager;
    private PhotoGrapherFollowViewPagerAdapter photoGrapherFollowViewPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_photographer_follow, container, false);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initViews();

    }




    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {

        followTableLayout = mainView.findViewById(R.id.photographer_follow_tabs);
        followViewPager = mainView.findViewById(R.id.photographer_follow_pager);
        photoGrapherFollowViewPagerAdapter = new PhotoGrapherFollowViewPagerAdapter(getChildFragmentManager(), getFragmentList());
        followViewPager.setAdapter(photoGrapherFollowViewPagerAdapter);
        followTableLayout.setupWithViewPager(followViewPager);

        followTableLayout.setupWithViewPager(followViewPager);
//        for (int i = 0; i < followTableLayout.getTabCount(); i++) {
            followTableLayout.getTabAt(0).setIcon(R.drawable.tab_follow_album);
            followTableLayout.getTabAt(1).setIcon(R.drawable.tab_follow_following);
            followTableLayout.getTabAt(2).setIcon(R.drawable.tab_follow_brand);
//        }
    }


    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(PhotoGrapherAlbumFragment.getInstance());
        fragmentList.add(PhotoGrapherFollowingFragment.getInstance());
        fragmentList.add(PhotoGrapherBrandFragment.getInstance());
        return fragmentList;
    }


}