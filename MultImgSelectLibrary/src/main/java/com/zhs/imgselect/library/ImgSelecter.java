package com.zhs.imgselect.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ImgSelecter {
    private static ImgSelecter mInstance;
    private boolean isShowCamera;
    private boolean isMultChoose;
    private int maxCount;

    public static ImgSelecter init() {
        if (mInstance == null) {
            mInstance = new ImgSelecter();
        }
        return mInstance;
    }

    /**
     * 是否显示camera为第一个item
     *
     * @param isShowCamera
     * @return
     */
    public ImgSelecter showCamera(boolean isShowCamera) {
        this.isShowCamera = isShowCamera;
        return mInstance;
    }

    /**
     * 是否是多选
     *
     * @param isMultChoose
     * @return
     */
    public ImgSelecter setChooseMode(boolean isMultChoose) {
        this.isMultChoose = isMultChoose;
        return mInstance;
    }

    /**
     * 最多选择个数
     *
     * @param maxCount
     * @return
     */
    public ImgSelecter maxCount(int maxCount) {
        this.maxCount = maxCount;
        return mInstance;
    }

    /**
     * 打开多图片选择界面
     * @param activity
     * @param requestCode
     */
    public void startMultChooseActivity(AppCompatActivity activity, int requestCode) {
        if (activity == null || requestCode == -1) {
            throw new IllegalArgumentException("activity is can't null or request is can't -1");
        }
        activity.startActivityForResult(createIntent(activity), requestCode);
    }

    private Intent createIntent(Context context) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        intent.putExtra(ImgSelectConfig.EXTRA_IS_SHOW_CAMERA, this.isShowCamera);
        intent.putExtra(ImgSelectConfig.EXTRA_MAX_COUNT, this.maxCount);
        intent.putExtra(ImgSelectConfig.EXTRA_IS_MULT_CHOOSE, isMultChoose);
        return intent;
    }


}

