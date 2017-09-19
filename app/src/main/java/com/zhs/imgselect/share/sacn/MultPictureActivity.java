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
import com.zhs.popmenu.PopMenuManager;

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
    private View rootView;
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
        rootView=findViewById(R.id.rl_root);
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

    private void showOutMenu() {
        PopMenuManager.getInstance().init(this, new PopMenuManager.Builder()
                .setFirstContent("相机")
                .setSecendContent("相册")
                .setThirdtContent("取消"), new PopMenuManager.OnViewClickListener() {
            @Override
            public void onMenuClick(int flag) {
                switch (flag){
                    case PopMenuManager.MENU_FIRST:

                        break;
                    case PopMenuManager.MENU_SECEND:

                        break;
                    case PopMenuManager.MENU_THIRD:

                        break;
                }

            }
        }).showOutMenu(rootView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
