package com.skyland.teotwsweibo.module.timeline;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.skyland.sky_data.bean.StatusInfo;
import com.skyland.teotwsweibo.R;
import com.skyland.teotwsweibo.base.LazyFragment;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyland on 2017/7/20
 */

public class TimelineFragment extends LazyFragment implements TimelineContract.View{

    private static final String TAG = "timeline";

    private TimelineContract.Presenter presenter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;


    private LinearLayoutManager layoutManager;
    private TimelineListAdapter timelineAdapter;
    private List<StatusInfo> statusInfoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.lib_timeline_fragment;
    }

    @Override
    protected void initView(View rootView) {
//        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout_lib_timeline);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_lib_timeline);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        timelineAdapter = new TimelineListAdapter(statusInfoList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(timelineAdapter);
    }

    @Override
    protected void initLogic() {
        presenter = new TimelinePresenter(getContext(), this);
        presenter.start();
        presenter.getTimelineList();
    }

    @Override
    public void setPresenter(TimelineContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickSpan(WeiboSpanClickEvent event) {
        Log.e(TAG, "onClickSpan: " + event.spanType.toString() + " __ " + event.text);
        presenter.onClickSpan(event.spanType, event.text);
    }


    @Override
    public void notifyTimelineList(List<StatusInfo> statusInfos) {
        Log.e(TAG, "refreshTimelineSuccess: " + statusInfos.size());
        for (int i = 0; i < statusInfos.size(); i++) {
            Log.e(TAG, "refreshTimelineSuccess: " + statusInfos.get(i).text);
        }
        statusInfoList.clear();
        statusInfoList.addAll(statusInfos);
        timelineAdapter.notifyDataSetChanged();
    }
}
