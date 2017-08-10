package com.skyland.teotwsweibo.module.account;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.skyland.teotwsweibo.R;

import java.util.List;

/**
 * Created by skyland on 2017/7/15
 */

public class AccountListAdapter extends BaseQuickAdapter<AccountInfo, BaseViewHolder> {

    public AccountListAdapter(List<AccountInfo> data) {
        super(R.layout.lib_session_item_account, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountInfo item) {
        helper.setText(R.id.text_item_account_name, item.name);
        ImageView imageView = helper.getView(R.id.img_circle_item_account);
        Glide.with(mContext)
                .load(item.header_url)
                .into(imageView);


    }
}
