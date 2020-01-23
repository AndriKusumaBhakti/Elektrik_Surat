package com.myproject.aplication;

public class Config {

    public static String APP_NAME = "E-Surat";
    public static final String APP_VERSION = "1.0.0";

    public static String DATABASE_NAME = APP_NAME + "_DB.sqlite";

    public static String CACHE_FOLDER = APP_NAME + "_data";

    public static String HTTP = "http://";
    public static String HTTPS = "https://";
    public static String DEVELOPMENT_URL = "desawaru.geraitani.id";
    public static String PRODUCTION_URL = "desawaru.geraitani.id";

    public static String PATH_FOTO = "/upload/profil";

    public static int isDevelopment;
    public static boolean isDebuging;

    public static String getURL() {
        if (isDevelopment == 0) {
            return HTTP + DEVELOPMENT_URL;
        } else  if (isDevelopment == 1) {
            return HTTPS + PRODUCTION_URL;
        }
        else{
            return HTTP + DEVELOPMENT_URL;
        }
    }

    public static String getUrlFoto() {
        if (isDevelopment == 0) {
            return HTTP + DEVELOPMENT_URL + PATH_FOTO;
        } else  if (isDevelopment == 1) {
            return HTTPS + PRODUCTION_URL + PATH_FOTO;
        }
        else{
            return HTTP + DEVELOPMENT_URL + PATH_FOTO;
        }
    }

    public static String getAPIUrl() {
        return getURL();
    }

    public enum MODE {
        DEVELOPMENT, PRODUCTION
    }

    public static void setMode(MODE mode) {
        switch (mode) {
            case DEVELOPMENT:
                isDevelopment = 0;
                break;
            case PRODUCTION:
                isDevelopment = 1;
                break;
            default:
                isDevelopment = 0;
                break;
        }
    }
}
