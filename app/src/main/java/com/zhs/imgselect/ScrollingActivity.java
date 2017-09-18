package com.zhs.imgselect;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.zhs.app.imgselect.R;

public class ScrollingActivity extends AppCompatActivity {
    CollapsingToolbarLayout ctl_title;
    Toolbar toolBarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ctl_title= (CollapsingToolbarLayout) findViewById(R.id.ctl_title);
        toolBarTitle= (Toolbar) findViewById(R.id.tl_title);
        setSupportActionBar(toolBarTitle);
        ctl_title.setCollapsedTitleGravity(Gravity.CENTER);
        ctl_title.setExpandedTitleGravity(Gravity.CENTER);
        ctl_title.setTitle("手机安全卫士");
        ctl_title.setExpandedTitleColor(Color.WHITE);
        ctl_title.setCollapsedTitleTextColor(Color.WHITE);
    }
}
