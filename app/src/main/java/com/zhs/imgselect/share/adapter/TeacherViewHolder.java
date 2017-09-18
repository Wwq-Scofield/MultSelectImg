package com.zhs.imgselect.share.adapter;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhs.app.imgselect.R;
import com.zhs.imgselect.share.view.ExpandTextView;


/**
 * Created by yiw on 2016/8/16.
 */
public abstract class TeacherViewHolder extends RecyclerView.ViewHolder  {

    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;

    public int viewType;

    public TextView tvYear;
    public TextView tvMonth;
    public TextView tvAddress;
    /** 动态的内容 */
    public ExpandTextView contentTv;
    public TeacherViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
        contentTv = (ExpandTextView) itemView.findViewById(R.id.contentTv);
        tvYear = (TextView) itemView.findViewById(R.id.tvYear);
        tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
        tvAddress= (TextView) itemView.findViewById(R.id.tv_address);
        initSubView(viewType, viewStub);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

}
