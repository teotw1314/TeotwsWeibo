package com.skyland.teotwsweibo.module.timeline;

import com.skyland.sky_data.bean.StatusInfo;
import com.skyland.teotwsweibo.BasePresenter;
import com.skyland.teotwsweibo.BaseView;

import java.util.List;

/**
 * Created by skyland on 2017/8/11
 */
@SuppressWarnings("unused")
public interface TimelineContract {


    interface View extends BaseView<Presenter> {
        void notifyTimelineList(List<StatusInfo> statusInfos);
    }


    interface Presenter extends BasePresenter {

        void getTimelineList();

        void onClickSpan(WeiboTextUtils.SpanType spanType, String span);
    }

}
