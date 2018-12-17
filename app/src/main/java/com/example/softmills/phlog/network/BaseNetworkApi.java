package com.example.softmills.phlog.network;

import com.androidnetworking.common.Priority;
import com.example.softmills.phlog.base.commonmodel.BaseStateResponse;
import com.example.softmills.phlog.ui.album.model.AlbumPreviewResponse;
import com.example.softmills.phlog.ui.album.model.ImgCommentResponse;
import com.example.softmills.phlog.ui.allphotos.model.PhotoGrapherPhotosResponse;
import com.example.softmills.phlog.ui.brand.model.BrandInnerResponse;
import com.example.softmills.phlog.ui.campaigns.inner.model.CampaignInnerPhotoResponse;
import com.example.softmills.phlog.ui.campaigns.inner.model.CampaignInnerResponse;
import com.example.softmills.phlog.ui.campaigns.model.CampaignResponse;
import com.example.softmills.phlog.ui.campaigns.model.FollowBrandResponse;
import com.example.softmills.phlog.ui.campaigns.model.FollowCampaignResponse;
import com.example.softmills.phlog.ui.earning.model.EarningResponse;
import com.example.softmills.phlog.ui.login.model.LoginResponse;
import com.example.softmills.phlog.ui.login.model.SocialLoginResponse;
import com.example.softmills.phlog.ui.notification.model.NotificationResponse;
import com.example.softmills.phlog.ui.photographerprofile.model.ProfilePhotoGrapherInfoResponse;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_camaigns.model.PhotoGrapherCampaignResponse;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.brand.model.PhotographerFollowingBrandResponse;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_follow.following.model.PhotoGrapherFollowingInResponse;
import com.example.softmills.phlog.ui.photographerprofile.view.ph_saved.model.PhotoGrapherSavedPhotosResponse;
import com.example.softmills.phlog.ui.search.view.album.model.AlbumSearchResponse;
import com.example.softmills.phlog.ui.search.view.album.model.SearchFiltersResponse;
import com.example.softmills.phlog.ui.search.view.brand.model.BrandSearchResponse;
import com.example.softmills.phlog.ui.search.view.profile.model.ProfileSearchResponse;
import com.example.softmills.phlog.ui.signup.model.AllCountersRepose;
import com.example.softmills.phlog.ui.signup.model.SignUpResponse;
import com.example.softmills.phlog.ui.signup.model.UploadImgResponse;
import com.example.softmills.phlog.ui.social.model.SocialResponse;
import com.example.softmills.phlog.ui.userprofile.model.FollowUserResponse;
import com.example.softmills.phlog.ui.userprofile.model.UserPhotosResponse;
import com.example.softmills.phlog.ui.welcome.model.WelcomeScreenResponse;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by abdalla-maged on 3/29/18.
 */

public class BaseNetworkApi {


    //Network Status
    public static String STATUS_OK = "200";
    public static String DEFAULT_USER_TYPE = "0";
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_401 = 401;
    public static final int STATUS_404 = 404;
    public static final int STATUS_500 = 500;
    public static String STATUS_ERROR = "405";
    public static final int ERROR_STATE_1 = 1;

    public static String IMAGE_TYPE_PHOTOS = "image";
    public static String IMAGE_TYPE_CAMPAIGN = "campaign_id";
    public static String IMAGE_TYPE_PROFILE = "image_profile";

