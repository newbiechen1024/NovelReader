package com.example.newbiechen.ireader.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-24.
 */

public class SortBookListActivity extends BaseActivity {
    @BindView(R.id.sort_book_tl_tab)
    TabLayout mTbTab;
    @BindView(R.id.sort_book_vp)
    ViewPager mVp;

    @Override
    protected int getContentId() {
        return R.layout.activity_sort_book_list;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }
}
