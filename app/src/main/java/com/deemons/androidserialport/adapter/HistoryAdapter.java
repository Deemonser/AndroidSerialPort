package com.deemons.androidserialport.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deemons.androidserialport.R;
import java.util.List;

/**
 * author： deemons
 * date:    2018/5/31
 * desc:
 */
public class HistoryAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public HistoryAdapter(@Nullable List<String> data) {
        super(R.layout.rv_item_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_history, item);
    }
}
