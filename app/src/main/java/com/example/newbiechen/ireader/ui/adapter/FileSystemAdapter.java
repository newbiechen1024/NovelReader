package com.example.newbiechen.ireader.ui.adapter;

import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.ui.adapter.view.FileHolder;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
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
    //记录item是否被选中的Map
    private HashMap<File,Boolean> mCheckMap = new HashMap<>();

    @Override
    protected IViewHolder<File> createViewHolder(int viewType) {
        return new FileHolder(mCheckMap);
    }

    @Override
    public void refreshItems(List<File> list) {
        mCheckMap.clear();
        for(File file : list){
            mCheckMap.put(file, false);
        }
        super.refreshItems(list);
    }

    @Override
    public void addItem(File value) {
        mCheckMap.put(value, false);
        super.addItem(value);
    }

    @Override
    public void addItem(int index, File value) {
        mCheckMap.put(value, false);
        super.addItem(index, value);
    }

    @Override
    public void addItems(List<File> values) {
        for(File file : values){
            mCheckMap.put(file, false);
        }
        super.addItems(values);
    }

    @Override
    public void removeItem(File value) {
        mCheckMap.remove(value);
        super.removeItem(value);
    }

    //设置点击切换
    public void setCheckedItem(int pos){
        File file = getItem(pos);
        if (isFileLoaded(file.getAbsolutePath())) return;

        boolean isSelected = mCheckMap.get(file);
        if (isSelected)
            mCheckMap.put(file, false);
        else
            mCheckMap.put(file, true);

        notifyDataSetChanged();
    }

    public void setSelectedAll(boolean isSelected){
        Set<Map.Entry<File, Boolean>> entrys = mCheckMap.entrySet();
        for (Map.Entry<File, Boolean> entry:entrys){
            if (!isFileLoaded(entry.getKey().getAbsolutePath())){
                entry.setValue(isSelected);
            }
        }
        notifyDataSetChanged();
    }

    private boolean isFileLoaded(String id){
        //如果是已加载的文件，则点击事件无效。
        if (BookRepository.getInstance().getCollBook(id) != null){
            return true;
        }
        return false;
    }

    public boolean getItemIsChecked(int pos){
        File file = getItem(pos);
        return mCheckMap.get(file);
    }

    public List<File> getCheckedFiles(){
        List<File> files = new ArrayList<>();
        Set<Map.Entry<File, Boolean>> entrys = mCheckMap.entrySet();
        for (Map.Entry<File, Boolean> entry:entrys){
            if (entry.getValue() && !entry.getKey().isDirectory()){
                files.add(entry.getKey());
            }
        }
        return files;
    }

    public HashMap<File,Boolean> getCheckMap(){
        return mCheckMap;
    }
}
