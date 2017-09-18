package com.zhs.imgselect.map.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhs.app.imgselect.R;


/**
 * Created by Administrator on 2017/9/12.
 */

public class ContentHolder extends RecyclerView.ViewHolder   {
    public TextView tvCity;
    public TextView tvAddress;
    public ImageView ivSelect;
    public ContentHolder(View view) {
        super(view);
        tvCity= (TextView) view.findViewById(R.id.title);
        tvAddress= (TextView) view.findViewById(R.id.details_address);
        ivSelect= (ImageView) view.findViewById(R.id.iv_select);
    }



}
