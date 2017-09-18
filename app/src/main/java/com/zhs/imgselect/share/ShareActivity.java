package com.zhs.imgselect.share;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.app.imgselect.R;
import com.zhs.imgselect.share.adapter.DensityUtil;
import com.zhs.imgselect.share.adapter.ShareAdapter;
import com.zhs.imgselect.share.adapter.UrlUtils;
import com.zhs.imgselect.share.bean.PhotoInfo;
import com.zhs.imgselect.share.bean.TeacherInfoItem;
import com.zhs.imgselect.share.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ShareActivity extends AppCompatActivity implements ObservableScrollView.ScrollViewListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ShareAdapter adapter;
    private List<TeacherInfoItem> mDatas;
    private List<PhotoInfo> mPhotos = new ArrayList<>();
//    private RelativeLayout rlHead;
//    private TextView tvSchool;
//    private ImageView ivHeader;
//    private TextView tvName;
//    private int mMinHight;
//    private int mOrignHight;
    private ArrayList<String> mPics=new ArrayList<>();
    private String  content;
    private  String address;
    private RelativeLayout rlHeader;
    private ObservableScrollView scrollView;
    private TextView tvName;
    private RelativeLayout rlbackground;
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
//        rlHead = (RelativeLayout) findViewById(R.id.rl_title);
//        tvSchool= (TextView) findViewById(R.id.tv_school);
//        ivHeader= (ImageView) findViewById(R.id.ivHeader);
//        tvName= (TextView) findViewById(R.id.tvName);

        ViewTreeObserver vto = rlHeader.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlHeader.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                rlHeadHight = rlHeader.getHeight()-DensityUtil.dip2px(ShareActivity.this,20);
                scrollView.setScrollViewListener(ShareActivity.this);
            }
        });
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new ShareAdapter(this);
        recyclerView.setAdapter(adapter);
        mDatas = new ArrayList<>();
        adapter.setDatas(mDatas);
        add(0);
        adapter.notifyDataSetChanged();


