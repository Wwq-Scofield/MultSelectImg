package com.zhs.imgselect.share.adapter;

import android.view.View;
import android.view.ViewStub;

import com.zhs.app.imgselect.R;

/**
 * Created by suneee on 2016/8/16.
 */
public class ImageViewHolder extends TeacherViewHolder {
    /** 图片*/
    public MultiImageView multiImageView;

    public ImageViewHolder(View itemView){
        super(itemView, ShareAdapter.TYPE_IMG);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
        if(multiImageView != null){
            this.multiImageView = multiImageView;
        }
    }
}
