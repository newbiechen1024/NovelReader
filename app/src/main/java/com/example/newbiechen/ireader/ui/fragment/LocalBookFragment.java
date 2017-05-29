package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.FileCheckedEvent;
import com.example.newbiechen.ireader.ui.adapter.FileSystemAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.utils.FileUtils;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DefaultItemDecoration;

import java.io.File;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-5-27.
 * 本地书籍
 */

public class LocalBookFragment extends BaseFragment{
    @BindView(R.id.refresh_layout)
    RefreshLayout mRlRefresh;
    @BindView(R.id.local_book_rv_content)
    RecyclerView mRvContent;
    private FileSystemAdapter mAdapter;
    private OnFileCheckedListener mCheckedListener;
    @Override
    protected int getContentId() {
        return R.layout.fragment_local_book;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter(){
        mAdapter = new FileSystemAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DefaultItemDecoration(getContext()));
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //点击选中
                    mAdapter.setCheckItem(pos);
                    //反馈
                    if (mCheckedListener != null){
                        mCheckedListener.fileChecked(mAdapter.getItemSelected(pos));
                    }
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //显示数据
        FileUtils.getSDTxtFile()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        files -> {
                            if (files.size() == 0){
                                //数据为空
                                mRlRefresh.showEmpty();
                            }
                            else {
                                mAdapter.refreshItems(files);
                                mRlRefresh.showFinish();
                                //反馈
                                if (mCheckedListener != null){
                                    mCheckedListener.fileCategoryChange();
                                }
                            }
                        }
                );
    }

    public void setOnFileCheckedListener(OnFileCheckedListener listener){
        mCheckedListener = listener;
    }

    public void setSelectedAll(boolean isSelected){
        mAdapter.setSelectedAll(isSelected);
    }

    public int getBookCount(){
        return mAdapter.getItemCount();
    }

    /**
     * 删除已选中的书籍
     */
    public void deleteCheckedBook(){
        //获取文件
        //弹出确定按钮
        //更新状态
    }
}
