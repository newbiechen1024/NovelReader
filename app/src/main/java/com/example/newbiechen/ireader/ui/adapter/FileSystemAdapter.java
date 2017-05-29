package com.example.newbiechen.ireader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newbiechen.ireader.ui.adapter.view.FileHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.ui.base.adapter.BaseViewHolder;
import com.example.newbiechen.ireader.ui.base.adapter.IViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by newbiechen on 17-5-27.
 */

public class FileSystemAdapter extends BaseListAdapter<File>{
    private HashMap<File,Boolean> mSelectedMap = new HashMap<>();

    @Override
    protected IViewHolder<File> createViewHolder(int viewType) {
        return new FileHolder(mSelectedMap);
    }

    @Override
    public void refreshItems(List<File> list) {
        mSelectedMap.clear();
        for(File file : list){
            mSelectedMap.put(file, false);
        }
        super.refreshItems(list);
    }

    @Override
    public void addItem(File value) {
        mSelectedMap.put(value, false);
        super.addItem(value);
    }

    @Override
    public void addItem(int index, File value) {
        mSelectedMap.put(value, false);
        super.addItem(index, value);
    }

    @Override
    public void addItems(List<File> values) {
        for(File file : values){
            mSelectedMap.put(file, false);
        }
        super.addItems(values);
    }

    @Override
    public void removeItem(File value) {
        mSelectedMap.remove(value);
        super.removeItem(value);
    }

    //设置点击切换
    public void setCheckItem(int pos){
        File file = getItem(pos);
        boolean isSelected = mSelectedMap.get(file);
        if (isSelected)
            mSelectedMap.put(file, false);
        else
            mSelectedMap.put(file, true);

        notifyDataSetChanged();
    }

    public void setSelectedAll(boolean isSelected){
        Set<Map.Entry<File, Boolean>> entrys = mSelectedMap.entrySet();
        for (Map.Entry<File, Boolean> entry:entrys){
            entry.setValue(isSelected);
        }
        notifyDataSetChanged();
    }

    public boolean getItemSelected(int pos){
        File file = getItem(pos);
        return mSelectedMap.get(file);
    }

    public HashMap<File,Boolean> getSelectedMap(){
        return mSelectedMap;
    }
}
