package com.zhs.imgselect.share.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhs.app.imgselect.R;
import com.zhs.imgselect.share.BaseRecycleViewAdapter;
import com.zhs.imgselect.share.bean.PhotoInfo;
import com.zhs.imgselect.share.bean.TeacherInfoItem;
import com.zhs.imgselect.share.sacn.MultPictureActivity;
import com.zhs.imgselect.share.view.ExpandTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ShareAdapter extends BaseRecycleViewAdapter {

    public static int TYPE_TEXT = 0;
    public static int TYPE_IMG = 1;

    private Context context;

    public ShareAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = 0;
//        if(position==0){
//            return -1;
//        }
        TeacherInfoItem item = (TeacherInfoItem) datas.get(position);
        if (TYPE_TEXT == item.getType()) {
            itemType = TYPE_TEXT;
        } else if (TYPE_IMG == (item.getType())) {
            itemType = TYPE_IMG;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType==-1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_share_header, parent, false);
            viewHolder=new HeaderViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_list_item, parent, false);
            viewHolder = new ImageViewHolder(view);
        }
        return viewHolder;
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        if(getItemViewType(position)==-1){
//            //HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
//            return;
//        }
        final int realPosition = position ;
        final TeacherViewHolder holder = (TeacherViewHolder) viewHolder;
        final TeacherInfoItem item= (TeacherInfoItem) datas.get(realPosition);
        String content=item.getContent();
        if(!TextUtils.isEmpty(content)){
            holder.contentTv.setExpand(item.isExpand());
            holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                @Override
                public void statusChange(boolean isExpand) {
                    item.setExpand(isExpand);
                }
            });
            holder.contentTv.setText(UrlUtils.formatUrlString(content));
        }else{
            holder.contentTv.setText("");
        }
        String address=item.getAddress();
        if(TextUtils.isEmpty(address)){
            holder.tvAddress.setText(address);
        }
        switch (holder.viewType) {
            case 0:
                break;
            case 1:
                if(holder instanceof ImageViewHolder){
                    final List<PhotoInfo> photos = item.getmPhotos();
                    if (photos != null && photos.size() > 0) {
                        ((ImageViewHolder)holder).multiImageView.setVisibility(View.VISIBLE);
                        ((ImageViewHolder)holder).multiImageView.setList(photos);
                        ((ImageViewHolder)holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                ArrayList<String> photoUrls = new ArrayList<String>();
                                for(PhotoInfo photoInfo : photos){
                                    photoUrls.add(photoInfo.url);
                                }
                                MultPictureActivity.startActivity(context,position,photoUrls);
                            }
                        });
                    } else {
                        ((ImageViewHolder)holder).multiImageView.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
