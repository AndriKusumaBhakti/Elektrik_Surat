package com.myproject.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ScreenUtil {
    private static ScreenUtil screenUtil;

    public static ScreenUtil getInstance() {
        if (screenUtil == null) {
            screenUtil = new ScreenUtil();
        }
        return screenUtil;
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public DisplayMetrics getDensityScreen(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public int getDPISize(int value, float scale) {
        return (int) (value * scale + 0.5f);
    }

    public static int getListViewHeightBasedOnChildrenWithMaxSize(ListView listView, int maxHeightSize) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, (View) listView.getChildAt(i), listView);
            listItem.measure(desiredWidth, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        int finalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        if (finalHeight > maxHeightSize) {
            finalHeight = maxHeightSize;
        }

        return finalHeight;
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}

