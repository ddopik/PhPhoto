package com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.brand.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.base.BaseFragment;
import com.example.softmills.phlog.base.commonmodel.Business;
import com.example.softmills.phlog.base.widgets.CustomRecyclerView;
import com.example.softmills.phlog.base.widgets.PagingController;
import com.example.softmills.phlog.ui.brand.view.BrandInnerActivity;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.brand.presenter.PhotoGrapherBrandPresenter;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.brand.presenter.PhotoGrapherBrandPresenterImpl;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;



/**
 * Created by abdalla_maged on 10/14/2018.
 */
public class PhotoGrapherBrandFragment extends BaseFragment implements PhotoGrapherBrandFragmentView {

    private View mainView;
    private CustomRecyclerView followingBrandRv;
    private ProgressBar photographerBrandSearchProgressBar;
    private EditText searchPhotographerBrand;
    private PhotoGrapherFollowingBrandAdapter photoGrapherFollowingBrandAdapter;
    private List<Business> photographerFollowingBrands = new ArrayList<>();
    private TextView placeHolder;
    private boolean becameVisible;
    private PagingController pagingController;
    private String nextPageUrl="1";
    private boolean isLoading;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PhotoGrapherBrandPresenter photoGrapherBrandPresenter;


    public static PhotoGrapherBrandFragment getInstance() {
        return new PhotoGrapherBrandFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return  mainView=inflater.inflate(R.layout.fragment_photographer_follow_brand,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initViews();
        initListener();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (becameVisible) {
            photographerFollowingBrands.clear();
            photoGrapherBrandPresenter.getFollowingBrand("", nextPageUrl);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !becameVisible) {
            becameVisible = true;

        }
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    @Override
    protected void initPresenter() {
        photoGrapherBrandPresenter = new PhotoGrapherBrandPresenterImpl(getContext(), this);
    }

    @Override
    protected void initViews() {
        searchPhotographerBrand = mainView.findViewById(R.id.search_photographer_brand);
        followingBrandRv = mainView.findViewById(R.id.photographer_search_brand_rv);
        photographerBrandSearchProgressBar = mainView.findViewById(R.id.photographer_brand_progress_bar);
        photoGrapherFollowingBrandAdapter = new PhotoGrapherFollowingBrandAdapter(photographerFollowingBrands);
        followingBrandRv.setAdapter(photoGrapherFollowingBrandAdapter);
        placeHolder = mainView.findViewById(R.id.place_holder);
    }



    private void initListener() {

        ////// initial block works by forcing then next Api for Each ScrollTop
        // cause recycler listener won't work until mainView ported with items
        followingBrandRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) followingBrandRv.getLayoutManager();
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                    if (firstVisibleItemPosition == 0) {
                        if (nextPageUrl != null) {
                            photoGrapherBrandPresenter.getFollowingBrand(searchPhotographerBrand.getText().toString(), nextPageUrl);
                        }

                    }
                }
            }
        });


        pagingController = new PagingController(followingBrandRv) {


            @Override
            protected void loadMoreItems() {
                 photoGrapherBrandPresenter.getFollowingBrand(searchPhotographerBrand.getText().toString(), nextPageUrl);

            }

            @Override
            public boolean isLastPage() {

                if (nextPageUrl ==null){
                    return  true;
                }else {
                    return false;
                }

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }


        };

        disposable.add(
                RxTextView.textChangeEvents(searchPhotographerBrand)
                        .skipInitialValue()
                        .debounce(Constants.QUERY_SEARCH_TIME_OUT, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery()));


        photoGrapherFollowingBrandAdapter.brandAdapterListener= brand -> {
            Intent intent=new Intent(getActivity(),BrandInnerActivity.class);
            intent.putExtra(BrandInnerActivity.BRAND_ID,brand.id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        };

    }

    private DisposableObserver<TextViewTextChangeEvent> searchQuery() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                // user cleared search get default data
                    photographerFollowingBrands.clear();
                    photoGrapherBrandPresenter.getFollowingBrand(searchPhotographerBrand.getText().toString(), "0");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }


    @Override
    public void viewPhotoGrapherFollowingBrand(List<Business> brandSearchList) {
        this.photographerFollowingBrands.addAll(brandSearchList);
        if (this.photographerFollowingBrands.isEmpty())
            placeHolder.setVisibility(View.VISIBLE);
        photoGrapherFollowingBrandAdapter.notifyDataSetChanged();

    }

    @Override
    public void viewPhotoGrapherFollowingBrandProgress(boolean state) {
        isLoading=state;

        if (state){
            photographerBrandSearchProgressBar.setVisibility(View.VISIBLE);
        }else {
            photographerBrandSearchProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void showMessage(String msg) {
        super.showToast(msg);
    }
    @Override
    public void setNextPageUrl(String page) {
        this.nextPageUrl=page;
    }

}
