package com.example.newbiechen.ireader.ui.adapter.view;

import android.content.Context;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.ui.base.AdapterImpl;

/**
 * Created by newbiechen on 17-5-16.
 */

public class CategoryView extends AdapterImpl<BookChapterBean>{

    private TextView mTvChapter;
    //
    private int curSelected;
    public CategoryView(Context context) {
        super(context);
    }

    @Override
    protected int getContentId() {
        return R.layout.item_category;
    }

    @Override
    protected void initView() {
        super.initView();
        mTvChapter = findById(R.id.category_tv_chapter);
    }

    @Override
    public void onBind(BookChapterBean value, int pos) {
        mTvChapter.setText(value.getTitle());
    }

    public void setCurSelected(int selected){
        curSelected = selected;
    }
}
