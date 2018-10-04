package com.example.softmills.phlog.ui.signup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.FileUtils;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.Utiltes.ImagePathUtil;
import com.example.softmills.phlog.Utiltes.ImageUtils;
import com.example.softmills.phlog.base.BaseActivity;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.softmills.phlog.Utiltes.ImageUtils.CAMERA_CODE;
import static com.example.softmills.phlog.Utiltes.ImageUtils.FILE_CODE;

public class PickProfilePhotoActivity extends BaseActivity {

    private String TAG = PickProfilePhotoActivity.class.getSimpleName();
    private ImageView pickImage;
    private final int CAMERA_AND_WRITE_EXTERNAL_CODE = 1223;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_upload_profile_pic);


        initPresenter();
        initView();
        initListener();
    }

    @Override
    public void initView() {

        pickImage = findViewById(R.id.pick_image);
        methodRequiresTwoPermission();

    }

    private void initListener() {
        pickImage.setOnClickListener(view -> {
            openPickerDialog();
        });
    }

    @Override
    public void initPresenter() {

    }


    private void openPickerDialog() {
        CharSequence photoChooserOptions[] = new CharSequence[]{getResources().getString(R.string.general_photo_chooser_camera), getResources().getString(R.string.general_photo_chooser_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.general_photo_chooser_title));
        builder.setItems(photoChooserOptions, (dialog, option) -> {
            if (option == 0) {
                EasyImage.openCamera(this, 0);
            } else if (option == 1) {
                EasyImage.openGallery(this, 0);
            }
        }).show();
    }

    @AfterPermissionGranted(CAMERA_AND_WRITE_EXTERNAL_CODE)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    CAMERA_AND_WRITE_EXTERNAL_CODE, perms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                GlideApp.with(PickProfilePhotoActivity.this)
                        .load(Uri.fromFile(imageFile))
                        .error(R.drawable.ic_launcher_foreground)
                        .override(612, 816)
                        .into(pickImage);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}