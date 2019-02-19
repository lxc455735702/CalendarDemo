package com.example.calendardemo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.calendardemo.R;

import java.util.List;

/**
 * Created by lxc on 2019/2/19
 * e-mail ：18867762063@163.com
 */
public class ContentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ContentAdapter(@Nullable List<String> data) {
        super(R.layout.item_content, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.item_content,item);
    }
}
