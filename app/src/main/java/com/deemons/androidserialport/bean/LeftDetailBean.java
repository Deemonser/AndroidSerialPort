package com.deemons.androidserialport.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.deemons.androidserialport.adapter.LeftAdapter;

/**
 * author： deemons
 * date:    2018/5/26
 * desc:
 */
public class LeftDetailBean implements MultiItemEntity {

    public String item;

    public boolean isCheck;

    public LeftDetailBean(String item) {
        this.item = item;
    }

    public LeftDetailBean(String item, boolean isCheck) {
        this.item = item;
        this.isCheck = isCheck;
    }

    @Override
    public int getItemType() {
        return LeftAdapter.TYPE_DETAIL;
    }
}