    public static String STATUS_IN_VALID_RESPONSE = "401";
    public static String NEW_FACEBOOK_USER_STATUS = "0";
    //
//    private static final String BASE_URL = "http://178.128.162.10/public/api/photographer";
    private static final String BASE_URL = "http://178.128.162.10/api";
    private static final String WELCOME_SLIDES_IMAGES = BASE_URL + "/photographer/init_slider"; //done
    private static final String ALL_COUNTRES = BASE_URL + "/common/countries/list"; //done
    private static final String SIGNUP_USER = BASE_URL + "/photographer/auth/signup";
    private static final String UPLOAD_PROFILE = BASE_URL + "/photographer/profile/upload";
    private static final String NORMAL_LOGIN = BASE_URL + "/photographer/auth/login";
    private static final String FACEBOOK_LOGIN_URL = BASE_URL + "/signup_facebook";
    private static final String USER_PROFILE_URL = BASE_URL + "/photographer/details";
    private static final String PHOTOGRAPHER_SAVED_PHOTO_URL = BASE_URL + "/photographer/photo/saved";
    private static final String PHOTOGRAPHER_ALL_PHOTO_URL = BASE_URL + "/photographer/photo/list";
    private static final String PHOTOGRAPHER_PROFILE_INFO = BASE_URL + "/photographer/details";
    private static final String PHOTOGRAPHER_ALL_CAMPAIGN_URL = BASE_URL + "/photographer/campaign/list";
    private static final String PHOTOGRAPHER_FOLLOWING_IN_URL = BASE_URL + "/photographer/following/list";
    private static final String PHOTOGRAPHER_FOLLOW_USER_URL = BASE_URL + "/photographer/follow";
    private static final String ALL_CAMPAIGN_URL = BASE_URL + "/photographer/campaign/running";
    private static final String CAMPAIGN_DETAILS_URL = BASE_URL + "/photographer/campaign/details";
    private static final String CAMPAIGN_PHOTOS_URL = BASE_URL + "/photographer/campaign/photos";
    private static final String FOLLOW_CAMPAIGN_URL = BASE_URL + "/photographer/campaign/join";
    private static final String USER_PROFILE_PHOTOS = BASE_URL + "/image_photographer";
    private static final String USER_SEARCH_FILTERS = BASE_URL + "/filters";
    private static final String SEARCH_ALBUM = BASE_URL + "/photographer/album/search";
    private static final String PHOTOGRAPHER_FOLLOWING_SEARCH_URL = BASE_URL + "/photographer/following/list";
    private static final String GET_SEARCH_ALBUM = BASE_URL + "/photographer/album/details";
    private static final String BRAND_SEARCH_URL = BASE_URL + "/photographer/business/following/list";
    private static final String INNER_BRAND_URL = BASE_URL + "/photographer/business/details";
    private static final String BRAND_FOLLOW_URL = BASE_URL + "/join_photographer_brand";
    private static final String SOCIAL_DATA_URL = BASE_URL + "/photographer/social/dummy";
    private static final String GET_IMAGE_COMMENT = BASE_URL + "/photographer/photo/comment/list";
    private static final String SUBMIT_IMAGE_COMMENT = BASE_URL + "/photographer/photo/comment";
    private static final String GET_ALL_NOTIFICATION = BASE_URL + "/notification";
    private static final String GET_EARNING = BASE_URL + "/earning";
    private static final String UPLOAD_PHOTOGRAPHER_PHOTO = BASE_URL + "/photographer/photo/upload";
    private static final String LIKE_PHOTOGRAPHER_PHOTO = BASE_URL + "/photographer/photo/like";


    //Path Parameters
    private static final String PAGER_PATH_PARAMETER = "page";


    //Body Parameters
    private static final String TOKEN_BODY_PARAMETER = "token";


    public static io.reactivex.Observable<WelcomeScreenResponse> getWelcomeSlidesImages() {
        return Rx2AndroidNetworking.get(WELCOME_SLIDES_IMAGES)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(WelcomeScreenResponse.class);
    }

    public static io.reactivex.Observable<AllCountersRepose> getAllCounters() {
        return Rx2AndroidNetworking.get(ALL_COUNTRES)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(AllCountersRepose.class);
    }

    public static io.reactivex.Observable<SignUpResponse> signUpUser(HashMap<String, String> signUpData) {
        return Rx2AndroidNetworking.post(SIGNUP_USER)
                .addBodyParameter("full_name", signUpData.get("full_name"))
                .addBodyParameter("password", signUpData.get("password"))
                .addBodyParameter("email", signUpData.get("email"))

                .addBodyParameter("mobile", signUpData.get("mobile"))
                .addBodyParameter("country_id", signUpData.get("country_id"))

                .addBodyParameter("mobile_os", signUpData.get("mobile_os"))
                .addBodyParameter("mobile_model", signUpData.get("mobile_model"))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(SignUpResponse.class);
    }

