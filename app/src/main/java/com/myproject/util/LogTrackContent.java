package com.myproject.util;


import com.myproject.aplication.APP;
import com.myproject.aplication.Config;

public class LogTrackContent {

    public static void setViewEvent(String contentName, String contentType, String contentId){

        if(Config.isDevelopment == 1) {
            APP.log("Web Content Name: "+ contentName);
            APP.log("Web Content Type: "+ contentType);
            APP.log("Web Content Id: "+ contentId);
        }
    }

    public static void setViewEventFulls(String contentName, String contentType, String contentId, String idPatient, String sex){

        if(Config.isDevelopment == 1) { //JIka Set Mode Production, maka tracking view dijalankan
            APP.log("Web Content Name: "+ contentName);
            APP.log("Web Content Type: "+ contentType);
            APP.log("Web Content Id: "+ contentId);
        }
    }

}

