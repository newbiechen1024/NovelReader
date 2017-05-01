package com.example.newbiechen.ireader.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.newbiechen.ireader.model.bean.CommentBean;
import com.example.newbiechen.ireader.ui.adapter.view.CommentView;
import com.example.newbiechen.ireader.ui.base.BaseListAdapter;

import org.w3c.dom.Comment;

/**
 * Created by newbiechen on 17-4-29.
 */

public class GodCommentAdapter extends BaseListAdapter<CommentBean>{
    @Override
    protected View createView(Context context, int viewType) {
        return new CommentView(context,true);
    }
}
