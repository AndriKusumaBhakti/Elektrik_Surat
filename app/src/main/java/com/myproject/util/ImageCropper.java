package com.myproject.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.myproject.aplication.APP;

import java.util.ArrayList;
import java.util.List;

public class ImageCropper {
    private Context mContext;
    private int mOutputX;
    private int mOutputY;
    private boolean mScale;
    private int mAspectX;
    private int mAspectY;
    private Uri mUri;

    private final ArrayList<DataHolder> ImageApplications = new ArrayList<DataHolder>();

    public ImageCropper(Context p_context) {
        mContext = p_context;
    }

    public ImageCropper setOutput(int p_x, int p_y) {
        mOutputX = p_x;
        mOutputY = p_y;
        mScale = true;
        return this;
    }

    public ImageCropper setAspect(int p_x, int p_y) {
        mAspectX = p_x;
        mAspectY = p_y;
        return this;
    }

    public ImageCropper setData(Uri p_uri) {
        //mUri = p_uri;
        ImageLoader loaderImage = new ImageLoader(mContext);
        mUri = loaderImage.decodeUriFile(p_uri);
        APP.log("URINYA ADALAH"+mUri);

        return this;
    }

    public ImageCropper setScale(boolean p_scale) {
        mScale = p_scale;
        return this;
    }

    public ImageCropper create() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setData(mUri);
        intent.putExtra("outputX", mOutputX);
        intent.putExtra("outputY", mOutputY);
        intent.putExtra("aspectX", mAspectX);
        intent.putExtra("aspectY", mAspectY);
        intent.putExtra("scale", mScale);
        intent.putExtra("return-data", true);
        List<ResolveInfo> list = getResolveInfo();

        APP.log("CROPPER SIZE : "+list.size());
        for (int x = 0; x < list.size(); x++) {
            ResolveInfo reso_info = list.get(x);
            DataHolder holder = new DataHolder();
            holder.mLabel = mContext.getPackageManager().getApplicationLabel(reso_info.activityInfo.applicationInfo).toString();
            holder.mIcon = mContext.getPackageManager().getApplicationIcon(reso_info.activityInfo.applicationInfo);
            holder.mIntent = new Intent(intent).setComponent(new ComponentName(reso_info.activityInfo.packageName, reso_info.activityInfo.name));
            ImageApplications.add(holder);
        }
        return this;
    }

    public final int size() {
        return ImageApplications.size();
    }

    public final DataHolder get(int p_index) {
        return ImageApplications.get(p_index);
    }

    private List<ResolveInfo> getResolveInfo() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        return mContext.getPackageManager().queryIntentActivities(intent, 0);
    }

    public static class DataHolder {

        protected String mLabel;
        protected Intent mIntent;
        protected Drawable mIcon;

        public final String getLabel() {
            return mLabel;
        }

        public final Intent getIntent() {
            return mIntent;
        }

        public final Drawable getIcon() {
            return mIcon;
        }

        public final Bitmap getIconBitmap() {
            return mIcon == null
                    ? null
                    : ((BitmapDrawable) mIcon).getBitmap();
        }

    }
}
