package com.example.softmills.phlog.ui.uploadimage.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.base.BaseActivity;
import com.example.softmills.phlog.base.commonmodel.Tag;
import com.example.softmills.phlog.base.commonmodel.UploadImageType;
import com.example.softmills.phlog.base.widgets.CustomRecyclerView;
import com.example.softmills.phlog.ui.uploadimage.presenter.AddTagActivityPresenter;
import com.example.softmills.phlog.ui.uploadimage.presenter.AddTagActivityPresenterImpl;
import com.example.softmills.phlog.ui.uploadimage.view.adapter.AutoCompleteTagMenuAdapter;
import com.example.softmills.phlog.ui.uploadimage.view.adapter.SelectedTagAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdalla_maged on 10/28/2018.
 */
public class AddTagActivity extends BaseActivity implements AddTagActivityView {

    public static String IMAGE_TYPE = "image_type";

    private AutoCompleteTextView autoCompleteTextView;
    private List<Tag> tagList = new ArrayList<Tag>();
    private List<Tag> tagMenuList = new ArrayList<Tag>();
    private SelectedTagAdapter selectedTagAdapter;
    private AutoCompleteTagMenuAdapter autoCompleteTagMenuAdapter;
    private ImageView imagePreview;
    private String imagePreviewPath;
    private ImageButton backBtn;
    private Button uploadBrn;
    private ProgressBar uploadImageProgress;
    private  UploadImageType imageType;
    private String imageCaption;
    private String draftState;
    private String imageLocation;


    private AddTagActivityPresenter addTagActivityPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        if ( bundle.getSerializable(IMAGE_TYPE) != null) {
            imageType=(UploadImageType) bundle.getSerializable(IMAGE_TYPE);
            imagePreviewPath =imageType.getImageUrl();
            draftState =String.valueOf(imageType.isDraft());

            if (imageType.getImageCaption() != null)
                imageCaption = imageType.getImageCaption();

            if (imageType.getImageLocation() != null)
                imageLocation = imageType.getImageLocation();

            initView();
            initPresenter();
            initListener();
        }


    }

    @Override
    public void initView() {


        uploadBrn = findViewById(R.id.upload_image_btn);
        uploadImageProgress = findViewById(R.id.upload_image_progress);

        imagePreview = findViewById(R.id.tag_img_preview);
        autoCompleteTextView = findViewById(R.id.tag_auto_complete);
        autoCompleteTagMenuAdapter = new AutoCompleteTagMenuAdapter(this, R.layout.item_drop_down, tagMenuList);
        autoCompleteTextView.setAdapter(autoCompleteTagMenuAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTagMenuAdapter.notifyDataSetChanged();
        backBtn = findViewById(R.id.back_btn);
        GlideApp.with(getBaseContext())
                .load(imagePreviewPath)
                .centerCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(imagePreview);

        CustomRecyclerView tagsRv = findViewById(R.id.tags_rv);

        // Create the FlexboxLayoutMananger, only flexbox library version 0.3.0 or higher support.
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        // Set JustifyContent.
        flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        tagsRv.setLayoutManager(flexboxLayoutManager);

        // Set adapter object.
        selectedTagAdapter = new SelectedTagAdapter(tagList);

        tagsRv.setAdapter(selectedTagAdapter);
    }

    @Override
    public void initPresenter() {
        addTagActivityPresenter = new AddTagActivityPresenterImpl(getBaseContext(), this);
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }

    private void initListener() {

        autoCompleteTagMenuAdapter.onMenuItemClicked = this::addSelectedTag;
        selectedTagAdapter.onSelectedItemClicked = tag -> {

            List<Tag> tempList = new ArrayList<Tag>();
            for (int i = 0; i < tagList.size(); i++) {
                if (!tagList.get(i).name.equals(tag.name)) {
                    tempList.add(tagList.get(i));
                }

            }
            tagList.clear();
            tagList.addAll(tempList);
            selectedTagAdapter.notifyDataSetChanged();
        };

        initKeyBoardListener();

        backBtn.setOnClickListener((view) -> onBackPressed());

        uploadBrn.setOnClickListener(v -> {
            addTagActivityPresenter.uploadPhoto(imagePreviewPath, imageCaption, imageLocation, draftState, imageType, tagList);
        });
    }


    private void initKeyBoardListener() {
        // Минимальное значение клавиатуры. Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        // Окно верхнего уровня view. Top-level window decor view.
        final View decorView = getWindow().getDecorView();
        // Регистрируем глобальный слушатель. Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            // Видимый прямоугольник внутри окна. Retrieve visible rectangle inside window.
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        //Key board is visible
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        Tag newTag = new Tag();
                        newTag.name = autoCompleteTextView.getText().toString();
                        boolean tagAlreadyExsist = false;
                        for (Tag tag : tagList) {
                            if (tag.name.equals(newTag.name))
                                tagAlreadyExsist = true;
                        }
                        if (!tagAlreadyExsist)
                            addSelectedTag(newTag);
                    }
                }
                // Сохраняем текущую высоту view до следующего вызова.
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }

    private void addSelectedTag(Tag tag) {
        tagList.add(tag);
        selectedTagAdapter.notifyDataSetChanged();
        autoCompleteTextView.dismissDropDown();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void viewUploadProgress(boolean state) {
        if (state) {
            uploadImageProgress.setVisibility(View.VISIBLE);
        } else {
            uploadImageProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void viewMessage(String msg) {
        showToast(msg);
    }
}
