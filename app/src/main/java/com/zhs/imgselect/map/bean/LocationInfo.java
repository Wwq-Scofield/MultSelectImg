package com.zhs.imgselect.map.bean;

import java.io.Serializable;

/**
 * Created by 木子饼干 on 2016/11/9.
 */

public class LocationInfo implements Serializable{
    private String city;
    private String title;
    private String address;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
