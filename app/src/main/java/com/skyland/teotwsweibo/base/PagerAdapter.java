package com.skyland.teotwsweibo.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyland on 2017/7/20
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();
    private Context context;

    public PagerAdapter(FragmentManager setManager, Context setContext) {
        super(setManager);
        this.context = setContext;
    }

    public void addFragment(Fragment setFragment, String setTitle) {
        fragments.add(setFragment);
        fragmentTitles.add(setTitle);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
