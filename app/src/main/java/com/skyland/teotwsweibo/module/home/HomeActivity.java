package com.skyland.teotwsweibo.module.home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.skyland.mylibrary.utils.DisplayUtils;
import com.skyland.teotwsweibo.R;
import com.skyland.teotwsweibo.base.PagerAdapter;
import com.skyland.teotwsweibo.module.timeline.TimelineFragment;
import com.skyland.teotwsweibo.utils.AccountUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by skyland on 2017/7/9
 * 状态栏沉浸部分: https://juejin.im/entry/57a42fb78ac247005f1bc948
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "home_activity";

    private static final String INTENT_KEY_UID = "intent_uid";

    private AppBarLayout appBarLayout;
    private View viewToolbarTop;
    private Toolbar toolbar;
    private CircleImageView imgvAvatar;
    private TextView tvToolbarTitle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TimelineFragment timelineFragment = new TimelineFragment();

    private PagerAdapter pagerAdapter;

    private ObjectAnimator showToolbarAnim;
    private ObjectAnimator hideToolbarAnim;
    private int toolbarHeight;
    private boolean isAppbarShown = true;

    public static void startActivity(Context setContext, String setUid) {
        Intent intent = new Intent(setContext, HomeActivity.class);
        intent.putExtra(INTENT_KEY_UID, setUid);
        setContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_home_activity);
        initView();
        initTransparentToolbar();
        initViewPager();
    }

    private void initView() {
        appBarLayout = (AppBarLayout) findViewById(R.id.lib_home_appbar);
        viewToolbarTop = findViewById(R.id.lib_home_view_toolbar_top);
        toolbar = (Toolbar) findViewById(R.id.lib_home_toolbar);
        imgvAvatar = (CircleImageView) findViewById(R.id.lib_home_toolbar_avatar);
        tvToolbarTitle = (TextView) findViewById(R.id.lib_home_toolbar_title);

        drawerLayout = (DrawerLayout) findViewById(R.id.lib_home_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.lib_home_nav_layout);
        navigationView.setNavigationItemSelectedListener(this);

        Glide.with(this).load(AccountUtils.getDefault().getCurrentAccount(this).header_url).into(imgvAvatar);
        tvToolbarTitle.setText("weibomm");
    }


    /**
     * 沉浸状态栏
     */
    private void initTransparentToolbar() {

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            drawerLayout.setClipToPadding(false);
        }

        //toolbar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);     //设置layout布局可以在状态栏里面显示

        ViewGroup.LayoutParams layoutParams = viewToolbarTop.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayUtils.getStatusBarHeight(this);
        viewToolbarTop.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        toolbarHeight = DisplayUtils.dp2px(this, 46);
        lp.height = toolbarHeight;
        toolbar.setLayoutParams(lp);
    }

    /**
     * 处理viewpager
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager_lib_home);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(timelineFragment, "timeline");
        viewPager.setAdapter(pagerAdapter);
    }


    /**
     * 获取状态栏高度
     *
     * @return
     */


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /**
     * 双击退出应用
     */
    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private boolean isExit = false;

    private void exitBy2Click() {
        Timer timer;
        if (!isExit) {
            isExit = true;
            Toast.makeText(HomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
