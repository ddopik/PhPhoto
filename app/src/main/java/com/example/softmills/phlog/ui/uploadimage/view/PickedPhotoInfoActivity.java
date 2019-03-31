package com.example.softmills.phlog.ui.uploadimage.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.Utiltes.MapUtls;
import com.example.softmills.phlog.base.BaseActivity;
import com.example.softmills.phlog.base.commonmodel.UploadImageData;
import com.example.softmills.phlog.ui.uploadimage.view.adapter.PlaceArrayAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.annotations.NonNull;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.softmills.phlog.Utiltes.Constants.REQUEST_CODE_LOCATION;

/**
 * Created by abdalla_maged on 10/24/2018.
 */
public class PickedPhotoInfoActivity extends BaseActivity implements MapUtls.OnLocationUpdate,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static String IMAGE_TYPE = "image_type";

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private final String TAG = PickedPhotoInfoActivity.class.getSimpleName();
    private String imagePath;
    private ImageView filtredImg;
    private AutoCompleteTextView placesAutoCompete;
    private Button nextBtn;
    private ImageButton backBtn;
    private ImageButton locateMeBtn;
    private EditText caption;
    private Switch draftBtn;
    private MapUtls mapUtls;
    private UploadImageData imageType;

    public static final int UPLOAD_PHOTO_REQUEST_CODE = 325;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        if (bundle.getSerializable(IMAGE_TYPE) != null) {
            setContentView(R.layout.activity_photo_info_activity);

            imageType = (UploadImageData) bundle.getSerializable(IMAGE_TYPE);
            imagePath = imageType.getImageUrl();
            initPresenter();
            initView();
            initListener();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {

        mapUtls = new MapUtls(this);

        mGoogleApiClient = new GoogleApiClient.Builder(PickedPhotoInfoActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        placesAutoCompete = findViewById(R.id.auto_complete_places);
        placesAutoCompete.setThreshold(3);


        filtredImg = findViewById(R.id.photo_info);
        GlideApp.with(getBaseContext())
                .load(imagePath)
                .error(R.drawable.ic_launcher_foreground)
                .into(filtredImg);

        nextBtn = findViewById(R.id.activity_info_photo_next_btn);
        backBtn = findViewById(R.id.activity_info_photo_back_btn);

        locateMeBtn = findViewById(R.id.locate_me_btn);

        caption = findViewById(R.id.photo_caption);
        placesAutoCompete.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                null, null);
        placesAutoCompete.setAdapter(mPlaceArrayAdapter);
    }

    @Override
    public void initPresenter() {

    }

    private void initListener() {
        nextBtn.setOnClickListener(v -> {
            caption.getText().toString();
            if (caption.getText().toString().isEmpty())  {
                showToast(getString(R.string.caption_is_required));
                return;
            }

            Bundle extras = new Bundle();
            if (placesAutoCompete.getText() != null)
                imageType.setImageLocation(String.valueOf(placesAutoCompete.getText()));
            if (caption.getText() != null)
                imageType.setImageCaption(String.valueOf(caption.getText()));

//            imageType.setDraft(draftBtn.isChecked());

            Intent intent = new Intent(this, AddTagActivity.class);
//            intent.putExtra(AddTagActivity.IMAGE_PREVIEW, imagePath);
//            intent.putExtra(AddTagActivity.IMAGE_DRAFT_STATE, String.valueOf(draftBtn.isChecked()));
//            Bundle extras = new Bundle();
//            extras.putSerializable(AddTagActivity.IMAGE_DATA, imageType); //passing image type
            extras.putSerializable(AddTagActivity.IMAGE_TYPE, imageType);
            intent.putExtras(extras);
            startActivity(intent);
//            startActivityForResult(intent, UPLOAD_PHOTO_REQUEST_CODE);
        });
        backBtn.setOnClickListener(v -> onBackPressed());

        locateMeBtn.setOnClickListener((view) -> requestLocation());
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }


    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private void requestLocation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mapUtls.startLocationUpdates(this, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT);
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, getString(R.string.need_location_permation),
                    REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onLocationUpdate(Location location) {
        // New location has now been determined
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mapUtls.getAddressFromLocation(latLng.latitude, latLng.longitude, this, new GeocodeHandler());
        mapUtls.removeLocationRequest();
        hideSoftKeyBoard();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    /// this method parsing and fetching placesObj
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = places -> {
        if (!places.getStatus().isSuccess()) {
            Log.e(TAG, "Place query did not complete. Error: " +
                    places.getStatus().toString());
            return;
        }
        // Selecting the first object buffer.
        final Place place = places.get(0);
        placesAutoCompete.setText(place.getAddress());
        hideSoftKeyBoard();

    };

    private void hideSoftKeyBoard() {
        placesAutoCompete.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.e(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class GeocodeHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    placesAutoCompete.setText(locationAddress);
                    break;
                default:
                    locationAddress = null;
            }
            Log.e("location Address=", locationAddress);
        }
    }


    protected void onDestroy() {
        super.onDestroy();

    }
}
