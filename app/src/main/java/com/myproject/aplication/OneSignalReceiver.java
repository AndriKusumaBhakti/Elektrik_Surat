package com.myproject.aplication;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.myproject.R;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

public class OneSignalReceiver extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                return builder.setColor(getBaseContext().getResources().getColor(R.color.google_blue));
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        APP.log("OneSignalExample Notification displayed with id: " + displayedResult.androidNotificationId);
        APP.log("OneSignalExample - Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}

