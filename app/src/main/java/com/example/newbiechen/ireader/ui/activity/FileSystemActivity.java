package com.example.newbiechen.ireader.ui.activity;

import android.support.v4.app.Fragment;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseTabActivity;
import com.example.newbiechen.ireader.ui.fragment.FileCategoryFragment;
import com.example.newbiechen.ireader.ui.fragment.LocalBookFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by newbiechen on 17-5-27.
 */

public class FileSystemActivity extends BaseTabActivity {
    @Override
    protected List<Fragment> createTabFragments() {
        return Arrays.asList(new LocalBookFragment(),new FileCategoryFragment());
    }

    @Override
    protected List<String> createTabTitles() {
        return Arrays.asList("智能导入","手机目录");
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_file_system;
    }
}
