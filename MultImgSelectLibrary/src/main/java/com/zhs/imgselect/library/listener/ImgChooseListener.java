package com.zhs.imgselect.library.listener;

import java.io.File;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface ImgChooseListener {
    void onSingleImageSelected(String path);
    void onImageSelected(String path);
    void onImageUnselected(String path);
    void onCameraShot(File imageFile);
}
