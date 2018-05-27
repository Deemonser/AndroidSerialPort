package com.deemons.androidserialport.bean;

import android.support.annotation.DrawableRes;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.deemons.androidserialport.adapter.LeftAdapter;

/**
 * author： deemons
 * date:    2018/5/26
 * desc:
 */
public class LeftHeadBean extends AbstractExpandableItem<LeftDetailBean> implements
    MultiItemEntity {


    @DrawableRes
    public int imageRes;

    public String title;

    public String value;
    public String spKey;

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return LeftAdapter.TYPE_HEAD;
    }
}
