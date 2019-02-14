package com.example.softmills.phlog.ui.album.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.softmills.phlog.R;
import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.Utiltes.GlideApp;
import com.example.softmills.phlog.Utiltes.Utilities;
import com.example.softmills.phlog.base.commonmodel.BaseImage;
import com.example.softmills.phlog.base.commonmodel.Business;
import com.example.softmills.phlog.base.commonmodel.Comment;
import com.example.softmills.phlog.base.commonmodel.MentionedUser;
import com.example.softmills.phlog.base.commonmodel.Mentions;
import com.example.softmills.phlog.base.commonmodel.Photographer;
import com.example.softmills.phlog.base.commonmodel.Tag;
import com.example.softmills.phlog.base.eventbus.SendCommentEvent;
import com.example.softmills.phlog.base.widgets.CustomAutoCompleteTextView;
import com.example.softmills.phlog.base.widgets.CustomTextView;
import com.example.softmills.phlog.ui.album.presenter.CommentAdapterPresenter;
import com.example.softmills.phlog.ui.album.presenter.CommentAdapterPresenterImpl;
import com.example.softmills.phlog.ui.album.view.CommentsAdapterView;
import com.example.softmills.phlog.ui.commentimage.view.MentionsAutoCompleteAdapter;
import com.example.softmills.phlog.ui.userprofile.view.UserProfileActivity;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by abdalla_maged On Nov,2018
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> implements CommentsAdapterView {

    private String TAG = CommentsAdapter.class.getSimpleName();
    private Context context;
    private List<Comment> commentList;
    private Mentions mentions;

    private List<MentionedUser> mentionedUserList = new ArrayList<>();
    private MentionsAutoCompleteAdapter mentionsAutoCompleteAdapter;
    private CommentAdapterPresenter commentAdapterPresenter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private BaseImage previewImage;
    public CommentAdapterAction commentAdapterAction;
    private int HEAD = 0;
    private int COMMENT = 1;
    private int ADD_COMMENT = 2;
    private final String USER_MENTION_IDENTIFIER = "%";
    private DisposableObserver<TextViewTextChangeEvent> searchQuery;


    public CommentsAdapter(BaseImage previewImage, List<Comment> commentList, Mentions mentions) {
        this.commentList = commentList;
        this.previewImage = previewImage;
        this.mentions = mentions;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        commentAdapterPresenter = new CommentAdapterPresenterImpl(context, this);

        if (i == HEAD) {
            return new CommentViewHolder(layoutInflater.inflate(R.layout.view_holder_comment_start_item, viewGroup, false), HEAD);
        } else if (i == commentList.size()) {
            return new CommentViewHolder(layoutInflater.inflate(R.layout.view_holder_image_send_comment, viewGroup, false), ADD_COMMENT);
        } else {
            return new CommentViewHolder(layoutInflater.inflate(R.layout.view_holder_image_comment, viewGroup, false), COMMENT);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i) {
//////////////////////////////////////HEAD/////////////////////////////////////////

        if (getItemViewType(i) == HEAD) {


            commentViewHolder.authorName.setText(previewImage.photographer.fullName);
            commentViewHolder.authorUserName.setText(previewImage.photographer.userName);

            GlideApp.with(context)
                    .load(previewImage.photographer.imageProfile)
                    .error(R.drawable.default_error_img)
                    .placeholder(R.drawable.default_place_holder)
                    .apply(RequestOptions.circleCropTransform())
                    .override(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                    .into(commentViewHolder.commentAuthorIcon);


            int rate = Math.round(previewImage.rate);
            commentViewHolder.photoRating.setRating(rate);


            GlideApp.with(context)
                    .load(previewImage.url)
                    .error(R.drawable.default_error_img)
                    .placeholder(R.drawable.default_place_holder)
                    .override(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                    .into(commentViewHolder.commentImg);

            String tagS = "";
            if (previewImage.tags != null)
                for (Tag tag : previewImage.tags) {
                    tagS = tagS + " #" + tag.name;
                }
            commentViewHolder.commentPreviewImgTags.setText(tagS);
            if (previewImage.likesCount != null)
                commentViewHolder.imgLikeNum.setText(new StringBuilder().append(previewImage.likesCount).append(" ").append(context.getResources().getString(R.string.like)).toString());
            if (previewImage.commentsCount != null)
                commentViewHolder.imgCommentNum.setText(new StringBuilder().append(previewImage.commentsCount).append(" ").append(context.getResources().getString(R.string.comment)).toString());
            if (commentAdapterAction != null) {
                commentViewHolder.imageCommentBtn.setOnClickListener(v -> {
//                    todo  add event bus here
                    SendCommentEvent sendCommentEvent=new SendCommentEvent();
                    sendCommentEvent.state=true;
                    EventBus.getDefault().post(sendCommentEvent);


                });


                if (!previewImage.isLiked) {
                    commentViewHolder.imageLikeBtn.setImageResource(R.drawable.ic_like_off_white);
                } else {
                    commentViewHolder.imageLikeBtn.setImageResource(R.drawable.ic_like_on);
                }

                commentViewHolder.imageLikeBtn.setOnClickListener(v -> {

                    commentAdapterAction.onImageLike(previewImage);
                });
            }


//////////////////////////////////////COMMENT/////////////////////////////////////////
        } else if (getItemViewType(i) == COMMENT) {
            if (commentList.get(i).business != null) {

                if (commentList.get(i).business.thumbnail != null)
                    GlideApp.with(context)
                            .load(commentList.get(i).business.thumbnail)
                            .error(R.drawable.default_error_img)
                            .placeholder(R.drawable.default_place_holder)
                            .override(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                            .apply(RequestOptions.circleCropTransform())
                            .into(commentViewHolder.commentAuthorImg);
                commentViewHolder.commentAuthorName.setText(commentList.get(i).business.firstName + " " + commentList.get(i).business.lastName);


            } else if (commentList.get(i).photographer != null) {
                if (commentList.get(i).photographer.imageProfile != null)
                    GlideApp.with(context)
                            .load(commentList.get(i).photographer.imageProfile)
                            .error(R.drawable.default_error_img)
                            .placeholder(R.drawable.default_place_holder)
                            .override(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                            .apply(RequestOptions.circleCropTransform())
                            .into(commentViewHolder.commentAuthorImg);
                commentViewHolder.commentAuthorName.setText(commentList.get(i).photographer.fullName);

            }

            if (commentList.get(i).comment !=null){
                handleCommentBody(commentViewHolder.commentVal, commentList.get(i).comment);
            }

            ////////////////////////////////ADD_COMMENT///////////////////////////////////////////////
        } else if (getItemViewType(i) == ADD_COMMENT) {

            mentionsAutoCompleteAdapter = new MentionsAutoCompleteAdapter(context, R.layout.view_holder_mentioned_user, mentionedUserList);
            mentionsAutoCompleteAdapter.setNotifyOnChange(true);
            commentViewHolder.sendCommentImgVal.setAdapter(mentionsAutoCompleteAdapter);
            commentViewHolder.sendCommentImgVal.setThreshold(0);


            commentViewHolder.sendCommentImgVal.setOnItemClickListener((parent, view, position, id) -> {
                commentViewHolder.sendCommentImgVal.handleMentionedCommentBody(position, mentionedUserList);

            });


            if (searchQuery == null) {
                searchQuery = getSearchTagQuery(commentViewHolder.sendCommentImgVal);
                commentViewHolder.sendCommentImgVal.addTextChangedListener(new TextWatcher() {
                    int cursorPosition = commentViewHolder.sendCommentImgVal.getSelectionStart();

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        int mentionIdentifierCharPosition = commentViewHolder.sendCommentImgVal.getText().toString().indexOf("@", cursorPosition - 2);
                        if ((mentionIdentifierCharPosition + 1) >= commentViewHolder.sendCommentImgVal.getText().toString().length() || mentionIdentifierCharPosition == -1) {
                            mentionedUserList.clear();
                            mentionsAutoCompleteAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                disposable.add(

                        RxTextView.textChangeEvents(commentViewHolder.sendCommentImgVal)
                                .skipInitialValue()
                                .debounce(900, TimeUnit.MILLISECONDS)
                                .distinctUntilChanged()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(searchQuery)
                );
            }

            if (commentAdapterAction != null) {
                commentViewHolder.sendCommentBtn.setOnClickListener(v -> {
                    String comment = commentViewHolder.sendCommentImgVal.prepareCommentToSend();
                    commentAdapterAction.onSubmitComment(comment);
                    commentViewHolder.sendCommentImgVal.getText().clear();
                });
            }
            mentionsAutoCompleteAdapter.onUserClicked = socialUser -> {
            };





        }
    }


    private DisposableObserver<TextViewTextChangeEvent> getSearchTagQuery(AutoCompleteTextView autoCompleteTextView) {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                // user cleared search get default
                Log.e(TAG, "search string: " + autoCompleteTextView.getText().toString().trim());
                int cursorPosition = autoCompleteTextView.getSelectionStart();

                ///getting String value before cursor
                if (cursorPosition > 0) {
                    int searKeyPosition = autoCompleteTextView.getText().toString().lastIndexOf("@", cursorPosition);
                    if (searKeyPosition >= 0) {
                        commentAdapterPresenter.getMentionedUser(autoCompleteTextView.getText().toString().substring(searKeyPosition + 1, autoCompleteTextView.getSelectionStart()));
                    }

                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void handleCommentBody(TextView commentView, String commentFinalValue) {

        List<String> authorsId = Utilities.getMentionsList(commentFinalValue);
        List<String> mentionsPhotoGrapherIdIdList = new ArrayList<>();
        List<String> mentionBusinessIdList = new ArrayList<>();
        List<MentionRange> mentionsPoint = new ArrayList<MentionRange>();
        List<ClickableSpan> clickableSpanList = new ArrayList<>();
        //

        if (authorsId.size() > 0) {

            for (String authorId : authorsId) {
                String[] singleId = authorId.split("\\_");

                if (singleId[0].equals("0")) {
                    mentionsPhotoGrapherIdIdList.add(singleId[1]);
                } else if (singleId[0].equals("1")) {
                    mentionBusinessIdList.add(singleId[1]);
                }
            }


            /// Append unique identifier to mentioned user to get highLighted later
            /// And Replacing All Occurrence of photoGrapherId with actualValue
            for (String photoGrapherId : mentionsPhotoGrapherIdIdList) {
                Photographer photographer = getMentionedPhotoGrapher(photoGrapherId);
                if (photographer != null) {

                    commentFinalValue = commentFinalValue.replace("@0_" + photoGrapherId, photographer.fullName + USER_MENTION_IDENTIFIER);
                    commentView.setText(commentFinalValue);
                }
            }

            /// Append unique identifier to mentioned user to get highLighted later
            /// And Replacing All Occurrence of businessId with actualValue
            for (String businessId : mentionBusinessIdList) {
                if (getMentionedBusiness(businessId) != null) {
                    Business business = getMentionedBusiness(businessId);
                    if (business != null) {
                        commentFinalValue = commentFinalValue.replace("@1_" + businessId, business.firstName + " " + business.lastName + USER_MENTION_IDENTIFIER);
                        commentView.setText(commentFinalValue);
                    }
                }
            }


            for (String photographerId : mentionsPhotoGrapherIdIdList) {
                if (getMentionedPhotoGrapher(photographerId) != null) {
                    Photographer photographer = getMentionedPhotoGrapher(photographerId);
                    ///////PhotoGrapher CallBack
                    ClickableSpan noUnderLineClickSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserProfileActivity.class);
                            intent.putExtra(UserProfileActivity.USER_ID, photographerId);
                            intent.putExtra(UserProfileActivity.USER_TYPE, Constants.UserType.USER_TYPE_PHOTOGRAPHER);
                            context.startActivity(intent);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.setColor(Color.BLUE); // specific color for this link
                        }
                    };


                    String replacement = photographer.fullName + USER_MENTION_IDENTIFIER;
                    int replacementStart = commentFinalValue.indexOf(replacement) - 1;
                    int replacementEnd = replacementStart + replacement.length();
                    MentionRange mentionRange = new MentionRange();
                    mentionRange.startPoint = replacementStart;
                    mentionRange.endPoint = replacementEnd;
                    mentionsPoint.add(mentionRange);
                    clickableSpanList.add(noUnderLineClickSpan);
                    commentFinalValue = commentFinalValue.replace(replacement, replacement.substring(0, replacement.length() - 1));
                }
            }


            //////////////////////////////////////////////////////////

            for (String businessId : mentionBusinessIdList) {
                if (getMentionedBusiness(businessId) != null) {
                    Business business = getMentionedBusiness(businessId);
                    //////business CallBack
                    ClickableSpan noUnderLineClickSpan2 = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, UserProfileActivity.class);
                            intent.putExtra(UserProfileActivity.USER_ID, businessId);
                            intent.putExtra(UserProfileActivity.USER_TYPE, Constants.UserType.USER_TYPE_BUSINESS);
                            context.startActivity(intent);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                            ds.setColor(Color.MAGENTA); // specific color for this link
                        }
                    };
                    String replacement = business.firstName + " " + business.lastName + USER_MENTION_IDENTIFIER;
                    int replacementStart = commentFinalValue.indexOf(replacement) - 1;
                    int replacementEnd = replacementStart + replacement.length();
                    MentionRange mentionRange = new MentionRange();
                    mentionRange.startPoint = replacementStart;
                    mentionRange.endPoint = replacementEnd;
                    mentionsPoint.add(mentionRange);
                    clickableSpanList.add(noUnderLineClickSpan2);
                    commentFinalValue = commentFinalValue.replace(replacement, replacement.substring(0, replacement.length() - 1));
                }
            }
            makeLinks(commentView, mentionsPoint, clickableSpanList, commentFinalValue);
        } else {
            commentView.setText(commentFinalValue);
        }


    }


    /**
     * @param viewHolder        --->view holder contain comment value
     * @param mentionsList      --->replacement user_value flaged with (?*_)
     * @param clickableSpanList --->link CallBack
     * @param commentFinalValue --->all text value insideViewHolder
     */
    private void makeLinks(TextView viewHolder, List<MentionRange> mentionsList, List<ClickableSpan> clickableSpanList, String commentFinalValue) {
        SpannableString spannableString = new SpannableString(commentFinalValue);
        for (int i = 0; i < mentionsList.size(); i++) {
            spannableString.setSpan(clickableSpanList.get(i), mentionsList.get(i).startPoint + 1, mentionsList.get(i).endPoint, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        viewHolder.setLinksClickable(true);
        viewHolder.setClickable(true);
        viewHolder.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.setText(spannableString);

    }


    private Photographer getMentionedPhotoGrapher(String userId) {

        if (mentions.photographers != null)
            for (Photographer photographer : mentions.photographers) {
                if (photographer.id == Integer.parseInt(userId)) {
                    return photographer;
                }
            }

        return null;
    }

    private Business getMentionedBusiness(String userId) {

        if (mentions.business != null)
            for (Business business : mentions.business) {
                if (business.id == Integer.parseInt(userId)) {
                    return business;
                }
            }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD; //-->For Image Header Preview
        } else if (position == (commentList.size() - 1)) {
            return ADD_COMMENT; //---> normal Comment viewHolder
        } else {
            return COMMENT; //--->  Comment Cell
        }
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        //header cell
        FrameLayout addToCartBtn;
        CustomTextView imgLikeNum, imgCommentNum, commentPreviewImgTags, authorName, authorUserName;
        ImageView commentImg, commentAuthorIcon;
        ImageButton imageLikeBtn, imageCommentBtn;
        RatingBar photoRating;
        ///Comment_value Cell
        TextView commentVal;
        CustomTextView commentAuthorName;
        ImageView commentAuthorImg;
        //SendCommentCell
        CustomAutoCompleteTextView sendCommentImgVal;
        ImageButton sendCommentBtn;


        CommentViewHolder(View view, int type) {
            super(view);
            if (type == HEAD) {

                commentAuthorIcon = view.findViewById(R.id.comment_author_icon);
                authorName = view.findViewById(R.id.author_name);
                authorUserName = view.findViewById(R.id.author_user_name);

                commentImg = view.findViewById(R.id.comment_preview_img);
                commentPreviewImgTags = view.findViewById(R.id.comment_preview_img_tag);
                imageLikeBtn = view.findViewById(R.id.comment_preview_img_like_btn);
                imageCommentBtn = view.findViewById(R.id.comment_preview_img_comment_btn);
                imgLikeNum = view.findViewById(R.id.comment_preview_img_like_num);
                imgCommentNum = view.findViewById(R.id.comment_preview_img_comment_num);
                photoRating = view.findViewById(R.id.photo_rating);

            } else if (type == COMMENT) {
                commentVal = view.findViewById(R.id.comment_val);
                commentAuthorImg = view.findViewById(R.id.commentAuthorImg);
                commentAuthorName = view.findViewById(R.id.comment_author);
            } else if (type == ADD_COMMENT) {
                sendCommentImgVal = view.findViewById(R.id.img_send_comment_val);
                sendCommentBtn = view.findViewById(R.id.send_comment_btn);
            }

        }

    }

    public interface CommentAdapterAction {
        void onImageLike(BaseImage baseImage);


        void onSubmitComment(String comment);
    }

    @Override
    public void viewMentionedUsers(List<MentionedUser> mentionedUserList) {
        this.mentionedUserList.clear();
        this.mentionedUserList.addAll(mentionedUserList);
        mentionsAutoCompleteAdapter.notifyDataSetChanged();
    }

    private class MentionRange {
        int startPoint;
        int endPoint;
    }
}
