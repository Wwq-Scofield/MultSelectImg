package com.zhs.imgselect.share.sacn;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zhs.app.imgselect.R;
import com.zhs.imgselect.MultiImageSelectorFragment;

import java.io.File;

/**
 * Created by wwq on 2017/3/5.
 */
public class MultiPictureFragment extends Fragment {
    private String mImageUrl;
    private Bitmap mBitmap;
    private ImageView imageView;
    public static MultiPictureFragment newInstance(String imageUrl) {
        MultiPictureFragment f = new MultiPictureFragment();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mult_image_layout,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageView= (ImageView) view.findViewById(R.id.mMultPicture);
        if(mImageUrl.contains("emulated")){
        Log.d("wwq","mImageUrl: "+mImageUrl);
            Glide.with(getActivity()).load(new File(mImageUrl)).into(imageView);
        }else{
            Glide.with(getActivity()).load(mImageUrl).into(imageView);
        }
//        Picasso.with(getContext()).load(mImageUrl).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
