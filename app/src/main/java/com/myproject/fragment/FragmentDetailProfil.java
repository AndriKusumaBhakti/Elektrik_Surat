package com.myproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.myproject.R;
import com.myproject.activity.CropImageActivity;
import com.myproject.api.TaskUpdateFoto;
import com.myproject.aplication.APP;
import com.myproject.aplication.Config;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelResponse;
import com.myproject.model.request.RequestPhoto;
import com.myproject.model.response.ResponseLogin;
import com.myproject.util.CircleTransform;
import com.myproject.util.Constants;
import com.myproject.util.FunctionUtil;
import com.myproject.util.PictureUtil;
import com.myproject.util.StringUtil;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.mime.TypedFile;

public class FragmentDetailProfil extends BaseFragment implements View.OnClickListener{
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final static int ACTIVITY_PHOTO_RESULT = 21;
    final static int ACTIVITY_IMAGE_CROPPED = 22;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    ExpandableLayout exp_biodata, exp_pendidikan, exp_pekerjaan;
    TextView profile_name, nik, name, alamat, ttl, status, kwg, pendi, kerja;
    TextView biodata, pendidikan, pekerjaan;
    ImageView profile_home_avatar;
    private static final int REQUEST_CODE_IMAGE = 10001;
    private String photoFilePaths;
    private Uri photoSelected;
    private final LoadingDialog loading = new LoadingDialog();

    private String filePathDatas;

    public FragmentDetailProfil() {}

    public static FragmentDetailProfil newInstance() {
        return newInstance("","");
    }

    public static FragmentDetailProfil newInstance(String param1, String param2) {
        FragmentDetailProfil fragment = new FragmentDetailProfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(getBaseActivity());
        profiles = account.getAllLanguage();
        for(int i = 0; i<profiles.size(); i++){
            if ((profiles.get(i).getNik()).equals(APP.getStringPref(getBaseActivity(), Preference.NIK))){
                accountEntity = profiles.get(i);
            }
        }
    }

