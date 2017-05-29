package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.FileTreeEvent;
import com.example.newbiechen.ireader.ui.adapter.FileSystemAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.utils.FileStack;
import com.example.newbiechen.ireader.widget.itemdecoration.DefaultItemDecoration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-27.
 */

public class FileCategoryFragment extends BaseFragment {
    private static final String TAG = "FileCategoryFragment";
    @BindView(R.id.file_category_tv_path)
    TextView mTvPath;
    @BindView(R.id.file_category_tv_back_last)
    TextView mTvBackLast;
    @BindView(R.id.file_category_rv_content)
    RecyclerView mRvContent;

    private FileSystemAdapter mAdapter;
    private FileStack mFileStack;
    private OnFileCheckedListener mCheckedListener;
    @Override
    protected int getContentId() {
        return R.layout.fragment_file_category;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        mFileStack = new FileStack();
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
                    File file = mAdapter.getItem(pos);
                    if (file.isDirectory()){
                        //保存当前信息。
                        FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                        snapshot.filePath = mTvPath.getText().toString();
                        snapshot.files = new ArrayList<File>(mAdapter.getItems());
                        snapshot.scrollOffset = mRvContent.computeVerticalScrollOffset();
                        mFileStack.push(snapshot);
                        //切换下一个文件
                        toggleFileTree(file);
                    }
                    else {
                        //点击选中
                        mAdapter.setCheckItem(pos);
                        //反馈
                        if (mCheckedListener != null){
                            mCheckedListener.fileChecked(mAdapter.getItemSelected(pos));
                        }
                    }
                }
        );

        mTvBackLast.setOnClickListener(
                (v) -> {
                    FileStack.FileSnapshot snapshot = mFileStack.pop();
                    int oldScrollOffset = mRvContent.computeHorizontalScrollOffset();
                    if (snapshot == null) return;
                    mTvPath.setText(snapshot.filePath);
                    mAdapter.refreshItems(snapshot.files);
                    mRvContent.scrollBy(0,snapshot.scrollOffset - oldScrollOffset);
                    //反馈
                    if (mCheckedListener != null){
                        mCheckedListener.fileCategoryChange();
                    }
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        File root = Environment.getExternalStorageDirectory();
        toggleFileTree(root);
    }

    private void toggleFileTree(File file){
        //路径名
        mTvPath.setText(getString(R.string.nb_file_path,file.getPath()));
        //获取数据
        File[] files = file.listFiles(new SimpleFileFilter());
        //转换成List
        List<File> rootFiles = Arrays.asList(files);
        //排序
        Collections.sort(rootFiles,new FileComparator());
        //加入
        mAdapter.refreshItems(rootFiles);
        //反馈
        if (mCheckedListener != null){
            mCheckedListener.fileCategoryChange();
        }
    }

    public void setSelectedAll(boolean isSelected){
        mAdapter.setSelectedAll(isSelected);
    }

    public void setOnFileCheckedListener(OnFileCheckedListener listener){
        mCheckedListener = listener;
    }

    public int getBookCount(){
        int count = 0;
        Set<Map.Entry<File, Boolean>> entrys = mAdapter.getSelectedMap().entrySet();
        for (Map.Entry<File, Boolean> entry:entrys){
            if (!entry.getKey().isDirectory()){
                ++count;
            }
        }
        return count;
    }

    /**
     * 删除已选中的书籍
     */
    public void deleteCheckedBook(){
    }

    public class FileComparator implements Comparator<File>{
        @Override
        public int compare(File o1, File o2){
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o2.isDirectory() && o1.isFile()) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public class SimpleFileFilter implements FileFilter{
        @Override
        public boolean accept(File pathname) {
            //文件夹内部数量为0
            if (pathname.isDirectory() && pathname.list().length == 0){
                return false;
            }
            //文件夹以.开头的
            if (pathname.isDirectory() && pathname.getName().startsWith(".")) {
                return false;
            }
            //文件内容为空
            if (!pathname.isDirectory() && pathname.length() == 0){
                return false;
            }
            return true;
        }
    }
}
