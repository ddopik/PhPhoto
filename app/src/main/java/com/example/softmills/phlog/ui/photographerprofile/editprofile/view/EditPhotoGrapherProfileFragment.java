package com.example.softmills.phlog.ui.photographerprofile.editprofile.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.base.BaseFragment;
import com.example.softmills.phlog.base.commonmodel.Photographer;
import com.example.softmills.phlog.ui.MainActivity;
import com.example.softmills.phlog.ui.dialog.changepasswroddialog.ChangePasswordDialogFragment;
import com.example.softmills.phlog.ui.photographerprofile.editprofile.presenter.EditPhotoGrapherProfileFragmentImpl;
import com.example.softmills.phlog.ui.photographerprofile.editprofile.presenter.EditPhotoGrapherProfileFragmentPresenter;
import com.example.softmills.phlog.ui.signup.model.Country;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by abdalla_maged On Nov,2018
 */
public class EditPhotoGrapherProfileFragment extends BaseFragment implements EditPhotoGrapherProfileFragmentView {

    private View mainView;
    private EditPhotoGrapherProfileFragmentPresenter presenter;

//    private EditText profileName, profileUserName, profileEmail, ProfilePassWord;
//    private ImageView profileImage;
//    private FrameLayout coverImage;
//    private ImageButton EditProfileBack;
//    private Button editProfileSave;

    private ImageView profileImage, coverImage;
    private EditText nameET, phoneET, emailET;
    private TextInputLayout nameLayout, phoneLayout, emailLayout, countryLayout;
    private AutoCompleteTextView countryET;
    private Button changePassword;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Country> countryListObj = new ArrayList<>();
    private ArrayList<String> countryList = new ArrayList<>();
    private ProgressBar loading;
    private Button saveButton;
    private Toolbar toolbar;
    private TextView title;
    private ImageButton backButton;

    private String newPassword, oldPassword;

    private Photographer photographer;


    public static EditPhotoGrapherProfileFragment getInstance() {
        EditPhotoGrapherProfileFragment editPhotoGrapherProfileFragment = new EditPhotoGrapherProfileFragment();
        return editPhotoGrapherProfileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_photographer_profile, container, false);
        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initViews();
        initListeners();
        presenter.getAllCountries(countries -> {
            this.countryList.clear();
            this.countryListObj.clear();
            this.countryListObj.addAll(countries);
            for (int i = 0; i < countries.size(); i++) {
                countryList.add(countries.get(i).nameEn);
            }
            arrayAdapter.notifyDataSetChanged();
        });
        presenter.getProfileEditData();
    }

    @Override
    public void onDestroy() {
        presenter.terminate();
        super.onDestroy();
    }

    @Override
    protected void initPresenter() {
        presenter = new EditPhotoGrapherProfileFragmentImpl(this, getContext());
    }

    @Override
    protected void initViews() {
        profileImage = mainView.findViewById(R.id.profile_image_image_view);
        coverImage = mainView.findViewById(R.id.cover_image_image_view);
        nameET = mainView.findViewById(R.id.name_edit_text);

        countryET = mainView.findViewById(R.id.country);
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
        countryET.setThreshold(0);
        countryET.setAdapter(arrayAdapter);

        emailET = mainView.findViewById(R.id.email_edit_text);
        phoneET = mainView.findViewById(R.id.phone_edit_text);
        loading = mainView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        saveButton = mainView.findViewById(R.id.save_button);
        saveButton.setVisibility(View.INVISIBLE);
        backButton = mainView.findViewById(R.id.back_btn);
        title = mainView.findViewById(R.id.toolbar_title);
        title.setText(R.string.profile);

        nameLayout = mainView.findViewById(R.id.name_layout);
        phoneLayout = mainView.findViewById(R.id.phone_layout);
        countryLayout = mainView.findViewById(R.id.country_layout);
        emailLayout = mainView.findViewById(R.id.email_layout);

        changePassword = mainView.findViewById(R.id.change_password_button);
    }

    private boolean detailsChanged;

    private void initListeners() {
        saveButton.setOnClickListener(v -> {
            saveButton.setVisibility(View.INVISIBLE);
            if (validate())
                presenter.updateProfile(getContext(), nameET.getText().toString()
                        , emailET.getText().toString(), phoneET.getText().toString(), getCountryID(), profile, cover, oldPassword, newPassword);
        });
        backButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigationManger.navigate(Constants.NavigationHelper.PROFILE);
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isCountry = false;
                Country country = photographer.country;
                if (country != null) {
                    isCountry = s.toString().equals(country.nameEn);
                }
                if (s.toString().equals(photographer.fullName)
                        || s.toString().equals(photographer.mobile)
                        || s.toString().equals(photographer.email)
//                        || photographer.country != null ? s.toString().equals(photographer.country.nameEn) : false
                        || isCountry
                        || s.toString().isEmpty())
                    return;
                detailsChanged = true;
                saveButton.setVisibility(View.VISIBLE);
            }
        };
        nameET.addTextChangedListener(textWatcher);
