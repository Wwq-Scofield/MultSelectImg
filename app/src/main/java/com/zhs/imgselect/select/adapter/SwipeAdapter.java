package com.zhs.imgselect.select.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhs.app.imgselect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class SwipeAdapter extends BaseAdapter {

    private ArrayList<String> mDatas;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public SwipeAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    public void setData(ArrayList<String> list){
        this.mDatas=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mDatas==null){
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view=mLayoutInflater.inflate(R.layout.item_post_activity,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) view.findViewById(R.id.sdv);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        if (position >=9) {//图片已选完时，隐藏添加按钮
            viewHolder.imageView.setVisibility(View.GONE);
        } else {
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(mDatas.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.imageView);
        return view;
    }

    public static class ViewHolder{
        private ImageView imageView;
    }


}
