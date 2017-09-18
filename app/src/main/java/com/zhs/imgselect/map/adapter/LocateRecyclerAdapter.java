package com.zhs.imgselect.map.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhs.app.imgselect.R;
import com.zhs.imgselect.map.bean.LocationInfo;
import com.zhs.imgselect.share.BaseRecycleViewAdapter;
import com.zhs.imgselect.share.adapter.MultiImageView;

import java.util.List;



public class LocateRecyclerAdapter extends BaseRecycleViewAdapter   {
    public int TYPE_HEAD=0;
    private int TYPE_CONTENT=1;

    private Context context;
    public LocateRecyclerAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEAD;
        }
        return TYPE_CONTENT;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view=null;
        if(viewType == TYPE_HEAD){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.locate_head_item, parent, false);
            viewHolder = new HeadHolder(view);
        }else{
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.locate_info_item, parent, false);
            viewHolder = new ContentHolder(view);
        }
        final RecyclerView.ViewHolder finalViewHolder = viewHolder;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onItemClick(view, finalViewHolder.getAdapterPosition());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_HEAD){
            HeadHolder holder1= (HeadHolder) holder;
            LocationInfo locationInfo = (LocationInfo) datas.get(0);
            if(locationInfo.isSelect()){
                holder1.ivSelect.setVisibility(View.VISIBLE);
            }else{
                holder1.ivSelect.setVisibility(View.GONE);
            }
        }else if(getItemViewType(position)==TYPE_CONTENT){
            ContentHolder holder1= (ContentHolder) holder;
            LocationInfo locationInfo = (LocationInfo) datas.get(position);
            if(position==1){
                holder1.tvCity.setText(locationInfo.getCity());
                holder1.tvAddress.setVisibility(View.GONE);
            }else{
                holder1.tvCity.setText(locationInfo.getTitle());
                holder1.tvAddress.setVisibility(View.VISIBLE);
            }
            holder1.tvAddress.setText(locationInfo.getAddress());
            if(locationInfo.isSelect()){
                holder1.ivSelect.setVisibility(View.VISIBLE);
            }else{
                holder1.ivSelect.setVisibility(View.GONE);
            }
        }

    }

    public void setSelect(int position){
        for (LocationInfo locationInfo:(List<LocationInfo>)datas){
            locationInfo.setSelect(false);
        }
        ((List<LocationInfo>)datas).get(position).setSelect(true);
//        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ((List<LocationInfo>)datas).size();
    }
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