    public static io.reactivex.Observable<LoginResponse> LoginUserNormal(HashMap<String, String> loginData) {
        return Rx2AndroidNetworking.post(NORMAL_LOGIN)
                .addBodyParameter("email", loginData.get("email"))
                .addBodyParameter("password", loginData.get("password"))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    public static io.reactivex.Observable<SocialLoginResponse> socialLoginFacebook(HashMap<String, String> loginData) {
        return Rx2AndroidNetworking.post(FACEBOOK_LOGIN_URL)
                .addBodyParameter("fullName", loginData.get("fullName"))
                .addBodyParameter("facebook_id", loginData.get("facebook_id"))
                .addBodyParameter("mobile_os", loginData.get("mobile_os"))
                .addBodyParameter("mobile_model", loginData.get("mobile_model"))
                .addBodyParameter("email", loginData.get("email"))
                .addBodyParameter("image_profile", loginData.get("image_profile"))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(SocialLoginResponse.class);
    }


    public static io.reactivex.Observable<ProfilePhotoGrapherInfoResponse> getUserProfile(String userID) {
        return Rx2AndroidNetworking.post(USER_PROFILE_URL)
                .addBodyParameter("photographer_id", userID)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(ProfilePhotoGrapherInfoResponse.class);
    }

    public static io.reactivex.Observable<FollowUserResponse> followUser(String userID) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_FOLLOW_USER_URL)
                .addBodyParameter("photographer_id", userID)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(FollowUserResponse.class);
    }

    public static io.reactivex.Observable<PhotoGrapherSavedPhotosResponse> getPhotoGrapherSavedPhotos(int pageNumber) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_SAVED_PHOTO_URL)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(pageNumber))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(PhotoGrapherSavedPhotosResponse.class);
    }


    public static io.reactivex.Observable<PhotoGrapherPhotosResponse> getPhotoGrapherPhotos(int pageNumber) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_ALL_PHOTO_URL)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(pageNumber))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(PhotoGrapherPhotosResponse.class);
    }

    public static io.reactivex.Observable<PhotoGrapherCampaignResponse> getPhotoGrapherProfileCampaign(String token, int pageNumber) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_ALL_CAMPAIGN_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(pageNumber))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(PhotoGrapherCampaignResponse.class);
    }

    public static io.reactivex.Observable<ProfilePhotoGrapherInfoResponse> getProfileInfo(String token) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_PROFILE_INFO)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(ProfilePhotoGrapherInfoResponse.class);
    }


    public static io.reactivex.Observable<PhotoGrapherFollowingInResponse> getPhotoGrapherProfileFollowingIn(int page) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_FOLLOWING_IN_URL)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(PhotoGrapherFollowingInResponse.class);
    }

    public static io.reactivex.Observable<PhotoGrapherFollowingInResponse> getPhotoGrapherProfileFollowingSearch(String token, String key, int page) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_FOLLOWING_SEARCH_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addBodyParameter("keyword", key)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(PhotoGrapherFollowingInResponse.class);
    }

    public static io.reactivex.Observable<BrandSearchResponse> getBrandSearch(String token, String key, int page) {
        return Rx2AndroidNetworking.post(BRAND_SEARCH_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addBodyParameter("keyword", key)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(BrandSearchResponse.class);
    }

    public static io.reactivex.Observable<ProfileSearchResponse> getProfileSearch(String token, String key, int page) {
        return Rx2AndroidNetworking.post(PHOTOGRAPHER_FOLLOWING_SEARCH_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addBodyParameter("keyword", key)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .getResponseOnlyFromNetwork()
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(ProfileSearchResponse.class);
    }

    public static io.reactivex.Observable<PhotographerFollowingBrandResponse> getProfileBrand(String key, String page) {
        return Rx2AndroidNetworking.post(BRAND_SEARCH_URL)
                .addBodyParameter("keyword", key)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .getResponseOnlyFromNetwork()
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(PhotographerFollowingBrandResponse.class);
    }

    public static io.reactivex.Observable<CampaignResponse> getAllRunningCampaign(String token, int page) {
        return Rx2AndroidNetworking.post(ALL_CAMPAIGN_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(CampaignResponse.class);
    }

    public static io.reactivex.Observable<CampaignInnerResponse> getCampaignDetails(String token, String campaignID) {
        return Rx2AndroidNetworking.post(CAMPAIGN_DETAILS_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addBodyParameter("campaign_id", campaignID)
                .getResponseOnlyFromNetwork()
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(CampaignInnerResponse.class);
    }


    public static io.reactivex.Observable<CampaignInnerPhotoResponse> getCampaignInnerPhotos(String token, String campaignID, int page) {
        return Rx2AndroidNetworking.post(CAMPAIGN_PHOTOS_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addBodyParameter("campaign_id", campaignID)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(CampaignInnerPhotoResponse.class);
    }

    public static io.reactivex.Observable<FollowCampaignResponse> followCampaign(String token, String campaignID) {
        return Rx2AndroidNetworking.post(FOLLOW_CAMPAIGN_URL)
                .addBodyParameter("campaign_id", campaignID)
                .setPriority(Priority.HIGH)
                .getResponseOnlyFromNetwork()
                .build()
                .getObjectObservable(FollowCampaignResponse.class);
    }


    public static io.reactivex.Observable<UserPhotosResponse> getUserProfilePhotos(String token, String userID, int page) {
        return Rx2AndroidNetworking.post(USER_PROFILE_PHOTOS)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addBodyParameter("photographer_id", userID)
                .addQueryParameter(PAGER_PATH_PARAMETER, String.valueOf(page))
                .getResponseOnlyFromNetwork()
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(UserPhotosResponse.class);
    }


    public static io.reactivex.Observable<BaseStateResponse> likePhoto(String imageId) {
        return Rx2AndroidNetworking.post(LIKE_PHOTOGRAPHER_PHOTO)
                .addBodyParameter("image_id", imageId)
                .getResponseOnlyFromNetwork()
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(BaseStateResponse.class);
    }

    public static io.reactivex.Observable<SearchFiltersResponse> getFilters() {
        return Rx2AndroidNetworking.get(USER_SEARCH_FILTERS)

                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(SearchFiltersResponse.class);
    }

    public static io.reactivex.Observable<AlbumSearchResponse> getSearchAlbum(String key, String page) {
        return Rx2AndroidNetworking.post(SEARCH_ALBUM)
                .addQueryParameter(PAGER_PATH_PARAMETER, page)
                .addQueryParameter("keyword", key)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(AlbumSearchResponse.class);
    }

    public static io.reactivex.Observable<AlbumPreviewResponse> getSearchSelectedAlbum(String albumId, String page) {
        return Rx2AndroidNetworking.post(GET_SEARCH_ALBUM)
                .addQueryParameter(PAGER_PATH_PARAMETER, page)
                .addBodyParameter("album_id", albumId)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(AlbumPreviewResponse.class);
    }

    public static io.reactivex.Observable<ImgCommentResponse> getImageComments(String image_id, String page) {
        return Rx2AndroidNetworking.post(GET_IMAGE_COMMENT)
                .addQueryParameter("image_id", image_id)
                .addQueryParameter(PAGER_PATH_PARAMETER, page)
                .getResponseOnlyFromNetwork()
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(ImgCommentResponse.class);
    }

    public static io.reactivex.Observable<ImgCommentResponse> submitImageComment(String image_id, String imageComment) {
        return Rx2AndroidNetworking.post(SUBMIT_IMAGE_COMMENT)
                .addQueryParameter("image_id", image_id)
                .addQueryParameter("comment", imageComment)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(ImgCommentResponse.class);
    }


    public static io.reactivex.Observable<BrandInnerResponse> getBrandInnerData(String token, String brandId) {
        return Rx2AndroidNetworking.post(INNER_BRAND_URL)
                .addBodyParameter("business_id", brandId)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(BrandInnerResponse.class);
    }

    public static io.reactivex.Observable<FollowBrandResponse> followBrand(String token, String brandId) {
        return Rx2AndroidNetworking.post(BRAND_FOLLOW_URL)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .getResponseOnlyFromNetwork()
                .addBodyParameter("brand_id", brandId)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(FollowBrandResponse.class);
    }

    public static io.reactivex.Observable<SocialResponse> getSocialData(String token) {
        return Rx2AndroidNetworking.get(SOCIAL_DATA_URL)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(SocialResponse.class);
    }

    public static io.reactivex.Observable<NotificationResponse> getNotification(String token, String page) {
        return Rx2AndroidNetworking.post(GET_ALL_NOTIFICATION)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .addQueryParameter(PAGER_PATH_PARAMETER, page)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(NotificationResponse.class);
    }

    public static io.reactivex.Observable<EarningResponse> geEarning(String token, String page) {
        return Rx2AndroidNetworking.post(GET_EARNING)
                .addBodyParameter(TOKEN_BODY_PARAMETER, token)
                .getResponseOnlyFromNetwork()
                .addQueryParameter(PAGER_PATH_PARAMETER, page)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(EarningResponse.class);
    }

    public static io.reactivex.Observable<UploadImgResponse> uploadProfileImg(File imgPath) {
        return Rx2AndroidNetworking.upload(UPLOAD_PROFILE)
                .addHeaders("x-user-type", DEFAULT_USER_TYPE)
                .addHeaders("x-lang-code", "en-us")
                .addMultipartFile(IMAGE_TYPE_PROFILE, imgPath)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(UploadImgResponse.class);
    }


    public static io.reactivex.Observable<UploadImgResponse> uploadPhotoGrapherPhoto(String token, String caption, String location, File imgPath, Map<String, String> tagList) {
        return Rx2AndroidNetworking.upload(UPLOAD_PHOTOGRAPHER_PHOTO)
                .addHeaders("x-auth-token", token)
                .addHeaders("x-user-type", DEFAULT_USER_TYPE)
                .addHeaders("x-lang-code", "en-us")
                .addMultipartParameter("caption", caption)
                .addMultipartParameter("location", location)
                .addMultipartParameter(tagList)
                .addMultipartFile("image", imgPath)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(UploadImgResponse.class);
    }

    public static io.reactivex.Observable<UploadImgResponse> uploadCampaignPhoto(String token, String caption, String location, File imgPath, Map<String, String> tagList, Map CampagnId) {
        return Rx2AndroidNetworking.upload(UPLOAD_PHOTOGRAPHER_PHOTO)
                .addHeaders("x-auth-token", token)
                .addHeaders("x-user-type", DEFAULT_USER_TYPE)
                .addHeaders("x-lang-code", "en-us")
                .addMultipartParameter("caption", caption)
                .addMultipartParameter("location", location)
                .addMultipartParameter(tagList)
                .addMultipartParameter(CampagnId)
                .addMultipartFile("image", imgPath)
                .setPriority(Priority.HIGH)
                .build()
                .getObjectObservable(UploadImgResponse.class);
    }


//    public static io.reactivex.Observable<GeoCodeAutoCompleteResponse> getGeoGodeAutoCompleteResponse(String key){
//        return Rx2AndroidNetworking.get()
//
//    }
//

//    public static io.reactivex.Observable<BaseResponse> makeGetRequest(String lang, String key) {
//        return Rx2AndroidNetworking.get(REQUEST_URL)
//                .addPathParameter("lang", lang)
//                .addQueryParameter("key", String.valueOf(key))
//                .getResponseOnlyFromNetwork()
//                .build()
//                .getObjectObservable(BaseResponse.class);
//    }

//    /**
//     * --Upload Attachment
//     *
//     * @param file -->File src
//     * @message -->Request parameter you can add multible parameter to request  body along with uploaded attachment
//     */
//    public static void complaint(String message, File file, final RequestCallBack requestCallBack) {
//        AndroidNetworking.upload(REQUEST_URL)
//                .addHeaders("Content-Type", "multipart/form-data")
//                .addMultipartParameter("message", message)
//                .addMultipartFile("files[]", file) //todo "files[]" is A key According to back End
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            boolean success = response.getBoolean("success");
//                            if (success) {
//                                requestCallBack.OnSuccsess();
//                            } else {
//                                requestCallBack.onFailer();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            requestCallBack.onFailer();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        requestCallBack.onFailer();
//                    }
//                });
//    }
//

}
