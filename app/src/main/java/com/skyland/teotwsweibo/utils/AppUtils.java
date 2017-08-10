package com.skyland.teotwsweibo.utils;

import android.text.TextUtils;

import com.skyland.mylibrary.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by skyland on 2017/8/2
 */

public class AppUtils {

    private static AppUtils instance;

    public static AppUtils getDefault() {
        if (instance == null) {
            instance = new AppUtils();
        }
        return instance;
    }

    public String getWeiboTime(String setTime) {
        if (TextUtils.isEmpty(setTime)) {
            return "";
        }
        Date startDate = new Date(setTime);
        Date nowDate = Calendar.getInstance().getTime();
        return DateUtils.getDefault().twoDateDistance(startDate, nowDate);
    }

    public String getWeiboSource(String setSourceStr) {
        if (TextUtils.isEmpty(setSourceStr)) {
            return "";
        }
        int indexStart = setSourceStr.indexOf(">") + 1;
        int indexEnd = setSourceStr.indexOf("</a>");
        String sub = setSourceStr.substring(indexStart, indexEnd);
        if (TextUtils.equals(sub, "Weibo.intl")) {
            sub = "Weico";
        }
        return "From:" + sub;

    }


}
