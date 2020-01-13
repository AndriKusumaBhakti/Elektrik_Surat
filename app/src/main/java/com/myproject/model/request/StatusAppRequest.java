package com.myproject.model.request;

/**
 * Created by PAC 2018
 */

public class StatusAppRequest {
    private String appType;
    private String versionCode;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

}
