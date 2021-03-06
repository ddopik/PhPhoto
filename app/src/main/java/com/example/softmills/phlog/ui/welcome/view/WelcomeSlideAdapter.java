package com.example.softmills.phlog.ui.welcome.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.ui.welcome.model.InitSlider;

import java.util.List;

public class WelcomeSlideAdapter extends PagerAdapter {

    private List<InitSlider> urlList;
    private LayoutInflater inflater;
    private Context context;


    public WelcomeSlideAdapter(Context context, List<InitSlider> urlList) {
        this.context = context;
        this.urlList = urlList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = null;
        InitSlider item = urlList.get(position);
        if (position != 0) {
            imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

            ImageView slideImage = imageLayout.findViewById(R.id.slide_img);
            TextView imageText = imageLayout.findViewById(R.id.image_description);


            if (item.text != null)
                imageText.setText(item.text);
            GlideApp.with(context)
                    .load(item.image)
                    .centerCrop()
                    .placeholder(R.drawable.splash_screen_background)
                    .error(R.drawable.splash_screen_background)
                    .into(slideImage);
        } else {
            imageLayout = inflater.inflate(R.layout.slidingimages_layout_with_logo, view, false);
            ImageView slideImage = imageLayout.findViewById(R.id.slide_img);
            GlideApp.with(context)
                    .load(item.image)
                    .centerCrop()
                    .placeholder(R.drawable.splash_screen_background)
                    .error(R.drawable.splash_screen_background)
                    .into(slideImage);
        }
//        view.addView(imageLayout, 0);
        ((ViewPager) view).addView(imageLayout);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
