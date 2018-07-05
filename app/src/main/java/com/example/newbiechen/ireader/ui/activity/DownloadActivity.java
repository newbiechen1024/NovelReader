package com.example.newbiechen.ireader.ui.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.service.DownloadService;
import com.example.newbiechen.ireader.ui.adapter.DownLoadAdapter;
import com.example.newbiechen.ireader.ui.base.BaseActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-11.
 * 下载面板
 */

public class DownloadActivity extends BaseActivity implements DownloadService.OnDownloadListener{
    private static final String TAG = "DownloadActivity";
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;

    private DownLoadAdapter mDownloadAdapter;

    private ServiceConnection mConn;
    private DownloadService.IDownloadManager mService;
    @Override
    protected int getContentId() {
        return R.layout.activity_refresh_list;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("下载列表");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
    }

    private void setUpAdapter(){
        mDownloadAdapter = new DownLoadAdapter();
        mRvContent.addItemDecoration(new DividerItemDecoration(this));
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.setAdapter(mDownloadAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mDownloadAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //传递信息
                    DownloadTaskBean bean = mDownloadAdapter.getItem(pos);
                    switch (bean.getStatus()){
                        //准备暂停
                        case DownloadTaskBean.STATUS_LOADING:
                            mService.setDownloadStatus(bean.getTaskName(),DownloadTaskBean.STATUS_PAUSE);
                            break;
                        //准备暂停
                        case DownloadTaskBean.STATUS_WAIT:
                            mService.setDownloadStatus(bean.getTaskName(),DownloadTaskBean.STATUS_PAUSE);
                            break;
                        //准备启动
                        case DownloadTaskBean.STATUS_PAUSE:
                            mService.setDownloadStatus(bean.getTaskName(),DownloadTaskBean.STATUS_WAIT);
                            break;
                        //准备启动
                        case DownloadTaskBean.STATUS_ERROR:
                            mService.setDownloadStatus(bean.getTaskName(),DownloadTaskBean.STATUS_WAIT);
                            break;
                    }
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();

        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = (DownloadService.IDownloadManager) service;
                //添加数据到队列中
                mDownloadAdapter.addItems(mService.getDownloadTaskList());

                mService.setOnDownloadListener(DownloadActivity.this);

                mRefreshLayout.showFinish();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        //绑定
        bindService(new Intent(this, DownloadService.class), mConn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }

    @Override
    public void onDownloadChange(int pos, int status, String msg) {
        DownloadTaskBean bean = mDownloadAdapter.getItem(pos);
        bean.setStatus(status);
        if (DownloadTaskBean.STATUS_LOADING == status){
            bean.setCurrentChapter(Integer.valueOf(msg));
        }
        mDownloadAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onDownloadResponse(int pos, int status) {
        DownloadTaskBean bean = mDownloadAdapter.getItem(pos);
        bean.setStatus(status);
        mDownloadAdapter.notifyItemChanged(pos);
    }
}
