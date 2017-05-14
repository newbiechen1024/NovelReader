package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.ui.adapter.view.DownloadView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

/**
 * Created by newbiechen on 17-5-12.
 */

public class DownLoadAdapter extends BaseListAdapter<DownloadTaskBean>{
    @Override
    protected View createView(Context context, int viewType) {
        return new DownloadView(context);
    }
}
