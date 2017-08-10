package com.skyland.teotwsweibo.module.timeline;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by skyland on 2017/8/2
 */

public class WeiboTextUtils {

    public enum SpanType {
        Unset,
        Url,
        User,
        Subject
    }

    public interface OnClickSpan {
        void onClick(SpanType spanType, String s);
    }

    private static WeiboTextUtils instance;

    public static WeiboTextUtils getDefault() {
        if (instance == null) {
            instance = new WeiboTextUtils();
        }
        return instance;
    }

    public SpannableStringBuilder getSpanContent(Context setContext, TextView setTextView, final String setContent, int setSpanColor) {
        if (TextUtils.isEmpty(setContent)) {
            return null;
        }

        SpannableStringBuilder spannableString = new SpannableStringBuilder(setContent);
        final SpannableString spannableString_temp = new SpannableString(setContent);

        ////////////////////////////////////////////////开始匹配
        //用户
        String schemeUser = "user";
        Linkify.addLinks(spannableString_temp, Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}"), schemeUser);

        //网页链接1
        String schemeUrl1 = "http://";
        Linkify.addLinks(spannableString_temp, Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]"), schemeUrl1);

        // 网页链接2
        String scheme2 = "https://";
        Linkify.addLinks(spannableString_temp, Pattern.compile("https://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]"), scheme2);

        // 手机号码 (13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}
        String schemePhone = "phone";
        Linkify.addLinks(spannableString_temp, Pattern.compile("(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\\\d{8}"), schemePhone);

        //话题
        String schemeSubject = "subject";
        Linkify.addLinks(spannableString_temp, Pattern.compile("#[^#]+#"), schemeSubject);
        ////////////////////////////////////////////////结束匹配

        URLSpan[] spans = spannableString_temp.getSpans(0, spannableString_temp.length(), URLSpan.class);


        for (URLSpan span : spans) {
            final int start = spannableString_temp.getSpanStart(span);
            final int end = spannableString_temp.getSpanEnd(span);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(setSpanColor);
            spannableString.setSpan(new WeiboClickSpan(setContent.substring(start, end)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            setTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return spannableString;
    }

    class WeiboClickSpan extends ClickableSpan {
        private String text;

        public WeiboClickSpan(String setText) {
            super();
            this.text = setText;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            SpanType spanType = SpanType.Unset;
            if (isUser(text)) {
                spanType = SpanType.User;
                text = text.replace("@", "");
            } else if (isSubject(text)) {
                spanType = SpanType.Subject;
                text = text.replaceAll("#", "");
            } else if (isUrl(text)) {
                spanType = SpanType.Url;
            }
            WeiboSpanClickEvent.postEvent(spanType, text);
        }
    }

    private boolean isUser(String setText) {
        if (TextUtils.isEmpty(setText)) {
            return false;
        } else {
            SpannableString temp = new SpannableString(setText);
            String schemeUser = "user";
            return Linkify.addLinks(temp, Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}"), schemeUser);
        }
    }

    private boolean isUrl(String setText) {
        if (TextUtils.isEmpty(setText)) {
            return false;
        } else {
            boolean isUrl1;
            boolean isUrl2;
            SpannableString temp = new SpannableString(setText);

            String schemeUrl1 = "http://";
            isUrl1 = Linkify.addLinks(temp, Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]"), schemeUrl1);

            String scheme2 = "https://";
            isUrl2 = Linkify.addLinks(temp, Pattern.compile("https://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]"), scheme2);

            return isUrl1 | isUrl2;
        }
    }

    private boolean isSubject(String setText) {
        if (TextUtils.isEmpty(setText)) {
            return false;
        } else {
            String schemeSubject = "subject";
            return Linkify.addLinks(new SpannableString(setText), Pattern.compile("#[^#]+#"), schemeSubject);
        }
    }


}
