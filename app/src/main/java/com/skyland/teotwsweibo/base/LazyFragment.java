package com.skyland.teotwsweibo.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by skyland on 2017/7/20
 */

public abstract class LazyFragment extends Fragment {

    protected boolean isVisible;
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(),null);
        initView(mRootView);
        return mRootView;
    }

    /**
     * 懒加载实现
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else{
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        initLogic();
    }
    protected void onInvisible(){

    }
    protected abstract @LayoutRes int getLayoutId();
    protected abstract void initView(View rootView);
    protected abstract void initLogic();
}
