package com.myproject.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Hashtable;

public class TextUtil {
    public static final String FONT_TYPE = "ProximaNova-Regular.otf";
    private static TextUtil textHelper;
    private Context context;

    public static TextUtil getInstance(Context context) {
        if (textHelper == null) {
            textHelper = new TextUtil();
        }
        textHelper.context = context;
        return textHelper;
    }

    public void setFont(ViewGroup group) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), FONT_TYPE);
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v);
        }
    }

    public String limitString(String value, int length) {
        StringBuilder buf = new StringBuilder(value);
        if (buf.length() > length) {
            buf.setLength(length);
            buf.append(" ...");
        }
        return buf.toString();
    }

    private static final Hashtable<String, Typeface> mCache = new Hashtable<String, Typeface>();

    public static Typeface loadFontFromAssets(Context context) {
        String fontName = FONT_TYPE;
        synchronized (mCache) {
            if (! mCache.containsKey(fontName)) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                mCache.put(fontName, typeface);
            }
            return mCache.get(fontName);
        }

    }
}
