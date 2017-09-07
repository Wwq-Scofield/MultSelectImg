package com.zhs.imgselect.library;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.zhs.imgselect.library.bean.Folder;
import com.zhs.imgselect.library.bean.Image;
import com.zhs.imgselect.library.listener.ImgChooseListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class MultiImageSelectorActivity extends AppCompatActivity implements ImgChooseListener, View.OnClickListener {

    private int maxCount;
    private boolean isMultChoose;
    private boolean isShowCamera;
    private Button btnSelect;
    private ArrayList<String> resultList = new ArrayList<>();
    private boolean hasFolderGened = false;
    // folder result data set
    private ArrayList<Folder> mResultFolder = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_choose);
        final Intent intent = getIntent();
        if(intent==null){
            throw  new  IllegalArgumentException("MultiImageSelectorActivity: intent is null");
        }
        maxCount = intent.getIntExtra(ImgSelectConfig.EXTRA_MAX_COUNT, ImgSelectConfig.DEFAULT_COUNT);
        isMultChoose=intent.getBooleanExtra(ImgSelectConfig.EXTRA_IS_MULT_CHOOSE,false);
        isShowCamera= intent.getBooleanExtra(ImgSelectConfig.EXTRA_IS_SHOW_CAMERA, false);
        btnSelect= (Button) findViewById(R.id.commit);
        btnSelect.setOnClickListener(this);
        getSupportLoaderManager().restartLoader(ImgSelectConfig.LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onSingleImageSelected(String path) {

    }

    @Override
    public void onImageSelected(String path) {

    }

    @Override
    public void onImageUnselected(String path) {

    }

    @Override
    public void onCameraShot(File imageFile) {

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.commit){
            if(resultList != null && resultList.size() >0){
                // Notify success
                Intent data = new Intent();
                data.putStringArrayListExtra(ImgSelectConfig.EXTRA_RESULT_LIST, resultList);
                setResult(RESULT_OK, data);
            }else{
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }



    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = null;
            if(id == ImgSelectConfig.LOADER_ALL) {
                cursorLoader = new CursorLoader(MultiImageSelectorActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? ",
                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            }else if(id ==  ImgSelectConfig.LOADER_CATEGORY){
                cursorLoader = new CursorLoader(MultiImageSelectorActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[0]+" like '%"+args.getString("path")+"%'",
                        null, IMAGE_PROJECTION[2] + " DESC");
            }
            return cursorLoader;
        }

        private boolean fileExist(String path){
            if(!TextUtils.isEmpty(path)){
                return new File(path).exists();
            }
            return false;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                if (data.getCount() > 0) {
                    List<Image> images = new ArrayList<>();
                    data.moveToFirst();
                    do{
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        if(!fileExist(path)){continue;}
                        Image image = null;
                        if (!TextUtils.isEmpty(name)) {
                            image = new Image(path, name, dateTime);
                            images.add(image);
                        }
                        if( !hasFolderGened ) {
                            // get all folder data
                            File folderFile = new File(path).getParentFile();
                            if(folderFile != null && folderFile.exists()){
                                String fp = folderFile.getAbsolutePath();
                                Folder f = getFolderByPath(fp);
                                if(f == null){
                                    Folder folder = new Folder();
                                    folder.name = folderFile.getName();
                                    folder.path = fp;
                                    folder.cover = image;
                                    List<Image> imageList = new ArrayList<>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    mResultFolder.add(folder);
                                }else {
                                    f.images.add(image);
                                }
                            }
                        }

                    }while(data.moveToNext());
                    Log.d("wwq","imgs: "+images.toString());
                }else{
                    Log.d("wwq","data is null");
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private Folder getFolderByPath(String path){
        if(mResultFolder != null){
            for (Folder folder : mResultFolder) {
                if(TextUtils.equals(folder.path, path)){
                    return folder;
                }
            }
        }
        return null;
    }
}
