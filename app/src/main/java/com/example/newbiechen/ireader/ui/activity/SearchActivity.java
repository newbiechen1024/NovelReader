package com.example.newbiechen.ireader.ui.activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.ui.base.BaseActivity;

/**
 * Created by newbiechen on 17-4-24.
 */

public class SearchActivity extends BaseActivity {
    @Override
    protected int getContentId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        //兼容低版本的方式
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setIconified(false);
        //是搜索框默认展开
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入作者名或书籍名");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
