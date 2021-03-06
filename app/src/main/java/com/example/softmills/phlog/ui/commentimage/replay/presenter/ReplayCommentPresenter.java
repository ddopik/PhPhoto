package com.example.softmills.phlog.ui.commentimage.replay.presenter;

public interface ReplayCommentPresenter {
    void getReplies(int commentId, int imageId, int page);
    void getMentionedUser(String key);
    void submitReplayComment(String imageId, String parentReplayId, String comment);

}
