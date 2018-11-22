package com.example.softmills.phlog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.base.BaseActivity;
import com.example.softmills.phlog.ui.campaigns.CampaignsFragment;
import com.example.softmills.phlog.ui.photographerprofile.view.PhotoGraphedProfileFragment;
import com.example.softmills.phlog.ui.social.view.SocialFragment;
import com.example.softmills.phlog.ui.uploadimage.view.GalleryImageFragment;

/**
 * Created by abdalla_maged on 10/1/2018.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView toolBarTitle;
    private BottomAppBar bottomNavigation;
    private Button homeBrn, campaignBtn, notificationBtn, myProfileBtn;
    private FloatingActionButton picImgHomeBtn;
    private Toolbar toolbar;
    private ImageButton backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.phlog_toolbar);
        super.setSupportActionBar(toolbar);
        initView();
        initPresenter();
        initListener();
        homeNavigateHelper(Constants.NavigationHelper.HOME);
     }

    @Override
    public void initView() {

        toolBarTitle=findViewById(R.id.toolbar_title);
        backBtn=findViewById(R.id.back_btn);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        homeBrn = findViewById(R.id.navigation_home);
        campaignBtn = findViewById(R.id.navigation_missions);
        myProfileBtn = findViewById(R.id.navigation_profile);
        notificationBtn = findViewById(R.id.navigation_notification);
        picImgHomeBtn = findViewById(R.id.pic_img_home_btn);

    }

    @Override
    public void initPresenter() {
        bottomNavigation.setOnClickListener(this);
        homeBrn.setOnClickListener(this);
        campaignBtn.setOnClickListener(this);
        myProfileBtn.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);
        picImgHomeBtn.setOnClickListener(this);
    }

    private void initListener() {
        backBtn.setOnClickListener((view)->{
//            homeNavigateHelper(Constants.NavigationHelper.HOME);
            onBackPressed();
        });
    }
    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.navigation_home:
                homeNavigateHelper(Constants.NavigationHelper.HOME);
                break;
            case R.id.navigation_missions:
                homeNavigateHelper(Constants.NavigationHelper.CAMPAIGN);
                break;
            case R.id.navigation_notification:
                homeNavigateHelper(Constants.NavigationHelper.NOTIFICATION);
                break;
            case R.id.navigation_profile:
                homeNavigateHelper(Constants.NavigationHelper.PROFILE);
                break;
            case R.id.pic_img_home_btn:
                homeNavigateHelper(Constants.NavigationHelper.UPLOAD_PHOTO);
                break;
            default:
        }
    }

    private void clearSelected() {

        int homeBrnImg_off = R.drawable.ic_tab_home_off;
        int campaignBtnImg_off = R.drawable.ic_tab_missions_off;
        int notificationBtnImg_off = R.drawable.ic_tab_notificatin_off;
        int myProfileBtnImg_off = R.drawable.ic_tab_profile_off;

        homeBrn.setTextColor(getResources().getColor(R.color.gray677078));
        homeBrn.setCompoundDrawablesWithIntrinsicBounds(0, homeBrnImg_off, 0, 0);
        homeBrn.setCompoundDrawablePadding(8);

        campaignBtn.setTextColor(getResources().getColor(R.color.gray677078));
        campaignBtn.setCompoundDrawablesWithIntrinsicBounds(0, campaignBtnImg_off, 0, 0);
        campaignBtn.setCompoundDrawablePadding(8);

        notificationBtn.setTextColor(getResources().getColor(R.color.gray677078));
        notificationBtn.setCompoundDrawablesWithIntrinsicBounds(0, notificationBtnImg_off, 0, 0);
        notificationBtn.setCompoundDrawablePadding(8);

        myProfileBtn.setTextColor(getResources().getColor(R.color.gray677078));
        myProfileBtn.setCompoundDrawablesWithIntrinsicBounds(0, myProfileBtnImg_off, 0, 0);
        myProfileBtn.setCompoundDrawablePadding(8);


        picImgHomeBtn.setImageResource(R.drawable.btn_upload_img);


//        homeBrn.setFocusableInTouchMode(false);
//        campaignBtn.setFocusableInTouchMode(false);
//        notificationBtn.setFocusableInTouchMode(false);
//        myProfileBtn.setFocusableInTouchMode(false);

    }

    public void homeNavigateHelper(Constants.NavigationHelper navigationHelper) {
        clearSelected();
        int homeBrnImg = R.drawable.ic_tab_home_on;
        int campaignBtnImg = R.drawable.ic_tab_missions_on;
        int notificationBtnImg = R.drawable.ic_tab_notificatin_on;
        int myProfileBtnImg = R.drawable.ic_tab_profile_on;

        switch (navigationHelper) {
            case HOME: {
                addFragment(R.id.view_container, new SocialFragment(), SocialFragment.class.getSimpleName(), true);
                homeBrn.setTextColor(getResources().getColor(R.color.text_input_color));
                homeBrn.setCompoundDrawablesWithIntrinsicBounds(0, homeBrnImg, 0, 0);
                homeBrn.setCompoundDrawablePadding(8);
                toolbar.setVisibility(View.GONE);
                break;
            }

            case CAMPAIGN: {
                addFragment(R.id.view_container, new CampaignsFragment(), CampaignsFragment.class.getSimpleName(), true);
                campaignBtn.setTextColor(getResources().getColor(R.color.text_input_color));
                campaignBtn.setCompoundDrawablesWithIntrinsicBounds(0, campaignBtnImg, 0, 0);
                campaignBtn.setCompoundDrawablePadding(8);
                toolbar.setVisibility(View.VISIBLE);
                toolBarTitle.setText(getResources().getString(R.string.campaigns));
                break;
            }
            case NOTIFICATION: {
                notificationBtn.setTextColor(getResources().getColor(R.color.text_input_color));
                notificationBtn.setCompoundDrawablesWithIntrinsicBounds(0, notificationBtnImg, 0, 0);
                notificationBtn.setCompoundDrawablePadding(8);
                break;
            }
            case UPLOAD_PHOTO: {
                addFragment(R.id.view_container, new GalleryImageFragment(), GalleryImageFragment.class.getSimpleName(), true);
                picImgHomeBtn.setImageResource(R.drawable.btn_upload_selected_img);
                toolbar.setVisibility(View.VISIBLE);
                toolBarTitle.setText(getResources().getString(R.string.upload_photo));
                break;
            }
            case PROFILE: {
                addFragment(R.id.view_container, new PhotoGraphedProfileFragment(), PhotoGraphedProfileFragment.class.getSimpleName(), true);
                myProfileBtn.setTextColor(getResources().getColor(R.color.text_input_color));
                myProfileBtn.setCompoundDrawablesWithIntrinsicBounds(0, myProfileBtnImg, 0, 0);
                myProfileBtn.setCompoundDrawablePadding(8);
                toolbar.setVisibility(View.VISIBLE);
                toolBarTitle.setText(getResources().getString(R.string.profile));
                break;
            }
        }

    }


}
