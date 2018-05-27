package com.deemons.androidserialport.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author： deemons
 * date:    2018/5/26
 * desc:
 */
public class MessageBean implements MultiItemEntity {

    public static final int TYPE_RECEIVE = 0;
    public static final int TYPE_SEND    = 1;

    private int mItemType;

    private String date;
    private String contain;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public MessageBean(int itemType) {
        mItemType = itemType;
    }

    public MessageBean(int itemType, String date, String contain) {
        mItemType = itemType;
        this.date = date;
        this.contain = contain;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }


}