//        mOrignHight = rlHead.getLayoutParams().height;
//        mMinHight = DensityUtil.dip2px(this,60);
//        mNeedDistance = mOrignHight - mMinHight;
//        Log.d("wwq", "rlHead.getHeight: " + rlHead.getLayoutParams().height);
//        RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) tvSchool.getLayoutParams();
//        mTextLeft = textParams.leftMargin;
//        mTextTop = textParams.topMargin;
//
//        RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) ivHeader.getLayoutParams();
//        ivTop = ivParams.topMargin;
//        RelativeLayout.LayoutParams tvNameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
//        tvNameTop = tvNameParams.topMargin;
//        tvNameLeft=tvNameParams.leftMargin;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        });
    }


    public void AddData(View vc) {
        Random random = new Random();
        int i = random.nextInt(5);
        add(i);
//        adapter.setDatas(mDatas);
        adapter.notifyDataSetChanged();
    }

    private void add(int i) {
        TeacherInfoItem teacherInfoItem = new TeacherInfoItem();
        teacherInfoItem.setYear("2017");
        teacherInfoItem.setMonth("Auguest");
        teacherInfoItem.setContent("");
        teacherInfoItem.setType(1);
        mPhotos.clear();
        for (int m=0;m<mPics.size();m++){
            PhotoInfo p1 = new PhotoInfo();
            p1.url = mPics.get(m);
            p1.w = 700;
            p1.h = 467;
            mPhotos.add(p1);
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

    private int mLastY = 0;  //最后的点
    private static int mNeedDistance;   // 需要滑动的距离
    private int mCurrentDistance;
    private VelocityTracker mVelocityTracker;
    private static final int SNAP_VELOCITY = 400;

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.d("wwq","y: "+y);
        if (y <= 0) {
            rlHeader.setAlpha(1);
            tvName.setAlpha(0);
            rlbackground.setAlpha(0);
        } else if (y > 0 && y <= rlHeadHight) {
            float scale = (float) y / rlHeadHight;
            float alpha = (scale);
            tvName.setAlpha(alpha);
            rlbackground.setAlpha(alpha);
            rlHeader.setAlpha(1-alpha);
        } else {
            tvName.setAlpha(1);
            rlbackground.setAlpha(1);
            rlHeader.setAlpha(0);
//            tvName.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
        }
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        mVelocityTracker.addMovement(ev);
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastY = (int) ev.getY();
//                Log.d("wwq", "event down");
//                return super.dispatchTouchEvent(ev);
//            case MotionEvent.ACTION_MOVE:
//                int y = (int) ev.getY();
//                int dy = mLastY - y;
////                if(dy<=0){
////                    return super.dispatchTouchEvent(ev); //把事件传递进去
////                }
//                Log.d("wwq", "mCurrentDistance:" + mCurrentDistance);
//                if (mCurrentDistance <= 0 && dy < 0) {
//                    return super.dispatchTouchEvent(ev); //把事件传递进去
//                }
//                if (mCurrentDistance >= mNeedDistance && dy > 0) {
//                    return super.dispatchTouchEvent(ev); //把事件传递进去
//                }
//                changeView(dy);
//                mLastY = y;
//                break;
//            case MotionEvent.ACTION_UP:
//
//                if (mVelocityTracker != null) {
//                    final VelocityTracker velocityTracker = mVelocityTracker;
//                    velocityTracker.computeCurrentVelocity(1000);
//                    int velocityY = (int) velocityTracker.getYVelocity();
//                    if (velocityY > SNAP_VELOCITY){//下划
//                        Log.d("wwq","下滑");
//
//                    } else if (velocityY < -SNAP_VELOCITY){//上划
//                        Log.d("wwq","上划");
//                        startAnim();
////                        int width=getResources().getDisplayMetrics().widthPixels;
////                        final RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) tvSchool.getLayoutParams();
////                        textParams.leftMargin = (int) (width/3);
////                        int mTextNeedMoveDistanceY =DensityUtil.dip2px(this,40);
////                        textParams.topMargin = (int) (mTextTop-mTextNeedMoveDistanceY);
////                        tvSchool.setLayoutParams(textParams);
////                        tvSchool.setAlpha(1);
//                    } else {
//
//                    }
//                    checkTheHeight();
//                    mVelocityTracker.recycle();
//                    mVelocityTracker = null;
//                }
//                return super.dispatchTouchEvent(ev);
//
//        }
//        return false;
//    }

//    private void startAnim() {
//        final int width=getResources().getDisplayMetrics().widthPixels;
//        final int mTextNeedMoveDistanceY =DensityUtil.dip2px(this,40);
//        final RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
//        final int mTextNeedMoveDistanceX1 = width / 4;
//        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0,1).setDuration(500);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float value= (float) valueAnimator.getAnimatedValue();
//
//
//                nameParams.leftMargin = (int) (mTextNeedMoveDistanceX1 * (value));
//                nameParams.topMargin = (int) (mTextNeedMoveDistanceY/2 * value);
//                tvName.setLayoutParams(nameParams);
//                ivHeader.setAlpha(1-value);
//            }
//        });
//        valueAnimator.start();
//
//    }

//    private float mRate;
//    private void changeView(int dy) {
//
//        final ViewGroup.LayoutParams layoutParams = rlHead.getLayoutParams();
//        layoutParams.height = layoutParams.height - dy;
//        rlHead.setLayoutParams(layoutParams);
//        checkTheHeight();
//        rlHead.requestLayout();
//        mCurrentDistance = mOrignHight - rlHead.getLayoutParams().height;
//        mRate = (float) (mCurrentDistance * 1.0 / mNeedDistance);
////        tvSchool.setAlpha(mRate);
//
//        int width=getResources().getDisplayMetrics().widthPixels;
//        int mTextNeedMoveDistanceX = width / 3 + DensityUtil.dip2px(this,20);
//        int mTextNeedMoveDistanceY =DensityUtil.dip2px(this,40);
//        final RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) tvSchool.getLayoutParams();
//        textParams.leftMargin = (int) (mTextNeedMoveDistanceX * mRate);
//        textParams.topMargin = (int) (mTextNeedMoveDistanceY * mRate);
//        tvSchool.setLayoutParams(textParams);
//
//
//        final RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) ivHeader.getLayoutParams();
////        ivParams.leftMargin = (int) (DensityUtil.dip2px(this,100) * mRate);
//        ivParams.topMargin = (int) (ivTop-mTextNeedMoveDistanceY * mRate);
//        ivHeader.setLayoutParams(ivParams);
//        ivHeader.setAlpha(1-mRate);
//
//        int mTextNeedMoveDistanceX1 = width / 4;
//        final RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
//        nameParams.leftMargin = (int) (mTextNeedMoveDistanceX1 * (mRate));
//        nameParams.topMargin = (int) (mTextNeedMoveDistanceY/2 * mRate);
//        tvName.setLayoutParams(nameParams);
////        tvName.setScaleX((float) (0.7*(1-mRate)));
////        tvName.setScaleY((float) (0.7*(1-mRate)));
//        Log.d("wwq", "mRate: " + (1-mRate));
//
//    }
//
//    /**
//     * 检查上边界和下边界
//     */
//    private void checkTheHeight() {
//        final ViewGroup.LayoutParams layoutParams = rlHead.getLayoutParams();
//        if (layoutParams.height < mMinHight) {
//            layoutParams.height = mMinHight;
//            rlHead.setLayoutParams(layoutParams);
//            rlHead.requestLayout();
//        }
//        if (layoutParams.height > mOrignHight) {
//            layoutParams.height = mOrignHight;
//            rlHead.setLayoutParams(layoutParams);
//            rlHead.requestLayout();
//        }
//
//    }
}
