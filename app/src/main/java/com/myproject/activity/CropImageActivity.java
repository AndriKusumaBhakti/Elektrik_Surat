package com.myproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.isseiaoki.simplecropview.CropImageView;
import com.myproject.R;
import com.myproject.base.OnActionbarListener;
import com.myproject.util.PictureUtil;

import java.io.File;

public class CropImageActivity extends BaseActivity{
    private Button cropImage;
    private Uri imageUri;
    private CropImageView imageCrops;
    private Bundle bundle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //pengecekan bundle harus ditaruh sebelum manggil super!!
        if(savedInstanceState != null){
            bundle = savedInstanceState;
        }

        imageUri = getIntent().getData();

        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
        cropImage = (Button) findViewById(R.id.crop_btn);
        imageCrops = (CropImageView) findViewById(R.id.cropImageView);
        toolbar = (Toolbar) findViewById(R.id.form_toolbar);

        imageCrops.setCropMode(CropImageView.CropMode.RATIO_1_1);
        setSupportActionBar(toolbar);

        if(bundle != null){
            return;
        }
    }

    @Override
    public void setUICallbacks() {
        setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                onBackPressed();
            }

            @Override
            public void onRightIconClick() {

            }

            @Override
            public void onRight2IconClick() {

            }
        });


        cropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();

                Bitmap image = imageCrops.getCroppedBitmap();

                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();

                String fname = "CROP_MT-" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                File file = PictureUtil.BitmapToFile(image, root, fname);

                i.putExtra("fileName",fname);
                //i.putExtra("file",file);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.crop_image_layout;
    }

    @Override
    public void updateUI() {
        setLeftIcon(R.drawable.back_white);
        setRightIcon2(0);
        setRightIcon(0);
        setActionBarTitle(getResources().getString(R.string.crop_image));

        if(imageUri!= null){
            //Glide.with(context).load(imageUri).into(imageCrops);

            /*
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_image)
                    .dontAnimate()
                    .error(R.drawable.placeholder_image)
                    .into(imageCrops);
            */

            try{
                imageCrops.setImageBitmap(PictureUtil.getBitmapFromUri(this,imageUri));
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}