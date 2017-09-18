package com.zhs.imgselect.share.sacn;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.app.imgselect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwq on 2017/3/5.
 */

public class MultPictureActivity extends AppCompatActivity {
    private int currentPos;
    private List<String> mListUrl;
    private ViewPager mViewPager;
    private TextView mTextView;
    private static final String MULTI_IMAGE_URL = "MULTI_IMAGE_URL";
    private static final String MULTI_IMAGE_POS = "MULTI_IMAGE_POS";
    public static void startActivity(Context context, int pos, ArrayList<String> list) {
        Intent intent = new Intent(context, MultPictureActivity.class);
        intent.putExtra(MULTI_IMAGE_POS, pos);
        intent.putStringArrayListExtra(MULTI_IMAGE_URL, list);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_mult_picture);
        mListUrl = getIntent().getStringArrayListExtra(MULTI_IMAGE_URL);
        currentPos = getIntent().getIntExtra(MULTI_IMAGE_POS, 0);
        mViewPager= (ViewPager) findViewById(R.id.picture_multi_pager);
        mTextView= (TextView) findViewById(R.id.picture_multi_pager_bottom);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MultiPictureFragment.newInstance(mListUrl.get(position));
            }
            @Override
            public int getCount() {
                return mListUrl.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTextView.setText(getString(R.string.picture_conut, position + 1, mListUrl.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(currentPos);
        mTextView.setText(getString(R.string.picture_conut, currentPos + 1, mListUrl.size()));

        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOutMenu();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public PopupWindow window = null;
    private void showOutMenu() {
        LayoutInflater layoutIn = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutIn.inflate(R.layout.pop_pic_delete, null);
        LinearLayout newpopwindow = (LinearLayout) view.findViewById(R.id.newpopwindow);
        newpopwindow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (window != null && window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
            }
        });
        Button btn1 = (Button) view.findViewById(R.id.popmenu_btn1);
        Button btn2 = (Button) view.findViewById(R.id.popmenu_btn2);
        Button btn3 = (Button) view.findViewById(R.id.popmenu_btn3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if (window == null) {
            window = new PopupWindow(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
            window.setBackgroundDrawable(new BitmapDrawable());
        }
        window.showAtLocation(findViewById(R.id.rl_root), Gravity.BOTTOM, 0, 0);
    }

}
