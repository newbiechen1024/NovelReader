package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.ui.adapter.view.BookReviewView;
import com.example.newbiechen.ireader.widget.WholeAdapter;

/**
 * Created by newbiechen on 17-4-21.
 */

public class BookReviewAdapter extends WholeAdapter<BookReviewBean> {

    public BookReviewAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new BookReviewView(context);
    }
}
