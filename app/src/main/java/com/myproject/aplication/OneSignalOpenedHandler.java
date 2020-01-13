package com.myproject.aplication;

import android.content.Intent;
import android.os.Bundle;

import com.myproject.StartActivity;
import com.myproject.activity.DashboardActivity;
import com.myproject.activity.DashboardKepalaDesa;
import com.myproject.util.StringUtil;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class OneSignalOpenedHandler implements OneSignal.NotificationOpenedHandler {

    private APP application;

    public OneSignalOpenedHandler(APP application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;
        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                APP.log("OneSignalExample Surat- customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            APP.log("OneSignalExample Surat - button pressed with id: " + result.action.actionID);

        startApp();
    }

    private void startApp() {
        if (!StringUtil.checkNullString(APP.getStringPref(application, Preference.TOKEN)).isEmpty()
                && !StringUtil.checkNullString(APP.getStringPref(application, Preference.ROLE)).isEmpty()){
            if (APP.getStringPref(application, Preference.ROLE).equals("1")){
                Intent intent = new Intent(application, DashboardActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putBoolean(DashboardActivity.IS_FROM_PUSH_NOTIF, true);
                b.putString(DashboardActivity.PUSH_NOTIF_MODEL_NAME, "TEST");
                intent.putExtras(b);
                application.startActivity(intent);
            }else{
                Intent intent = new Intent(application, DashboardKepalaDesa.class)
                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putBoolean(DashboardKepalaDesa.IS_FROM_PUSH_NOTIF, true);
                b.putString(DashboardKepalaDesa.PUSH_NOTIF_MODEL_NAME, "TEST");
                intent.putExtras(b);
                application.startActivity(intent);
            }
        }else{
            Intent intent = new Intent(application, StartActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putBoolean(StartActivity.IS_FROM_PUSH_NOTIF, true);
            b.putString(StartActivity.PUSH_NOTIF_MODEL_NAME, "TEST");
            intent.putExtras(b);
            application.startActivity(intent);
        }
    }
}

