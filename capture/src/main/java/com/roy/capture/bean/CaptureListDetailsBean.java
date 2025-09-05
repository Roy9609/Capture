package com.roy.capture.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class CaptureListDetailsBean implements MultiItemEntity {

    private String GeneralTitle;
    private String title;
    private String value;
    private int type;
    private boolean isResponseFormat; // 响应结果是否为格式化

    public boolean isResponseFormat() {
        return isResponseFormat;
    }

    public void setResponseFormat(boolean responseFormat) {
        isResponseFormat = responseFormat;
    }

    public String getGeneralTitle() {
        return GeneralTitle;
    }

    public void setGeneralTitle(String generalTitle) {
        GeneralTitle = generalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
