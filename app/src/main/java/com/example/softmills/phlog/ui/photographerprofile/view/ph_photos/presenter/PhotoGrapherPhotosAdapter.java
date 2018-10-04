package com.example.softmills.phlog.ui.photographerprofile.view.ph_photos.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_photos.model.PhotoGrapherPhoto;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_saved.view.PhotographerSavedPhotoAdapter;

import java.util.List;

/**
 * Created by abdalla_maged on 10/1/2018.
 */
public class PhotoGrapherPhotosAdapter  extends RecyclerView.Adapter<PhotoGrapherPhotosAdapter.PhotosViewHolder> {


    private List<PhotoGrapherPhoto> photoGrapherPhotosList;
    private Context context;
    private PhotoGrapherPhotosAdapter.PhotosViewHolder photosViewHolder;
    private PhotoAction photoAction;

    public PhotoGrapherPhotosAdapter(Context context, List<PhotoGrapherPhoto> photoGrapherPhotosList) {
        this.context = context;
        this.photoGrapherPhotosList = photoGrapherPhotosList;
    }

    @NonNull
    @Override
    public PhotoGrapherPhotosAdapter.PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new PhotosViewHolder(layoutInflater.inflate(R.layout.view_holder_photo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGrapherPhotosAdapter.PhotosViewHolder campaignsViewHolder, int i) {



        GlideApp.with(context)
                .load(photoGrapherPhotosList.get(i).image)
                .centerCrop()
//                .override(450,450)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(campaignsViewHolder.photographerPhoto);
        if (photoAction != null)
            campaignsViewHolder.photographerPhoto.setOnClickListener(view -> {
                photoAction.onPhotoClicked(photoGrapherPhotosList.get(i));
            });
    }

    @Override
    public int getItemCount() {
        return photoGrapherPhotosList.size();
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder {

        ImageView photographerPhoto;

        public PhotosViewHolder(View view) {
            super(view);
            photographerPhoto = view.findViewById(R.id.photographer_photo);
        }
    }

    public interface PhotoAction {
        void onPhotoClicked(PhotoGrapherPhoto photoGrapherSavedPhoto);
    }
}