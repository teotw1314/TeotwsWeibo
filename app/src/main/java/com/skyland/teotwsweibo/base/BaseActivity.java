package com.skyland.teotwsweibo.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.skyland.mylibrary.utils.CheckRomUtil;
import com.skyland.teotwsweibo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

/**
 * Created by skyland on 2017/7/12
 */

public abstract class BaseActivity extends AppCompatActivity {


    protected View stateBarPlace;
    protected Toolbar toolbar;
    private FrameLayout frameContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.base_activity);
        init();
        initContentView(savedInstanceState);
        initToolbar();
        initView();
        initLogic();
    }

    private void init() {
        stateBarPlace = findViewById(R.id.view_statue_bar_place);
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        frameContent = (FrameLayout) findViewById(R.id.frame_base_content);

        ViewGroup.LayoutParams params = stateBarPlace.getLayoutParams();
        params.height = getStatusBarHeight();
        stateBarPlace.setLayoutParams(params);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setImmersiveStatusBar(false);
    }

    @Override
    public void setContentView(int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        frameContent.addView(contentView);
    }

    /**
     * 设置ContentView
     */
    protected abstract void initContentView(Bundle savedInstanceState);

    protected abstract void initToolbar();

    protected abstract void initView();

    protected abstract void initLogic();


    /**
     * 设置沉浸式状态栏
     *
     * @param fontIconDark 状态栏字体和图标颜色是否为深色
     */
    protected void setImmersiveStatusBar(boolean fontIconDark) {
        setTranslucentStatus();
        if (fontIconDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    || CheckRomUtil.isMIUI()
                    || CheckRomUtil.isFlyme()) {
                setStatusBarFontIconDark(true);
            } else {
                if (ContextCompat.getColor(this, R.color.colorPrimary) == ContextCompat.getColor(this, android.R.color.white)) {
                    if (stateBarPlace != null) {
                        stateBarPlace.setBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusBarGrey));
                    }
                }
            }
        }
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    private void setStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    /**
     * 设置状态栏透明
     */
    private void setTranslucentStatus() {
        Window window =  getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(FLAG_TRANSLUCENT_STATUS);
            try {
                Class[] argsClass=new Class[]{int.class};
                Method setStatusBarColorMethod = Window.class.getMethod("setStatusBarColor",argsClass);
                setStatusBarColorMethod.invoke(window, Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            window.addFlags(FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
