package com.zhs.imgselect.select;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhs.imgselect.MultiImageSelector;
import com.zhs.imgselect.MultiImageSelectorActivity;
import com.zhs.app.imgselect.R;
import com.zhs.imgselect.map.MapActivity;
import com.zhs.imgselect.select.adapter.SwipeAdapter;
import com.zhs.imgselect.share.ShareActivity;
import com.zhs.imgselect.share.adapter.DensityUtil;
import com.zhs.imgselect.share.view.DragGridView;
import com.zhs.imgselect.util.DensityUtils;
import com.zhs.imgselect.util.ImageUtils;
import com.zhs.imgselect.util.SdcardUtils;
import com.zhs.imgselect.util.ToastUtils;
import com.zhs.timepicker.util.PickerViewAnimateUtil;
import com.zhs.timepicker.view.TimePickerView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 图片拖拽 Activity
 * Created by kuyue on 2017/7/13 上午10:21.
 * 邮箱:595327086@qq.com
 **/
public class PostImagesActivity extends AppCompatActivity {

    public static final String FILE_DIR_NAME = "com.kuyue.wechatpublishimagesdrag";//应用缓存地址
    public static final String FILE_IMG_NAME = "images";//放置图片缓存
    public static final int IMAGE_SIZE = 9;//可添加图片最大数
    private static final int REQUEST_IMAGE = 1002;
    private static final int REQUEST_MAP = 1005;
    private List<String> originImages=new ArrayList<>();//原始图片
    private List<String> dragImages=new ArrayList<>();//压缩长宽后图片
    private Context mContext;
//    private PostArticleImgAdapter postArticleImgAdapter;
    private   SwipeAdapter mAdapter;
    private ItemTouchHelper itemTouchHelper;
//    private RecyclerView rcvImg;
    private DragGridView gridView;
    private TextView tvShowDelete;//删除区域提示
    private TextView tvAddress;
    private EditText mEvContent;
    private String address;
    private  String plusPath;
    public static int ADD_POSITION;
    private int number;
    private TextView tvLeftLength;
    private TextView tvSend;
    private RelativeLayout rlTimeChoose;

    private  TimePickerView pvTime;
    private TextView tvTimeSelect;
    public static void startPostActivity(Context context, ArrayList<String> images) {
        Intent intent = new Intent(context, PostImagesActivity.class);
        intent.putStringArrayListExtra("img", images);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_images);
        initData();
        initView();
    }

    private void initData() {
        mContext = getApplicationContext();
        dragImages = new ArrayList<>();
        InitCacheFileUtils.initImgDir(FILE_DIR_NAME, FILE_IMG_NAME);//清除图片缓存
        plusPath  = getString(R.string.glide_plus_icon_string) + getPackageName() + "/drawable/" + R.drawable.add_icon_img;
        originImages.add(plusPath);//添加按键，超过9张时在adapter中隐藏
        dragImages.addAll(originImages);
       if(getIntent().getStringArrayListExtra("img")!=null){
           originImages =getIntent().getStringArrayListExtra("img");
           Log.d("wwq","originImages.size: "+originImages.size());
           //添加按钮图片资源
           new Thread(new MyRunnable(dragImages, originImages, dragImages, myHandler, false)).start();//开启线程，在新线程中去压缩图片
       }
    }

    private void initView() {
        gridView = (DragGridView) findViewById(R.id.recycleView);
        tvShowDelete = (TextView) findViewById(R.id.tvShowDelete);
        tvAddress= (TextView) findViewById(R.id.tv_address);
        mEvContent= (EditText) findViewById(R.id.et_content);
        tvLeftLength= (TextView) findViewById(R.id.tv_left_lenghth);
        rlTimeChoose= (RelativeLayout) findViewById(R.id.rl_time_choose);
        tvSend= (TextView) findViewById(R.id.send);
        tvTimeSelect= (TextView) findViewById(R.id.tv_time_select);
        findViewById(R.id.rl_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PostImagesActivity.this, MapActivity.class),REQUEST_MAP);
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dragImages!=null){
                    Log.d("wwq","list: "+dragImages.toString());
                    ArrayList<String> mPic=new ArrayList<String>();
                    for (String pic:dragImages){
                        if(pic.contains("android.resource")){
                            continue;
                        }
                        mPic.add(pic);
                    }
                    Intent intent=new Intent(PostImagesActivity.this, ShareActivity.class);
                    intent.putStringArrayListExtra("picList",mPic);
                    intent.putExtra("content",mEvContent.getText()!=null?mEvContent.getText().toString():"");
                    intent.putExtra("address",address);
                    startActivity(intent);
//                    pics.clear();
                }else{

                }
            }
        });
        initRcv();
        number = 140 - mEvContent.getText().length();
        mEvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("wwq", "beforeTextChanged:" + s.toString());
                Log.d("wwq", "beforeTextChangedcount:" + count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("wwq", "onTextChanged:" + s.toString());
                Log.d("wwq", "onTextChangeddcount:" + count);
            }
            @Override
            public void afterTextChanged(Editable s) {
                number = 140 - s.length();
                tvLeftLength.setText(number + " " +"字");
                Log.d("wwq","afterTextChanged:" + s.toString());
                Log.d("wwq","afterTextChanged:" + s.length());
                if (s.length() <= 0 || (TextUtils.isEmpty(mEvContent.getText().toString().trim()))) {
                    tvSend.setTextColor(Color.parseColor("#60333333"));
                } else {
                    tvSend.setTextColor(Color.parseColor("#17c2e9"));
                }
            }
        });
        rlTimeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });


        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        // 控制时间范围
