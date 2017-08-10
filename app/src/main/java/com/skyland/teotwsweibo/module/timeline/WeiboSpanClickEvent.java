package com.skyland.teotwsweibo.module.timeline;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by skyland on 2017/8/10
 */

public class WeiboSpanClickEvent {

    public WeiboTextUtils.SpanType spanType = WeiboTextUtils.SpanType.Unset;
    public String text;

    public static void postEvent(WeiboTextUtils.SpanType setSpanType, String setText){
        WeiboSpanClickEvent event = new WeiboSpanClickEvent();
        event.spanType = setSpanType;
        event.text = setText;
        EventBus.getDefault().post(event);
    }

    private WeiboSpanClickEvent(){

    }

}
