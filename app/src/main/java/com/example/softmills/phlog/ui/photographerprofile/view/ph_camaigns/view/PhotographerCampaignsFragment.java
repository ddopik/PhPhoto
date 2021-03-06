package com.example.softmills.phlog.ui.photographerprofile.view.ph_camaigns.view;
/**
 * Created by Abdalla_maged on 9/30/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.base.BaseFragment;
import com.example.softmills.phlog.base.commonmodel.Campaign;
import com.example.softmills.phlog.base.widgets.CustomRecyclerView;
import com.example.softmills.phlog.base.widgets.PagingController;
import com.example.softmills.phlog.ui.campaigns.inner.ui.CampaignInnerActivity;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_camaigns.presenter.FragmentPhotoGrapherCampaignsPresenter;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_camaigns.presenter.FragmentPhotoGrapherCampaignsPresenterImpl;

import java.util.ArrayList;
import java.util.List;


public class PhotographerCampaignsFragment extends BaseFragment implements FragmentPhotoGrapherCampaignsView {

    private View mainView;
    private List<Campaign> campaignList = new ArrayList<Campaign>();
    private photographerCampaignsAdapter photoGrapherCampaignsAdapter;
    private FragmentPhotoGrapherCampaignsPresenter photoGrapherCampaignsPresenter;
    private LinearLayoutManager mLayoutManager;
    private CustomRecyclerView campaignsRv;
    private PagingController pagingController;
    private String nextPageUrl = "1";
    private boolean isLoading;

    public static PhotographerCampaignsFragment getInstance() {

        return new PhotographerCampaignsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_photographer_campains, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initViews();
        initListener();
        if (becameVisible) {
            photoGrapherCampaignsPresenter.getPhotographerCampaigns(0);
        }
    }

    private boolean becameVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !becameVisible) {
            becameVisible = true;
            if (getView() != null)
                photoGrapherCampaignsPresenter.getPhotographerCampaigns(1);
        }
    }

    @Override
    protected void initPresenter() {
        photoGrapherCampaignsPresenter = new FragmentPhotoGrapherCampaignsPresenterImpl(getContext(), this);
    }

    @Override
    protected void initViews() {
        photoGrapherCampaignsAdapter = new photographerCampaignsAdapter(getContext(), campaignList);
        campaignsRv = mainView.findViewById(R.id.campaigns_rv);
        campaignsRv.setAdapter(photoGrapherCampaignsAdapter);


    }

    private void initListener() {


        ////// initial block works by forcing then next Api for Each ScrollTop
        // cause recycler listener won't work until mainView ported with items
        campaignsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) campaignsRv.getLayoutManager();
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                    if (firstVisibleItemPosition == 0) {
                        if (nextPageUrl != null) {
                            photoGrapherCampaignsPresenter.getPhotographerCampaigns(Integer.parseInt(nextPageUrl));
                        }

                    }
                }
            }
        });
        ////////////////

        pagingController = new PagingController(campaignsRv) {


            @Override
            protected void loadMoreItems() {
                photoGrapherCampaignsPresenter.getPhotographerCampaigns(Integer.parseInt(nextPageUrl));
            }

            @Override
            public boolean isLastPage() {

                if (nextPageUrl == null) {
                    return true;
                } else {
                    return false;
                }

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }


        };
        photoGrapherCampaignsAdapter.campaignLister = new photographerCampaignsAdapter.CampaignLister() {
            @Override
            public void onCampaignClicked(String campaignID) {
                Intent intent = new Intent(getContext(), CampaignInnerActivity.class);
                intent.putExtra(CampaignInnerActivity.CAMPAIGN_ID, campaignID);
                startActivity(intent);
            }

            @Override
            public void onCampaignJoinClicked(String campaignID) {
                photoGrapherCampaignsPresenter.joinCampaign(campaignID);
            }
        };
    }


    @Override
    public void showCampaigns(List<Campaign> campaignList) {
        this.campaignList.addAll(campaignList);
        photoGrapherCampaignsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String msg) {
        showToast(msg);
    }

    @Override
    public void setNextPageUrl(String page) {
        this.nextPageUrl = page;
    }

    @Override
    public void viewPhotoGrapherCampaignLoading(boolean state) {
        isLoading = state;
    }
}