//		 Calendar calendar = Calendar.getInstance();
//		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//		 calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener()
        {
            @Override
            public void onTimeSelect(Date date)
            {
                tvTimeSelect.setText(PickerViewAnimateUtil.getTime(date));
            }
        });
    }

    private void initRcv() {
        mAdapter=new SwipeAdapter(this);
        gridView.setAdapter(mAdapter);
        mAdapter.setData(dragImages);
        gridView.setDragResponseMS(300);
        gridView.setDP(60);
        gridView.setOnChangeListener(new DragGridView.OnChanageListener() {
            @Override
            public void onChange(int from, int to) {
                //这里的处理需要注意下
                Log.d("wwq","form= "+from+" to="+to);
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(dragImages, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(dragImages, i, i-1);
                    }
                }
                dragImages.remove(plusPath);
                dragImages.add(plusPath);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDel(int form) {
                if(form!=-1){
                    dragImages.remove(form);
                    originImages.remove(form);
                }
                if (form!=-1&&dragImages.size()>0&&originImages.size()>0){
                    dragImages.remove(plusPath);
                    dragImages.add(plusPath);
                    ADD_POSITION=dragImages.size();
                    mAdapter.notifyDataSetChanged();
                }
                dismissAnim();
            }
            @Override
            public void onShowDel(int form) {
                tvShowDelete.setVisibility(View.VISIBLE);
                startAnim();
            }
            @Override
            public void onViewSelected(int flag) {
                if(flag==1){
                    tvShowDelete.setText("松手即可删除");
                }else if(flag==0){
                    tvShowDelete.setText(getResources().getString(R.string.post_delete_tv_d));
                }
            }
        });
        //事件监听
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("wwq","i: "+i+"   "+dragImages.get(i));
                if (dragImages.get(i).contains(getString(R.string.glide_plus_icon_string))) {//打开相册
                    MultiImageSelector.create()
                            .showCamera(true)
                            .count(IMAGE_SIZE - dragImages.size() + 1)
                            .multi()
                            .start(PostImagesActivity.this, REQUEST_IMAGE);
                } else {
                    ToastUtils.getInstance().show(MyApplication.getInstance().getContext(), "预览图片");
                }
            }
        });

    }
    private void startAnim() {
        int height=getResources().getDisplayMetrics().heightPixels;
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(height, height-DensityUtil.dip2px(this,50)-getStatusHeight(this)).setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();
                tvShowDelete.setY(value);
            }
        });
        valueAnimator.start();

    }
    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    private void dismissAnim() {
        int height=getResources().getDisplayMetrics().heightPixels;
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(tvShowDelete.getY(), height).setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();
                tvShowDelete.setY(value);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvShowDelete.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();
    }

    //------------------图片相关-----------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//从相册选择完图片
            //压缩图片
            new Thread(new MyRunnable(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT),
                    originImages, dragImages, myHandler, true)).start();
        }else if(requestCode ==REQUEST_MAP&&resultCode==RESULT_OK){
            if(data!=null){
                address=data.getStringExtra("address");
                tvAddress.setText(address);
            }
        }
    }

    /**
     * 另起线程压缩图片
     */
      class MyRunnable implements Runnable {

        List<String> images;
        List<String> originImages;
        List<String> dragImages;
        Handler handler;
        boolean add;//是否为添加图片

        public MyRunnable(List<String> images, List<String> originImages, List<String> dragImages, Handler handler, boolean add) {
            this.images = images;
            this.originImages = originImages;
            this.dragImages = dragImages;
            this.handler = handler;
            this.add = add;
        }

        @Override
        public void run() {
            SdcardUtils sdcardUtils = new SdcardUtils();
            String filePath;
            Bitmap newBitmap;
            int addIndex = originImages.size() - 1;
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).contains(MyApplication.getInstance().getString(R.string.glide_plus_icon_string))) {//说明是添加图片按钮
                    continue;
                }
                //压缩
                newBitmap = ImageUtils.compressScaleByWH(images.get(i),
                        DensityUtils.dp2px(MyApplication.getInstance().getContext(), 100),
                        DensityUtils.dp2px(MyApplication.getInstance().getContext(), 100));
                //文件地址
                filePath = sdcardUtils.getSDPATH() + FILE_DIR_NAME + "/"
                        + FILE_IMG_NAME + "/" + String.format("img_%d.jpg", System.currentTimeMillis());
                //保存图片
                ImageUtils.save(newBitmap, filePath, Bitmap.CompressFormat.JPEG, true);
                //设置值
                Log.d("wwq","addIndex="+addIndex);
                if (!add) {
                    images.set(i, filePath);
                } else {//添加图片，要更新
                    if(addIndex==-1){
                        continue;
                    }
                    dragImages.add(addIndex, filePath);
                    originImages.add(addIndex++, filePath);
                }
            }
            dragImages.remove(plusPath);
            dragImages.add(plusPath);
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    private MyHandler myHandler = new MyHandler(this);

    private   class MyHandler extends Handler {
        private WeakReference<Activity> reference;

        public MyHandler(Activity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Log.d("wwq","dragImages: "+dragImages.size()+dragImages.toString());
                        ADD_POSITION=dragImages.size();
                        mAdapter.setData(dragImages);
                        break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }

}
