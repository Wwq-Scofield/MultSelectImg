package com.zhs.imgselect.share;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.app.imgselect.R;
import com.zhs.imgselect.MultiImageSelectorActivity;
import com.zhs.imgselect.select.PostImagesActivity;
import com.zhs.imgselect.share.adapter.DensityUtil;
import com.zhs.imgselect.share.adapter.ShareAdapter;
import com.zhs.imgselect.share.bean.PhotoInfo;
import com.zhs.imgselect.share.bean.TeacherInfoItem;
import com.zhs.imgselect.share.view.ObservableScrollView;
import com.zhs.imgselect.utils.FileUtils;
import com.zhs.popmenu.PopMenuManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.zhs.imgselect.select.PostImagesActivity.REQUEST_IMAGE;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ShareActivity extends AppCompatActivity implements ObservableScrollView.ScrollViewListener {
    private RecyclerView recyclerView;
    private View rootView;
    private LinearLayoutManager manager;
    private ShareAdapter adapter;
    private List<TeacherInfoItem> mDatas;
    private List<PhotoInfo> mPhotos = new ArrayList<>();
    private ArrayList<String> mPics=new ArrayList<>();
    private String  content;
    private  String address;
    private RelativeLayout rlHeader;
    private ObservableScrollView scrollView;
    private TextView tvName;
    private RelativeLayout rlbackground;
    private LinearLayout llCenterView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llEmpty;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_main);
        mPics=getIntent().getStringArrayListExtra("picList");
        content=getIntent().getStringExtra("content");
        address=getIntent().getStringExtra("address");
        initView();
    }
    private int rlHeadHight;
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        rlHeader= (RelativeLayout) findViewById(R.id.rlHeader);
        scrollView= (ObservableScrollView) findViewById(R.id.scrollView);
        tvName= (TextView) findViewById(R.id.tvName);
        rlbackground= (RelativeLayout) findViewById(R.id.rlbackground);
        llCenterView= (LinearLayout) findViewById(R.id.ll_center_view);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        llEmpty= (LinearLayout) findViewById(R.id.rl_empty);
        rootView=findViewById(R.id.rootView);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new ShareAdapter(this);
        recyclerView.setAdapter(adapter);
        mDatas = new ArrayList<>();
        adapter.setDatas(mDatas);
//        add(0);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setColorSchemeResources(R.color.color_8290AF,R.color.colorAccent);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if(mDatas==null||mDatas.size()<=0){
                            recyclerView.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                        }else{
                            recyclerView.setVisibility(View.VISIBLE);
                            llEmpty.setVisibility(View.GONE);
                        }
                        Random random = new Random();
                        int i = random.nextInt(5);
                        add(i);
                        adapter.notifyDataSetChanged();
                    }

                },2000);
            }
        });
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
                        Log.d("wwq","--123--");
                    }
                }
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                llEmpty.setVisibility(View.VISIBLE);
            }
        },2000);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            rlHeadHight = rlHeader.getHeight()-DensityUtil.dip2px(ShareActivity.this,20);
            scrollView.setScrollViewListener(ShareActivity.this);
        }
    }

    public void AddData(View vc) {
        showOutMenu();
    }
    private void showOutMenu() {
        PopMenuManager.getInstance().init(this, new PopMenuManager.Builder()
                .setFirstContent("相机")
                .setSecendContent("相册")
                .setThirdtContent("取消"), new PopMenuManager.OnViewClickListener() {
            @Override
            public void onMenuClick(int flag) {
                switch (flag){
                    case PopMenuManager.MENU_FIRST:
                        showCameraAction();
                        break;
                    case PopMenuManager.MENU_SECEND:
                        PostImagesActivity.startPostGallery(ShareActivity.this,REQUEST_IMAGE);
                        break;
                    case PopMenuManager.MENU_THIRD:
                        break;
                }

            }
        }).showOutMenu(rootView);
    }

    File mTmpFile;
    /**
     * Open camera
     */
    private void showCameraAction() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            Log.d("wwq","no permission");
        }else {
            Log.d("wwq","has permission");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                try {
                    mTmpFile = FileUtils.createTmpFile(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                    startActivityForResult(intent, 111);
                } else {
                    Toast.makeText(this, com.zhs.imgselect.R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, com.zhs.imgselect.R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("wwq","requestCode:"+requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111&&resultCode==RESULT_OK){
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTmpFile)));
            PostImagesActivity.startPostActivity(this,mTmpFile.getAbsolutePath());
        }else if(requestCode==REQUEST_IMAGE&&resultCode==RESULT_OK){
            PostImagesActivity.startPostGallery(this,data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
        }
    }

    private void add(int i) {
        TeacherInfoItem teacherInfoItem = new TeacherInfoItem();
        teacherInfoItem.setYear("2017");
        teacherInfoItem.setMonth("Auguest");
        teacherInfoItem.setContent("");
        teacherInfoItem.setType(1);
        mPhotos.clear();
        if(mPics!=null){
            for (int m=0;m<mPics.size();m++){
                PhotoInfo p1 = new PhotoInfo();
                p1.url = mPics.get(m);
                p1.w = 700;
                p1.h = 467;
                mPhotos.add(p1);
            }
        }
        teacherInfoItem.setContent(content);
        teacherInfoItem.setAddress(address);
        teacherInfoItem.setmPhotos(mPhotos);
        mDatas.add(teacherInfoItem);
        if (i == 0) {
            adapter.setDatas(mDatas);
        } else {
            adapter.getDatas().addAll(mDatas);
        }
    }
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.d("wwq","y: "+y);
        if (y <= 0) {
            rlHeader.setAlpha(1);
            llCenterView.setAlpha(0);
            rlbackground.setAlpha(0);
        } else if (y > 0 && y <= rlHeadHight) {
            float scale = (float) y / rlHeadHight;
            float alpha = (scale);
            llCenterView.setAlpha(alpha);
            rlbackground.setAlpha(alpha);
            rlHeader.setAlpha(1-alpha);
        } else {
            llCenterView.setAlpha(1);
            rlbackground.setAlpha(1);
            rlHeader.setAlpha(0);
        }
    }
}
