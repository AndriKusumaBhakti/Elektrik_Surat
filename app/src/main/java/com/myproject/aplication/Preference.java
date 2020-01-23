package com.myproject.aplication;

/**
 * Created by frensky on 7/2/15.
 */
public class Preference {

    public static final String TOKEN = "token";
    public static final String NIK = "nik";
    public static final String ROLE = "role";
    public static final String ONE_SIGNAL_USER_ID = "one_signal_user_id";
    public static final String ONE_SIGNAL_REG_ID = "one_signal_reg_id";
    public static final String PHOTOPATH = "PhotoPaths";


    private static String getPrefKey(int StringID) {
        return APP.getContext().getResources().getString(StringID);
    }

}
