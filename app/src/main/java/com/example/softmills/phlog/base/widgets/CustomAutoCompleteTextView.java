package com.example.softmills.phlog.base.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;

import com.example.softmills.phlog.Utiltes.Constants;
import com.example.softmills.phlog.base.commonmodel.Business;
import com.example.softmills.phlog.base.commonmodel.MentionedUser;
import com.example.softmills.phlog.base.commonmodel.Photographer;
import com.example.softmills.phlog.ui.userprofile.view.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdalla maged
 * on 03,February,2019
 */
public class CustomAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public List<Photographer> currentMentionedPhotoGrapherList = new ArrayList<>();
    public List<Business> currentMentionedBusiness = new ArrayList<>();

    public CustomAutoCompleteTextView(Context context) {
        super(context);

    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void replaceText(CharSequence text) {

    }

    @Override
    public void dismissDropDown() {
        super.dismissDropDown();
    }


    public void handleMentionedCommentBody(int mentionedUserPosition, List<MentionedUser> mentionedUserList) {


        //position where we will insert our new mentioned  user
        int IndicatorPosition = getText().toString().lastIndexOf("@", getSelectionStart());
        int searchKeysCount = getSelectionStart() - IndicatorPosition;
        /**
         * User has selected "Mentioned user" from mentionList After writing The "@" symbol
         * now we convert this selection to the predefined scheme in order to send it through Api
         * then add it to our mentionList for later injecting
         * **/
        if (IndicatorPosition >= 0) {

            if (mentionedUserList.get(mentionedUserPosition).mentionedType == Constants.UserType.USER_TYPE_PHOTOGRAPHER) {
                Photographer photographer = new Photographer();
                photographer.id = mentionedUserList.get(mentionedUserPosition).mentionedUserId;
                photographer.fullName = mentionedUserList.get(mentionedUserPosition).mentionedUserName;
                photographer.mentionedImage = mentionedUserList.get(mentionedUserPosition).mentionedImage;
                currentMentionedPhotoGrapherList.add(photographer);
                //////////

                setUserSpannable(getPhotoGrapherClickableSpanObj(photographer), IndicatorPosition, searchKeysCount);

                //////////


            } else if (mentionedUserList.get(mentionedUserPosition).mentionedType == Constants.UserType.USER_TYPE_BUSINESS) {

                Business business = new Business();
                business.id = mentionedUserList.get(mentionedUserPosition).mentionedUserId;
                business.userName = mentionedUserList.get(mentionedUserPosition).mentionedUserName;
                business.mentionedImage = mentionedUserList.get(mentionedUserPosition).mentionedImage;
                currentMentionedBusiness.add(business);
                setUserSpannable(getBusinessClickableSpanObj(business), IndicatorPosition, searchKeysCount);

            }


        }


    }

    private void setUserSpannable(UserClickableSpan userSpannable, int position, int oldSearchKeysCount) {
        CharSequence charSequence = TextUtils.concat(getText());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
        int startPosition = position;

        if (startPosition <= 0) {
            startPosition = 0;
        }
        int endPosition = startPosition + userSpannable.userName.length() - 1;
        spannableStringBuilder.insert(startPosition, userSpannable.userName);
        spannableStringBuilder.setSpan(userSpannable, startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(" ");
        spannableStringBuilder.delete(endPosition + 1, endPosition + oldSearchKeysCount + 1);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableStringBuilder);

        setSelection(endPosition + 1);
        dismissDropDown();
    }


    private UserClickableSpan getPhotoGrapherClickableSpanObj(Photographer photographer) {

        ///////PhotoGrapher CallBack
        UserClickableSpan photoGrapherClickableSpan = new UserClickableSpan() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.USER_ID, photographer.id);
                intent.putExtra(UserProfileActivity.USER_TYPE, Constants.UserType.USER_TYPE_PHOTOGRAPHER);
                getContext().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.BLUE); // specific color for this link
            }
        };
        photoGrapherClickableSpan.userId = photographer.id.toString();
        photoGrapherClickableSpan.userName = photographer.fullName + " ";
        photoGrapherClickableSpan.userType = Constants.UserType.USER_TYPE_PHOTOGRAPHER;
        return photoGrapherClickableSpan;
    }

    private UserClickableSpan getBusinessClickableSpanObj(Business business) {

        ///////PhotoGrapher CallBack
        UserClickableSpan businessClickableSpan = new UserClickableSpan() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.USER_ID, business.id);
                intent.putExtra(UserProfileActivity.USER_TYPE, Constants.UserType.USER_TYPE_BUSINESS);
                getContext().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.MAGENTA); // specific color for this link
            }
        };
        businessClickableSpan.userId = business.id.toString();
        businessClickableSpan.userName = business.userName + " ";
        businessClickableSpan.userType = Constants.UserType.USER_TYPE_BUSINESS;
        return businessClickableSpan;
    }

    public String prepareCommentToSend() {

        UserClickableSpan[] clickableSpansList = getText().getSpans(0, getText().length(), UserClickableSpan.class);

        String oldCommentVal = getText().toString();
        String newCommentValue = "";

        UserClickableSpan[] clickableSpansListSorted = sortMentionListRanges(clickableSpansList);

        //handle multiple mentions
        for (int i = 0; i < clickableSpansListSorted.length; i++) {

            int startPoint = getText().getSpanStart(clickableSpansListSorted[i]);
            int endPoint = getText().getSpanEnd(clickableSpansListSorted[i]);

            String after = "";
            String before;

            if (i == 0) {
                before = oldCommentVal.substring(0, startPoint);
                if (clickableSpansListSorted.length - 1 == i) {
                    after = oldCommentVal.substring(endPoint);
                }

            } else {
                //get spannable  string full range from specified point
                //in order to get spannable end point
                int previousSegmentEndPoint = getText().getSpanEnd(clickableSpansListSorted[i - 1]);
                before = oldCommentVal.substring(previousSegmentEndPoint, startPoint);
            }


            String mentionedId;
            if (clickableSpansListSorted[i].userType.equals(Constants.UserType.USER_TYPE_PHOTOGRAPHER)) {
                if (oldCommentVal.substring(endPoint + 1).equals(" ") && (endPoint) < oldCommentVal.length()) {
                    mentionedId = "@0_" + clickableSpansListSorted[i].userId;
                } else {
                    mentionedId = "@0_" + clickableSpansListSorted[i].userId + " ";
                }
                newCommentValue = newCommentValue + before + mentionedId + after;
            } else if (clickableSpansListSorted[i].userType.equals(Constants.UserType.USER_TYPE_BUSINESS)) {
                if (oldCommentVal.substring(endPoint + 1).equals(" ") && (endPoint) < oldCommentVal.length()) {
                    mentionedId = "@1_" + clickableSpansListSorted[i].userId;
                } else {
                    mentionedId = "@1_" + clickableSpansListSorted[i].userId + " ";
                }
                newCommentValue = newCommentValue + before + mentionedId + after;
            }


        }

        if (newCommentValue.length() > 0) {
            return newCommentValue;
        } else {
            return oldCommentVal;
        }
    }

    private UserClickableSpan[] sortMentionListRanges(UserClickableSpan[] clickableSpansList) {


        int len = clickableSpansList.length;
        for (int i = 0; i < clickableSpansList.length; i++) {
            for (int j = 0; j < clickableSpansList.length - 1; j++) {
                UserClickableSpan userClickableSpansTemp;
                if (getText().getSpanStart(clickableSpansList[j]) > getText().getSpanStart(clickableSpansList[j + 1])) {
                    userClickableSpansTemp = clickableSpansList[j];
                    clickableSpansList[j] = clickableSpansList[j + 1];
                    clickableSpansList[j + 1] = userClickableSpansTemp;

                }


            }
        }
        return clickableSpansList;
    }

}