    @Override
    public void initView(View view) {
        exp_biodata = (ExpandableLayout) view.findViewById(R.id.exp_biodata);
        exp_pendidikan = (ExpandableLayout) view.findViewById(R.id.exp_pendidikan);
        exp_pekerjaan = (ExpandableLayout) view.findViewById(R.id.exp_pekerjaan);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        nik = (TextView) view.findViewById(R.id.nik);
        name = (TextView) view.findViewById(R.id.name);
        alamat = (TextView) view.findViewById(R.id.alamat);
        ttl = (TextView) view.findViewById(R.id.ttl);
        status = (TextView) view.findViewById(R.id.status);
        kwg = (TextView) view.findViewById(R.id.kwg);
        pendi = (TextView) view.findViewById(R.id.pendi);
        kerja = (TextView) view.findViewById(R.id.kerja);
        biodata = (TextView) view.findViewById(R.id.biodata);
        pendidikan = (TextView) view.findViewById(R.id.pendidikan);
        pekerjaan = (TextView) view.findViewById(R.id.pekerjaan);
        profile_home_avatar = (ImageView) view.findViewById(R.id.profile_home_avatar);
        profile_home_avatar.setOnClickListener(this);
        if (!StringUtil.checkNullString(accountEntity.getNik()).isEmpty()){
            nik.setText(accountEntity.getNik());
        }
        if (!StringUtil.checkNullString(accountEntity.getNama()).isEmpty()){
            name.setText(accountEntity.getNama());
            profile_name.setText(accountEntity.getNama());
        }else{
            profile_name.setVisibility(View.INVISIBLE);
        }
        if (!StringUtil.checkNullString(accountEntity.getAlamat()).isEmpty()){
            alamat.setText(accountEntity.getAlamat());
        }
        String ttlahir = null;
        if (!StringUtil.checkNullString(accountEntity.getTempat()).isEmpty()){
            ttlahir = accountEntity.getNik();
        }
        if (ttlahir != null) {
            if (!StringUtil.checkNullString(accountEntity.getTanggal()).isEmpty()) {
                ttlahir = ttlahir + ", " + accountEntity.getTanggal();
            }
            ttl.setText(ttlahir);
        }else{
            if (!StringUtil.checkNullString(accountEntity.getTanggal()).isEmpty()) {
                ttl.setText(accountEntity.getTanggal());
            }
        }
        if (!StringUtil.checkNullString(accountEntity.getStatus()).isEmpty()){
            status.setText(accountEntity.getStatus());
        }
        if (!StringUtil.checkNullString(accountEntity.getKewarganegaraan()).isEmpty()){
            kwg.setText(accountEntity.getKewarganegaraan());
        }
        if (!StringUtil.checkNullString(accountEntity.getPendidikan()).isEmpty()){
            pendi.setText(accountEntity.getPendidikan());
        }
        if (!StringUtil.checkNullString(accountEntity.getPekerjaan()).isEmpty()){
            kerja.setText(accountEntity.getPekerjaan());
        }
        Glide.with(getActivity())
                .load(Config.getUrlFoto()+accountEntity.getFoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .transform(new CircleTransform(getBaseActivity()))
                .into(profile_home_avatar);
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                getFragmentManager().popBackStack();
            }

            @Override
            public void onRightIconClick() {

            }

            @Override
            public void onRight2IconClick() {

            }
        });
        biodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exp_biodata.isExpanded()){
                    exp_biodata.collapse(true);
                }else{
                    exp_biodata.expand(true);
                    exp_pendidikan.collapse(true);
                    exp_pekerjaan.collapse(true);
                }
            }
        });
        pendidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exp_pendidikan.isExpanded()){
                    exp_pendidikan.collapse(true);
                }else{
                    exp_pendidikan.expand(true);
                    exp_biodata.collapse(true);
                    exp_pekerjaan.collapse(true);
                }
            }
        });
        pekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exp_pekerjaan.isExpanded()){
                    exp_pekerjaan.collapse(true);
                }else{
                    exp_pekerjaan.expand(true);
                    exp_biodata.collapse(true);
                    exp_pendidikan.collapse(true);
                }
            }
        });
    }

    @Override
    public void updateUI() {
        getBaseActivity().setLeftIcon(R.drawable.back_white);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
    }

    @Override
    public String getPageTitle() {
        return "Detail Profil";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_detail_profil;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_home_avatar){
            selectImage();
        }
    }

    private void selectImage() {
        final ArrayList<String> items = new ArrayList<String>();
        items.add(getResources().getString(R.string.take_photo));
        items.add(getResources().getString(R.string.take_gallery));

        SelectionDialogFragment photoChooser = new SelectionDialogFragment();

        photoChooser.setTitle(getResources().getString(R.string.take_photo_method));

        photoChooser.setItems(items, new SelectionDialogFragment.SelectionDialogFragmenListener() {
            @Override
            public void onSelection(String selection) {
                photoSelected = null;
                photoFilePaths = "";

                if (selection.equals(getResources().getString(R.string.take_photo))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(ContextCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getBaseActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getBaseActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                                    !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_ASK_PERMISSIONS_CAMERA);
                            } else {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                } else if (selection.equals(getResources().getString(R.string.take_gallery))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getBaseActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getBaseActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                    !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_ASK_PERMISSIONS_STORAGE);
                            } else {
                                openGallery();
                            }
                        } else {
                            openGallery();
                        }
                    } else {
                        openGallery();
                    }
                }
            }
        });

        photoChooser.show(getFragmentManager(), "");
    }

    private void openCamera(){
        try {
            String newImages = "Md_Tr_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + Config.CACHE_FOLDER);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File fileImage = new File(dir, newImages);

            Uri photoURI = FileProvider.getUriForFile(getBaseActivity(), getBaseActivity().getApplicationContext().getPackageName() + ".my.package.name.provider", fileImage);

            photoSelected = photoURI;
            photoFilePaths = fileImage.getPath();

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoSelected);
            cameraIntent.putExtra("return-data", true);

            FragmentDetailProfil.this.startActivityForResult(cameraIntent, ACTIVITY_PHOTO_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        photoFilePaths = "";

        FragmentDetailProfil.this.startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_file)), ACTIVITY_PHOTO_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case ACTIVITY_PHOTO_RESULT://
                if (resultCode == Activity.RESULT_OK) {
                    if (photoSelected == null) {
                        if (imageReturnedIntent != null && imageReturnedIntent.getData() != null) {
                            photoSelected = imageReturnedIntent.getData();
                        } else {
                            return;
                        }
                    }

                    if (photoSelected != null) {
                        Uri mUri = PictureUtil.getImageRotated(getBaseActivity(),photoSelected,photoFilePaths);
                        filePathDatas = FunctionUtil.getRealPathFromURI(getBaseActivity(),mUri);

                        Intent formIntent = new Intent(getActivity(), CropImageActivity.class);
                        formIntent.setData(mUri);
                        startActivityForResult(formIntent, ACTIVITY_IMAGE_CROPPED);
                    }
                }
                break;
            case ACTIVITY_IMAGE_CROPPED:
                if (resultCode == Activity.RESULT_OK){
                    Bundle extras = imageReturnedIntent.getExtras();
                    if (extras != null) {
                        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();

                        String fname = extras.getString("fileName");
                        File file = new File(root, fname);

                        photoSelected = Uri.fromFile(file);
                        photoFilePaths = file.getPath();

                        APP.logError("FOTO SELECTED : "+photoSelected);
                        setAvatarImg();

                        APP.setPreference(getActivity().getBaseContext(), Preference.PHOTOPATH, file.getPath());
                    }
                }
                break;
        }
    }

    public void setAvatarImg() {
        if(photoSelected!=null){
            loading.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
            RequestPhoto photo = new RequestPhoto();
            photo.setMethod("uploadImg");
            photo.setNik_penduduk(String.valueOf(accountEntity.getNik()));
            if(photoSelected != null){
                File photoFile = new File(photoFilePaths);
                TypedFile avatar = new TypedFile("image/*", photoFile);
                photo.setFoto(avatar);
            }
            TaskUpdateFoto task = new TaskUpdateFoto(getBaseActivity()) {
                @Override
                protected void onSuccess(ResponseLogin response) {
                    removeTask(this);
                    if(isFragmentSafety()) {
                        if (loading.isResumed()) {
                            loading.dismiss();
                        }
                        if (response.isSukses()){
                            getBaseActivity().showAlertDialog(getResources().getString(R.string.alert), response.getMessage());
                            APP.setPreference(getBaseActivity(), Preference.TOKEN, String.valueOf(response.getResult().getToken()));
                            APP.setPreference(getBaseActivity(), Preference.ROLE, String.valueOf(response.getResult().getId_user()));
                            APP.setPreference(getBaseActivity(), Preference.NIK, String.valueOf(response.getResult().getNik_penduduk()));
                            Account akun = new Account(getBaseActivity());
                            akun.deleteAllInterval();
                            akun.parseAccount(response.getResult());
                            Glide.with(getActivity())
                                    .load(Config.getUrlFoto()+response.getResult().getFoto())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.no_user)
                                    .error(R.drawable.no_user)
                                    .transform(new CircleTransform(getBaseActivity()))
                                    .into(profile_home_avatar);
                        }else{
                            getBaseActivity().showAlertDialog(getResources().getString(R.string.alert), response.getMessage());
                        }
                    }
                }

                @Override
                protected void onFailed(String message) {
                    removeTask(this);
                    if(isFragmentSafety()) {
                        if (loading.isResumed()) {
                            loading.dismiss();
                        }
                        getBaseActivity().showAlertDialog(getResources().getString(R.string.alert), message);
                    }
                }
            };
            registerTask(task);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, photo);
        }else{
            getBaseActivity().showAlertDialog(getResources().getString(R.string.alert), "Foto tidak ditemukan");
        }
    }
}
