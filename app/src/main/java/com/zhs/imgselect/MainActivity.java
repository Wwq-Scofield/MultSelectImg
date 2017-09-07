package com.zhs.imgselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.zhs.imgselect.library.ImgSelecter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImgSelecter.init().maxCount(9).setChooseMode(true).showCamera(true).startMultChooseActivity(this,1);
    }
}
