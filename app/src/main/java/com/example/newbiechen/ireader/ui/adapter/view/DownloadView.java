package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.DownloadTaskBean;
import com.example.newbiechen.ireader.ui.base.AdapterImpl;
import com.example.newbiechen.ireader.utils.FileUtils;

/**
 * Created by newbiechen on 17-5-12.
 */

public class DownloadView extends AdapterImpl<DownloadTaskBean> {

    private TextView mTvTitle;
    private TextView mTvMsg;
    private TextView mTvTip;
    private ProgressBar mPbShow;
    private RelativeLayout mRlToggle;
    private ImageView mIvStatus;
    private TextView mTvStatus;

    public DownloadView(Context context) {
        super(context);
    }

    @Override
    protected int getContentId() {
        return R.layout.item_download;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvTitle = findById(R.id.download_tv_title);
        mTvMsg = findById(R.id.download_tv_msg);
        mTvTip = findById(R.id.download_tv_tip);
        mPbShow = findById(R.id.download_pb_show);
        mRlToggle = findById(R.id.download_rl_toggle);
        mIvStatus = findById(R.id.download_iv_status);
        mTvStatus = findById(R.id.download_tv_status);
    }


    @Override
    public void onBind(DownloadTaskBean value, int pos) {

        if (!mTvTitle.getText().equals(value.getTaskName())){
            mTvTitle.setText(value.getTaskName());
        }

        switch (value.getStatus()){
            case DownloadTaskBean.STATUS_LOADING:
                changeBtnStyle(R.string.nb_download_pause,
                        R.color.nb_download_pause,R.drawable.ic_download_pause);

                //进度状态
                setProgressMax(value);
                mPbShow.setProgress(value.getCurrentChapter());

                setTip(R.string.nb_download_loading);

                mTvMsg.setText(getResources().getString(R.string.nb_download_progress,
                        value.getCurrentChapter(),value.getBookChapters().size()));
                break;
            case DownloadTaskBean.STATUS_PAUSE:
                //按钮状态
                changeBtnStyle(R.string.nb_download_start,
                        R.color.nb_download_loading,R.drawable.ic_download_loading);

                //进度状态
                setProgressMax(value);
                setTip(R.string.nb_download_pausing);

                mPbShow.setProgress(value.getCurrentChapter());
                mTvMsg.setText(getResources().getString(R.string.nb_download_progress,
                        value.getCurrentChapter(),value.getBookChapters().size()));
                break;
            case DownloadTaskBean.STATUS_WAIT:
                //按钮状态
                changeBtnStyle(R.string.nb_download_wait,
                        R.color.nb_download_wait,R.drawable.ic_download_wait);

                //进度状态
                setProgressMax(value);
                setTip(R.string.nb_download_waiting);

                mPbShow.setProgress(value.getCurrentChapter());
                mTvMsg.setText(getResources().getString(R.string.nb_download_progress,
                        value.getCurrentChapter(),value.getBookChapters().size()));
                break;
            case DownloadTaskBean.STATUS_ERROR:
                //按钮状态
                changeBtnStyle(R.string.nb_download_error,
                        R.color.nb_download_error,R.drawable.ic_download_error);
                setTip(R.string.nb_download_source_error);
                mPbShow.setVisibility(INVISIBLE);
                mTvMsg.setVisibility(GONE);
                break;
            case DownloadTaskBean.STATUS_FINISH:
                //按钮状态
                changeBtnStyle(R.string.nb_download_finish,
                        R.color.nb_download_finish,R.drawable.ic_download_complete);
                setTip(R.string.nb_download_complete);
                mPbShow.setVisibility(INVISIBLE);

                //设置文件大小
                mTvMsg.setText(FileUtils.getFileSize(value.getSize()));
                break;
        }
    }

    private void changeBtnStyle(int strRes,int colorRes,int drawableRes){
        //按钮状态
        if (!mTvStatus.getText().equals(
                getResources().getString(strRes))){
            mTvStatus.setText(getResources().getString(strRes));
            mTvStatus.setTextColor(getResources().getColor(colorRes));
            mIvStatus.setImageResource(drawableRes);
        }
    }

    private void setProgressMax(DownloadTaskBean value){
        if (mPbShow.getMax() != value.getBookChapters().size()){
            mPbShow.setVisibility(VISIBLE);
            mPbShow.setMax(value.getBookChapters().size());
        }
    }

    //提示
    private void setTip(int strRes){
        if (!mTvTip.getText().equals(getResources().getString(strRes))){
            mTvTip.setText(getResources().getString(strRes));
        }
    }
}
