package com.zhs.imgselect.share.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/11.
 */

public class PhotoInfo implements Serializable {

    public String url;
    public int w;
    public int h;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "url='" + url + '\'' +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}
