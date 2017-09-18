package com.zhs.imgselect.share.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class TeacherInfoItem implements Serializable{
    private String year;
    private String month;
    private List<PhotoInfo> mPhotos;
    private String content;
    private int type;
    private boolean isExpand;
    private String address;


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<PhotoInfo> getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(List<PhotoInfo> mPhotos) {
        this.mPhotos = mPhotos;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "TeacherInfoItem{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", mPhotos=" + mPhotos +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
