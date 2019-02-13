package com.example.softmills.phlog.ui.notification.view;

import com.example.softmills.phlog.ui.notification.model.NotificationResponse;
import com.example.softmills.phlog.ui.notification.model.NotificationSortedObj;

import java.util.List;

/**
 * Created by abdalla_maged On Nov,2018
 */
public interface NotificationFragmentView {

    void viewNotification(List<NotificationResponse> notificationResponseList);
     void viewNotificationProgress(boolean state);
}