//        usernameET.addTextChangedListener(textWatcher);
        phoneET.addTextChangedListener(textWatcher);
        emailET.addTextChangedListener(textWatcher);
        countryET.addTextChangedListener(textWatcher);


        profileImage.setOnClickListener(v -> {
            whichImage = WhichImage.PROFILE;
            openPickerDialog();
        });
        coverImage.setOnClickListener(v -> {
            whichImage = WhichImage.COVER;
            openPickerDialog();
        });

        changePassword.setOnClickListener(v -> {
            ChangePasswordDialogFragment.getInstance((oldPassword, newPassword) -> {
                presenter.changePassword(getContext(), oldPassword, newPassword);
            }).show(getChildFragmentManager(), ChangePasswordDialogFragment.class.getSimpleName());
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (nameET.getText().toString().isEmpty()) {
            nameLayout.setError(getString(R.string.name_required));
            valid = false;
        }
        if (phoneET.getText().toString().isEmpty() || !android.util.Patterns.PHONE.matcher(phoneET.getText()).matches()) {
            phoneLayout.setError(getString(R.string.invalid_phone_number));
            valid = false;
        }
        if (!countryET.getText().toString().isEmpty())
            if (getCountryID() == -1) {
                countryLayout.setError(getString(R.string.select_country_not_exist));
                valid = false;
            }
        return valid;
    }

    private void openPickerDialog() {
        CharSequence photoChooserOptions[] = new CharSequence[]{getResources().getString(R.string.general_photo_chooser_camera), getResources().getString(R.string.general_photo_chooser_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.general_photo_chooser_title))
        .setPositiveButton(R.string.general_photo_chooser_camera, (dialog, which) -> {
            RequestCameraPermutations();
            dialog.dismiss();
        }).setNegativeButton(R.string.general_photo_chooser_gallery, (dialog, which) -> {
            requestGalleryPermutations();
            dialog.dismiss();
        }).show();
    }

    @AfterPermissionGranted(Constants.REQUEST_CODE_CAMERA)
    private void RequestCameraPermutations() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {

            ImagePicker.cameraOnly().start(this);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    Constants.REQUEST_CODE_CAMERA, perms);
        }

    }

    @AfterPermissionGranted(Constants.REQUEST_CODE_GALLERY)
    private void requestGalleryPermutations() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            ImagePicker.create(this)
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .single() // single mode
                    .showCamera(false)
                    .theme(R.style.AppTheme)
                    .start();
        }
        // Already have permission

        else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    Constants.REQUEST_CODE_GALLERY, perms);
        }

    }

    @Override
    public void showPhotoGrapherProfileData(Photographer photographer) {
        this.photographer = photographer;
        nameET.setText(photographer.fullName);
        emailET.setText(photographer.email);
        phoneET.setText(photographer.mobile);
        if (photographer.country != null)
            countryET.setText(photographer.country.nameEn);
        GlideApp.with(this)
                .load(photographer.imageProfile)
                .apply(RequestOptions.circleCropTransform())
                .error(AppCompatResources.getDrawable(getContext(), R.drawable.default_error_img))
                .placeholder(AppCompatResources.getDrawable(getContext(), R.drawable.default_place_holder))
                .into(profileImage);
        Glide.with(this)
                .load(photographer.imageCover)
                .into(coverImage);
    }

    @Override
    public void showMessage(int message) {
        showToast(getString(message));
    }

    @Override
    public void viewEditProfileProgress(Boolean State) {
        if (State)
            loading.setVisibility(View.VISIBLE);
        else
            loading.setVisibility(View.GONE);
    }

    private int getCountryID() {
        for (int i = 0; i < countryListObj.size(); i++) {
            if (countryListObj.get(i).nameEn.equals(countryET.getText().toString())) {
                return countryListObj.get(i).id;
            }
        }
        return -1;
    }

    String profile;
    String cover;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            detailsChanged = true;
            saveButton.setVisibility(View.VISIBLE);
            switch (whichImage) {
                case COVER:
                    cover = ImagePicker.getFirstImageOrNull(data).getPath();
                    GlideApp.with(this)
                            .load(cover)
                            .into(coverImage);
                    break;
                case PROFILE:
                    profile = ImagePicker.getFirstImageOrNull(data).getPath();
                    GlideApp.with(this)
                            .load(profile)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profileImage);
                    break;
            }
        }
    }

    private WhichImage whichImage;

    private enum WhichImage {
        PROFILE, COVER
    }
}
