package com.example.softmills.phlog.ui.dialog.popup.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.softmills.phlog.R;
import com.example.softmills.phlog.base.BaseDialogFragment;
import com.example.softmills.phlog.fgm.model.FirebaseNotificationData;
import com.example.softmills.phlog.ui.notification.model.NotificationList;

public class PopupDialogFragment extends BaseDialogFragment {

    private ImageView image;
    private TextView text;
    private Button close;

    private NotificationList data;
    private Action navigation;

    public static PopupDialogFragment newInstance(NotificationList data, Action navigation) {
        PopupDialogFragment fragment = new PopupDialogFragment();
        fragment.data = data;
        fragment.navigation = navigation;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cancelable = true;
        return inflater.inflate(R.layout.fragment_popup_dialog, container, false);
    }

    @Override
    protected void setViews(View view) {
        image = view.findViewById(R.id.image);
        text = view.findViewById(R.id.text);
        close = view.findViewById(R.id.close);
        Glide.with(this)
                .load(data.popupImage)
                .into(image);
        if (data.message != null)
            text.setText(data.message);
    }

    @Override
    protected void setListeners() {
//        image.setOnClickListener(v -> {
//            navigation.run();
//            dismiss();
//        });
        close.setOnClickListener(v -> {
            dismiss();
        });
    }
}
