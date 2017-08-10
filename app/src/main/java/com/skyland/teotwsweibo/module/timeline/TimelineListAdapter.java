package com.skyland.teotwsweibo.module.timeline;

import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.skyland.sky_data.bean.StatusInfo;
import com.skyland.teotwsweibo.R;
import com.skyland.teotwsweibo.WeiboApp;
import com.skyland.teotwsweibo.utils.AppUtils;

import java.util.List;

/**
 * Created by skyland on 2017/7/20
 */

public class TimelineListAdapter extends BaseQuickAdapter<StatusInfo, BaseViewHolder> {

    public TimelineListAdapter(List<StatusInfo> data) {
        super(R.layout.lib_timeline_item_weibo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StatusInfo item) {

        ImageView avatar = helper.getView(R.id.item_weibo_imgv_avatar);
        Glide.with(WeiboApp.getInstance()).load(item.user.avatar_hd).into(avatar);

        helper.setText(R.id.item_weibo_tv_name, item.user.name);
        helper.setText(R.id.item_weibo_tv_time, AppUtils.getDefault().getWeiboTime(item.created_at));
        helper.setText(R.id.item_weibo_tv_from, AppUtils.getDefault().getWeiboSource(item.source));

        helper.setText(R.id.item_weibo_tv_repost_count, String.valueOf(item.reposts_count));
        helper.setText(R.id.item_weibo_tv_comment_count, String.valueOf(item.comments_count));
        helper.setText(R.id.item_weibo_tv_like_count, String.valueOf(item.attitudes_count));

        TextView tvContent = helper.getView(R.id.item_weibo_tv_content);
        SpannableStringBuilder content = WeiboTextUtils.getDefault()
                .getSpanContent(mContext, tvContent, item.text, ContextCompat.getColor(mContext, R.color.text_color_timeline_content_span));
        tvContent.setText(content);


    }
}
