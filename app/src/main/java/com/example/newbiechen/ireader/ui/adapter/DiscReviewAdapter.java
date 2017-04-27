package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.BookReviewBean;
import com.example.newbiechen.ireader.ui.adapter.view.DiscReviewView;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-4-21.
 */

public class DiscReviewAdapter extends WholeAdapter<BookReviewBean> {

    public DiscReviewAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected View createView(Context context, int viewType) {
        return new DiscReviewView(context);
    }
}
