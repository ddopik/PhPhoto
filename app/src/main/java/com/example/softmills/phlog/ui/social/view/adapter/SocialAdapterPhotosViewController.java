package com.example.softmills.phlog.ui.social.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.ui.album.view.AllAlbumImgActivity;
import com.example.softmills.phlog.ui.commentimage.view.ImageCommentActivity;
import com.example.softmills.phlog.ui.social.model.SocialData;

import java.util.ArrayList;

import static com.example.softmills.phlog.ui.album.view.AllAlbumImgActivity.ALL_ALBUM_IMAGES;
import static com.example.softmills.phlog.ui.album.view.AllAlbumImgActivity.LIST_NAME;
import static com.example.softmills.phlog.ui.album.view.AllAlbumImgActivity.LIST_TYPE;
import static com.example.softmills.phlog.ui.album.view.AllAlbumImgActivity.SELECTED_IMG_ID;

public class SocialAdapterPhotosViewController {


    private Context context;

    public SocialAdapterPhotosViewController(Context context) {
        this.context = context;
    }


    public void setPhotosViewType5(SocialAdapter.SocialViewHolder socialViewHolder, SocialData socialData, SocialAdapter.OnSocialItemListener onSocialItemListener) {


        socialViewHolder.socialImageSliderType5.setVisibility(View.VISIBLE);
        socialViewHolder.socialImageName.setText(socialData.title);
        socialViewHolder.storyTitle.setPadding(0, 0, 0, 0);


        SocialImagesAdapter socialImagesAdapter = new SocialImagesAdapter(socialData.photos);

        socialViewHolder.socialImgSlideRv.setAdapter(socialImagesAdapter);


        socialImagesAdapter.onSocialSliderImgClick = img -> {
//            Intent intent = new Intent(context, AllAlbumImgActivity.class);
//            intent.putParcelableArrayListExtra(ALL_ALBUM_IMAGES, (ArrayList<? extends Parcelable>) socialData.photos);
//            intent.putExtra(SELECTED_IMG_ID, socialData.photos.get(0).id);
//            intent.putExtra(LIST_TYPE, Constants.PhotosListType.SOCIAL_LIST);
//            intent.putExtra(LIST_NAME, socialData.title);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            if (img == null)
                return;
            Intent intent = new Intent(context, ImageCommentActivity.class);
            intent.putExtra(ImageCommentActivity.IMAGE_DATA, img);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);

        };

    }


}
