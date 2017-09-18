package com.zhs.imgselect.map.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhs.app.imgselect.R;

/**
 * Created by Administrator on 2017/9/12.
 */

public class HeadHolder extends RecyclerView.ViewHolder {
    public ImageView ivSelect;
    public HeadHolder(View headView) {
        super(headView);
        ivSelect= (ImageView) headView.findViewById(R.id.iv_select);
    }
}
