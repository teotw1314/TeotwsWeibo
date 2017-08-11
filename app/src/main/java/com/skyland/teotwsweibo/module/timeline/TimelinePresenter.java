package com.skyland.teotwsweibo.module.timeline;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skyland.sky_data.base.NetworkListener;
import com.skyland.sky_data.network.SkyNetwork;
import com.skyland.sky_data.result.StatusResult;
import com.skyland.teotwsweibo.app.WeiboApp;
import com.skyland.teotwsweibo.module.account.AccountInfo;
import com.skyland.teotwsweibo.utils.AccountUtils;


/**
 * Created by skyland on 2017/8/11
 */

public class TimelinePresenter implements TimelineContract.Presenter {

    private Context context;
    private TimelineContract.View timelineView;

    public TimelinePresenter(@NonNull Context context, @NonNull TimelineContract.View timelineView){
        this.context = context;
        this.timelineView = timelineView;

        this.timelineView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getTimelineList() {
        AccountInfo accountInfo = AccountUtils.getDefault().getCurrentAccount(WeiboApp.getInstance());

        SkyNetwork.getDefault().getTimeline(accountInfo.refresh_token, "0", "0", 20, 1, new NetworkListener<StatusResult>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(StatusResult statusResult) {
                timelineView.notifyTimelineList(statusResult.statuses);
            }
        });
    }

    @Override
    public void onClickSpan(WeiboTextUtils.SpanType spanType, String span) {

    }

}
