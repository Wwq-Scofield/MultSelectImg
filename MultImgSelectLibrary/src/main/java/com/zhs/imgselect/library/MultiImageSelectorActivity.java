package com.zhs.imgselect.library;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.imgselect.library.adapter.FolderAdapter;
import com.zhs.imgselect.library.adapter.ImageGridAdapter;
import com.zhs.imgselect.library.bean.Folder;
import com.zhs.imgselect.library.bean.Image;
import com.zhs.imgselect.library.listener.ImgChooseListener;
import com.zhs.imgselect.library.util.FileUtil;
import com.zhs.imgselect.library.util.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zhs.imgselect.library.ImgSelectConfig.LOADER_ALL;
import static com.zhs.imgselect.library.ImgSelectConfig.REQUEST_CAMERA;

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
    private GridView photiogrid;
    private ImageGridAdapter mAdapter;
    private TextView tvShowPopFolder;
    private RelativeLayout footer;
    private ListPopupWindow folderWindow;
    private FolderAdapter folderAdapter;
    private File  mTmpFile;
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

        initView();

        getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private void initView() {
        btnSelect= (Button) findViewById(R.id.commit);
        tvShowPopFolder= (TextView) findViewById(R.id.category_btn);
        footer= (RelativeLayout) findViewById(R.id.footer);
        btnSelect.setOnClickListener(this);
        tvShowPopFolder.setOnClickListener(this);
        photiogrid= (GridView) findViewById(R.id.photiogrid);
        mAdapter=new ImageGridAdapter(this,isShowCamera,3);
        photiogrid.setAdapter(mAdapter);


        photiogrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mAdapter.isShowCamera()) {
                    if (i == 0) {
                        showCameraAction();
                    } else {
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image);
                    }
                } else {
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image);
                }
            }
        });

        folderAdapter = new FolderAdapter(this);

    }
    /**
     * Open camera
     */
    private void showCameraAction() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "mis_permission_rationale_write_storage",
                    123);
        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                try {
                    mTmpFile=FileUtil.createTmpFile(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(this,"mis_error_image_not_exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"mis_msg_no_camera", Toast.LENGTH_SHORT).show();
            }
        }
    }



    /**
     * notify callback
     * @param image image data
     */
    private void selectImageFromGrid(Image image ) {
        if(image != null) {
            if(isMultChoose) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                        onImageUnselected(image.path);
                } else {
                    if(maxCount == resultList.size()){
                        Toast.makeText(this,"mis_msg_amount_limit", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resultList.add(image.path);
                        onImageSelected(image.path);
                }
                mAdapter.select(image);
            }else if(!isMultChoose){
                  onSingleImageSelected(image.path);
            }
        }
    }



    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(final String permission, String rationale, final int requestCode){
        if(shouldShowRequestPermissionRationale(permission)){
            new AlertDialog.Builder(this)
                    .setTitle("mis_permission_dialog_title")
                    .setMessage(rationale)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton("cancel", null)
                    .create().show();
        }else{
            requestPermissions(new String[]{permission}, requestCode);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CAMERA){
            if(resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                      onCameraShot(mTmpFile);
                }
            }else{
                // delete tmp file
                while (mTmpFile != null && mTmpFile.exists()){
                    boolean success = mTmpFile.delete();
                    if(success){
                        mTmpFile = null;
                    }
                }
            }
        }

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
        Log.d("wwq","imgeFile: "+imageFile!=null?imageFile.getAbsolutePath():null);

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
        }else if(R.id.category_btn==id){
            if(folderWindow == null){
                createPopupFolderList();
            }

            if (folderWindow.isShowing()) {
                folderWindow.dismiss();
            } else {
                folderWindow.show();
                int index = folderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderWindow.getListView().setSelection(index);
            }
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
            if(id == LOADER_ALL) {
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
                    mAdapter.setData(images);
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


    /**
     * Create popup ListView
     */
    private void createPopupFolderList() {
        Point point = ScreenUtils.getScreenSize(MultiImageSelectorActivity.this);
        int width = point.x;
        int height = (int) (point.y * (4.5f/8.0f));
        folderWindow = new ListPopupWindow(MultiImageSelectorActivity.this);
        folderWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        folderWindow.setAdapter(folderAdapter);
        folderWindow.setContentWidth(width);
        folderWindow.setWidth(width);
        folderWindow.setHeight(height);
        folderWindow.setAnchorView(footer);
        folderWindow.setModal(true);
        folderWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                folderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        folderWindow.dismiss();

                        if (index == 0) {
                            MultiImageSelectorActivity.this.getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            tvShowPopFolder.setText("All Images");
                            if (isShowCamera) {
                                mAdapter.setShowCamera(true);
                            } else {
                                mAdapter.setShowCamera(false);
                            }
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mAdapter.setData(folder.images);
                                tvShowPopFolder.setText(folder.name);
                                if (resultList != null && resultList.size() > 0) {
                                    mAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mAdapter.setShowCamera(false);
                        }

                        photiogrid.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }


}
